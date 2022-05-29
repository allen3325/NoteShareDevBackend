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

package ntou.notesharedevbackend.noteNodule.entity;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;

//TODO:
// 1. update schema by frontend
// 2. 喜歡筆記的人（前端判斷使用者是否按過喜歡）
// 3. 收藏（把isFavorite拔掉了）。因為改成 by reference 所以沒用
// 4. favorite 前端判斷方法，應該是去看此 user 的 Favorite 資料夾有無此筆記？ -> 影響我們要不要有一個 favoriter（像是liker）
// 5. Unlocker？（解鎖的人，前端也需要判斷此筆記是否已購買。還是搜尋的時候要篩掉？）
// 6. 需要前端給他們的 versionContent 送什麼？（Date 後端建還是前端建）
// 7. mycustom-components 是 Array 嗎？
// 8. mycustom-components 的 - 改成 _ 的問題
@Document(collection = "note")
public class Note {

    // attributes
    private String id;
    private String type; // normal, reward, collaboration
    private String name;
    private String slug;
    private Date createdAt;
    private Date updatedAt;
    private int __v;
    private String department;
    private String subject;
//    private String title; // change to name
    private String headerEmail; //建立筆記者
    private String headerName; //建立筆記者
    private ArrayList<String> authorEmail; //所有作者
    private ArrayList<String> authorName; //所有作者
    // token: private ArrayList<String> editorEmail;
    private String managerEmail; //共筆用
    private String professor;
    private String school;
//    private String parentFolder; //目前資料夾
    private ArrayList<String> liker; // 喜灣筆記的人
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
    private ArrayList<VersionContent> versionToSave; // 存檔欄位
    private ArrayList<VersionContent> version; // 版本
    private ArrayList<String> contributors;
    private String postID; // 紀錄貼文ID用於投稿後存在哪
    private Boolean isBest; // 用於懸賞區看是否為最佳解
//    private Boolean isFavorite; // 收藏區

    // constructors
    public Note() {}


    // getter and setter
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

//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }

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

//    public ArrayList<String> getExitFolders() {
//        return exitFolders;
//    }
//
//    public void setExitFolders(ArrayList<String> exitFolders) {
//        this.exitFolders = exitFolders;
//    }

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

//    public Boolean getFavorite() {
//        return isFavorite;
//    }
//
//    public void setFavorite(Boolean favorite) {
//        isFavorite = favorite;
//    }
}
