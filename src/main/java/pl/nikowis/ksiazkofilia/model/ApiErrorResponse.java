package pl.nikowis.ksiazkofilia.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ApiErrorResponse {

    private Date timestramp;
    private int status;
    private String error;
    private List<ApiError> errors = new ArrayList<>();

    public ApiErrorResponse(HttpStatus status, List<ApiError> errors) {
        this.errors = errors;
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.timestramp = new Date();
        this.errors = errors;
    }

}
