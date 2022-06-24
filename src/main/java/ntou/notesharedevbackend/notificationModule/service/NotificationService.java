package ntou.notesharedevbackend.notificationModule.service;

import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class NotificationService {
    @Autowired
    private UserRepository userRepository;

    public void saveNotificationPrivate(String email, Message message) {
        AppUser appUser = userRepository.findByEmail(email);
        ArrayList<Message> notificationList = appUser.getNotification();
        notificationList.add(message);
        appUser.setNotification(notificationList);

        Integer unreadMessageCount = appUser.getUnreadMessageCount();
        unreadMessageCount++;
        appUser.setUnreadMessageCount(unreadMessageCount);

        userRepository.save(appUser);
    }

    public ArrayList<Message> getNotification(String email) {
        AppUser appUser = userRepository.findByEmail(email);
        return appUser.getNotification();
    }

    public void clearUnreadMessageCount(String email) {
        AppUser appUser = userRepository.findByEmail(email);
        appUser.setUnreadMessageCount(0);
        userRepository.save(appUser);
    }
}
