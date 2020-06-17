package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.Offer_;

public class CannotBuyOwnOfferException extends BusinessException {

    @Override
    public String getFieldName() {
        return Offer_.OWNER;
    }
}
