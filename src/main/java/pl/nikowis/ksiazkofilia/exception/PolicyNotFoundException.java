package pl.nikowis.ksiazkofilia.exception;

import pl.nikowis.ksiazkofilia.model.Policy_;

public class PolicyNotFoundException extends BusinessException {
    @Override
    public String getFieldName() {
        return Policy_.FILE_NAME;
    }
}
