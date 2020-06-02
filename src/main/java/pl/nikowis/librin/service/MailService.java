package pl.nikowis.librin.service;

public interface MailService {

    void sendEmailConfirmationMessage(String recipient, String confirmUrl);

    void sendResetPasswordEmail(String email, String confirmUrl);
}
