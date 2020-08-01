package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.User_;

public class IncorrectCreateReportRquestException extends BusinessException {

    @Override
    public String getFieldName() {
        return User_.ID;
    }
}
