package pl.nikowis.librin.service.impl;

import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.nikowis.librin.dto.MessageDTO;
import pl.nikowis.librin.model.Message;
import pl.nikowis.librin.service.WebsocketSenderService;

@Service
public class WebsocketSenderServiceImpl implements WebsocketSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketSenderServiceImpl.class);

    @Autowired
    private SimpMessagingTemplate websocketTemplate;

    @Autowired
    private MapperFacade mapperFacade;

    @Override
    @Async
    public void sendConversationUpdate(Message newMessage, Long recipientId, Long conversationId) {
        String destination = String.format("/users/%s/conversation/%s", recipientId, conversationId);
        LOGGER.info("Sending websocket message update to {}", destination);
        websocketTemplate.convertAndSend(destination, mapperFacade.map(newMessage, MessageDTO.class));

    }
}
