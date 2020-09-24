package pl.nikowis.librin.domain.offer.dto;

import lombok.Data;
import pl.nikowis.librin.domain.attachment.dto.AttachmentDTO;
import pl.nikowis.librin.domain.offer.model.OfferCategory;
import pl.nikowis.librin.domain.offer.model.OfferCondition;

import javax.validation.constraints.Max;
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
    @Size(min = 2, max = 128)
    private String title;
    @NotBlank
    @Size(min = 2, max = 128)
    private String author;

    @Size(max = 512)
    private String description;

    @NotNull
    protected OfferCategory category;

    @NotNull
    protected OfferCondition condition;

    @NotNull
    @Min(0)
    @Max(999999)
    private BigDecimal price;

    @Size(min = MIN_PHOTOS, max = MAX_PHOTOS)
    private List<AttachmentDTO> photos;
}
