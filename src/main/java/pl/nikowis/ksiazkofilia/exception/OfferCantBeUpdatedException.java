package pl.nikowis.ksiazkofilia.exception;

import pl.nikowis.ksiazkofilia.model.Offer_;

public class OfferCantBeUpdatedException extends BusinessException {

    @Override
    public String getFieldName() {
        return Offer_.ID;
    }
}
