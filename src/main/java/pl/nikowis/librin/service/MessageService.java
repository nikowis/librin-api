package pl.nikowis.librin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.nikowis.librin.dto.ConversationDTO;
import pl.nikowis.librin.dto.ConversationWithoutMessagesDTO;
import pl.nikowis.librin.dto.CreateConversationDTO;
import pl.nikowis.librin.dto.SendMessageDTO;
import pl.nikowis.librin.model.OfferStatus;

public interface MessageService {

    ConversationDTO getConversation(Long conversationId);

    ConversationDTO sendMessage(Long conversationId, SendMessageDTO messageDTO);

    ConversationDTO createConversation(CreateConversationDTO createConversationDTO);

    Page<ConversationWithoutMessagesDTO> getUserConversations(Pageable pageable);

    void notifyAllConversationsOfferStatusChange(Long offerId, OfferStatus status, Long buyerId);
}
