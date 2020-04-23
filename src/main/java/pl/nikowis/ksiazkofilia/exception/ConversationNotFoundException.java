package pl.nikowis.ksiazkofilia.exception;

public class ConversationNotFoundException extends BusinessException {

    public static final String ID_FIELD = "id";

    @Override
    public String getFieldName() {
        return ID_FIELD;
    }
}
