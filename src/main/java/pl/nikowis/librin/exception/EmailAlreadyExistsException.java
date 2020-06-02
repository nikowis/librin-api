package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.User_;

public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException(Object[] args) {
        super(args);
    }

    @Override
    public String getFieldName() {
        return User_.EMAIL;
    }
}
