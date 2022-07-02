package ntou.notesharedevbackend.verificationModule.entity.service;

import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;

import ntou.notesharedevbackend.verificationModule.config.MailConfig;
import ntou.notesharedevbackend.verificationModule.entity.AuthRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.Random;

@Service
public class MailService {

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private AppUserService userService;

    private BCryptPasswordEncoder passwordEncoder;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";
    private JavaMailSenderImpl mailSender;

    @PostConstruct
    private void init() {
        passwordEncoder = new BCryptPasswordEncoder();
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());
        mailSender.setProtocol(mailConfig.getProtocol());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", mailConfig.isAuthEnabled());
        props.put("mail.smtp.starttls.enable", mailConfig.isStarttlsEnabled());
        props.put("mail.transport.protocol", mailConfig.getProtocol());
    }

    public void randomPasswordMail(String email) {

        AppUser user = userService.getUserByEmail(email);//拿id去找email，就不用Request email
        String password = genRandomPassword();
        userService.replacePassword(email,password);

        sendEmailToUser(user.getEmail(),"NoteShare Random Password",password);
    }
    public void resetPasswordMail(AuthRequest request) {
        AppUser user = userService.getUserByEmail(request.getEmail());
        if(passwordEncoder.matches(request.getPassword(),user.getPassword())){
            userService.replacePassword(request.getEmail(), request.getNewPassword());
        }
        else{
            System.out.println("wrong password");
            return;
        }

        sendEmailToUser(user.getEmail(),"NoteShare Password Reset Successfully" ,"Password reset Successfully!");
    }
    public void resendCodeMail(String email) {
        AppUser user = userService.getUserByEmail(email);
        user.setVerifyCode(randomCode());
        userService.replaceUser(user);

        sendEmailToUser(user.getEmail(),"NoteShare verification code" ,user.getVerifyCode());
    }

    public void sendEmailToUser(String email,String subject,String content){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailConfig.getUsername());
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            LOGGER.error(e.getMessage());
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static String genRandomPassword(){
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
