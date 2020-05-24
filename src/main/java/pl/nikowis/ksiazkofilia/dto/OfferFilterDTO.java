package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;
import pl.nikowis.ksiazkofilia.model.OfferStatus;

import java.util.List;

@Data
public class OfferFilterDTO {

    private String title;
    private String author;
    private List<OfferStatus> statuses;
    private Long owner;

}
