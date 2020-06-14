package pl.nikowis.librin.service;

import pl.nikowis.librin.model.Message;

public interface WebsocketSenderService {

    void sendConversationUpdate(Message newMessage, String recipientEmail, Long conversationId);
}
