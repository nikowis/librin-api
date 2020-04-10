package pl.nikowis.ksiazkofilia.exception;

public class UsernameAlreadyExistsException extends BusinessException {

    public static final String LOGIN_FIELD = "login";

    public UsernameAlreadyExistsException(Object[] args) {
        super(args);
    }

    @Override
    public String getFieldName() {
        return LOGIN_FIELD;
    }
}
