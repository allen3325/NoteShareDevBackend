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

    @MessageMapping("/many-to-many-message/{noteID}")
    public void getGroupMessage(@DestinationVariable String noteID, final Message message, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        messagingTemplate.convertAndSend("/topic/group-messages/" + noteID, message);
        notificationService.saveNotificationGroup(noteID, message);
    }

    @MessageMapping("/one-to-many-message/{email}")
    public void getBellMessage(@DestinationVariable String email, final Message message, final Principal principal) throws  InterruptedException {
        Thread.sleep(1000);
        messagingTemplate.convertAndSend("/topic/bell-messages/" + email, message);
        notificationService.saveNotificationBell(email, message);
    }

    @MessageMapping("/one-to-one-message")
    public void getPrivateMessage(final MessageWithReceiver messageWithReceiver, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);

        Message message = new Message(messageWithReceiver);
        messagingTemplate.convertAndSendToUser(messageWithReceiver.getReceiver(), "/topic/private-messages", message);
        notificationService.saveNotificationPrivate(messageWithReceiver.getReceiver(), message);
    }

//    @Scheduled(fixedRate = 5000)
    public void sendScheduledMessage() {
        messagingTemplate.convertAndSend("/topic/scheduled-messages", "Hello World");
    }
}
