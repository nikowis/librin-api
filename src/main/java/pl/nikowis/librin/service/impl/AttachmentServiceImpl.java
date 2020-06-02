package pl.nikowis.librin.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.dto.AttachmentDTO;
import pl.nikowis.librin.model.Attachment;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.repository.AttachmentRepository;
import pl.nikowis.librin.service.AttachmentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@Transactional
class AttachmentServiceImpl implements AttachmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentServiceImpl.class);
    public static final String OFFERS_DIR = "offers";

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Value("${userfiles.directory}")
    private String userFilesDirectory;

    @Override
    public Attachment addAttachmentToOffer(Offer offer, AttachmentDTO photo) {
        Attachment attachment = new Attachment();
        attachment.setName(photo.getName());
        attachment.setOffer(offer);
        attachment.setOwner(offer.getOwner());
        attachment.setOwnerId(offer.getOwner().getId());
        attachment.setSize((long) photo.getContent().getBytes().length);
        Attachment savedAttachment = attachmentRepository.save(attachment);
        String filePath = resolveOfferAttachmentFilePath(savedAttachment);
        Path resolvedFilePath = Paths.get(userFilesDirectory, filePath);
        try {
            Files.createDirectories(resolvedFilePath.getParent());
            String imageDataBytes = photo.getContent().substring(photo.getContent().indexOf(",") + 1);
            byte[] decodedImg = Base64.getDecoder().decode(imageDataBytes.getBytes());
            Files.write(resolvedFilePath, decodedImg);
            savedAttachment.setContent(photo.getContent());
        } catch (IOException e) {
            LOGGER.error("Cant create directories", e);
        }
        return savedAttachment;
    }

    @Override
    public Attachment fillAttachmentContent(Attachment attachment) {
        String filePath = resolveOfferAttachmentFilePath(attachment);
        Path resolvedFilePath = Paths.get(userFilesDirectory, filePath);
        try {
            byte[] bytesContent = Files.readAllBytes(resolvedFilePath);
            String base64Img = Base64.getEncoder().encodeToString(bytesContent);
            String contentType = Files.probeContentType(resolvedFilePath);
            attachment.setContent("data:" + contentType + ";base64," + base64Img);
            return attachment;
        } catch (IOException e) {
            LOGGER.error("Cant read content from file ", e);
            return null;
        }
    }

    @Override
    public void removeOfferAttachment(Attachment oldAtt) {
        String filePath = resolveOfferAttachmentFilePath(oldAtt);
        Path resolvedFilePath = Paths.get(userFilesDirectory, filePath);
        try {
            Files.delete(resolvedFilePath);
        } catch (IOException e) {
            LOGGER.error("Cant remove file", e);
        }
        attachmentRepository.delete(oldAtt);
    }


    private String resolveOfferAttachmentFilePath(Attachment attachment) {
        return File.separator +
                getUserIdSubstr(attachment.getOwnerId()) +
                File.separator +
                attachment.getOwnerId() +
                File.separator +
                OFFERS_DIR +
                File.separator +
                attachment.getOffer().getId() +
                File.separator +
                attachment.getId() +
                "_" +
                attachment.getName();
    }

    private String getUserIdSubstr(Long userId) {
        String userIdStr = String.valueOf(userId);
        if (userIdStr.length() > 2) {
            userIdStr = userIdStr.substring(0, 2);
        }
        return userIdStr;
    }
}
