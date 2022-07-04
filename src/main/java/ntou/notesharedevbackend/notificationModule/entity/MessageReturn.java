package ntou.notesharedevbackend.notificationModule.entity;

import ntou.notesharedevbackend.userModule.entity.*;

public class MessageReturn {
    private String content;
    private String time;

    private UserObj messageObj;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserObj getMessageObj() {
        return messageObj;
    }

    public void setMessageObj(UserObj messageObj) {
        this.messageObj = messageObj;
    }
}
