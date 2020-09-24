package pl.nikowis.librin.domain.user.dto;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.user.model.User_;

public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException(Object[] args) {
        super(args);
    }

    @Override
    public String getFieldName() {
        return User_.EMAIL;
    }
}
