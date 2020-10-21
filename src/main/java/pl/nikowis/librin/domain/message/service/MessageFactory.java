package pl.nikowis.librin.domain.message.service;

import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.nikowis.librin.domain.message.dto.SendMessageDTO;
import pl.nikowis.librin.domain.message.model.Message;

import java.util.Date;

@Component
public class MessageFactory {

    @Autowired
    private MapperFacade mapperFacade;

    Message createMessage(Long conversationId, Long currentUserId, SendMessageDTO messageDTO) {
        Message newMessage = mapperFacade.map(messageDTO, Message.class);
        newMessage.setCreatedBy(currentUserId);
        newMessage.setConversationId(conversationId);
        newMessage.setCreatedAt(new Date());
        return newMessage;
    }
}
