//folder* [FolderSchema]
//      default [
//                { folderName: ‘Uncategorized’,
//                folder:[ ],
//                note:[ ],
//                isPublic:true
//                },
//                { folderName:’Favorite’,//收藏
//                folder:[ ],
//                note:[ ],
//                isPublic:true
//                },
//                { folderName: ‘Likes’,//愛心
//                folder:[ ],
//                note:[ ],
//                isPublic:true
//                },
//                { folderName: ‘Purchased’,//已購買
//                folder:[ ],
//                note:[ ],
//                isPublic:true
//                },
//              ]

package ntou.notesharedevbackend.folderModule.entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "folder")
public class Folder {

    // attributes
    private String id;
    private String folderName;
//    private ArrayList<String> folders; // folder's ID
    private ArrayList<String> notes; // note's ID
    private Boolean isPublic;
    private Boolean isFavorite; // 收藏區
    private String path; // 路徑
    private String parent; // 父資料夾
    private ArrayList<String> children; // 子資料夾

    // constructors
    public Folder() {
        this.id = new ObjectId().toString();
    }

    public Folder(FolderRequest folderRequest){
        this.id = new ObjectId().toString();
        this.folderName = folderRequest.getFolderName();
        this.isPublic = folderRequest.getPublic();
        this.path = folderRequest.getPath();
        this.parent = folderRequest.getParent();
        this.children = new ArrayList<String>();
//        this.folders = new ArrayList<String>();
        this.notes = new ArrayList<String>();
    }

    // getter and setter

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

//    public ArrayList<String> getFolders() {
//        return folders;
//    }
//
//    public void setFolders(ArrayList<String> folders) {
//        this.folders = folders;
//    }

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
}
