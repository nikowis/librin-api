package pl.nikowis.ksiazkofilia.exception;

import pl.nikowis.ksiazkofilia.model.User_;

public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException(Object[] args) {
        super(args);
    }

    @Override
    public String getFieldName() {
        return User_.EMAIL;
    }
}
