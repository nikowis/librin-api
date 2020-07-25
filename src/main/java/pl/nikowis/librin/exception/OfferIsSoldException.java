package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.Offer_;

public class OfferIsSoldException extends BusinessException {

    @Override
    public String getFieldName() {
        return Offer_.ID;
    }
}
