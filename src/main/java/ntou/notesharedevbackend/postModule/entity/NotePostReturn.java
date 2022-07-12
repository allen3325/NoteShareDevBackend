package ntou.notesharedevbackend.postModule.entity;

import ntou.notesharedevbackend.userModule.entity.UserObj;
;
import java.util.Date;

public class NotePostReturn {
    private String id;
    private Date date;
    private UserObj userObj;

    public NotePostReturn() {
    }

    ;

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

    public UserObj getUserObj() {
        return userObj;
    }

    public void setUserObj(UserObj userObj) {
        this.userObj = userObj;
    }
}
