package pl.nikowis.librin.dto;

import lombok.Data;
import pl.nikowis.librin.model.OfferCategory;
import pl.nikowis.librin.model.OfferCondition;
import pl.nikowis.librin.model.OfferStatus;

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

}
