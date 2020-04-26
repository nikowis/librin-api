package pl.nikowis.ksiazkofilia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.nikowis.ksiazkofilia.dto.ConversationDTO;
import pl.nikowis.ksiazkofilia.dto.CreateConversationDTO;
import pl.nikowis.ksiazkofilia.dto.SendMessageDTO;

public interface MessageService {

    ConversationDTO getConversation(Long conversationId);

    ConversationDTO sendMessage(Long conversationId, SendMessageDTO messageDTO);

    ConversationDTO createConversation(CreateConversationDTO createConversationDTO);

    Page<ConversationDTO> getUserConversations(Pageable pageable);
}
