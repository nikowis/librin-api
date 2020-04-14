package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class OfferDTO {

    protected Long id;
    @NotEmpty
    protected String title;
    @NotEmpty
    protected String author;

    protected Date createdAt;
    protected Boolean active;

}
