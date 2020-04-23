package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;
import pl.nikowis.ksiazkofilia.model.OfferStatus;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OfferDTO {

    private Long id;
    private Long ownerId;
    private String title;
    private String author;
    private Date createdAt;
    private BigDecimal price;
    private OfferStatus status;

}
