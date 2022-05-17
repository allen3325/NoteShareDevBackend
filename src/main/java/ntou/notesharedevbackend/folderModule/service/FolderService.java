package ntou.notesharedevbackend.folderModule.service;

import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
import ntou.notesharedevbackend.folderModule.entity.FolderReturn;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.repository.UserRepository;
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
    public FolderService(FolderRepository folderRepository, UserRepository userRepository, AppUserService appUserService, NoteService noteService) {
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
        ArrayList<String> folderIDList = getFolderByID(folderID).getFolders();
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
        ArrayList<String> folderIDList = folder.getFolders();
        FolderReturn folderReturn = new FolderReturn(folder);
        ArrayList<Note> notes = new ArrayList<Note>();
        ArrayList<Folder> folders = new ArrayList<Folder>();

        for (String noteID : noteIDList) {
            notes.add(noteService.getNote(noteID));
        }
        for (String tmpFolderID : folderIDList) {
            folders.add(getFolderByID(tmpFolderID));
        }

        folderReturn.setFolders(folders);
        folderReturn.setNotes(notes);

        return folderReturn;
    }

    public void addChildrenToParent(String childrenID, String parentID) {
        Folder parent = getFolderByID(parentID);
        ArrayList<String> folderList = parent.getFolders();
        folderList.add(childrenID);
        parent.setFolders(folderList);

        folderRepository.save(parent);
    }

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
        for(Folder tmpFolder : folders){
            if(tmpFolder.getDirection().contains(folder.getFolderName())){
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
        if(folder.getParent() != null){
            Folder parentFolder = getFolderByID(folder.getParent());
            ArrayList<String> parentFolderIDList = parentFolder.getFolders();
            parentFolderIDList.remove(folderID);
            parentFolder.setFolders(parentFolderIDList);
            folderRepository.save(parentFolder);
        }
        // delete folder
        folderRepository.deleteById(folderID);
    }

    public Folder renameFolderByID(String email, String folderID, String wannaChangeName) {
        System.out.println("wannaChangeName = "+wannaChangeName);
        // get all folders and check its contains folder's name and change it.
        Folder wannaChangeFolder = getFolderByID(folderID);
        ArrayList<Folder> allFolders = getAllFoldersFromUser(email);
        String oldName = wannaChangeFolder.getFolderName();

        // change all directions
        for(Folder folder : allFolders){
            // find its related children folder
            if(folder.getDirection().contains(oldName)){
                // get new direction
                String newDirection = "";
                String[] hasOldNameDirection = folder.getDirection().split("/");
                for (String oldDirection : hasOldNameDirection) {
                    if (oldDirection.equals(oldName)) {
                        newDirection += wannaChangeName;
                    } else {
                        newDirection += "/"+oldDirection;
                    }
                }
                // update and write to its children
                System.out.println(folder.getFolderName() + "will change into");
                System.out.println(newDirection);
                folder.setDirection(newDirection);
                folderRepository.save(folder);
            }
        }

        // change its name
        Folder folder = getFolderByID(folderID);
        folder.setFolderName(wannaChangeName);
        folderRepository.save(folder);

        return folder;
    }
}
