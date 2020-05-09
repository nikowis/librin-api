package pl.nikowis.ksiazkofilia.exception;

import pl.nikowis.ksiazkofilia.model.User_;

public class UsernameAlreadyExistsException extends BusinessException {

    public UsernameAlreadyExistsException(Object[] args) {
        super(args);
    }

    @Override
    public String getFieldName() {
        return User_.USERNAME;
    }
}
