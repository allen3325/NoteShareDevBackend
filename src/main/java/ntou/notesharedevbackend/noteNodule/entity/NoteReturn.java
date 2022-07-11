package ntou.notesharedevbackend.noteNodule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import ntou.notesharedevbackend.commentModule.entity.*;
import ntou.notesharedevbackend.userModule.entity.UserObj;

import java.util.ArrayList;
import java.util.Date;

public class NoteReturn {
    private String id;
    private String type; // normal, reward, collaboration
    private String department;
    private String subject;
    private String title; // title
    private String headerEmail; // 建立筆記者
    private String headerName; // 建立筆記者
    private ArrayList<String> authorEmail; // 所有作者
    private ArrayList<String> authorName; // 所有作者
    private String managerEmail; // 共筆用
    private String professor;
    private String school;
    private ArrayList<String> liker; // 喜灣筆記的人
    private ArrayList<String> buyer; // 購買筆記的人
    private ArrayList<String> favoriter; // 收藏這筆記的人
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer unlockCount;
    private Boolean downloadable;
    private Integer commentCount;
    private ArrayList<Comment> comments;
    private ArrayList<CommentReturn> commentsUserObj;
    private Integer price;
    private Boolean isPublic; // 筆記是否公開
    private Boolean isSubmit; // 用於懸賞區投稿
    private Boolean quotable;
    private ArrayList<String> tag;
    private ArrayList<String> hiddenTag;
    private ArrayList<VersionContent> version; // 版本
    private ArrayList<String> contributors;
    private String postID; // 紀錄貼文ID用於投稿後存在哪
    private Boolean isReference; // 是不是參考解
    private Boolean isBest; // 用於懸賞區看是否為最佳解
    @JsonFormat(timezone = "GMT+08:00")
    private Date publishDate; // publish 後更新，預設為 NULL
    private String description;

    private UserObj headerUserObj;
    private UserObj managerUserObj;
    private ArrayList<UserObj> authorUserObj;
    private ArrayList<UserObj> likerUserObj;
    private ArrayList<UserObj> buyerUserObj;
    private ArrayList<UserObj> favoriterUserObj;
    private ArrayList<UserObj> contributorUserObj;

    public NoteReturn() {
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

    public ArrayList<CommentReturn> getCommentsUserObj() {
        return commentsUserObj;
    }

    public void setCommentsUserObj(ArrayList<CommentReturn> commentsUserObj) {
        this.commentsUserObj = commentsUserObj;
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

    public ArrayList<VersionContent> getVersion() {
        return version;
    }

    public void setVersion(ArrayList<VersionContent> version) {
        this.version = version;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public Boolean getReference() {
        return isReference;
    }

    public void setReference(Boolean reference) {
        isReference = reference;
    }

    public Boolean getBest() {
        return isBest;
    }

    public void setBest(Boolean best) {
        isBest = best;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserObj getHeaderUserObj() {
        return headerUserObj;
    }

    public void setHeaderUserObj(UserObj headerUserObj) {
        this.headerUserObj = headerUserObj;
    }

    public UserObj getManagerUserObj() {
        return managerUserObj;
    }

    public void setManagerUserObj(UserObj managerUserObj) {
        this.managerUserObj = managerUserObj;
    }

    public ArrayList<UserObj> getAuthorUserObj() {
        return authorUserObj;
    }

    public void setAuthorUserObj(ArrayList<UserObj> authorUserObj) {
        this.authorUserObj = authorUserObj;
    }

    public ArrayList<UserObj> getLikerUserObj() {
        return likerUserObj;
    }

    public void setLikerUserObj(ArrayList<UserObj> likerUserObj) {
        this.likerUserObj = likerUserObj;
    }

    public ArrayList<UserObj> getBuyerUserObj() {
        return buyerUserObj;
    }

    public void setBuyerUserObj(ArrayList<UserObj> buyerUserObj) {
        this.buyerUserObj = buyerUserObj;
    }

    public ArrayList<UserObj> getFavoriterUserObj() {
        return favoriterUserObj;
    }

    public void setFavoriterUserObj(ArrayList<UserObj> favoriterUserObj) {
        this.favoriterUserObj = favoriterUserObj;
    }

    public ArrayList<UserObj> getContributorUserObj() {
        return contributorUserObj;
    }

    public void setContributorUserObj(ArrayList<UserObj> contributorUserObj) {
        this.contributorUserObj = contributorUserObj;
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

    public ArrayList<String> getLiker() {
        return liker;
    }

    public void setLiker(ArrayList<String> liker) {
        this.liker = liker;
    }

    public ArrayList<String> getBuyer() {
        return buyer;
    }

    public void setBuyer(ArrayList<String> buyer) {
        this.buyer = buyer;
    }

    public ArrayList<String> getFavoriter() {
        return favoriter;
    }

    public void setFavoriter(ArrayList<String> favoriter) {
        this.favoriter = favoriter;
    }

    public ArrayList<String> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<String> contributors) {
        this.contributors = contributors;
    }
}
