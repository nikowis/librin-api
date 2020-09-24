package pl.nikowis.librin.domain.offer.dto;

import lombok.Data;
import pl.nikowis.librin.domain.attachment.dto.AttachmentDTO;

@Data
public class OfferPreviewDTO extends BaseOfferDTO {

    private AttachmentDTO attachment;

}
