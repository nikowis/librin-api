package pl.nikowis.ksiazkofilia.exception;

import pl.nikowis.ksiazkofilia.model.Conversation_;

public class ConversationNotFoundException extends BusinessException {

    @Override
    public String getFieldName() {
        return Conversation_.ID;
    }
}
