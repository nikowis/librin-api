package pl.nikowis.librin.domain.message.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ConversationDTO extends ConversationWithoutMessagesDTO {

    private List<MessageDTO> messages = new ArrayList<>();

}
