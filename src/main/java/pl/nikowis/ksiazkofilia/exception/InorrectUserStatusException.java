package pl.nikowis.ksiazkofilia.exception;

import pl.nikowis.ksiazkofilia.model.User_;

public class InorrectUserStatusException extends BusinessException {
    @Override
    public String getFieldName() {
        return User_.STATUS;
    }
}
