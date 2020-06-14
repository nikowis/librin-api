package pl.nikowis.librin.service;

import pl.nikowis.librin.model.WsConversationUpdateDTO;

public interface WebsocketSenderService {

    void sendConversationUpdate(WsConversationUpdateDTO wsUpdate, String recipientEmail, Long conversationId);
}
