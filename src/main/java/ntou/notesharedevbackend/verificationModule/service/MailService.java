package ntou.notesharedevbackend.verificationModule.service;

import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.User;
import ntou.notesharedevbackend.userModule.service.UserService;

import ntou.notesharedevbackend.verificationModule.entity.SendMailRequest;
import ntou.notesharedevbackend.verificationModule.config.MailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Service
public class MailService {

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private UserRepository repository;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private JavaMailSenderImpl mailSender;

    @PostConstruct
    private void init() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailConfig.isAuthEnabled());
        props.put("mail.smtp.starttls.enable", mailConfig.isStarttlsEnabled());
        props.put("mail.transport.protocol", mailConfig.getProtocol());
    }

    public void randomPasswordMail( SendMailRequest request) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailConfig.getUsername());
        message.setTo(request.getReceivers());
        message.setSubject("NoteShare隨機密碼");//request.getSubject()
        message.setText("456");//request.getContent()

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
    public void resetPasswordMail(SendMailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailConfig.getUsername());
        message.setTo(request.getReceivers());
        message.setSubject("NoteShare 重設密碼成功");//request.getSubject()
        message.setText("重設密碼成功！");

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
    public void resendCodeMail(SendMailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailConfig.getUsername());
        message.setTo(request.getReceivers());
        message.setSubject("NoteShare 開通帳號驗證碼");//request.getSubject()
        message.setText("123");

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
