package ntou.notesharedevbackend.notificationModule.entity;

import lombok.*;

@ToString
public class MessageWithReceiver {
    private String user;
    private String content;
    private String time;
    private String receiver;

    public MessageWithReceiver() {

    }

    public MessageWithReceiver(Message message) {
        this.user = message.getUser();
        this.content = message.getContent();
        this.time = message.getTime();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}

