package pl.nikowis.librin.infrastructure.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.nikowis.librin.domain.conversation.dto.WsConversationUpdateDTO;

@Service
public class WebsocketSenderServiceImpl implements WebsocketSenderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketSenderServiceImpl.class);

    @Autowired
    private SimpMessagingTemplate websocketTemplate;

    @Override
    @Async
    public void sendConversationUpdate(WsConversationUpdateDTO wsUpdate, String recipientEmail, Long conversationId) {
        String destination = String.format("/queue/conversation/%s", conversationId);
        LOGGER.info("Sending websocket message update to {}", destination);
        websocketTemplate.convertAndSendToUser(recipientEmail, destination, wsUpdate);
    }
}
