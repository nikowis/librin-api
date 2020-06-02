package pl.nikowis.librin.model;

import lombok.Data;

@Data
public class ApiError {
    private String defaultMessage;
    private String field;

    public ApiError(String field, String defaultMessage) {
        this.field = field;
        this.defaultMessage = defaultMessage;
    }
}