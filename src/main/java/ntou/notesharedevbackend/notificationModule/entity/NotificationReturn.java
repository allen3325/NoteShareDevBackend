package ntou.notesharedevbackend.notificationModule.entity;

import ntou.notesharedevbackend.userModule.entity.*;

import java.util.*;

public class NotificationReturn {
    ArrayList<MessageReturn> messageReturn;
    Integer unreadMessageCount;

    public NotificationReturn() {

    }

    public NotificationReturn(ArrayList<MessageReturn> messageReturn, AppUser appUser) {
        setMessageReturn(messageReturn);
        setUnreadMessageCount(appUser.getUnreadMessageCount());
    }

    public ArrayList<MessageReturn> getMessageReturn() {
        return messageReturn;
    }

    public void setMessageReturn(ArrayList<MessageReturn> messageReturn) {
        this.messageReturn = messageReturn;
    }

    public Integer getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(Integer unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }
}
