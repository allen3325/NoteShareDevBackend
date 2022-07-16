package ntou.notesharedevbackend.notificationModule.controller;
import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.notificationModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.security.*;

@EnableScheduling
@Controller
public class MessageController {
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private NotificationService notificationService;

    @Autowired
    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

//    @MessageMapping("/group-messages/{noteID}")
//    public void getGroupMessage(@DestinationVariable String noteID, final Message message) throws InterruptedException {
//        Thread.sleep(1000);
//        MessageReturn messageReturn = new MessageReturn(message);
//        messagingTemplate.convertAndSend("/topic/group-messages/" + noteID, messageReturn);
//        notificationService.saveNotificationGroup(noteID, messageReturn);
//    }
//
//    @MessageMapping("/group-messages-manager/{noteID}")
//    public void getGroupMessageForManager(@DestinationVariable String noteID, final Message message) throws InterruptedException {
//        Thread.sleep(1000);
//        MessageReturn messageReturn = new MessageReturn(message);
//        notificationService.sendToManagerAndHeader(noteID, messageReturn);
//    }

    @MessageMapping("/bell-messages/{email}")
    public void getBellMessage(@DestinationVariable String email, final Message message) throws  InterruptedException {
        Thread.sleep(1000);
        MessageReturn messageReturn = new MessageReturn(message);
        messagingTemplate.convertAndSend("/topic/bell-messages/" + email, messageReturn);
        notificationService.saveNotificationBell(email, messageReturn);
    }

    @MessageMapping("/private-messages")
    public void getPrivateMessage(final Message message) throws InterruptedException {
        Thread.sleep(1000);
        MessageReturn messageReturn = new MessageReturn(message);
        messagingTemplate.convertAndSendToUser(message.getReceiverEmail(), "/topic/private-messages", messageReturn);
        notificationService.saveNotificationPrivate(message.getReceiverEmail(), messageReturn);
    }

//    @Scheduled(fixedRate = 5000)
    public void sendScheduledMessage() {
        messagingTemplate.convertAndSend("/topic/scheduled-messages", "Hello World");
    }
}
