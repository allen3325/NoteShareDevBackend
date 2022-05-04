package ntou.notesharedevbackend.folderModule.entity;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class FolderRequest {
    private Integer floor; // 樓層
    private String folderName;
    private Boolean isPublic;
    private Integer indexAtFloor; // 樓層的index

    // getter and setter

    public Integer getIndexAtFloor() {
        return indexAtFloor;
    }

    public void setIndexAtFloor(Integer indexAtFloor) {
        this.indexAtFloor = indexAtFloor;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

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
