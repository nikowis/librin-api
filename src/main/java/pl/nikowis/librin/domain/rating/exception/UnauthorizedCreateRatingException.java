package pl.nikowis.librin.domain.rating.exception;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.offer.model.Offer_;

public class UnauthorizedCreateRatingException extends BusinessException {
    @Override
    public String getFieldName() {
        return Offer_.ID;
    }
}
