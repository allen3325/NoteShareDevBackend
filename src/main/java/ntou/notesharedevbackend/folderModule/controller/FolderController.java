package ntou.notesharedevbackend.folderModule.controller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.entity.FolderRequest;
import ntou.notesharedevbackend.folderModule.entity.FolderReturn;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/folder", produces = MediaType.APPLICATION_JSON_VALUE )
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @Operation(summary = "Get All Folders From User.")
    @GetMapping("/{email}")
    public ResponseEntity<Object> getAllFoldersFromUser(@PathVariable(name = "email")String email){
        ArrayList<Folder> folders = folderService.getAllFoldersFromUser(email);

        Map<String,Object> res = new HashMap<>();
        res.put("res",folders);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get All Folders and Notes Under User's specific folder.")
    @GetMapping("/{email}/{folderID}")
    public ResponseEntity<Object> getAllFoldersFromFolderID(@PathVariable(name = "email")String email, @PathVariable(name
            = "folderID")String folderID) {
        FolderReturn folders = folderService.getAllContentUnderFolderID(folderID);

        Map<String,Object> res = new HashMap<>();
        res.put("res",folders);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Can easily rename folder name.",description = "設想可以用在主畫面，點擊進入資料夾前對資料夾做簡易的編輯。")
    @PutMapping("/rename/{email}/{folderID}/{name}")
    public ResponseEntity<Object> renameFolderByID(@PathVariable(name = "email")String email, @PathVariable(name =
            "folderID")String folderID,@PathVariable(name = "name")String name){
        Folder folder = folderService.renameFolderByID(email,folderID,name);

        Map<String,Object> res = new HashMap<>();
        res.put("res",folder);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Change folder's path.(!!!!attention!!!!,path should be correct)")
    @PutMapping("/save/{email}/{folderID}")
    public ResponseEntity<Object> changePathByID(@PathVariable(name = "email")String email, @PathVariable(name =
            "folderID")String folderID,@RequestBody Folder request){
        Folder folder = folderService.changePathByID(email,folderID,request);

        Map<String,Object> res = new HashMap<>();
        res.put("res",folder);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Change favorite state.")
    @PutMapping("/favorite/{email}/{folderID}")
    public ResponseEntity<Object> favorite(@PathVariable(name = "email")String email,
    @PathVariable(name = "folderID")String folderID){
        folderService.setFavorite(email,folderID);

        Map<String,Object> res = new HashMap<>();
        res.put("msg","success");

        return ResponseEntity.status(201).body(res);
    }

    @Operation(summary = "Create a new folder.")
    @PostMapping("/{email}")
    public ResponseEntity<Object> createFolder(@PathVariable(name = "email")String email,
                                               @RequestBody FolderRequest request){
        Folder folder = folderService.createFolder(email,request);

        Map<String,Object> res = new HashMap<>();
        res.put("res",folder);

        return ResponseEntity.status(201).body(res);
    }

    @Operation(summary = "Delete this folder and all children folder under this folder.")
    @DeleteMapping("/{email}/{folderID}")
    public ResponseEntity<Object> deleteFolder(@PathVariable(name = "email")String email,
                                          @PathVariable(name = "folderID")String folderID){
        folderService.deleteFolderByID(email,folderID);

        return ResponseEntity.noContent().build();
    }
}
