package pl.nikowis.librin.domain.report.dto;

import pl.nikowis.librin.domain.base.BusinessException;
import pl.nikowis.librin.domain.user.model.User_;

public class IncorrectCreateReportRquestException extends BusinessException {

    @Override
    public String getFieldName() {
        return User_.ID;
    }
}
