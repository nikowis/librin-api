package pl.nikowis.librin.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.dto.ConversationDTO;
import pl.nikowis.librin.dto.ConversationWithoutMessagesDTO;
import pl.nikowis.librin.dto.CreateConversationDTO;
import pl.nikowis.librin.dto.SendMessageDTO;
import pl.nikowis.librin.exception.CantCreateConversationOnNonActiveOfferException;
import pl.nikowis.librin.exception.ConversationNotFoundException;
import pl.nikowis.librin.exception.OfferDoesntExistException;
import pl.nikowis.librin.model.BaseEntity;
import pl.nikowis.librin.model.Conversation;
import pl.nikowis.librin.model.ConversationSpecification;
import pl.nikowis.librin.model.Message;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.repository.ConversationRepository;
import pl.nikowis.librin.repository.MessageRepository;
import pl.nikowis.librin.repository.OfferRepository;
import pl.nikowis.librin.repository.UserRepository;
import pl.nikowis.librin.service.MessageService;
import pl.nikowis.librin.util.SecurityUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
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

    @Override
    @Transactional
    public ConversationDTO getConversation(Long conversationId) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Conversation conversation = conversationRepository.findByIdAndCustomerIdOrOfferOwnerId(conversationId, currentUserId).orElseThrow(ConversationNotFoundException::new);
        if (conversation == null) {
            throw new ConversationNotFoundException();
        }
        if(isCustomer(conversation, currentUserId)) {
            conversation.setCustomerRead(true);
        } else {
            conversation.setOfferOwnerRead(true);
        }
        Conversation saved = conversationRepository.save(conversation);
        processSortUpdateMessages(currentUserId, saved);
        setRead(saved, currentUserId);
        return mapperFacade.map(saved, ConversationDTO.class);
    }

    @Override
    public ConversationDTO sendMessage(Long conversationId, SendMessageDTO messageDTO) {
        Conversation conversation = conversationRepository.findByIdAndCustomerIdOrOfferOwnerId(conversationId, SecurityUtils.getCurrentUserId()).orElseThrow(ConversationNotFoundException::new);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!isOfferOwner(conversation, currentUserId) && !isCustomer(conversation, currentUserId)) {
            throw new ConversationNotFoundException();
        }

        if(isCustomer(conversation, currentUserId)) {
            conversation.setOfferOwnerRead(false);
            conversation.setCustomerRead(true);
        } else {
            conversation.setOfferOwnerRead(true);
            conversation.setCustomerRead(false);
        }

        Message newMessage = mapperFacade.map(messageDTO, Message.class);
        newMessage.setCreatedBy(currentUserId);
        newMessage.setConversation(conversation);
        conversation.getMessages().add(newMessage);
        conversation.setUpdatedAt(new Date());
        Conversation saved = conversationRepository.save(conversation);
        processSortUpdateMessages(currentUserId, saved);
        setRead(saved, currentUserId);
        return mapperFacade.map(saved, ConversationDTO.class);
    }

    @Override
    public ConversationDTO createConversation(CreateConversationDTO createConversationDTO) {
        Long currentUser = SecurityUtils.getCurrentUserId();
        Optional<Conversation> conv = conversationRepository.findByUserAndOfferId(createConversationDTO.getOfferId(), currentUser);
        if (conv.isPresent()) {
            return mapperFacade.map(conv.get(), ConversationDTO.class);
        }

        Offer offer = offerRepository.findById(createConversationDTO.getOfferId()).orElseThrow(OfferDoesntExistException::new);
        if (!OfferStatus.ACTIVE.equals(offer.getStatus())) {
            throw new CantCreateConversationOnNonActiveOfferException();
        }

        Conversation conversation = new Conversation();
        conversation.setCustomer(userRepository.findById(currentUser).get());
        conversation.setOffer(offer);
        conversation.setCustomerRead(true);
        conversation.setOfferOwnerRead(true);

        Conversation saved = conversationRepository.save(conversation);
        setRead(saved, currentUser);
        return mapperFacade.map(saved, ConversationDTO.class);
    }

    @Override
    public Page<ConversationWithoutMessagesDTO> getUserConversations(Pageable pageable) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Page<Conversation> allByCustomerIdOrOfferOwnerId = conversationRepository.findAll(new ConversationSpecification(currentUserId), pageable);
        allByCustomerIdOrOfferOwnerId.forEach(conv -> setRead(conv, currentUserId));
        return allByCustomerIdOrOfferOwnerId.map(c -> mapperFacade.map(c, ConversationWithoutMessagesDTO.class));
    }

    private void processSortUpdateMessages(Long currentUserId, Conversation conversation) {
        conversation.getMessages().sort(Comparator.comparing(BaseEntity::getCreatedAt));
        List<Message> unReadMessagse = conversation.getMessages().stream().filter(m -> !m.getCreatedBy().equals(currentUserId) && !m.isRead()).collect(Collectors.toList());
        unReadMessagse.forEach(message ->message.setRead(true));
        messageRepository.saveAll(unReadMessagse);
    }

    private void setRead(Conversation conversation, Long userId) {
        if(isCustomer(conversation, userId)) {
            conversation.setRead(conversation.isCustomerRead());
        } else {
            conversation.setRead(conversation.isOfferOwnerRead());
        }
    }

    private boolean isOfferOwner(Conversation conversation, Long userId) {
        return userId.equals(conversation.getOffer().getOwnerId());
    }

    private boolean isCustomer(Conversation conversation, Long userId) {
        return userId.equals(conversation.getCustomer().getId());
    }

}
