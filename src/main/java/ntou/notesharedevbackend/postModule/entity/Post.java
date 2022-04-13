//貼文postSchema
//        type* string //QA, reward, collaboration
//        author* string
//        email* String
//        department* string
//        subject* string
//        title* string
//        content* string
//        date* date
//        point* int
//        comment [commentSchema]
//        answer [NoteSchema]
package ntou.notesharedevbackend.postModule.entity;

import ntou.notesharedevbackend.entity.Comment;
import ntou.notesharedevbackend.entity.Note;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

@Document(collection = "post")
public class Post {

    // attributes
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
    private ArrayList<Comment> comments;
    private ArrayList<String> answers; // to save note's ID
    private ArrayList<String> wantEnterUsersEmail;
    private Boolean isPublic;

    // constructors
    public Post() {
    }

    // getter and setter

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    //TODO: note's ID
//    public ArrayList<Note> getAnswers() {
//        return answers;
//    }
//
//    public void setAnswers(ArrayList<Note> answers) {
//        this.answers = answers;
//    }

    public ArrayList<String> getWantEnterUsersEmail() {
        return wantEnterUsersEmail;
    }

    public void setWantEnterUsersEmail(ArrayList<String> wantEnterUsersEmail) {
        this.wantEnterUsersEmail = wantEnterUsersEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
