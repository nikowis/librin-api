package pl.nikowis.ksiazkofilia.exception;

import pl.nikowis.ksiazkofilia.model.Token_;

public class TokenNotFoundException extends BusinessException {
    @Override
    public String getFieldName() {
        return Token_.ID;
    }
}
