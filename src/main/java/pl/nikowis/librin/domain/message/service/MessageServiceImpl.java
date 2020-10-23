package pl.nikowis.librin.domain.message.service;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.domain.message.dto.ConversationDTO;
import pl.nikowis.librin.domain.message.dto.ConversationWithoutMessagesDTO;
import pl.nikowis.librin.domain.message.dto.CreateConversationDTO;
import pl.nikowis.librin.domain.message.dto.SendMessageDTO;
import pl.nikowis.librin.domain.message.dto.WsConversationUpdateDTO;
import pl.nikowis.librin.domain.message.exception.ConversationNotFoundException;
import pl.nikowis.librin.domain.message.model.Conversation;
import pl.nikowis.librin.domain.message.model.Message;
import pl.nikowis.librin.domain.offer.exception.OfferDoesntExistException;
import pl.nikowis.librin.domain.offer.model.Offer;
import pl.nikowis.librin.domain.offer.model.OfferStatus;
import pl.nikowis.librin.domain.message.exception.CantCreateConversationOnNonActiveOfferException;
import pl.nikowis.librin.domain.user.model.User;
import pl.nikowis.librin.infrastructure.repository.ConversationRepository;
import pl.nikowis.librin.infrastructure.repository.ConversationSpecification;
import pl.nikowis.librin.infrastructure.repository.MessageRepository;
import pl.nikowis.librin.infrastructure.repository.OfferRepository;
import pl.nikowis.librin.infrastructure.repository.UserRepository;
import pl.nikowis.librin.infrastructure.security.SecurityConstants;
import pl.nikowis.librin.infrastructure.service.WebsocketSenderService;
import pl.nikowis.librin.util.SecurityUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Secured(SecurityConstants.ROLE_USER)
public class MessageServiceImpl implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private WebsocketSenderService websocketSenderService;

    @Autowired
    private MessageFactory messageFactory;

    @Autowired
    private ConversationFactory conversationFactory;

    @Override
    @Transactional
    public ConversationDTO getConversation(Long conversationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Conversation conversation = conversationRepository.findByIdAndCustomerIdOrOfferOwnerId(conversationId, currentUserId).orElseThrow(ConversationNotFoundException::new);
        conversation.markAsRead(currentUserId);
        Conversation saved = conversationRepository.save(conversation);
        saved.setMessages(messageRepository.findByConversationIdOrderByCreatedAt(conversationId));
        return mapperFacade.map(saved, ConversationDTO.class);
    }

    @Override
    public ConversationDTO sendMessage(Long conversationId, SendMessageDTO messageDTO) {
        Conversation conversation = conversationRepository.findByIdAndCustomerIdOrOfferOwnerId(conversationId, SecurityUtils.getCurrentUserId()).orElseThrow(ConversationNotFoundException::new);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String recipientEmail = conversation.sendMessageToRecipient(currentUserId);
        Message newMessage = messageRepository.save(messageFactory.createMessage(conversationId, currentUserId, messageDTO));
        Conversation saved = conversationRepository.save(conversation);
        saved.setMessages(messageRepository.findByConversationIdOrderByCreatedAt(conversationId));
        messageRepository.markMessagesAsRead(currentUserId, conversationId);
        sendWsUpdate(conversation, recipientEmail, newMessage, saved);
        return mapperFacade.map(saved, ConversationDTO.class);
    }

    private void sendWsUpdate(Conversation conversation, String recipientEmail, Message newMessage, Conversation conv) {
        Message lastSavedMessage = conversation.getMessages().get(conversation.getMessages().size() - 1);
        WsConversationUpdateDTO wsUpdate = mapperFacade.map(newMessage, WsConversationUpdateDTO.class);
        wsUpdate.setCreatedAt(lastSavedMessage.getCreatedAt());
        wsUpdate.setConversationId(conv.getId());
        wsUpdate.setId(lastSavedMessage.getId());
        websocketSenderService.sendConversationUpdate(wsUpdate, recipientEmail, conv.getId());
    }

    @Override
    public ConversationDTO createConversation(CreateConversationDTO createConversationDTO) {
        Long currentUser = SecurityUtils.getCurrentUserId();
        Optional<Conversation> conv = conversationRepository.findByUserAndOfferId(createConversationDTO.getOfferId(), currentUser);
        if (conv.isPresent()) {
            Conversation conversation = conv.get();
            conversation.setMessages(messageRepository.findByConversationIdOrderByCreatedAt(conversation.getId()));
            return mapperFacade.map(conversation, ConversationDTO.class);
        }
        Offer offer = offerRepository.findById(createConversationDTO.getOfferId()).orElseThrow(OfferDoesntExistException::new);
        User customer = userRepository.findById(currentUser).get();
        Conversation saved = conversationRepository.save(conversationFactory.createConversation(customer, offer));
        return mapperFacade.map(saved, ConversationDTO.class);
    }

    @Override
    public Page<ConversationWithoutMessagesDTO> getUserConversations(Pageable pageable) {
        Page<Conversation> allUsersConvs = conversationRepository.findAll(new ConversationSpecification(SecurityUtils.getCurrentUserId(), false), pageable);
        return allUsersConvs.map(c -> mapperFacade.map(c, ConversationWithoutMessagesDTO.class));
    }

    @Override
    public void notifyAllConversationsOfferStatusChange(Long offerId, OfferStatus status, Long buyerId) {
        List<Conversation> conversationList = conversationRepository.findAllByOfferId(offerId);
        conversationList.forEach(c -> {
            User convCust = c.getCustomer();
            User owner = c.getOffer().getOwner();
            websocketSenderService.sendConversationUpdate(new WsConversationUpdateDTO(c.getId(), owner.getId(), convCust.getId(), buyerId, status), convCust.getEmail().toString(), c.getId());
        });
    }

}
