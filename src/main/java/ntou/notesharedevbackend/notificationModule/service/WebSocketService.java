package ntou.notesharedevbackend.notificationModule.service;

import ntou.notesharedevbackend.notificationModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.*;

@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate, NotificationService notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontend(final String message) {
        ResponseMessage response = new ResponseMessage(message);

        notificationService.sendGlobalNotification();
        messagingTemplate.convertAndSend("/topic/messages", response);
    }

    public void notifyUser(final String id, final String message) {
        ResponseMessage response = new ResponseMessage(message);

        notificationService.sendPrivateNotification(id);
        messagingTemplate.convertAndSendToUser(id, "/topic/private-messages", response);
    }
}
