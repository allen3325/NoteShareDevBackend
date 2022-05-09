package ntou.notesharedevbackend.folderModule.controller;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/folder", produces = MediaType.APPLICATION_JSON_VALUE )
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

//    @GetMapping("/{email}")
//    public ResponseEntity<List<Folder>> getAllFoldersFromAUser(@PathVariable("email") String email){
//        List<Folder> folder = folderService.getAllFolderByEmail(email);
//        return ResponseEntity.ok(folder);
//    }

    @GetMapping("/{folderID}")
    public ResponseEntity<Folder> getFolderFromID(@PathVariable(name = "folderID")String folderID) {
        Folder folder = folderService.getFolderByID(folderID);
        return ResponseEntity.ok(folder);
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

}
