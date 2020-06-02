package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.Token_;

public class TokenNotFoundException extends BusinessException {
    @Override
    public String getFieldName() {
        return Token_.ID;
    }
}
