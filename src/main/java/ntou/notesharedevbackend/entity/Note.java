//        type* string // normal, reward, collaboration
//        科系department* string
//        科目subject* string
//        標題title* string
//        作者Email authorEmail* [string]
//        編輯人Email editorEmail* string default //建立筆記者 (共筆用)
// 	      發起人headerEmail* string default 建立筆記者
//        管理員Email managerEmail  string (共筆用)
//        作者Name authorName* [string]
// 	      發起人headerName* string default 建立筆記者
//        老師professor string
//        學校school string
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
//        是否投稿 isSubmit; // 用於懸賞區投稿
//        可否引用* boolean default false
//        tag   [string]
//        hiddenTag [string]
//        貢獻者 [string] // 投稿人 (懸賞用)
//        存檔版本 versionToSave* [VersionContent]
//        版本 version   [VersionContent]
//        postID string // 紀錄貼文ID用於投稿後存在哪
//        isBest Boolean // 用於懸賞區看是否為最佳解

package ntou.notesharedevbackend.entity;

import java.util.ArrayList;
import java.util.Date;

public class Note {

    // attributes
    private String type;
    private String department;
    private String subject;
    private String title;
    private String headerEmail; //建立筆記者
    private String headerName; //建立筆記者
    private ArrayList<String> authorEmail; //所有作者
    private ArrayList<String> authorName; //所有作者
    // token: private ArrayList<String> editorEmail;
    private String managerEmail; //共筆用
    private String professor;
    private String school;
    private ArrayList<String> exitFolders; //所屬的所有資料夾
    private String parentFolders; //目前資料夾
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer unlockCount;
    private Boolean downloadable;
    private Integer commentCount;
    private ArrayList<Comment> comments;
    private Integer price;
    private Boolean isPublic;
    private Boolean isSubmit; // 用於懸賞區投稿
    private Boolean quotable;
    private ArrayList<String> tag;
    private ArrayList<String> hiddenTag;
    private ArrayList<VersionContent> versionToSave;
    private ArrayList<VersionContent> version;
    private ArrayList<String> contributors;
    private String postID; // 紀錄貼文ID用於投稿後存在哪
    private Boolean isBest; // 用於懸賞區看是否為最佳解

    // constructors
    public Note() {
    }
    public Note(String type, String department, String subject, String title, String headerEmail, String headerName, ArrayList<String> authorEmail, ArrayList<String> authorName, String managerEmail, String professor, String school, ArrayList<String> exitFolders, String parentFolders, Integer likeCount, Integer favoriteCount, Integer unlockCount, Boolean downloadable, Integer commentCount, ArrayList<Comment> comments, Integer price, Boolean isPublic, Boolean isSubmit, Boolean quotable, ArrayList<String> tag, ArrayList<String> hiddenTag, ArrayList<VersionContent> versionToSave, ArrayList<VersionContent> version, ArrayList<String> contributors, String postID, Boolean isBest) {
        this.type = type;
        this.department = department;
        this.subject = subject;
        this.title = title;
        this.headerEmail = headerEmail;
        this.headerName = headerName;
        this.authorEmail = authorEmail;
        this.authorName = authorName;
        this.managerEmail = managerEmail;
        this.professor = professor;
        this.school = school;
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
        this.isSubmit = isSubmit;
        this.quotable = quotable;
        this.tag = tag;
        this.hiddenTag = hiddenTag;
        this.versionToSave = versionToSave;
        this.version = version;
        this.contributors = contributors;
        this.postID = postID;
        this.isBest = isBest;
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

    public String getHeaderEmail() {
        return headerEmail;
    }

    public void setHeaderEmail(String headerEmail) {
        this.headerEmail = headerEmail;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public ArrayList<String> getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(ArrayList<String> authorEmail) {
        this.authorEmail = authorEmail;
    }

    public ArrayList<String> getAuthorName() {
        return authorName;
    }

    public void setAuthorName(ArrayList<String> authorName) {
        this.authorName = authorName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
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

    public Boolean getSubmit() {
        return isSubmit;
    }

    public void setSubmit(Boolean submit) {
        isSubmit = submit;
    }

    public Boolean getQuotable() {
        return quotable;
    }

    public void setQuotable(Boolean quotable) {
        this.quotable = quotable;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public ArrayList<String> getHiddenTag() {
        return hiddenTag;
    }

    public void setHiddenTag(ArrayList<String> hiddenTag) {
        this.hiddenTag = hiddenTag;
    }

    public ArrayList<VersionContent> getVersionToSave() {
        return versionToSave;
    }

    public void setVersionToSave(ArrayList<VersionContent> versionToSave) {
        this.versionToSave = versionToSave;
    }

    public ArrayList<VersionContent> getVersion() {
        return version;
    }

    public void setVersion(ArrayList<VersionContent> version) {
        this.version = version;
    }

    public ArrayList<String> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<String> contributors) {
        this.contributors = contributors;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public Boolean getBest() {
        return isBest;
    }

    public void setBest(Boolean best) {
        isBest = best;
    }
}
