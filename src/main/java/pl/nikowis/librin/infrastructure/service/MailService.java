package pl.nikowis.librin.infrastructure.service;

import java.util.Locale;

public interface MailService {

    void sendEmailConfirmationMessage(String recipient, String confirmUrl, Locale locale);

    void sendResetPasswordEmail(String email, String confirmUrl, Locale locale);
}
