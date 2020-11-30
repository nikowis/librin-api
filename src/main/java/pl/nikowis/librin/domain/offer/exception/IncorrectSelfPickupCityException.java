package pl.nikowis.librin.domain.offer.exception;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.offer.model.Offer_;

public class IncorrectSelfPickupCityException extends BusinessException {

    @Override
    public String getFieldName() {
        return Offer_.SELF_PICKUP_CITY;
    }
}
