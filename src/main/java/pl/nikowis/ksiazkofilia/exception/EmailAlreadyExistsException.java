package pl.nikowis.ksiazkofilia.exception;

public class EmailAlreadyExistsException extends BusinessException {

    public static final String EMAIL_FIELD = "email";

    public EmailAlreadyExistsException(Object[] args) {
        super(args);
    }

    @Override
    public String getFieldName() {
        return EMAIL_FIELD;
    }
}
