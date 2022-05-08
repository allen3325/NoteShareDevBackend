//留言commentSchema
//    author* string
//    內容content* string
//    最佳解* boolean default false
//    參考解* boolean default false
//    愛心數* int default 0
//    按愛心的人 [string]
//    參考筆記 [string]//url link
//    樓層數* int
package ntou.notesharedevbackend.commentModule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class Comment {

    // attributes
    private String id;
    private String author;
    private String email;
    private String content;
    private Boolean isBest; // 是不是最佳解
    private Boolean isReference; // 是不是參考解
    private Integer likeCount; // 愛心數
    private ArrayList<String> liker; // 按愛心的人
    private ArrayList<String> referenceNotesURL; // 參考筆記的URL
    private Integer floor; // 樓層數
    @JsonFormat(timezone = "GMT+08:00")
    private Date date;

    // constructors
    public Comment() {

        this.id = new ObjectId().toString();
        this.isBest = false;
        this.isReference = false;
        this.likeCount = 0;
        this.liker = new ArrayList<String>();
    }

    // getter and setter

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

    public Boolean getBest() {
        return isBest;
    }

    public void setBest(Boolean best) {
        isBest = best;
    }

    public Boolean getReference() {
        return isReference;
    }

    public void setReference(Boolean reference) {
        isReference = reference;
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

    public ArrayList<String> getReferenceNotesURL() {
        return referenceNotesURL;
    }

    public void setReferenceNotesURL(ArrayList<String> referenceNotesURL) {
        this.referenceNotesURL = referenceNotesURL;
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
}