package pl.nikowis.librin.domain.user.exception;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.user.model.Token_;

public class TokenNotFoundException extends BusinessException {
    @Override
    public String getFieldName() {
        return Token_.ID;
    }
}
