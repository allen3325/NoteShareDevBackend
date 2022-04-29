package ntou.notesharedevbackend.folderModule.service;

import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FolderService {
    @Autowired
    private UserRepository userRepository;

    public List<Folder> getAllFolderByEmail(String email){
        return userRepository.findByEmail(email).getFolders();
    }

    public Folder getFolderByID(String email,String folderID){
        Folder folders = userRepository.findFolderById(folderID);
//        for(Folder folder:folders){
//            if(folder.getId()==folderID){
//                return folder;
//            }
//        }
//        System.out.println("Can't find folder");
//        return null;
        return folders;
    }

//    public Folder createFolder(FolderRequest request){
//        AppUser appUser = userRepository.findByEmail(request.getEmail());
//        ArrayList<Folder> folders = appUser.getFolders();
//        for(Folder folder: folders){
//            if(request.getFolderName()==folder.getFolderName()){
//                System.out.println("Folder already exist");
//                return null;
//            }
//        }
//        Folder folder = new Folder();
//        folder.setFolders(request.getFolders());
//        folder.setFolderName(request.getFolderName());
//        folder.setPublic(request.getPublic());
//        folder.setFavorite(request.getFavorite());
//        folder.setNotes(request.getNotes());
//        folders.add(folder);
//        appUser.setFolders(folders);
//
//    }
}
