package ntou.notesharedevbackend.folderModule.entity;

import ntou.notesharedevbackend.noteNodule.entity.Note;

import java.util.ArrayList;

public class FolderReturn {
    // attributes
    private String id;
    private String folderName;
    private ArrayList<Folder> folders; // folder
    private ArrayList<Note> notes; // note
    private Boolean isPublic;
    private Boolean isFavorite; // 收藏區
    private String direction; // 路徑
    private String parent; // 父資料夾
    private ArrayList<String> children; // 子資料夾

    public FolderReturn() {
    }
    public FolderReturn(Folder folder){
        this.id = folder.getId();
        this.folderName = folder.getFolderName();
        this.isPublic = folder.getPublic();
        this.isFavorite = folder.getFavorite();
        this.direction = folder.getDirection();
        this.parent = folder.getParent();
        this.children = folder.getChildren();
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

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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
}

