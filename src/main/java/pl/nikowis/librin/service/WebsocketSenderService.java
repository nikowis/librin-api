package pl.nikowis.librin.service;

import pl.nikowis.librin.model.Message;

public interface WebsocketSenderService {

    void sendConversationUpdate(Message newMessage, Long recipientId, Long conversationId);
}
