package ntou.notesharedevbackend.searchModule.entity;

import ntou.notesharedevbackend.folderModule.entity.*;
import ntou.notesharedevbackend.userModule.entity.*;

import java.util.*;

public class FolderBasicReturn {
    // attributes
    private String id;
    private String folderName;
    private ArrayList<String> notes; // note's ID
    private Boolean isPublic;
    private Boolean isFavorite; // 收藏區
    private String path; // 路徑
    private String parent; // 父資料夾
    private ArrayList<String> children; // 子資料夾
    private String creatorEmail; // 資料夾主人email
    private String creatorName;
    private String headshotPhoto;

    public FolderBasicReturn() {

    }

    public FolderBasicReturn(Folder folder, AppUser appUser) {
        setId(folder.getId());
        setFolderName(folder.getFolderName());
        setNotes(folder.getNotes());
        setPublic(folder.getPublic());
        setFavorite(folder.getFavorite());
        setPath(folder.getPath());
        setParent(folder.getParent());
        setChildren(folder.getChildren());
        setCreatorName(folder.getCreatorName());
        setCreatorEmail(appUser.getEmail());
        setHeadshotPhoto(appUser.getHeadshotPhoto());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public ArrayList<String> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getHeadshotPhoto() {
        return headshotPhoto;
    }

    public void setHeadshotPhoto(String headshotPhoto) {
        this.headshotPhoto = headshotPhoto;
    }
}
