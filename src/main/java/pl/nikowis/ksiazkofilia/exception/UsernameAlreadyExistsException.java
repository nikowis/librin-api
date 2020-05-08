package pl.nikowis.ksiazkofilia.exception;

public class UsernameAlreadyExistsException extends BusinessException {

    public static final String EMAIL_FIELD = "username";

    public UsernameAlreadyExistsException(Object[] args) {
        super(args);
    }

    @Override
    public String getFieldName() {
        return EMAIL_FIELD;
    }
}
