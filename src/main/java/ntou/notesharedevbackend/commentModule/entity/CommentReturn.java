package ntou.notesharedevbackend.commentModule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ntou.notesharedevbackend.userModule.entity.UserObj;

import java.util.ArrayList;
import java.util.Date;

public class CommentReturn {
    private String id;
    private String content;
    private Integer likeCount; // 愛心數
    //    private ArrayList<String> referenceNotesURL; // 參考筆記的URL
    //    private Boolean isReference; // 是不是參考解
    private Integer floor; // 樓層數
    @JsonFormat(timezone = "GMT+08:00")
    private Date date;
    private ArrayList<String> picURL; //截圖後引用筆記
    private Boolean isBest; // 是不是最佳解

    private UserObj userObj;
    private ArrayList<UserObj> likerUserObj;

    public CommentReturn() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public UserObj getUserObj() {
        return userObj;
    }

    public void setUserObj(UserObj userObj) {
        this.userObj = userObj;
    }

    public ArrayList<UserObj> getLikerUserObj() {
        return likerUserObj;
    }

    public void setLikerUserObj(ArrayList<UserObj> likerUserObj) {
        this.likerUserObj = likerUserObj;
    }
}
