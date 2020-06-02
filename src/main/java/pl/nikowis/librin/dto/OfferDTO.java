package pl.nikowis.librin.dto;

import lombok.Data;
import pl.nikowis.librin.model.OfferStatus;

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
    private PublicUserDTO owner;
    private AttachmentDTO attachment;

}
