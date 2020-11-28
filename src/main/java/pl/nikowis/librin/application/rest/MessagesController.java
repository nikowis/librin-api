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
import pl.nikowis.librin.domain.message.dto.ConversationDTO;
import pl.nikowis.librin.domain.message.dto.ConversationWithoutMessagesDTO;
import pl.nikowis.librin.domain.message.dto.CreateConversationDTO;
import pl.nikowis.librin.domain.message.dto.SendMessageDTO;
import pl.nikowis.librin.domain.message.service.MessageService;


@RestController
@RequestMapping(path = MessagesController.CONVERSATIONS_ENDPOINT)
public class MessagesController {

    public static final String CONVERSATIONS_ENDPOINT = "/conversations";
    public static final String CONVERSATION_ID_VARIABLE = "conversationId";
    public static final String CONVERSATION_PATH = "/{" + CONVERSATION_ID_VARIABLE + "}";
    public static final String MESSAGES_PATH = CONVERSATION_PATH + "/messages";
    public static final String CONVERSATION_READ = CONVERSATION_PATH + "/read";
    public static final String CONVERSATION_ENDPOINT = CONVERSATIONS_ENDPOINT + CONVERSATION_PATH;
    public static final String MESSAGES_ENDPOINT = CONVERSATIONS_ENDPOINT + CONVERSATION_PATH + MESSAGES_PATH;

    @Autowired
    private MessageService messageService;

    @GetMapping(path = CONVERSATION_PATH)
    public ConversationDTO conversation(@PathVariable(CONVERSATION_ID_VARIABLE) Long conversationId) {
        return messageService.getConversation(conversationId);
    }

    @PostMapping(path = CONVERSATION_PATH)
    public ConversationDTO sendMessage(@PathVariable(CONVERSATION_ID_VARIABLE) Long conversationId, @Validated @RequestBody SendMessageDTO messageDTO) {
        return messageService.sendMessage(conversationId, messageDTO);
    }

    @PutMapping(path = CONVERSATION_READ)
    public void markConversationRead(@PathVariable(CONVERSATION_ID_VARIABLE) Long conversationId) {
        messageService.getConversation(conversationId);
    }

    @GetMapping
    public Page<ConversationWithoutMessagesDTO> getUserConversations(Pageable pageable) {
        return messageService.getUserConversations(pageable);
    }

    @PostMapping
    public ConversationDTO createConversation(@Validated @RequestBody CreateConversationDTO createConversationDTO) {
        return messageService.createConversation(createConversationDTO);
    }

}
