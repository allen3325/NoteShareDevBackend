package ntou.notesharedevbackend.notificationModule.controller;
import ntou.notesharedevbackend.notificationModule.entity.*;
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
    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message/{postID}")
    public void getMessage(@DestinationVariable String postID, final Message message) throws InterruptedException {
        Thread.sleep(1000);
        messagingTemplate.convertAndSend("/topic/messages/" + postID, message);
        messagingTemplate.convertAndSend("/topic/global-notifications/" + postID, "Global Notification");
    }

    @MessageMapping("/private-message")
    public void getPrivateMessage(final Message message, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/private-messages", message);
        messagingTemplate.convertAndSendToUser(principal.getName(),"/topic/private-notifications", "Private Notification");
    }

//    @Scheduled(fixedRate = 5000)
    public void sendScheduledMessage() {
        messagingTemplate.convertAndSend("/topic/scheduled-messages", "Hello World");
    }
}
