//        type* string // normal, reward, collaboration
//        科系department* string
//        科目subject* string
//        標題title* string
//        日期date* date  default
//	      內容content string
//        作者author* [string]
//        編輯人editor* string default建立筆記者 (共筆用)
//        管理員manager  string (共筆用)
//        老師professor string
//        學校school string
//        圖片picURL [string]
//        檔案filesURL [string]
//        tag   [string]
//        hiddenTag [string]
//        markdown [string]
//        exitFolders* [string]//所屬的所有資料夾
//        parentFolder* string //目前資料夾
//        愛心數like* int default 0
//        收藏數* int default 0
//        解鎖次數* int default 0
//        可否下載* boolean default false
//        留言數 int default 0
//        留言 [commentSchema]
//        販賣點數 int
//        公開/私人* boolean default false
//        可否引用* boolean default false
//        存檔版本* int default 1
//        版本號* int default 1
//        貢獻者 [string]
package ntou.notesharedevbackend.entity;

import java.util.ArrayList;
import java.util.Date;

public class Note {

    // attributes
    private String type;
    private String department;
    private String subject;
    private String title;
    private Date date;
    private String content;
    private String author;
    // token: private String editor;
    private String manager; //共筆用
    private String professor;
    private String school;
    private ArrayList<String> picURL;
    private ArrayList<String> fileURL;
    private ArrayList<String> tags;
    private ArrayList<String> hiddenTags;
    private ArrayList<String> markdown;
    private ArrayList<String> exitFolders;
    private String parentFolders;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer unlockCount;
    private Boolean downloadable;
    private Integer commentCount;
    private ArrayList<Comment> comments;
    private Integer price;
    private Boolean isPublic;
    private Boolean quotable;
    private Integer versionToSave;
    private Integer version;
    private ArrayList<String> contributors;

    // constructors
    public Note() {
    }
    public Note(String type, String department, String subject, String title, Date date, String content, String author, String manager, String professor, String school, ArrayList<String> picURL, ArrayList<String> fileURL, ArrayList<String> tags, ArrayList<String> hiddenTags, ArrayList<String> markdown, ArrayList<String> exitFolders, String parentFolders, Integer likeCount, Integer favoriteCount, Integer unlockCount, Boolean downloadable, Integer commentCount, ArrayList<Comment> comments, Integer price, Boolean isPublic, Boolean quotable, Integer versionToSave, Integer version, ArrayList<String> contributors) {
        this.type = type;
        this.department = department;
        this.subject = subject;
        this.title = title;
        this.date = date;
        this.content = content;
        this.author = author;
        this.manager = manager;
        this.professor = professor;
        this.school = school;
        this.picURL = picURL;
        this.fileURL = fileURL;
        this.tags = tags;
        this.hiddenTags = hiddenTags;
        this.markdown = markdown;
        this.exitFolders = exitFolders;
        this.parentFolders = parentFolders;
        this.likeCount = likeCount;
        this.favoriteCount = favoriteCount;
        this.unlockCount = unlockCount;
        this.downloadable = downloadable;
        this.commentCount = commentCount;
        this.comments = comments;
        this.price = price;
        this.isPublic = isPublic;
        this.quotable = quotable;
        this.versionToSave = versionToSave;
        this.version = version;
        this.contributors = contributors;
    }

    // getter and setter
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
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

    public ArrayList<String> getPicURL() {
        return picURL;
    }

    public void setPicURL(ArrayList<String> picURL) {
        this.picURL = picURL;
    }

    public ArrayList<String> getFileURL() {
        return fileURL;
    }

    public void setFileURL(ArrayList<String> fileURL) {
        this.fileURL = fileURL;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public ArrayList<String> getHiddenTags() {
        return hiddenTags;
    }

    public void setHiddenTags(ArrayList<String> hiddenTags) {
        this.hiddenTags = hiddenTags;
    }

    public ArrayList<String> getMarkdown() {
        return markdown;
    }

    public void setMarkdown(ArrayList<String> markdown) {
        this.markdown = markdown;
    }

    public ArrayList<String> getExitFolders() {
        return exitFolders;
    }

    public void setExitFolders(ArrayList<String> exitFolders) {
        this.exitFolders = exitFolders;
    }

    public String getParentFolders() {
        return parentFolders;
    }

    public void setParentFolders(String parentFolders) {
        this.parentFolders = parentFolders;
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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
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

    public Boolean getQuotable() {
        return quotable;
    }

    public void setQuotable(Boolean quotable) {
        this.quotable = quotable;
    }

    public Integer getVersionToSave() {
        return versionToSave;
    }

    public void setVersionToSave(Integer versionToSave) {
        this.versionToSave = versionToSave;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ArrayList<String> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<String> contributors) {
        this.contributors = contributors;
    }
}
