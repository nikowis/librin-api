package pl.nikowis.librin.domain.message.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateConversationDTO {

    @NotNull
    private Long offerId;
}
