package ntou.notesharedevbackend.folderModule.service;

import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
import ntou.notesharedevbackend.folderModule.entity.FolderReturn;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.entity.NoteFolderReturn;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.entity.UserObj;
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

    public Folder getFavoriteFolderByUserEmail(String email) {
        ArrayList<String> tmpFolderIDList = appUserService.getUserByEmail(email).getFolders();
        for (String folderID : tmpFolderIDList) {
            if (getFolderByID(folderID).getFolderName().equals("Favorite")) {
                return getFolderByID(folderID);
            }
        }
        return null;
    }

    public Folder getBuyFolderByUserEmail(String email) {
        ArrayList<String> tmpFolderIDList = appUserService.getUserByEmail(email).getFolders();
        for (String folderID : tmpFolderIDList) {
            if (getFolderByID(folderID).getFolderName().equals("Buy")) {
                return getFolderByID(folderID);
            }
        }
        return null;
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

    public ArrayList<NoteFolderReturn> getAllNotesByFolderID(String folderID) {
        ArrayList<String> noteIDList = getFolderByID(folderID).getNotes();
        ArrayList<NoteFolderReturn> notes = new ArrayList<NoteFolderReturn>();
        for (String noteID : noteIDList) {
            Note noteTmp = noteService.getNote(noteID);
            NoteFolderReturn noteBasicReturn = new NoteFolderReturn(noteTmp);
            noteBasicReturn.setHeaderUserObj(appUserService.getUserInfo(noteTmp.getHeaderEmail()));
            if (noteTmp.getManagerEmail() != null) {
                noteBasicReturn.setManagerEmail(noteTmp.getManagerEmail());
                noteBasicReturn.setManagerUserObj(appUserService.getUserInfo(noteTmp.getManagerEmail()));
            }
            ArrayList<UserObj> authorUserObj = new ArrayList<>();
            for (String authorEmail : noteTmp.getAuthorEmail()) {
                UserObj userObj = appUserService.getUserInfo(authorEmail);
                authorUserObj.add(userObj);
            }
            noteBasicReturn.setAuthorUserObj(authorUserObj);
            notes.add(noteBasicReturn);
        }

        return notes;
    }

    public FolderReturn getAllContentUnderFolderID(String folderID) {
        Folder folder = getFolderByID(folderID);
        FolderReturn folderReturn = new FolderReturn(folder);
        ArrayList<NoteFolderReturn> notes = getAllNotesByFolderID(folderID);
        ArrayList<Folder> folders = getAllFoldersByFolderID(folderID);

        folderReturn.setChildren(folders);
        folderReturn.setNotes(notes);
        folderReturn.setFolderName(folder.getFolderName());
        folderReturn.setFavorite(folder.getFavorite());
        folderReturn.setId(folder.getId());
        folderReturn.setParent(folder.getParent());
        folderReturn.setPath(folder.getPath());
        folderReturn.setPublic(folder.getPublic());
        folderReturn.setCreatorName(folder.getCreatorName());
        AppUser appUser = appUserService.getUserByName(folder.getCreatorName());
        UserObj userObj = appUserService.getUserInfo(appUser.getEmail());
        folderReturn.setCreatorUserObj(userObj);
        return folderReturn;
    }

    public void addChildrenToParent(String childrenID, String parentID) {
        Folder parent = getFolderByID(parentID);
        ArrayList<String> childrenList = parent.getChildren();
        childrenList.add(childrenID);
        parent.setChildren(childrenList);

        replaceFolder(parent);
//        folderRepository.save(parent);
    }

    public Folder createFolder(String email, FolderRequest request) {
        AppUser appUser = appUserService.getUserByEmail(email);
        Folder folder = new Folder(request);
        folder.setCreatorName(appUser.getName());
        ArrayList<String> tmpFoldersList = new ArrayList<>();
        // check users has folders
        if (appUser.getFolders() == null || appUser.getFolders().isEmpty()) {
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
        // fetch user's Favorite
        Folder favorite = getFavoriteFolderByUserEmail(email);
        ArrayList<String> foldersInFavorite = favorite.getChildren();

        for (Folder tmpFolder : folders) {
            if (tmpFolder.getPath().contains(folder.getFolderName())) {
                // update AppUser schema's folders
                foldersIDList.remove(tmpFolder.getId());
                // update user's favorite
                foldersInFavorite.remove(tmpFolder.getId());
                // delete from DB
                folderRepository.deleteById(tmpFolder.getId());
            }
        }

        // write update to AppUser schema
        foldersIDList.remove(folderID);
        user.setFolders(foldersIDList);
        appUserService.replaceUser(user);
        // write update to Folder Schema
        foldersInFavorite.remove(folderID);
        favorite.setChildren(foldersInFavorite);
        replaceFolder(favorite);
        // update parent's folders and write to Folder's schema
        if (folder.getParent() != null) {
            Folder parentFolder = getFolderByID(folder.getParent());
            ArrayList<String> parentFolderIDList = parentFolder.getChildren();
            parentFolderIDList.remove(folderID);
            parentFolder.setChildren(parentFolderIDList);
            replaceFolder(parentFolder);
//            folderRepository.save(parentFolder);
        }
        // delete folder
        folderRepository.deleteById(folderID);
    }

    public Folder renameFolderByID(String email, String folderID, String wannaChangeName) {
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
                    if (!oldDirection.equals("")) {
                        if (oldDirection.equals(oldName)) {
                            newDirection += "/" + wannaChangeName;
                        } else {
                            newDirection += "/" + oldDirection;
                        }
                    }
                }
                // update and write to its children
                folder.setPath(newDirection);
                replaceFolder(folder);
//                folderRepository.save(folder);
            }
        }

        // change its name
        Folder folder = getFolderByID(folderID);
        folder.setFolderName(wannaChangeName);
        replaceFolder(folder);
