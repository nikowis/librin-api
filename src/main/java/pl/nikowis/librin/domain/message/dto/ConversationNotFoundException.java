package pl.nikowis.librin.domain.message.dto;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.message.model.Conversation_;

public class ConversationNotFoundException extends BusinessException {

    @Override
    public String getFieldName() {
        return Conversation_.ID;
    }
}
