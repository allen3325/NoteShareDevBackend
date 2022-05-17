package ntou.notesharedevbackend.folderModule.controller;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
import ntou.notesharedevbackend.folderModule.entity.FolderReturn;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/folder", produces = MediaType.APPLICATION_JSON_VALUE )
public class FolderController {
    // TODO: folder delete 有問題，因為現在的寫法用parent判斷 -> 不優（因為只能追溯一層）。可能改路徑判斷或是處理好 children 陣列

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService, AppUserService appUserService) {
        this.folderService = folderService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<ArrayList<Folder>> getAllFoldersFromUser(@PathVariable(name = "email")String email){
        ArrayList<Folder> folders = folderService.getAllFoldersFromUser(email);

        return ResponseEntity.ok(folders);
    }

    @GetMapping("/{email}/{folderID}")
    public ResponseEntity<FolderReturn> getAllFoldersFromFolderID(@PathVariable(name = "email")String email, @PathVariable(name = "folderID")String folderID) {
        FolderReturn folders = folderService.getAllContentUnderFolderID(folderID);
        return ResponseEntity.ok(folders);
    }

    @PostMapping("/{email}")
    public ResponseEntity<Folder> createFolder(@PathVariable(name = "email")String email,
                                               @RequestBody FolderRequest request){
        Folder folder = folderService.createFolder(email,request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{folderID}")
                .buildAndExpand(folder.getId())
                .toUri();

        return ResponseEntity.created(location).body(folder);
    }

    @DeleteMapping("/{email}/{folderID}")
    public ResponseEntity<String> deleteFolder(@PathVariable(name = "email")String email, @PathVariable(name = "folderID")String folderID){
        folderService.deleteFolderByID(email,folderID);

        return ResponseEntity.noContent().build();
    }
}
