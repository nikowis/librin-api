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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Attachment> addAttachmentsToOffer(Offer offer, List<AttachmentDTO> photosDTO) {
        List<Attachment> attachments = new ArrayList<>();
        if(photosDTO != null) {
            setMainAttachment(photosDTO);
            attachments = photosDTO.stream().map(photo -> {
                Attachment attachment = new Attachment();
                attachment.setName(photo.getName());
                attachment.setOffer(offer);
                attachment.setOwner(offer.getOwner());
                attachment.setOwnerId(offer.getOwner().getId());
                attachment.setSize((long) photo.getContent().getBytes().length);
                attachment.setMain(photo.isMain());
                Attachment savedAtt = attachmentRepository.save(attachment);
                saveToFileSystem(savedAtt, photo.getContent());
                savedAtt.setContent(photo.getContent());
                return savedAtt;
            }).collect(Collectors.toList());
        }
        offer.setAttachments(attachments);
        offer.setAttachment(attachments.stream().filter(Attachment::isMain).findFirst().orElse(null));
        return attachments;
    }

    private void setMainAttachment(List<AttachmentDTO> photosDTO) {
        photosDTO.forEach(attachmentDTO -> attachmentDTO.setMain(false));
        photosDTO.get(0).setMain(true);
    }

    @Override
    public void removeOfferAttachments(List<Attachment> oldAtts) {
        oldAtts.forEach(a -> {
            String filePath = resolveOfferAttachmentFilePath(a);
            Path resolvedFilePath = Paths.get(userFilesDirectory, filePath);
            try {
                Files.delete(resolvedFilePath);
            } catch (IOException e) {
                LOGGER.error("Cant remove file", e);
            }
        });

        attachmentRepository.deleteAll(oldAtts);
    }

    @Override
    public List<Attachment> fillAttachmentContent(List<Attachment> attachments) {
        attachments.forEach(this::fillAttachmentContent);
        return attachments;
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

    private void saveToFileSystem(Attachment attachment, String photoContent) {
        String filePath = resolveOfferAttachmentFilePath(attachment);
        Path resolvedFilePath = Paths.get(userFilesDirectory, filePath);
        try {
            Files.createDirectories(resolvedFilePath.getParent());
            Files.write(resolvedFilePath, getFileBytes(photoContent));
        } catch (IOException e) {
            LOGGER.error("Cant create directories", e);
        }
    }

    private byte[] getFileBytes(String base64Content) {
        String imageDataBytes = base64Content.substring(base64Content.indexOf(",") + 1);
        return Base64.getDecoder().decode(imageDataBytes.getBytes());
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
