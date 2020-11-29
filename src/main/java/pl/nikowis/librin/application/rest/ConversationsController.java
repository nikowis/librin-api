package pl.nikowis.librin.application.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.domain.conversation.dto.ConversationDTO;
import pl.nikowis.librin.domain.conversation.dto.CreateConversationDTO;
import pl.nikowis.librin.domain.conversation.dto.MessageDTO;
import pl.nikowis.librin.domain.conversation.dto.SendMessageDTO;
import pl.nikowis.librin.domain.conversation.service.ConversationService;


@RestController
@RequestMapping(path = ConversationsController.CONVERSATIONS_ENDPOINT)
public class ConversationsController {

    public static final String CONVERSATIONS_ENDPOINT = "/conversations";
    public static final String CONVERSATION_ID_VARIABLE = "conversationId";
    public static final String CONVERSATION_PATH = "/{" + CONVERSATION_ID_VARIABLE + "}";
    public static final String MESSAGES_PATH = CONVERSATION_PATH + "/messages";
    public static final String CONVERSATION_READ = CONVERSATION_PATH + "/read";
    public static final String CONVERSATION_ENDPOINT = CONVERSATIONS_ENDPOINT + CONVERSATION_PATH;
    public static final String MESSAGES_ENDPOINT = CONVERSATIONS_ENDPOINT + MESSAGES_PATH;

    @Autowired
    private ConversationService messageService;

    @GetMapping(path = CONVERSATION_PATH)
    public ConversationDTO conversation(@PathVariable(CONVERSATION_ID_VARIABLE) Long conversationId) {
        return messageService.getConversation(conversationId);
    }

    @PostMapping(path = CONVERSATION_PATH)
    public MessageDTO sendMessage(@PathVariable(CONVERSATION_ID_VARIABLE) Long conversationId, @Validated @RequestBody SendMessageDTO messageDTO) {
        return messageService.sendMessage(conversationId, messageDTO);
    }

    @PutMapping(path = CONVERSATION_READ)
    public void markConversationRead(@PathVariable(CONVERSATION_ID_VARIABLE) Long conversationId) {
        messageService.getConversation(conversationId);
    }

    @GetMapping
    public Page<ConversationDTO> getUserConversations(Pageable pageable) {
        return messageService.getUserConversations(pageable);
    }

    @PostMapping
    public ConversationDTO createConversation(@Validated @RequestBody CreateConversationDTO createConversationDTO) {
        return messageService.createConversation(createConversationDTO);
    }

    @GetMapping(path = MESSAGES_PATH)
    public Page<MessageDTO> getConversationMessages(@PathVariable(CONVERSATION_ID_VARIABLE) Long conversationId, Pageable pageable) {
        return messageService.getConversationMessages(conversationId, pageable);
    }


}
