package ntou.notesharedevbackend.searchModule.controller;

import io.swagger.v3.oas.annotations.*;
import ntou.notesharedevbackend.folderModule.entity.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.searchModule.entity.*;
import ntou.notesharedevbackend.searchModule.service.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {
    @Autowired
    private SearchService searchService;

    @Operation(summary = "Get users by search", description = "找user名稱")
    @GetMapping("/user/{userName}")
    public ResponseEntity<Map<String, AppUser[]>> getSearchedUser(@PathVariable("userName") String userName) {
        AppUser[] appUser = searchService.getSearchedUser(userName);
        Map<String, AppUser[]> map = new HashMap<>();
        map.put("search", appUser);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Get notes by search", description = "找note名稱\n haveNormal, haveCollaboration, haveReward 想要得到的筆記的種類，三個都true代表三種類型的筆記都會找")
    @GetMapping("/note/{keyword}")
    public ResponseEntity<Map<String, Note[]>> getSearchedNoteByKeyword(@PathVariable("keyword") String keyword, @RequestBody SearchNote request) {
        Note[] notes = searchService.getSearchedNoteByKeyword(keyword, request);
        Map<String, Note[]> map = new HashMap<>();
        map.put("search", notes);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Get posts by search", description = "找post名稱\n haveQA, haveCollaboration, haveReward 想要得到的筆記的種類，三個都true代表三種類型的筆記都會找")
    @GetMapping("/post/{keyword}")
    public ResponseEntity<Map<String, Post[]>> getSearchedPostByKeyword(@PathVariable("keyword") String keyword, @RequestBody SearchPost request) {
        Post[] posts = searchService.getSearchedPostByKeyword(keyword, request);
        Map<String, Post[]> map = new HashMap<>();
        map.put("search", posts);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Get folders by search", description = "找folder名稱")
    @GetMapping("/folder/{keyword}")
    public ResponseEntity<Map<String, Folder[]>> getSearchedFolderByKeyword(@PathVariable("keyword") String keyword) {
        Folder[] folders = searchService.getSearchedFolderByKeyword(keyword);
        Map<String, Folder[]> map = new HashMap<>();
        map.put("search", folders);

        return ResponseEntity.ok(map);
    }
}
