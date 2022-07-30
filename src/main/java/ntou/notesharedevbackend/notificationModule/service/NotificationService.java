package ntou.notesharedevbackend.notificationModule.service;

import ntou.notesharedevbackend.noteModule.entity.*;
import ntou.notesharedevbackend.noteModule.service.*;
import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.userModule.entity.*;
import ntou.notesharedevbackend.userModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class NotificationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteService noteService;
    @Autowired
    private AppUserService appUserService;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

//    public void saveNotificationGroup(String noteID, MessageReturn message) {
//        Note note = noteService.getNote(noteID);
//        ArrayList<String> authorEmail = note.getAuthorEmail();
//        for (String email: authorEmail)
//            saveNotificationPrivate(email, message);
//    }

//    public void sendToManagerAndHeader(String noteID, MessageReturn message) {
//        Note note = noteService.getNote(noteID);
//        String managerEmail = note.getManagerEmail();
//        String headerEmail = note.getHeaderEmail();
//        if (managerEmail != null) {
//            messagingTemplate.convertAndSendToUser(managerEmail, "/topic/private-messages", message);
//            saveNotificationPrivate(managerEmail, message);
//        }
//        messagingTemplate.convertAndSendToUser(headerEmail, "/topic/private-messages", message);
//        saveNotificationPrivate(headerEmail, message);
//    }

    public void saveNotificationBell(String email, MessageReturn message) {
        AppUser appUser = appUserService.getUserByEmail(email);
        ArrayList<String> bellSubscribers = appUser.getBelledBy();
        for (String bellSubscriber: bellSubscribers)
            saveNotificationPrivate(bellSubscriber, message);
    }

    public void saveNotificationPrivate(String email, MessageReturn message) {
        AppUser appUser = appUserService.getUserByEmail(email);
        ArrayList<MessageReturn> notificationList = appUser.getNotification();
        notificationList.add(message);
        appUser.setNotification(notificationList);

        Integer unreadMessageCount = appUser.getUnreadMessageCount();
        unreadMessageCount++;
        appUser.setUnreadMessageCount(unreadMessageCount);

        userRepository.save(appUser);
    }

    public ArrayList<MessageReturn> getNotification(String email) {
        AppUser appUser = appUserService.getUserByEmail(email);
        return appUser.getNotification();
    }

    public void clearUnreadMessage(String email) {
        AppUser appUser = appUserService.getUserByEmail(email);
        appUser.setUnreadMessageCount(0);
        userRepository.save(appUser);
    }

    public MessageReturn getMessageReturn(String senderEmail, String message, String type, String id) {
        MessageReturn messageReturn = new MessageReturn();
        UserObj userObj = appUserService.getUserInfo(senderEmail);
        messageReturn.setMessage(userObj.getUserObjName() + message);
        messageReturn.setUserObj(userObj);
        messageReturn.setType(type);
        messageReturn.setId(id);
        messageReturn.setDate(new Date());
        return messageReturn;
    }

    public MessageReturn getMessageReturnFromVotes(String result, String postID, boolean isKickTarget, String kickTarget) {
        MessageReturn messageReturn = new MessageReturn();
        UserObj kickTargetObj = appUserService.getUserInfo(kickTarget);
        UserObj userObj = new UserObj();
        userObj.setUserObjEmail("noteshare@gmail.com");
        userObj.setUserObjName("NoteShare System");
        userObj.setUserObjAvatar("https://i.imgur.com/5V1waq3.png");
        messageReturn.setUserObj(userObj);
        messageReturn.setType("collaboration");
        messageReturn.setId(postID);
        messageReturn.setDate(new Date());
        if (result.equals("agree kick")) {
            if (isKickTarget)   //被踢出本人收到的訊息
                messageReturn.setMessage("You are kicked out from the Collaboration");
            else    //群組其他人收到的訊息
                messageReturn.setMessage(kickTargetObj.getUserObjName() + " has been kicked out from the Collaboration");
        }
        else if (result.equals("disagree kick")) {
            messageReturn.setMessage(kickTargetObj.getUserObjName() + " has not been kicked out from the Collaboration");
        }
        else {
            messageReturn.setMessage("Invalid vote");
        }
        return messageReturn;
    }
}
