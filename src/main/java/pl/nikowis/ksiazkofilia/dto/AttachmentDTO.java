package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AttachmentDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String content;

}
