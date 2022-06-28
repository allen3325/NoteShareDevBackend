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
    @GetMapping("/user/{userName}/{offset}/{pageSize}")
    public ResponseEntity<Map<String, Pages>> getSearchedUser(@PathVariable("userName") String userName,
                                                                  @PathVariable("offset") int offset,
                                                                  @PathVariable("pageSize") int pageSize) {
        Pages appUser = searchService.getSearchedUser(userName, offset, pageSize);
        Map<String, Pages> map = new HashMap<>();
        map.put("search", appUser);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Get notes by search", description = "找note名稱\n haveNormal, haveCollaboration, haveReward 想要得到的筆記的種類，三個都true代表三種類型的筆記都會找")
    @GetMapping("/note/{keyword}/{offset}/{pageSize}")
    public ResponseEntity<Map<String, Pages>> getSearchedNoteByKeyword(@PathVariable("keyword") String keyword,
                                                                        @PathVariable("offset") int offset,
                                                                        @PathVariable("pageSize") int pageSize,
                                                                        SearchNote request,
                                                                        @RequestParam(value = "sortBy", defaultValue = "") String sortBy) {
        Pages notes = searchService.getSearchedNoteByKeyword(keyword, offset, pageSize, request, sortBy);
        Map<String, Pages> map = new HashMap<>();
        map.put("search", notes);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Get posts by search", description = "找post名稱\n haveQA, haveCollaboration, haveReward 想要得到的筆記的種類，三個都true代表三種類型的筆記都會找")
    @GetMapping("/post/{keyword}/{offset}/{pageSize}")
    public ResponseEntity<Map<String, Pages>> getSearchedPostByKeyword(@PathVariable("keyword") String keyword,
                                                                        @PathVariable("offset") int offset,
                                                                        @PathVariable("pageSize") int pageSize,
                                                                        SearchPost request,
                                                                        @RequestParam(value = "sortBy", defaultValue = "") String sortBy) {
        Pages posts = searchService.getSearchedPostByKeyword(keyword, offset, pageSize, request, sortBy);
        Map<String, Pages> map = new HashMap<>();
        map.put("search", posts);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Get folders by search", description = "找folder名稱")
    @GetMapping("/folder/{keyword}/{offset}/{pageSize}")
    public ResponseEntity<Map<String, Pages>> getSearchedFolderByKeyword(@PathVariable("keyword") String keyword,
                                                                         @PathVariable("offset") int offset,
                                                                         @PathVariable("pageSize") int pageSize,
                                                                         @RequestParam(value = "creator", defaultValue = "") String creator) {
        Pages folders = searchService.getSearchedFolderByKeyword(keyword, offset, pageSize, creator);
        Map<String, Pages> map = new HashMap<>();
        map.put("search", folders);

        return ResponseEntity.ok(map);
    }
}
