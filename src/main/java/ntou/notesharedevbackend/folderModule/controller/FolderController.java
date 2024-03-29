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
@RequestMapping(value = "/folder", produces = MediaType.APPLICATION_JSON_VALUE)
public class FolderController {

    private final FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @Operation(summary = "Get All Folders From User.", description = "email填入使用者email")
    @GetMapping("/all/{email}")
    public ResponseEntity<Object> getAllFoldersFromUser(@PathVariable(name = "email") String email) {
        ArrayList<Folder> folders = folderService.getAllFoldersFromUser(email);
        ArrayList<FolderReturn> folderReturns = folderService.turnAllFolderToFolderReturn(folders);

        Map<String, Object> res = new HashMap<>();
        res.put("res", folderReturns);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get Root Folder from a User.", description = "email填入使用者email")
    @GetMapping("/root/{email}")
    public ResponseEntity<Object> getRootFolderFromUser(@PathVariable(name = "email") String email) {
        ArrayList<Folder> folders = folderService.getRootFoldersFromUser(email);
        ArrayList<FolderReturn> folderReturns = folderService.turnAllFolderToFolderReturn(folders);

        Map<String, Object> res = new HashMap<>();
        res.put("res", folderReturns);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get Favorite from a User.", description = "email填入使用者email")
    @GetMapping("/favorite/{email}")
    public ResponseEntity<Object> getFavoriteFromUser(@PathVariable(name = "email") String email) {
        Folder folders = folderService.getFavoriteFolderByUserEmail(email);
        FolderReturn folderReturns = folderService.turnFolderToFolderReturn(folders);

        Map<String, Object> res = new HashMap<>();
        res.put("res", folderReturns);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get Buy from a User.", description = "email填入使用者email")
    @GetMapping("/buy/{email}")
    public ResponseEntity<Object> getBuyFromUser(@PathVariable(name = "email") String email) {
        Folder folders = folderService.getBuyFolderByUserEmail(email);
        FolderReturn folderReturns = folderService.turnFolderToFolderReturn(folders);

        Map<String, Object> res = new HashMap<>();
        res.put("res", folderReturns);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get All Folders and Notes Under User's specific folder.", description = "ID填入folderID")
    @GetMapping("/{folderID}")
    public ResponseEntity<Object> getAllFoldersFromFolderID(@PathVariable(name
            = "folderID") String folderID) {
        FolderReturn folders = folderService.getAllContentUnderFolderID(folderID);

        Map<String, Object> res = new HashMap<>();
        res.put("res", folders);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Can easily rename folder name.設想可以用在主畫面，點擊進入資料夾前對資料夾做簡易的編輯。", description = "name填入想修改的name")
    @PutMapping("/rename/{email}/{folderID}/{name}")
    public ResponseEntity<Object> renameFolderByID(@PathVariable(name = "email") String email, @PathVariable(name =
            "folderID") String folderID, @PathVariable(name = "name") String name) {
        Folder folder = folderService.renameFolderByID(email, folderID, name);
        FolderReturn folderReturns = folderService.turnFolderToFolderReturn(folder);

        Map<String, Object> res = new HashMap<>();
        res.put("res", folderReturns);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Change folder's path.(!!!!attention!!!!,path should be correct)", description =
            "路徑填法為：/前面的資料夾名稱" +
                    "/此資料夾名稱。例如：第一層資料夾為Buy -> /Buy。Buy底下有OS -> /Buy/OS。" +
                    "body需傳入新路徑在path跟新爸爸的ID在parent")
    @PutMapping("/save/{email}/{folderID}")
    public ResponseEntity<Object> changePathByID(@PathVariable(name = "email") String email, @PathVariable(name =
            "folderID") String folderID, @RequestBody Folder request) {
        Folder folder = folderService.changePathByID(email, folderID, request);
        FolderReturn folderReturns = folderService.turnFolderToFolderReturn(folder);

        Map<String, Object> res = new HashMap<>();
        res.put("res", folderReturns);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Change favorite state.", description = "將資料夾加入或移出最愛")
    @PutMapping("/favorite/{email}/{folderID}")
    public ResponseEntity<Object> favorite(@PathVariable(name = "email") String email,
                                           @PathVariable(name = "folderID") String folderID) {
        folderService.setFavorite(email, folderID);

        Map<String, Object> res = new HashMap<>();
        res.put("msg", "success");

        return ResponseEntity.status(201).body(res);
    }

    @Operation(summary = "Create a new folder.", description = "body需填入folderName,public,path(路徑填法為：/前面的資料夾名稱" +
            "/此資料夾名稱。例如：第一層資料夾為Buy -> /Buy。Buy底下有OS -> /Buy/OS。\"),parent" +
            "(上層資料夾的ID)")
    @PostMapping("/{email}")
    public ResponseEntity<Object> createFolder(@PathVariable(name = "email") String email,
                                               @RequestBody FolderRequest request) {
        Folder folder = folderService.createFolder(email, request);
        FolderReturn folderReturns = folderService.turnFolderToFolderReturn(folder);

        Map<String, Object> res = new HashMap<>();
        res.put("res", folderReturns);

        return ResponseEntity.status(201).body(res);
    }

    @Operation(summary = "Delete this folder and all children folder under this folder.")
    @DeleteMapping("/{email}/{folderID}")
    public ResponseEntity<Object> deleteFolder(@PathVariable(name = "email") String email,
                                               @PathVariable(name = "folderID") String folderID) {
        folderService.deleteFolderByID(email, folderID);
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.status(204).body(res);
    }

    @Operation(summary = "Get TempRewardNote folder from a User.", description = "email填入使用者email")
    @GetMapping("/tempRewardNote/{email}")
    public ResponseEntity<Object> getTempRewardNoteFromUser(@PathVariable(name = "email") String email) {
        Folder folders = folderService.getTempRewardNoteFolder(email);
        FolderReturn folderReturns = folderService.turnFolderToFolderReturn(folders);

        Map<String, Object> res = new HashMap<>();
        res.put("res", folderReturns);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Change folder's public state.", description = "填入資料夾ID")
    @PutMapping("/public/{folderID}")
    public ResponseEntity<Object> changeFolderPublicState(@PathVariable(name = "folderID") String folderID) {
        Map<String, Object> res = new HashMap<>();
        if (folderService.changeState(folderID)) {
            res.put("msg","Success");
            return ResponseEntity.ok(res);
        } else {
            res.put("msg","Fail");
            return ResponseEntity.status(418).body(res);
        }
    }
}
