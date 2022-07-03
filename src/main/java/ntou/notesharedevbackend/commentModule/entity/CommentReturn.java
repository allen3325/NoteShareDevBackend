package ntou.notesharedevbackend.commentModule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ntou.notesharedevbackend.userModule.entity.UserObj;

import java.util.ArrayList;
import java.util.Date;

public class CommentReturn {
    private String id;
    private String author;
    private String email;
    private String content;
    private Integer likeCount; // 愛心數
    private ArrayList<String> liker; // 按愛心的人
    //    private ArrayList<String> referenceNotesURL; // 參考筆記的URL
    //    private Boolean isReference; // 是不是參考解
    private Integer floor; // 樓層數
    @JsonFormat(timezone = "GMT+08:00")
    private Date date;
    private ArrayList<String> picURL; //截圖後引用筆記
    private Boolean isBest; // 是不是最佳解
    private String userObjEmail;
    private String userObjName;
    private String userObjAvatar;
    private ArrayList<UserObj> likerUserObj;

    public CommentReturn() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public ArrayList<String> getLiker() {
        return liker;
    }

    public void setLiker(ArrayList<String> liker) {
        this.liker = liker;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getPicURL() {
        return picURL;
    }

    public void setPicURL(ArrayList<String> picURL) {
        this.picURL = picURL;
    }

    public Boolean getBest() {
        return isBest;
    }

    public void setBest(Boolean best) {
        isBest = best;
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

    public ArrayList<UserObj> getLikerUserObj() {
        return likerUserObj;
    }

    public void setLikerUserObj(ArrayList<UserObj> likerUserObj) {
        this.likerUserObj = likerUserObj;
    }
}
