//留言commentSchema
//    author* string
//    內容content* string
//    最佳解* boolean default false
//    參考解* boolean default false
//    愛心數* int default 0
//    按愛心的人 [string]
//    參考筆記 [string]//url link
//    樓層數* int
package ntou.notesharedevbackend.entity;

import java.util.ArrayList;
import java.util.Date;

public class Comment {

    // attributes
    private String author;
    private String email;
    private String content;
    private Boolean isBest;
    private Boolean isReference;
    private Integer likeCount;
    private ArrayList<String> liker;
    private ArrayList<String> referenceNotesURL;
    private Integer floor;
    private Date date;

    // constructors
    public Comment() {
    }
    public Comment(String author, String email, String content, Boolean isBest, Boolean isReference,
                   Integer likeCount, ArrayList<String> liker, ArrayList<String> referenceNotesURL, Integer floor,
                   Date date) {
        this.author = author;
        this.email = email;
        this.content = content;
        this.isBest = isBest;
        this.isReference = isReference;
        this.likeCount = likeCount;
        this.liker = liker;
        this.referenceNotesURL = referenceNotesURL;
        this.floor = floor;
        this.date = date;
    }

    // getter and setter
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

    public ArrayList<String> getReferenceNotes() {
        return referenceNotesURL;
    }

    public void setReferenceNotes(ArrayList<String> referenceNotesURL) {
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
