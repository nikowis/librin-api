package pl.nikowis.librin.domain.conversation.exception;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.conversation.model.Conversation_;

public class ConversationNotFoundException extends BusinessException {

    @Override
    public String getFieldName() {
        return Conversation_.ID;
    }
}
