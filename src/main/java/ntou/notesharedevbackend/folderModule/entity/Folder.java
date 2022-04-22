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

import java.util.ArrayList;

public class Folder {

    // attributes
    private String id;
    private String folderName;
    private ArrayList<Folder> folders;
    private ArrayList<String> notes; // note's ID
    private Boolean isPublic;
    private Boolean isFavorite; // 收藏區

    // constructors
    public Folder() {
        this.id = new ObjectId().toString();
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

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
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
}
