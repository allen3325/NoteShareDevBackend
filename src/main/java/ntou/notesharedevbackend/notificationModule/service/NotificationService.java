package ntou.notesharedevbackend.notificationModule.service;

import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.noteNodule.service.*;
import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.userModule.entity.*;
import ntou.notesharedevbackend.userModule.service.*;
import org.springframework.beans.factory.annotation.*;
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

    public void saveNotificationGroup(String noteID, Message message) {
        Note note = noteService.getNote(noteID);
        ArrayList<String> authorEmail = note.getAuthorEmail();
        for (String email: authorEmail)
            saveNotificationPrivate(email, message);
    }

    public void saveNotificationBell(String email, Message message) {
        AppUser appUser = appUserService.getUserByEmail(email);
        ArrayList<String> bellSubscribers = appUser.getBell();
        for (String bellSubscriber: bellSubscribers)
            saveNotificationPrivate(bellSubscriber, message);
    }

    public void saveNotificationPrivate(String email, Message message) {
        AppUser appUser = appUserService.getUserByEmail(email);
        ArrayList<Message> notificationList = appUser.getNotification();
        notificationList.add(message);
        appUser.setNotification(notificationList);

        Integer unreadMessageCount = appUser.getUnreadMessageCount();
        unreadMessageCount++;
        appUser.setUnreadMessageCount(unreadMessageCount);

        userRepository.save(appUser);
    }

    public ArrayList<Message> getNotification(String email) {
        AppUser appUser = appUserService.getUserByEmail(email);
        appUser.setUnreadMessageCount(0);
        userRepository.save(appUser);

        return appUser.getNotification();
    }

    public MessageReturn getUserInfo(Message message) {
        MessageReturn messageReturn = new MessageReturn();
        messageReturn.setContent(message.getContent());
        messageReturn.setTime(message.getTime());

        UserObj userObj = appUserService.getUserInfo(message.getUser());
        messageReturn.setMessageObj(userObj);

        return messageReturn;
    }
}
