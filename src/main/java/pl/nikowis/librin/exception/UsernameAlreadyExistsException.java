package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.User_;

public class UsernameAlreadyExistsException extends BusinessException {

    public UsernameAlreadyExistsException(Object[] args) {
        super(args);
    }

    @Override
    public String getFieldName() {
        return User_.USERNAME;
    }
}
