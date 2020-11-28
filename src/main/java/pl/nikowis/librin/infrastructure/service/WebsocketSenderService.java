package pl.nikowis.librin.infrastructure.service;

import pl.nikowis.librin.domain.conversation.dto.WsConversationUpdateDTO;

public interface WebsocketSenderService {

    void sendConversationUpdate(WsConversationUpdateDTO wsUpdate, String recipientEmail, Long conversationId);
}
