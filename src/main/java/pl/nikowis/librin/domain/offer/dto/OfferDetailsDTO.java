package pl.nikowis.librin.domain.offer.dto;

import lombok.Data;
import pl.nikowis.librin.domain.attachment.dto.AttachmentDTO;

import java.util.List;

@Data
public class OfferDetailsDTO extends BaseOfferDTO {

    private List<AttachmentDTO> attachments;

}
