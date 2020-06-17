package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.User_;

public class CustomerAccountBlockedException extends BusinessException {

    @Override
    public String getFieldName() {
        return User_.STATUS;
    }
}
