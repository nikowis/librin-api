package pl.nikowis.librin.domain.user.exception;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.user.model.User_;

public class UsernameAlreadyExistsException extends BusinessException {

    public UsernameAlreadyExistsException(Object[] args) {
        super(args);
    }

    @Override
    public String getFieldName() {
        return User_.USERNAME;
    }
}
