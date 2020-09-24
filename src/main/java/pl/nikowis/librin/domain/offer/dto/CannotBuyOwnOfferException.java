package pl.nikowis.librin.domain.offer.dto;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.offer.model.Offer_;

public class CannotBuyOwnOfferException extends BusinessException {

    @Override
    public String getFieldName() {
        return Offer_.OWNER;
    }
}
