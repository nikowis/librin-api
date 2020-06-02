package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.Conversation_;

public class ConversationNotFoundException extends BusinessException {

    @Override
    public String getFieldName() {
        return Conversation_.ID;
    }
}
