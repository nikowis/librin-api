package pl.nikowis.ksiazkofilia.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.dto.AttachmentDTO;
import pl.nikowis.ksiazkofilia.model.Attachment;
import pl.nikowis.ksiazkofilia.model.Offer;
import pl.nikowis.ksiazkofilia.repository.AttachmentRepository;
import pl.nikowis.ksiazkofilia.service.AttachmentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            Files.write(resolvedFilePath, photo.getContent().getBytes());
            savedAttachment.setContent(photo.getContent());
        } catch (IOException e) {
            LOGGER.error("Cant create directories", e);
        }
        return savedAttachment;
    }

    @Override
    public Attachment fillAttachmentContent(Attachment attachment) {
        String filePath = resolveOfferAttachmentFilePath(attachment);
        Path resolvedFilePath =  Paths.get(userFilesDirectory, filePath);
        try {
            String content = Files.readAllLines(resolvedFilePath).stream().reduce("", (s, s2) -> s+s2);
            attachment.setContent(content);
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
        if(userIdStr.length() > 2) {
            userIdStr = userIdStr.substring(0, 2);
        }
        return userIdStr;
    }
}
