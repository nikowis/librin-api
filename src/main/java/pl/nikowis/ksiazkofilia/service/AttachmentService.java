package pl.nikowis.ksiazkofilia.service;

import pl.nikowis.ksiazkofilia.dto.AttachmentDTO;
import pl.nikowis.ksiazkofilia.model.Attachment;
import pl.nikowis.ksiazkofilia.model.Offer;

public interface AttachmentService {
    Attachment addAttachmentToOffer(Offer offer, AttachmentDTO photo);

    void removeOfferAttachment(Attachment oldAtt);

    Attachment fillAttachmentContent(Attachment attachment);
}
