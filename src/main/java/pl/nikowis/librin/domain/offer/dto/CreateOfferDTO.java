package pl.nikowis.librin.domain.offer.dto;

import lombok.Data;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.photo.dto.PhotoDTO;
import pl.nikowis.librin.domain.offer.model.OfferCategory;
import pl.nikowis.librin.domain.offer.model.OfferCondition;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateOfferDTO {

    @NotNull
    protected OfferCategory category;
    @NotNull
    protected OfferCondition condition;
    @NotBlank
    @Size(min = 2, max = 128)
    private String title;
    @NotBlank
    @Size(min = 2, max = 128)
    private String author;
    @Size(max = 512)
    private String description;
    @NotNull
    @Min(0)
    @Max(999999)
    private BigDecimal price;

    @Size(min = Offer.MIN_PHOTOS, max = Offer.MAX_PHOTOS)
    private List<PhotoDTO> photos;
}
