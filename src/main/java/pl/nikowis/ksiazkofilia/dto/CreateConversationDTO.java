package pl.nikowis.ksiazkofilia.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateConversationDTO {

    @NotNull
    private Long offerId;
}
