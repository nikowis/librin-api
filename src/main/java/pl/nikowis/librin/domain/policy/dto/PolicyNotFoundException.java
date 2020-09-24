package pl.nikowis.librin.domain.policy.dto;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.policy.model.Policy_;

public class PolicyNotFoundException extends BusinessException {
    @Override
    public String getFieldName() {
        return Policy_.FILE_NAME;
    }
}
