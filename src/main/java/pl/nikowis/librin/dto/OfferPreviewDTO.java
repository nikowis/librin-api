package pl.nikowis.librin.dto;

import lombok.Data;
import pl.nikowis.librin.model.OfferCategory;
import pl.nikowis.librin.model.OfferCondition;
import pl.nikowis.librin.model.OfferStatus;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OfferPreviewDTO extends BaseOfferDTO{

    private AttachmentDTO attachment;

}
