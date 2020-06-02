package pl.nikowis.librin.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateConversationDTO {

    @NotNull
    private Long offerId;
}
