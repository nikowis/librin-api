package pl.nikowis.ksiazkofilia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.nikowis.ksiazkofilia.dto.ConversationDTO;
import pl.nikowis.ksiazkofilia.dto.CreateConversationDTO;
import pl.nikowis.ksiazkofilia.dto.SendMessageDTO;

public interface MailService {

    void sendEmailConfirmationMessage(String recipient, String confirmUrl);
}
