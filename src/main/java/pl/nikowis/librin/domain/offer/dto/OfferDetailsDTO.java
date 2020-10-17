package pl.nikowis.librin.domain.offer.dto;

import lombok.Data;
import pl.nikowis.librin.domain.photo.dto.PhotoDTO;

import java.util.List;

@Data
public class OfferDetailsDTO extends BaseOfferDTO {

    private List<PhotoDTO> photos;

}
