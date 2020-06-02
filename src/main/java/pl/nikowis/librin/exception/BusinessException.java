package pl.nikowis.librin.exception;

import lombok.Data;

@Data
public abstract class BusinessException extends RuntimeException {

    private Object[] args;

    public BusinessException() {
    }

    public BusinessException(Object[] args) {
        this.args = args;
    }

    public abstract String getFieldName();
}
