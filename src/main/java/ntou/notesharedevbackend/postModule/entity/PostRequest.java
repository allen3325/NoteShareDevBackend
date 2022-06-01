package ntou.notesharedevbackend.postModule.entity;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;

import java.util.ArrayList;
import java.util.Date;

public class PostRequest {
    private String id;
    private String type; // QA, reward, collaboration
    private ArrayList<String> email; // 共筆發起人以及管理員，用來核准加入共筆queue的
    private String author;
    private String department;
    private String subject;
    private String title;
    private String content;
    private Date date;
    private Integer price;
    private Boolean isPublic;
    private ArrayList<Comment> comments;
    private ArrayList<String> answers; // to save note's ID , 共筆post存共筆note's ID
    private ArrayList<String> wantEnterUsersEmail;
    private Task task;
    private ArrayList<Vote> vote;
    private Date createdAt;

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

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

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

    public ArrayList<String> getWantEnterUsersEmail() {
        return wantEnterUsersEmail;
    }

    public void setWantEnterUsersEmail(ArrayList<String> wantEnterUsersEmail) {
        this.wantEnterUsersEmail = wantEnterUsersEmail;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public ArrayList<Vote> getVote() {
        return vote;
    }

    public void setVote(ArrayList<Vote> vote) {
        this.vote = vote;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}