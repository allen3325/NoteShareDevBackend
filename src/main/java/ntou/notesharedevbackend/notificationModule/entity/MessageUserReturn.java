package ntou.notesharedevbackend.notificationModule.entity;

import ntou.notesharedevbackend.userModule.entity.*;

public class MessageUserReturn {
    private String user;
    private String content;
    private String time;

    private String userObjEmail;
    private String userObjName;
    private String userObjAvatar;

    public MessageUserReturn() {

    }

    public MessageUserReturn(Message message, UserObj userObj) {
        setUser(message.getUser());
        setContent(message.getContent());
        setTime(message.getTime());
        setUserObjEmail(userObj.getUserObjEmail());
        setUserObjName(userObj.getUserObjName());
        setUserObjAvatar(userObj.getUserObjAvatar());
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

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

    public String getUserObjEmail() {
        return userObjEmail;
    }

    public void setUserObjEmail(String userObjEmail) {
        this.userObjEmail = userObjEmail;
    }

    public String getUserObjName() {
        return userObjName;
    }

    public void setUserObjName(String userObjName) {
        this.userObjName = userObjName;
    }

    public String getUserObjAvatar() {
        return userObjAvatar;
    }

    public void setUserObjAvatar(String userObjAvatar) {
        this.userObjAvatar = userObjAvatar;
    }
}
