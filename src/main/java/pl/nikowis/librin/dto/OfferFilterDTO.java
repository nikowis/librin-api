package pl.nikowis.librin.dto;

import lombok.Data;
import pl.nikowis.librin.model.OfferCategory;
import pl.nikowis.librin.model.OfferCondition;
import pl.nikowis.librin.model.OfferStatus;

import java.util.List;

@Data
public class OfferFilterDTO {

    private String title;
    private String author;
    private List<OfferStatus> statuses;
    private Long owner;
    private List<OfferCategory> categories;
    private List<OfferCondition> conditions;

}
