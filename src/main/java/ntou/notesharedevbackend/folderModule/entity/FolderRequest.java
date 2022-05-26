package ntou.notesharedevbackend.folderModule.entity;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class FolderRequest {
    private String folderName;
    private Boolean isPublic;
    private String path; // 路徑
    private String parent; // 父資料夾
//    private ArrayList<String> children; // 子資料夾

    // getter and setter


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

//    public ArrayList<String> getChildren() {
//        return children;
//    }
//
//    public void setChildren(ArrayList<String> children) {
//        this.children = children;
//    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

}
