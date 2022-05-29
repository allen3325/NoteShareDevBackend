package ntou.notesharedevbackend.folderModule.service;

import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
import ntou.notesharedevbackend.folderModule.entity.FolderReturn;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FolderService {
    private final FolderRepository folderRepository;
    private final AppUserService appUserService;
    private final NoteService noteService;

    @Autowired
    public FolderService(FolderRepository folderRepository, AppUserService appUserService, NoteService noteService) {
        this.folderRepository = folderRepository;
        this.appUserService = appUserService;
        this.noteService = noteService;
    }

    public ArrayList<Folder> getAllFoldersFromUser(String email) {
        ArrayList<String> folderIDList = appUserService.getUserByEmail(email).getFolders();
        ArrayList<Folder> folders = new ArrayList<Folder>();
        if (!folderIDList.isEmpty()) {
            for (String id : folderIDList) {
                Folder folder = getFolderByID(id);
                folders.add(folder);
            }
        }
        return folders;
    }

    public Folder getFolderByID(String folderID) {
        return folderRepository.findById(folderID)
                .orElseThrow(() -> new NotFoundException("Can't find folder."));
    }

    public ArrayList<Folder> getAllFoldersByFolderID(String folderID) {
        ArrayList<String> folderIDList = getFolderByID(folderID).getChildren();
        ArrayList<Folder> folders = new ArrayList<Folder>();
        if (!folderIDList.isEmpty()) {
            for (String id : folderIDList) {
                folders.add(getFolderByID(id));
            }
        }

        return folders;
    }

    public ArrayList<Note> getAllNotesByFolderID(String folderID) {
        ArrayList<String> noteIDList = getFolderByID(folderID).getNotes();
        ArrayList<Note> notes = new ArrayList<Note>();
        for (String noteID : noteIDList) {
            notes.add(noteService.getNote(noteID));
        }

        return notes;
    }

    public FolderReturn getAllContentUnderFolderID(String folderID) {
        Folder folder = getFolderByID(folderID);
        ArrayList<String> noteIDList = folder.getNotes();
        ArrayList<String> folderIDList = folder.getChildren();
        FolderReturn folderReturn = new FolderReturn(folder);
        ArrayList<Note> notes = new ArrayList<Note>();
        ArrayList<Folder> folders = new ArrayList<Folder>();

        for (String noteID : noteIDList) {
            notes.add(noteService.getNote(noteID));
        }
        for (String tmpFolderID : folderIDList) {
            folders.add(getFolderByID(tmpFolderID));
        }

        folderReturn.setChildren(folders);
        folderReturn.setNotes(notes);

        return folderReturn;
    }

    public void addChildrenToParent(String childrenID, String parentID) {
        Folder parent = getFolderByID(parentID);
        ArrayList<String> childrenList = parent.getChildren();
        childrenList.add(childrenID);
        parent.setChildren(childrenList);

        folderRepository.save(parent);
    }

    //TODO: update parent's children
    public Folder createFolder(String email, FolderRequest request) {
        AppUser appUser = appUserService.getUserByEmail(email);
        Folder folder = new Folder(request);
        ArrayList<String> tmpFoldersList = new ArrayList<>();
        // check users has folders
        if (appUser.getFolders() == null) {
            tmpFoldersList.add(folder.getId());
            appUser.setFolders(tmpFoldersList);
        } else {
            tmpFoldersList = appUser.getFolders();
            // check parent is null
            if (request.getParent() != null) {
                // update parent's folders
                for (String folderID : tmpFoldersList) {
                    if (folderID.equals(request.getParent())) {
                        addChildrenToParent(folder.getId(), folderID);
                        break;
                    }
                }
            }
            tmpFoldersList.add(folder.getId());
        }
        // update user's folders
        appUser.setFolders(tmpFoldersList);
        appUserService.replaceUser(appUser);
        return folderRepository.insert(folder);
    }

    public void deleteFolderByID(String email, String folderID) {
        Folder folder = getFolderByID(folderID);
        AppUser user = appUserService.getUserByEmail(email);
        ArrayList<String> foldersIDList = user.getFolders();

        // fetch all folders under user and check its direction has contains wannaDelete folder's name
        ArrayList<Folder> folders = getAllFoldersFromUser(email);
        for (Folder tmpFolder : folders) {
            if (tmpFolder.getPath().contains(folder.getFolderName())) {
                // update AppUser schema's folders
                foldersIDList.remove(tmpFolder.getId());
                folderRepository.deleteById(tmpFolder.getId());
            }
        }

        // write update to AppUser schema
        foldersIDList.remove(folderID);
        user.setFolders(foldersIDList);
        appUserService.replaceUser(user);

        // update parent's folders and write to Folder's schema
        if (folder.getParent() != null) {
            Folder parentFolder = getFolderByID(folder.getParent());
            ArrayList<String> parentFolderIDList = parentFolder.getChildren();
            parentFolderIDList.remove(folderID);
            parentFolder.setChildren(parentFolderIDList);
            folderRepository.save(parentFolder);
        }
        // delete folder
        folderRepository.deleteById(folderID);
    }

    public Folder renameFolderByID(String email, String folderID, String wannaChangeName) {
        System.out.println("wannaChangeName = " + wannaChangeName);
        // get all folders and check its contains folder's name and change it.
        Folder wannaChangeFolder = getFolderByID(folderID);
        ArrayList<Folder> allFolders = getAllFoldersFromUser(email);
        String oldName = wannaChangeFolder.getFolderName();

        // change all directions
        for (Folder folder : allFolders) {
            // find its related children folder
            if (folder.getPath().contains(oldName)) {
                // get new direction
                String newDirection = "";
                String[] hasOldNameDirection = folder.getPath().split("/");
                for (String oldDirection : hasOldNameDirection) {
                    if(!oldDirection.equals("")){
                        if (oldDirection.equals(oldName)) {
                            newDirection += "/" + wannaChangeName;
                        } else {
                            newDirection += "/" + oldDirection;
                        }
                    }
                }
                // update and write to its children
                folder.setPath(newDirection);
                folderRepository.save(folder);
            }
        }

        // change its name
        Folder folder = getFolderByID(folderID);
        folder.setFolderName(wannaChangeName);
        folderRepository.save(folder);

        return folder;
    }

    // old:/CA/Ch2/Ch1-1 -> new:/CA/Ch1/Ch1-1
    public void changeAllChildrenPath(String email, String folderID, Folder request) {
        Folder folder = getFolderByID(folderID);
        String oldPath = folder.getPath();
        String newPath = request.getPath();
        ArrayList<Folder> allFolders = getAllFoldersFromUser(email);
        for (Folder tmpFolder : allFolders) {
            // get related children and change their path
            String path = tmpFolder.getPath();
            if (path.contains(oldPath) && !path.equals(oldPath)) {
                path = path.replace(oldPath, newPath);
                tmpFolder.setPath(path);
                folderRepository.save(tmpFolder);
            }
        }
    }

    public void changeOldParentChildren(String email, String folderID, Folder request) {
        // get related old parent and delete folderID
        Folder folder = getFolderByID(folderID);
        Folder oldParent = getFolderByID(folder.getParent());
        ArrayList<String> oldChildren = oldParent.getChildren();
        oldChildren.remove(folderID);
        oldParent.setChildren(oldChildren);
        folderRepository.save(oldParent);
    }

    public Folder changePathByID(String email, String folderID, Folder request) {
        Folder folder = getFolderByID(folderID);
        String newPath = request.getPath();
        String newParentID = request.getParent();
        // has children and parent
        if (!folder.getChildren().isEmpty() && folder.getParent() != null) {
            changeAllChildrenPath(email, folderID, request);
            changeOldParentChildren(email, folderID, request);
        }
        // has no children but has parent
        else if (folder.getChildren().isEmpty()) {
            changeOldParentChildren(email, folderID, request);
        }
        // update own parent and path
        folder.setPath(newPath);
        folder.setParent(newParentID);
        folderRepository.save(folder);
        // update new parent's children
        Folder newParent = getFolderByID(newParentID);
        ArrayList<String> newParentChildrenList = newParent.getChildren();
        newParentChildrenList.add(folderID);
        folderRepository.save(newParent);

        return folder;
    }

    public void setFavorite(String email, String folderID) {
        Folder folder = getFolderByID(folderID);
        AppUser appUser = appUserService.getUserByEmail(email);
        folder.setFavorite(!folder.getFavorite());
        ArrayList<String> folderIDList = appUser.getFolders();
        // find Favorite folder in user
        for (String tmpFolderID : folderIDList) {
            Folder tmpFolder = getFolderByID(tmpFolderID);
            if (tmpFolder.getFolderName().equals("Favorite")) {
                ArrayList<String> tmpFolderChildren = tmpFolder.getChildren();
                // add folder into Favorite
                if (folder.getFavorite()) {
                    // check Favorite folder does not contain this folder
                    if (!tmpFolderChildren.contains(folderID)) {
                        tmpFolderChildren.add(folderID);
                    }
                }
                // delete folder from Favorite
                else {
                    tmpFolderChildren.remove(folderID);
                }
                // update tmpFolder and save to repo.
                tmpFolder.setChildren(tmpFolderChildren);
                folderRepository.save(tmpFolder);
            }
        }
    }
}
