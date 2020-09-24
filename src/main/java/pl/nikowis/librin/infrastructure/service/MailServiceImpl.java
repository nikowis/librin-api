package pl.nikowis.librin.infrastructure.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

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

    @Value("classpath:logo.png")
    private Resource brandLogoFile;

    @Override
    @Async
    public void sendEmailConfirmationMessage(String recipient, String confirmUrl, Locale locale) {
        String msgPrefix = MAIL_LOC_PREFIX + CONFIRM_EMAIL_PREFIX;
        createAndSend(recipient, confirmUrl, msgPrefix, locale);
    }

    @Override
    @Async
    public void sendResetPasswordEmail(String recipient, String resetUrl, Locale locale) {
        String msgPrefix = MAIL_LOC_PREFIX + RESET_PSWD_PREFIX;
        createAndSend(recipient, resetUrl, msgPrefix, locale);
    }

    private void createAndSend(String recipient, String resetUrl, String msgPrefix, Locale locale) {
        Context context = new Context();
        context.setVariable("url", resetUrl);
        context.setVariable("title", getMsg(msgPrefix + "title", locale));
        context.setVariable("info", getMsg(msgPrefix + "info", locale));
        context.setVariable("linkText", getMsg(msgPrefix + "linkText", locale));
        context.setVariable("footer", getMsg(msgPrefix + "footer", locale));
        context.setVariable("brandLogo", "brandLogo");
        String messageText = templateEngine.process("emailWithLinkTemplate", context);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setText(messageText, true);
            messageHelper.setFrom(senderEmailAddress);
            messageHelper.setTo(recipient);
            messageHelper.setSubject(getMsg("brand", locale) + ": " + getMsg(msgPrefix + "subject", locale));
            messageHelper.addInline("brandLogo", brandLogoFile, "image/png");
        };
        try {
            javaMailSender.send(messagePreparator);
        } catch (MailException e) {
            LOGGER.error("Error sending email confirmation email to {}", recipient, e);
        }
    }

    private String getMsg(String key, Locale locale) {
        return messageSource.getMessage(key, new Object[]{}, locale);
    }
}
