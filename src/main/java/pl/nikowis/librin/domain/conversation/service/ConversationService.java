package pl.nikowis.librin.domain.conversation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.nikowis.librin.domain.conversation.dto.ConversationDTO;
import pl.nikowis.librin.domain.conversation.dto.CreateConversationDTO;
import pl.nikowis.librin.domain.conversation.dto.MessageDTO;
import pl.nikowis.librin.domain.conversation.dto.SendMessageDTO;
import pl.nikowis.librin.domain.offer.model.OfferStatus;

public interface ConversationService {

    ConversationDTO getConversation(Long conversationId);

    MessageDTO sendMessage(Long conversationId, SendMessageDTO messageDTO);

    ConversationDTO createConversation(CreateConversationDTO createConversationDTO);

    Page<ConversationDTO> getUserConversations(Pageable pageable);

    void notifyAllConversationsOfferStatusChange(Long offerId, OfferStatus status, Long buyerId);

    Page<MessageDTO> getConversationMessages(Long conversationId, Pageable pageable);
}
