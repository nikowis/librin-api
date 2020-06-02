package pl.nikowis.librin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.dto.ConversationDTO;
import pl.nikowis.librin.dto.CreateConversationDTO;
import pl.nikowis.librin.dto.SendMessageDTO;
import pl.nikowis.librin.service.MessageService;


@RestController
@RequestMapping(path = MessagesController.CONVERSATIONS_ENDPOINT)
public class MessagesController {

    public static final String CONVERSATIONS_ENDPOINT = "/messages";
    public static final String CONVERSATION_ID_VARIABLE = "conversationId";
    public static final String CONVERSATION_PATH = "/{" + CONVERSATION_ID_VARIABLE + "}";
    public static final String CONVERSATION_ENDPOINT = CONVERSATIONS_ENDPOINT + CONVERSATION_PATH;

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

    @GetMapping
    public Page<ConversationDTO> getUserConversations(Pageable pageable) {
        return messageService.getUserConversations(pageable);
    }

    @PostMapping
    public ConversationDTO createConversation(@Validated @RequestBody CreateConversationDTO createConversationDTO) {
        return messageService.createConversation(createConversationDTO);
    }

}