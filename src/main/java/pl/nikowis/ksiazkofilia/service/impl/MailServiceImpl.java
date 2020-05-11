package pl.nikowis.ksiazkofilia.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.nikowis.ksiazkofilia.service.MailService;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);
    private static final String MAIL_LOC_PREFIX = "mail.";
    private static final String CONFIRM_EMAIL_PREFIX = "confirmemail.";
    private static final String RESET_PSWD_PREFIX = "resetpswd.";

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MessageSource messageSource;

    @Value("${sender.email.address}")
    private String senderEmailAddress;

    @Override
    public void sendEmailConfirmationMessage(String recipient, String confirmUrl) {
        String msgPrefix = MAIL_LOC_PREFIX + CONFIRM_EMAIL_PREFIX;
        createAndSend(recipient, confirmUrl, msgPrefix);
    }

    @Override
    public void sendResetPasswordEmail(String recipient, String resetUrl) {
        String msgPrefix = MAIL_LOC_PREFIX + RESET_PSWD_PREFIX;
        createAndSend(recipient, resetUrl, msgPrefix);
    }

    private void createAndSend(String recipient, String resetUrl, String msgPrefix) {
        Context context = new Context();
        context.setVariable("url", resetUrl);
        context.setVariable("title", getMsg(msgPrefix + "title"));
        context.setVariable("info", getMsg(msgPrefix + "info"));
        context.setVariable("linkText", getMsg(msgPrefix + "linkText"));
        context.setVariable("footer", getMsg(msgPrefix + "footer"));
        String messageText = templateEngine.process("emailWithLinkTemplate", context);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(senderEmailAddress);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(getMsg(msgPrefix + "subject"));
            messageHelper.setText(messageText, true);
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            LOGGER.error("Error sending email confirmation email to {}", recipient, e);
        }
    }

    private String getMsg(String key) {
        return messageSource.getMessage(key, new Object[]{}, LocaleContextHolder.getLocale());
    }
}
