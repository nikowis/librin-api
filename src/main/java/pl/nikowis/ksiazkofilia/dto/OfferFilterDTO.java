package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OfferFilterDTO {

    private String title;
    private String author;
    private Boolean active;
    private Long owner;

}
