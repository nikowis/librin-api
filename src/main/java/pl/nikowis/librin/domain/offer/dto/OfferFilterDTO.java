package pl.nikowis.librin.domain.offer.dto;

import lombok.Data;
import pl.nikowis.librin.domain.offer.model.OfferCategory;
import pl.nikowis.librin.domain.offer.model.OfferCondition;
import pl.nikowis.librin.domain.offer.model.OfferStatus;

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
