package pl.nikowis.librin.domain.offer.dto;

import lombok.Data;
import pl.nikowis.librin.domain.offer.model.OfferCategory;
import pl.nikowis.librin.domain.offer.model.OfferCondition;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.rating.dto.RatingDTO;
import pl.nikowis.librin.domain.user.dto.PublicUserDTO;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BaseOfferDTO {

    protected Long id;
    protected Long ownerId;
    protected String title;
    protected String author;
    protected String description;
    protected Date createdAt;
    protected BigDecimal price;
    protected OfferStatus status;
    protected OfferCategory category;
    protected OfferCondition condition;
    protected PublicUserDTO owner;
    protected Boolean soldToMe;
    protected Long buyerId;
    protected RatingDTO rating;

}
