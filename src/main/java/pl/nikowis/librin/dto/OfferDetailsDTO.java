package pl.nikowis.librin.dto;

import lombok.Data;

import java.util.List;

@Data
public class OfferDetailsDTO extends BaseOfferDTO{

    private List<AttachmentDTO> attachments;

}
