package pl.nikowis.librin.exception;

import pl.nikowis.librin.model.Policy_;

public class PolicyNotFoundException extends BusinessException {
    @Override
    public String getFieldName() {
        return Policy_.FILE_NAME;
    }
}
