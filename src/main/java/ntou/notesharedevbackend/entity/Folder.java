//folder* [FolderSchema]
//      default [
//                { folderName: ‘Uncategorized’,
//                folder:[ ],
//                note:[ ]
//                },
//                { folderName:’Favorite’,//收藏
//                folder:[ ],
//                note:[ ]
//                },
//                { folderName: ‘Likes’,//愛心
//                folder:[ ],
//                note:[ ]
//                },
//                { folderName: ‘Purchased’,//已購買
//                folder:[ ],
//                note:[ ]
//                },
//              ]

package ntou.notesharedevbackend.entity;

import java.util.ArrayList;

public class Folder {

    // attributes
    private String folderName;
    private ArrayList<Folder> folders;
    private ArrayList<Note> notes;

    // constructors
    public Folder() {
    }
    public Folder(String folderName, ArrayList<Folder> folders, ArrayList<Note> notes) {
        this.folderName = folderName;
        this.folders = folders;
        this.notes = notes;
    }

    // getter and setter
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
}
