package ntou.notesharedevbackend.postModule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PostRequest {
    // attributes
    private String id;
    private String type; // QA, reward, collaboration
    private ArrayList<String> email; // 共筆發起人以及管理員，用來核准加入共筆queue的
    private String author;
    private String authorName;
    private String department;
    private String subject;
    private String school;
    private String professor;
    private String title;
    private String content;
    @JsonFormat(timezone = "GMT+08:00")
    private Date date;
//    private Integer price; //為共筆貼文時，為共筆筆記的金額
    private Integer bestPrice; //最佳解金額
    private Integer referencePrice; //參考解金額
    private Integer referenceNumber; //剩餘參考解數目
    private Boolean isPublic;
    private ArrayList<Comment> comments;
    private Integer commentCount;

    private ArrayList<String> answers; // to save note's ID , 共筆post存共筆note's ID
//    private ArrayList<String> wantEnterUsersEmail;
    //    private Task task;
    @JsonFormat(timezone = "GMT+08:00")
    private Date publishDate;
    private ArrayList<Vote> vote;
    private Integer collabNoteAuthorNumber;
    private Apply collabApply;

    private ArrayList<String> applyEmail;

    // getter and setter


    public ArrayList<String> getApplyEmail() {
        return applyEmail;
    }

    public void setApplyEmail(ArrayList<String> applyEmail) {
        this.applyEmail = applyEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Apply getCollabApply() {
        return collabApply;
    }

    public void setCollabApply(Apply collabApply) {
        this.collabApply = collabApply;
    }

    public Integer getCollabNoteAuthorNumber() {
        return collabNoteAuthorNumber;
    }

    public void setCollabNoteAuthorNumber(Integer collabNoteAuthorNumber) {
        this.collabNoteAuthorNumber = collabNoteAuthorNumber;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public Integer getBestPrice() {
        return bestPrice;
    }

    public void setBestPrice(Integer bestPrice) {
        this.bestPrice = bestPrice;
    }

    public Integer getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(Integer referencePrice) {
        this.referencePrice = referencePrice;
    }

    public Integer getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(Integer referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date date) {
        this.publishDate = date;
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

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
        this.date = calendar.getTime();
    }

//    public Integer getPrice() {
//        return price;
//    }
//
//    public void setPrice(Integer price) {
//        this.price = price;
//    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

//    public ArrayList<String> getWantEnterUsersEmail() {
//        return wantEnterUsersEmail;
//    }
//
//    public void setWantEnterUsersEmail(ArrayList<String> wantEnterUsersEmail) {
//        this.wantEnterUsersEmail = wantEnterUsersEmail;
//    }
//
//    public Task getTask() {
//        return task;
//    }
//
//    public void setTask(Task task) {
//        this.task = task;
//    }

    public ArrayList<Vote> getVote() {
        return vote;
    }

    public void setVote(ArrayList<Vote> vote) {
        this.vote = vote;
    }

//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Date createdAt) {
//        this.createdAt = createdAt;
//    }
}
