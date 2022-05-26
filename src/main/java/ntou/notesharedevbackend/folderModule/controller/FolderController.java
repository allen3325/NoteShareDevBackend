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

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
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

    @PutMapping("/rename/{email}/{folderID}/{name}")
    public ResponseEntity<Folder> renameFolderByID(@PathVariable(name = "email")String email, @PathVariable(name = "folderID")String folderID,@PathVariable(name = "name")String name){
        Folder folder = folderService.renameFolderByID(email,folderID,name);
        return ResponseEntity.ok(folder);
    }

    @PutMapping("/save/{email}/{folderID}")
    public ResponseEntity<Folder> changePathByID(@PathVariable(name = "email")String email, @PathVariable(name =
            "folderID")String folderID,@RequestBody Folder request){
        Folder folder = folderService.changePathByID(email,folderID,request);
        return ResponseEntity.ok(folder);
    }

    @PutMapping("/favorite/{email}/{folderID}")
    public ResponseEntity favorite(@PathVariable(name = "email")String email, @PathVariable(name = "folderID")String folderID){
        folderService.setFavorite(email,folderID);
        return ResponseEntity.status(201).build();
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
