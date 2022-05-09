package ntou.notesharedevbackend.folderModule.service;

import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
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
    private final UserRepository userRepository;
    private final AppUserService appUserService;

    @Autowired
    public FolderService(FolderRepository folderRepository, UserRepository userRepository, AppUserService appUserService) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.appUserService = appUserService;
    }

    public Folder getFolderByID(String folderID){
        return folderRepository.findById(folderID)
                .orElseThrow(() -> new NotFoundException("Can't find folder."));
    }

    public Folder createFolder(String email,FolderRequest request){
        AppUser appUser = userRepository.findByEmail(email);
        Folder folder = new Folder(request);
        ArrayList<String> tmpFolders = new ArrayList<>();
        // TODO: folder 跟 user 型態為 ArrayList 的預設為 null 很麻煩
        if(appUser.getFolders() == null){
            tmpFolders.add(folder.getId());
            appUser.setFolders(tmpFolders);
        }else{
            tmpFolders = appUser.getFolders();
            tmpFolders.add(folder.getId());
        }
        appUser.setFolders(tmpFolders);
        appUserService.replaceUser(appUser);
        return folderRepository.insert(folder);
    }
}
