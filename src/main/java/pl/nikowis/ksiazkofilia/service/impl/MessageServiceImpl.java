package pl.nikowis.ksiazkofilia.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.ksiazkofilia.dto.ConversationDTO;
import pl.nikowis.ksiazkofilia.dto.CreateConversationDTO;
import pl.nikowis.ksiazkofilia.dto.SendMessageDTO;
import pl.nikowis.ksiazkofilia.exception.ConversationNotFoundException;
import pl.nikowis.ksiazkofilia.exception.OfferDoesntExistException;
import pl.nikowis.ksiazkofilia.model.Conversation;
import pl.nikowis.ksiazkofilia.model.Message;
import pl.nikowis.ksiazkofilia.model.Offer;
import pl.nikowis.ksiazkofilia.repository.ConversationRepository;
import pl.nikowis.ksiazkofilia.repository.OfferRepository;
import pl.nikowis.ksiazkofilia.repository.UserRepository;
import pl.nikowis.ksiazkofilia.service.MessageService;
import pl.nikowis.ksiazkofilia.util.SecurityUtils;

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
        Conversation saved = conversationRepository.save(conversation);

        return mapperFacade.map(saved, ConversationDTO.class);
    }

    @Override
    public ConversationDTO createConversation(CreateConversationDTO createConversationDTO) {
        Long currentUser = SecurityUtils.getCurrentUserId();
        Optional<Conversation> conv = conversationRepository.findByUserAndOfferId(createConversationDTO.getOfferId(), currentUser);
        if(conv.isPresent()) {
            return mapperFacade.map(conv.get(), ConversationDTO.class);
        }

        Offer offer = offerRepository.findById(createConversationDTO.getOfferId()).orElseThrow(OfferDoesntExistException::new);
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
