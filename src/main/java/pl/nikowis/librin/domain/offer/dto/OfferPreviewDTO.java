package pl.nikowis.librin.domain.offer.dto;

import lombok.Data;
import pl.nikowis.librin.domain.photo.dto.PhotoDTO;

@Data
public class OfferPreviewDTO extends BaseOfferDTO {

    private PhotoDTO photo;

}
