package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.Offer_;

public class CantCreateConversationOnNonActiveOfferException extends BusinessException {
    @Override
    public String getFieldName() {
        return Offer_.STATUS;
    }
}