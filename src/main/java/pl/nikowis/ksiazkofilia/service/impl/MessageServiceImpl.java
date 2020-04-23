package pl.nikowis.ksiazkofilia.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
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
import pl.nikowis.ksiazkofilia.repository.MessageRepository;
import pl.nikowis.ksiazkofilia.repository.OfferRepository;
import pl.nikowis.ksiazkofilia.repository.UserRepository;
import pl.nikowis.ksiazkofilia.service.MessageService;
import pl.nikowis.ksiazkofilia.util.SecurityUtils;

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
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(ConversationNotFoundException::new);
        return mapperFacade.map(conversation, ConversationDTO.class);
    }

    @Override
    public ConversationDTO sendMessage(Long conversationId, SendMessageDTO messageDTO) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(ConversationNotFoundException::new);
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
        Offer offer = offerRepository.findById(createConversationDTO.getOfferId()).orElseThrow(OfferDoesntExistException::new);

        Conversation conversation = new Conversation();
        conversation.setCustomer(userRepository.findById(SecurityUtils.getCurrentUserId()).get());
        conversation.setOffer(offer);

        Conversation saved = conversationRepository.save(conversation);
        return mapperFacade.map(saved, ConversationDTO.class);
    }
}
