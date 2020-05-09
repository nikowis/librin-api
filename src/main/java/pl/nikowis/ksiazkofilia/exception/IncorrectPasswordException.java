package pl.nikowis.ksiazkofilia.exception;

import pl.nikowis.ksiazkofilia.model.User_;

public class IncorrectPasswordException extends BusinessException {

    @Override
    public String getFieldName() {
        return User_.PASSWORD;
    }
}
