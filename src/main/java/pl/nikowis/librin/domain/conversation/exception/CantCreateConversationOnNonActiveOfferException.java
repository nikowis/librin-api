package pl.nikowis.librin.domain.conversation.exception;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.offer.model.Offer_;

public class CantCreateConversationOnNonActiveOfferException extends BusinessException {
    @Override
    public String getFieldName() {
        return Offer_.STATUS;
    }
}
