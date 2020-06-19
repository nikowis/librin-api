package pl.nikowis.librin.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateOfferDTO {

    private static final int MIN_PHOTOS = 1;
    private static final int MAX_PHOTOS = 3;

    @NotBlank
    @Size(min = 2)
    private String title;
    @NotBlank
    @Size(min = 2)
    private String author;
    @NotNull
    @Min(0)
    private BigDecimal price;

    @Size(min = MIN_PHOTOS, max = MAX_PHOTOS)
    private List<AttachmentDTO> photos;
}
