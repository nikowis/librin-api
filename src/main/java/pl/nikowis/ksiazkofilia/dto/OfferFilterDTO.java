package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;
import pl.nikowis.ksiazkofilia.model.OfferStatus;

@Data
public class OfferFilterDTO {

    private String title;
    private String author;
    private OfferStatus status;
    private Long owner;

}
