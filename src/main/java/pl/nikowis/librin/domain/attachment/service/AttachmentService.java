package pl.nikowis.librin.domain.attachment.service;

import pl.nikowis.librin.domain.attachment.dto.AttachmentDTO;
import pl.nikowis.librin.domain.attachment.model.Attachment;
import pl.nikowis.librin.domain.offer.model.Offer;

import java.util.List;

public interface AttachmentService {

    List<Attachment> addAttachmentsToOffer(Offer offer, List<AttachmentDTO> photo);

    void removeOfferAttachments(List<Attachment> oldAtts);

    List<Attachment> fillAttachmentContent(List<Attachment> attachments);

    Attachment fillAttachmentContent(Attachment attachment);
}
