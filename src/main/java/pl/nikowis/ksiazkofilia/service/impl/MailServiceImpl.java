package pl.nikowis.ksiazkofilia.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import pl.nikowis.ksiazkofilia.service.MailService;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmailConfirmationMessage(String recipient, String confirmUrl) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("pomoc@ksiazkofilia.pl");
            messageHelper.setTo(recipient);
            messageHelper.setSubject("Confirm account email");
            messageHelper.setText(confirmUrl);
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            LOGGER.error("Error sending email confirmation email to {}", recipient, e);
        }
    }

}
