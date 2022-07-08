package ntou.notesharedevbackend.noteNodule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ntou.notesharedevbackend.userModule.entity.UserObj;

import java.util.ArrayList;
import java.util.Date;

public class NoteFolderReturn {
    // field
    private String id;
    private String subject;
    private String title; // title
    private String description;
    @JsonFormat(timezone = "GMT+08:00")
    private Date publishDate; // publish 後更新，預設為 NULL
    private Boolean isPublic; // 筆記是否公開
    private String headerEmail; // 建立筆記者
    private String headerName; // 建立筆記者
    private ArrayList<String> authorEmail; // 所有作者
    private ArrayList<String> authorName; // 所有作者
    private String managerEmail; // 共筆用
    private UserObj headerUserObj;
    private UserObj managerUserObj;
    private ArrayList<UserObj> authorUserObj;

    // constructor
    public NoteFolderReturn(Note note) {
        this.id = note.getId();
        this.subject = note.getSubject();
        this.title = note.getTitle();
        this.description = note.getDescription();
        this.publishDate = note.getPublishDate();
        this.isPublic = note.getPublic();
        this.headerEmail = note.getHeaderEmail();
        this.headerName = note.getHeaderName();
        this.authorEmail = note.getAuthorEmail();
        this.authorName = note.getAuthorName();
    }
    // getter and setter

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeaderEmail() {
        return headerEmail;
    }

    public void setHeaderEmail(String headerEmail) {
        this.headerEmail = headerEmail;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public ArrayList<String> getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(ArrayList<String> authorEmail) {
        this.authorEmail = authorEmail;
    }

    public ArrayList<String> getAuthorName() {
        return authorName;
    }

    public void setAuthorName(ArrayList<String> authorName) {
        this.authorName = authorName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public UserObj getHeaderUserObj() {
        return headerUserObj;
    }

    public void setHeaderUserObj(UserObj headerUserObj) {
        this.headerUserObj = headerUserObj;
    }

    public UserObj getManagerUserObj() {
        return managerUserObj;
    }

    public void setManagerUserObj(UserObj managerUserObj) {
        this.managerUserObj = managerUserObj;
    }

    public ArrayList<UserObj> getAuthorUserObj() {
        return authorUserObj;
    }

    public void setAuthorUserObj(ArrayList<UserObj> authorUserObj) {
        this.authorUserObj = authorUserObj;
    }
}
