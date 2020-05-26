package pl.nikowis.ksiazkofilia.exception;

import pl.nikowis.ksiazkofilia.model.User_;

public class UserNotFoundException extends BusinessException {

    @Override
    public String getFieldName() {
        return User_.ID;
    }
}
