package pl.nikowis.librin.domain.user.dto;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.user.model.User_;

public class IncorrectPasswordException extends BusinessException {

    @Override
    public String getFieldName() {
        return User_.PASSWORD;
    }
}
