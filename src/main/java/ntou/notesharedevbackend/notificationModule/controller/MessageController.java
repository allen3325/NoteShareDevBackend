package ntou.notesharedevbackend.notificationModule.controller;

import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.notificationModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.util.*;

import java.security.*;

@Controller
public class MessageController {
    @Autowired
    private NotificationService notificationService;

    @MessageMapping("/message/{postID}")
    @SendTo("/topic/messages/{postID}")
    public ResponseMessage getMessage(@DestinationVariable String postID, final Message message) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendGlobalNotification(postID);
        return new ResponseMessage(HtmlUtils.htmlEscape(message.getMessageContent()));
    }

    @MessageMapping("/private-message")
    @SendToUser("/topic/private-messages")
    public ResponseMessage getPrivateMessage(final Message message,
                                             final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        notificationService.sendPrivateNotification(principal.getName());
        return new ResponseMessage(HtmlUtils.htmlEscape(
                "Sending private message to user " + principal.getName() + ": "
                        + message.getMessageContent())
        );
    }
}
