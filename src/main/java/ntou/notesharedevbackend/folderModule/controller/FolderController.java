package ntou.notesharedevbackend.folderModule.controller;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/folder", produces = MediaType.APPLICATION_JSON_VALUE )
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping("/{email}")
    public ResponseEntity<List<Folder>> getAllFoldersFromAUser(@PathVariable("email") String email){
        List<Folder> folder = folderService.getAllFolderByEmail(email);
        return ResponseEntity.ok(folder);
    }

    @GetMapping("/{email}/{folderID}")
    public ResponseEntity<Folder> getFolderFromUser(@PathVariable(name="email")String email,
                                                    @PathVariable(name = "folderID")String folderID) {
        Folder folder = folderService.getFolderByID(email,folderID);
        return ResponseEntity.ok(folder);
    }

//    @PostMapping
//    public ResponseEntity<Folder> createFolder(@RequestBody FolderRequest request){
//        Folder folder = folderService.createFolder(request);
//    }



}
