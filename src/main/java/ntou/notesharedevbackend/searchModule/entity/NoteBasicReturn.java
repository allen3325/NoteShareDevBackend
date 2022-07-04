package ntou.notesharedevbackend.searchModule.entity;

import com.fasterxml.jackson.annotation.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.userModule.entity.*;

import java.util.*;

public class NoteBasicReturn {
    // attributes
    private String id;
    private String type; // normal, reward, collaboration
    private String department;
    private String subject;
    private String title; // title
    private String professor;
    private String school;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer unlockCount;
    private Boolean downloadable;
    private Integer commentCount;
    private Integer price;
    private ArrayList<String> tag;
    @JsonFormat(timezone = "GMT+08:00")
    private Date publishDate; // publish 後更新，預設為 NULL
    private String description;

    private UserObj headerEmailUserObj;
    private ArrayList<UserObj> authorEmailUserObj;

    public NoteBasicReturn (Note note) {
        this.id = note.getId();
        this.type = note.getType();
        this.department = note.getDepartment();
        this.subject = note.getSubject();
        this.title = note.getTitle();
        this.professor = note.getProfessor();
        this.school = note.getSchool();
        this.likeCount = note.getLikeCount();
        this.favoriteCount = note.getFavoriteCount();
        this.unlockCount = note.getUnlockCount();
        this.downloadable = note.getDownloadable();
        this.commentCount = note.getCommentCount();
        this.price = note.getPrice();
        this.tag = note.getTag();
        this.publishDate = note.getPublishDate();
        this.description = note.getDescription();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Integer getUnlockCount() {
        return unlockCount;
    }

    public void setUnlockCount(Integer unlockCount) {
        this.unlockCount = unlockCount;
    }

    public Boolean getDownloadable() {
        return downloadable;
    }

    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserObj getHeaderEmailUserObj() {
        return headerEmailUserObj;
    }

    public void setHeaderEmailUserObj(UserObj headerEmailUserObj) {
        this.headerEmailUserObj = headerEmailUserObj;
    }

    public ArrayList<UserObj> getAuthorEmailUserObj() {
        return authorEmailUserObj;
    }

    public void setAuthorEmailUserObj(ArrayList<UserObj> authorEmailUserObj) {
        this.authorEmailUserObj = authorEmailUserObj;
    }
}
