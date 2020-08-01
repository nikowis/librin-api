package pl.nikowis.librin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateReportDTO {

    @Size(min = 1, max = 512)
    @NotBlank
    private String description;

    private Long conversationId;

    private Long userId;

    private Long offerId;

}