//        folderRepository.save(folder);

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
                replaceFolder(tmpFolder);
//                folderRepository.save(tmpFolder);
            }
        }
    }

    public Folder replaceFolder(Folder request) {
        Folder newFolder = getFolderByID(request.getId());

        newFolder.setFolderName(request.getFolderName());
        newFolder.setNotes(request.getNotes());
        newFolder.setPublic(request.getPublic());
        newFolder.setFavorite(request.getFavorite());
        newFolder.setPath(request.getPath());
        newFolder.setParent(request.getParent());
        newFolder.setChildren(request.getChildren());
        newFolder.setCreatorName(request.getCreatorName());
        return folderRepository.save(newFolder);
    }

    public void changeOldParentChildren(String email, String folderID, Folder request) {
        // get related old parent and delete folderID
        Folder folder = getFolderByID(folderID);
        Folder oldParent = getFolderByID(folder.getParent());
        ArrayList<String> oldChildren = oldParent.getChildren();
        oldChildren.remove(folderID);
        oldParent.setChildren(oldChildren);
        replaceFolder(oldParent);
//        folderRepository.save(oldParent);
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
        replaceFolder(folder);
        folderRepository.save(folder);
        // update new parent's children if it has new parent
        if (newParentID != null) {
            Folder newParent = getFolderByID(newParentID);
            ArrayList<String> newParentChildrenList = newParent.getChildren();
            newParentChildrenList.add(folderID);
            replaceFolder(newParent);
//            folderRepository.save(newParent);
        }
        return folder;
    }

    public void setFavorite(String email, String folderID) {
        Folder folder = getFolderByID(folderID);
        AppUser appUser = appUserService.getUserByEmail(email);
        folder.setFavorite(!folder.getFavorite());
        ArrayList<String> folderIDList = appUser.getFolders();
        // find Favorite folder in user
        Folder favoriteFolder = getFavoriteFolderByUserEmail(email);
        ArrayList<String> favoriteFolderChildren = favoriteFolder.getChildren();
        // add folder into Favorite
        if (folder.getFavorite()) {
            // check Favorite folder does not contain this folder
            if (!favoriteFolderChildren.contains(folderID)) {
                favoriteFolderChildren.add(folderID);
            }
        }
        // delete folder from Favorite
        else {
            favoriteFolderChildren.remove(folderID);
        }
        // update tmpFolder and save to repo.
        favoriteFolder.setChildren(favoriteFolderChildren);
        replaceFolder(favoriteFolder);
//        folderRepository.save(favoriteFolder);
    }

    // (/folder).length == 2
    // [0][1]
    public ArrayList<Folder> getRootFoldersFromUser(String email) {
        ArrayList<Folder> allFolders = getAllFoldersFromUser(email);
        ArrayList<Folder> res = new ArrayList<Folder>();
        for (Folder folder : allFolders) {
            if (folder.getPath().split("/").length == 2) {
                res.add(folder);
            }
        }
        return res;
    }

    public ArrayList<FolderReturn> turnAllFolderToFolderReturn(ArrayList<Folder> folders) {
        ArrayList<FolderReturn> folderReturns = new ArrayList<>();
        for (Folder folder : folders) {
            folderReturns.add(getAllContentUnderFolderID(folder.getId()));
        }
        return folderReturns;
    }

    public FolderReturn turnFolderToFolderReturn(Folder folder) {
        return getAllContentUnderFolderID(folder.getId());
    }

    public Folder getTempRewardNoteFolder(String email) {
        AppUser appUser = appUserService.getUserByEmail(email);
        for (String folderID : appUser.getFolders()) {
            if (getFolderByID(folderID).getFolderName().equals("Temp Reward Note")) {
                return getFolderByID(folderID);
            }
        }
        return null;
    }

    public Folder getFolderFolderByEmail(String email) {
        AppUser appUser = appUserService.getUserByEmail(email);
        for (String folderID : appUser.getFolders()) {
            if (getFolderByID(folderID).getFolderName().equals("Folder")) {
                return getFolderByID(folderID);
            }
        }
        return null;
    }

}
