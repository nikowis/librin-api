package pl.nikowis.librin.domain.photo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PhotoDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @NotNull
    private String content;

    private String uuid;

    private String path;

}
