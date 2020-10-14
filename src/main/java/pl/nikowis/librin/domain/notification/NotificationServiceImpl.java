package pl.nikowis.librin.domain.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.nikowis.librin.infrastructure.service.MailService;
import pl.nikowis.librin.kernel.UserEmail;

import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private MailService mailService;

    @Override
    @Async
    public void notifyUserRegistered(UserEmail email, UUID tokenId, String confirmEmailBaseUrl) {
        String confirmUrl = confirmEmailBaseUrl + "/" + tokenId.toString();
        mailService.sendEmailConfirmationMessage(email.toString(), confirmUrl, LocaleContextHolder.getLocale());
    }

    @Override
    public void sendUserResetPasswordRequestCreated(UserEmail email, UUID id, String confirmEmailBaseUrl) {
        String confirmUrl = confirmEmailBaseUrl + "/" + id.toString();
        mailService.sendResetPasswordEmail(email.toString(), confirmUrl, LocaleContextHolder.getLocale());
    }
}
