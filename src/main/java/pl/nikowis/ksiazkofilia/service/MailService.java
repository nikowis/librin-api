package pl.nikowis.ksiazkofilia.service;

public interface MailService {

    void sendEmailConfirmationMessage(String recipient, String confirmUrl);

    void sendResetPasswordEmail(String email, String confirmUrl);
}
