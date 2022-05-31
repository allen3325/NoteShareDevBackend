package ntou.notesharedevbackend.searchModule.controller;

import ntou.notesharedevbackend.folderModule.entity.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
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

    @GetMapping("/user/{userName}")
    public ResponseEntity<Map<String, AppUser[]>> getSearchedUser(@PathVariable("userName") String userName) {
        AppUser[] appUser = searchService.getSearchedUser(userName);
        Map<String, AppUser[]> map = new HashMap<>();
        map.put("search", appUser);

        return ResponseEntity.ok(map);
    }

    @GetMapping("/note/{keyword}")
    public ResponseEntity<Map<String, Note[]>> getSearchedNoteByKeyword(@PathVariable("keyword") String keyword, @RequestBody Search request) {
        Note[] notes = searchService.getSearchedNoteByKeyword(keyword, request);
        Map<String, Note[]> map = new HashMap<>();
        map.put("search", notes);

        return ResponseEntity.ok(map);
    }

    @GetMapping("/folder/{keyword}")
    public ResponseEntity<Map<String, Folder[]>> getSearchedFolderByKeyword(@PathVariable("keyword") String keyword) {
        Folder[] folders = searchService.getSearchedFolderByKeyword(keyword);
        Map<String, Folder[]> map = new HashMap<>();
        map.put("search", folders);

        return ResponseEntity.ok(map);
    }
}
