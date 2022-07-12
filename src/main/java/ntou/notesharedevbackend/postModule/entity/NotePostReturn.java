package ntou.notesharedevbackend.postModule.entity;

import ntou.notesharedevbackend.userModule.entity.UserObj;
;
import java.util.Date;

public class NotePostReturn {
    private String noteID;
    private Date date;
    private UserObj userObj;
    public NotePostReturn(){};

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
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
