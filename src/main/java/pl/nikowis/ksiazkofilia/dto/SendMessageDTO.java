package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SendMessageDTO {

    @NotBlank
    private String content;
}
