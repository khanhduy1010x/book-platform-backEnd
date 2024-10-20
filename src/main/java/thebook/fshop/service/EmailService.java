package thebook.fshop.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.spring6.SpringTemplateEngine;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {

    JavaMailSender mailSender;
    SpringTemplateEngine templateEngine;

    public void sendEmail(String name, String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("body", body);
        String htmlContent = templateEngine.process("forgotPasswordEmail", context);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}