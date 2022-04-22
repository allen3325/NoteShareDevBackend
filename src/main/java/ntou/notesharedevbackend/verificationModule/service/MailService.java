package ntou.notesharedevbackend.verificationModule.service;

import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.User;
import ntou.notesharedevbackend.userModule.service.UserService;

import ntou.notesharedevbackend.verificationModule.entity.SendMailRequest;
import ntou.notesharedevbackend.verificationModule.config.MailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.Random;

@Service
public class MailService {

    @Autowired
    private MailConfig mailConfig;


    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";
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

    public void randomPasswordMail(String userID, SendMailRequest request) {
        System.out.println(userID);
        String randPassword = randomPassword();
        UserService userService=new UserService();
        User oldUser = userService.getUser(userID);//拿id去找email，就不用Request email


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
    public void resendCodeMail(SendMailRequest request) { //每次季都換一組 卡好寫
        String randomCode = randomCode();
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

    public static String randomPassword(){
        char[] chars = new char[8];
        Random rnd = new Random();
        for(int i =0 ; i < 8; i ++){
            chars[i]= nextChar(rnd);
        }
        return new String(chars);
    }

    private static char nextChar(Random rnd){
        switch(rnd.nextInt(4)){
            case 0:
                return (char)('a' + rnd.nextInt(26));
            case 1:
                return (char)('A' + rnd.nextInt(26));
            case 2:
                return (char)('0' + rnd.nextInt(10));
            default:
                return SPECIAL_CHARS.charAt(rnd.nextInt(SPECIAL_CHARS.length()));
        }
    }

    public static String randomCode(){
        char[] chars = new char[4];
        Random rnd = new Random();
        for(int i = 0; i < 4; i++){
            chars[i]=(char)('0'+rnd.nextInt(10));
        }
        return new String(chars);
    }
}
