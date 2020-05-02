package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AttachmentDTO {

    @NotBlank
    private String name;

    @NotBlank
    @NotNull
    private String content;

}
