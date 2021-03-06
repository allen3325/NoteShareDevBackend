package ntou.notesharedevbackend.postModule.entity;

import com.fasterxml.jackson.annotation.*;
import ntou.notesharedevbackend.commentModule.entity.*;
import ntou.notesharedevbackend.schedulerModule.entity.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.security.core.userdetails.*;

import java.util.*;

public class PostReturn {
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
    private ArrayList<CommentReturn> commentsUserObj;
    private Integer commentCount;
    private ArrayList<String> answers; // to save note's ID , 共筆post存共筆note's ID，放入對應QA答案
    //    private ArrayList<String> wantEnterUsersEmail;
//    private Task task;
    @JsonFormat(timezone = "GMT+08:00")
    private Date publishDate;
    private ArrayList<Vote> vote = new ArrayList<Vote>();
    private ArrayList<VoteReturn> voteUserObj = new ArrayList<VoteReturn>();
    private Integer collabNoteAuthorNumber;

    private ArrayList<ApplyReturn> collabApplyUserObj;

    private ArrayList<Apply> collabApply;
    private UserObj authorUserObj;
    private ArrayList<UserObj> emailUserObj;
    private Boolean isArchive;
    private ArrayList<NotePostReturn> answersUserObj;
//    private ArrayList<UserObj> applyUserObj;
//    private ArrayList<String> applyEmail;

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

    public ArrayList<CommentReturn> getCommentsUserObj() {
        return commentsUserObj;
    }

    public void setCommentsUserObj(ArrayList<CommentReturn> commentsUserObj) {
        this.commentsUserObj = commentsUserObj;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public ArrayList<Vote> getVote() {
        return vote;
    }

    public void setVote(ArrayList<Vote> vote) {
        this.vote = vote;
    }

    public ArrayList<VoteReturn> getVoteUserObj() {
        return voteUserObj;
    }

    public void setVoteUserObj(ArrayList<VoteReturn> voteUserObj) {
        this.voteUserObj = voteUserObj;
    }

    public Integer getCollabNoteAuthorNumber() {
        return collabNoteAuthorNumber;
    }

    public void setCollabNoteAuthorNumber(Integer collabNoteAuthorNumber) {
        this.collabNoteAuthorNumber = collabNoteAuthorNumber;
    }


    public UserObj getAuthorUserObj() {
        return authorUserObj;
    }

    public void setAuthorUserObj(UserObj authorUserObj) {
        this.authorUserObj = authorUserObj;
    }

    public ArrayList<UserObj> getEmailUserObj() {
        return emailUserObj;
    }

    public void setEmailUserObj(ArrayList<UserObj> emailUserObj) {
        this.emailUserObj = emailUserObj;
    }

    public Boolean getArchive() {
        return isArchive;
    }

    public void setArchive(Boolean archive) {
        isArchive = archive;
    }

//    public ArrayList<UserObj> getApplyUserObj() {
//        return applyUserObj;
//    }
//
//    public void setApplyUserObj(ArrayList<UserObj> applyUserObj) {
//        this.applyUserObj = applyUserObj;
//    }

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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public ArrayList<ApplyReturn> getCollabApplyUserObj() {
        return collabApplyUserObj;
    }

    public void setCollabApplyUserObj(ArrayList<ApplyReturn> collabApplyUserObj) {
        this.collabApplyUserObj = collabApplyUserObj;
    }

    public ArrayList<Apply> getCollabApply() {
        return collabApply;
    }

    public void setCollabApply(ArrayList<Apply> collabApply) {
        this.collabApply = collabApply;
    }

    public ArrayList<NotePostReturn> getAnswersUserObj() {
        return answersUserObj;
    }

    public void setAnswersUserObj(ArrayList<NotePostReturn> answersUserObj) {
        this.answersUserObj = answersUserObj;
    }

//    public ArrayList<String> getApplyEmail() {
//        return applyEmail;
//    }
//
//    public void setApplyEmail(ArrayList<String> applyEmail) {
//        this.applyEmail = applyEmail;
//    }
}
