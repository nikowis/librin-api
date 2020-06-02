package pl.nikowis.librin.service;

import pl.nikowis.librin.dto.AttachmentDTO;
import pl.nikowis.librin.model.Attachment;
import pl.nikowis.librin.model.Offer;

public interface AttachmentService {
    Attachment addAttachmentToOffer(Offer offer, AttachmentDTO photo);

    void removeOfferAttachment(Attachment oldAtt);

    Attachment fillAttachmentContent(Attachment attachment);
}
