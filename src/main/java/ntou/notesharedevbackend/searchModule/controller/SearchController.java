package ntou.notesharedevbackend.searchModule.controller;

import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.searchModule.entity.*;
import ntou.notesharedevbackend.searchModule.service.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/user/{userName}")
    public ResponseEntity<AppUser[]> getSearchedUser(@PathVariable("userName") String userName) {
        AppUser[] appUser = searchService.getSearchedUser(userName);
        return ResponseEntity.ok(appUser);
    }

    @GetMapping("/{keyword}")
    public ResponseEntity<Note[]> getSearchedNoteByKeyword(@PathVariable("keyword") String keyword, @RequestBody Search request) {
        Note[] notes = searchService.getSearchedNoteByKeyword(keyword, request);
        return ResponseEntity.ok(notes);
    }
}
