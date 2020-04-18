package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OfferDTO {

    private Long id;
    private String title;
    private String author;
    private Date createdAt;
    private Boolean active;
    private BigDecimal price;

}
