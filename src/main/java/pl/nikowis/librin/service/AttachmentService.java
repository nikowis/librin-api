package pl.nikowis.librin.service;

import pl.nikowis.librin.dto.AttachmentDTO;
import pl.nikowis.librin.model.Attachment;
import pl.nikowis.librin.model.Offer;

import java.util.List;

public interface AttachmentService {

    List<Attachment> addAttachmentsToOffer(Offer offer, List<AttachmentDTO> photo);

    void removeOfferAttachments(List<Attachment> oldAtts);

    List<Attachment> fillAttachmentContent(List<Attachment> attachments);

    Attachment fillAttachmentContent(Attachment attachment);
}
