package ntou.notesharedevbackend.notificationModule.service;

import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.noteNodule.service.*;
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

    public void saveNotificationGroup(String noteID, MessageReturn message) {
        Note note = noteService.getNote(noteID);
        ArrayList<String> authorEmail = note.getAuthorEmail();
        for (String email: authorEmail)
            saveNotificationPrivate(email, message);
    }

    public void sendToManagerAndHeader(String noteID, MessageReturn message) {
        Note note = noteService.getNote(noteID);
        String managerEmail = note.getManagerEmail();
        String headerEmail = note.getHeaderEmail();
        if (managerEmail != null) {
            messagingTemplate.convertAndSendToUser(managerEmail, "/topic/private-messages" + noteID, message);
            saveNotificationPrivate(managerEmail, message);
        }
        messagingTemplate.convertAndSendToUser(headerEmail, "/topic/private-messages" + noteID, message);
        saveNotificationPrivate(headerEmail, message);
    }

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
}
