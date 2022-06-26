package ntou.notesharedevbackend.noteNodule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    // constructor
    public NoteFolderReturn(Note note) {
        this.id = note.getId();
        this.subject = note.getSubject();
        this.title = note.getTitle();
        this.description = note.getDescription();
        this.publishDate = note.getPublishDate();
        this.isPublic = note.getPublic();
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
}
