package pl.nikowis.librin.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.dto.ConversationDTO;
import pl.nikowis.librin.dto.CreateConversationDTO;
import pl.nikowis.librin.dto.SendMessageDTO;
import pl.nikowis.librin.exception.CantCreateConversationOnNonActiveOfferException;
import pl.nikowis.librin.exception.ConversationNotFoundException;
import pl.nikowis.librin.exception.OfferDoesntExistException;
import pl.nikowis.librin.model.Conversation;
import pl.nikowis.librin.model.Message;
import pl.nikowis.librin.model.Offer;
import pl.nikowis.librin.model.OfferStatus;
import pl.nikowis.librin.repository.ConversationRepository;
import pl.nikowis.librin.repository.OfferRepository;
import pl.nikowis.librin.repository.UserRepository;
import pl.nikowis.librin.service.MessageService;
import pl.nikowis.librin.util.SecurityUtils;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public ConversationDTO getConversation(Long conversationId) {
        Conversation conversation = conversationRepository.findByIdAndCustomerIdOrOfferOwnerId(conversationId, SecurityUtils.getCurrentUserId()).orElseThrow(ConversationNotFoundException::new);
        return mapperFacade.map(conversation, ConversationDTO.class);
    }

    @Override
    public ConversationDTO sendMessage(Long conversationId, SendMessageDTO messageDTO) {
        Conversation conversation = conversationRepository.findByIdAndCustomerIdOrOfferOwnerId(conversationId, SecurityUtils.getCurrentUserId()).orElseThrow(ConversationNotFoundException::new);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (!currentUserId.equals(conversation.getOffer().getOwnerId()) && !currentUserId.equals(conversation.getCustomer().getId())) {
            throw new ConversationNotFoundException();
        }

        Message newMessage = mapperFacade.map(messageDTO, Message.class);
        newMessage.setCreatedBy(currentUserId);
        newMessage.setConversation(conversation);
        conversation.getMessages().add(newMessage);
        conversation.setUpdatedAt(new Date());
        Conversation saved = conversationRepository.save(conversation);

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
        if(!OfferStatus.ACTIVE.equals(offer.getStatus())) {
            throw new CantCreateConversationOnNonActiveOfferException();
        }

        Conversation conversation = new Conversation();
        conversation.setCustomer(userRepository.findById(currentUser).get());
        conversation.setOffer(offer);

        Conversation saved = conversationRepository.save(conversation);
        return mapperFacade.map(saved, ConversationDTO.class);
    }

    @Override
    public Page<ConversationDTO> getUserConversations(Pageable pageable) {
        Page<Conversation> allByCustomerIdOrOfferOwnerId = conversationRepository.findAllByUserId(SecurityUtils.getCurrentUserId(), pageable);
        return allByCustomerIdOrOfferOwnerId.map(c -> mapperFacade.map(c, ConversationDTO.class));
    }
}
