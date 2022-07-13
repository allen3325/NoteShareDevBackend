package ntou.notesharedevbackend.notificationModule.entity;

import ntou.notesharedevbackend.userModule.entity.*;

import java.util.*;

public class MessageReturn {
    private String message;
    private String type;
    private UserObj userObj;
    private String id;
    private Date date;

    public MessageReturn() {

    }

    public MessageReturn(Message message) {
        setMessage(message.getMessage());
        setType(message.getType());
        setUserObj(message.getUserObj());
        setId(message.getId());
        setDate(new Date());
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserObj getUserObj() {
        return userObj;
    }

    public void setUserObj(UserObj userObj) {
        this.userObj = userObj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
