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

    @MessageMapping("/message/{noteID}")
    public void getMessage(@DestinationVariable String noteID, final Message message, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println(principal.getName());
        messagingTemplate.convertAndSend("/topic/messages/" + noteID, message);
        notificationService.saveNotificationPublic(noteID, message);
    }

    @MessageMapping("/private-message")
    public void getPrivateMessage(final Message message, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/private-messages", message);
        notificationService.saveNotificationPrivate(principal.getName(), message);
    }

//    @Scheduled(fixedRate = 5000)
    public void sendScheduledMessage() {
        messagingTemplate.convertAndSend("/topic/scheduled-messages", "Hello World");
    }
}
