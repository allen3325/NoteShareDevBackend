package ntou.notesharedevbackend.basicFunctionModule.controller;

import ntou.notesharedevbackend.basicFunctionModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

//    @PutMapping("/note/{noteID}")
//    public ResponseEntity favoriteNote(@PathVariable("noteID") String id) {
//        favoriteService.favoriteNote(id);
//        return ResponseEntity.ok().build();
//    }

    @PutMapping("/note/{noteID}/{commentID}")
    public ResponseEntity favoriteNoteComment(@PathVariable("noteID") String noteID, @PathVariable("commentID") String commentID, @RequestParam("email") String email) {
        favoriteService.favoriteNoteComment(noteID, commentID, email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/post/{postID}/{commentID}")
    public ResponseEntity favoritePostComment(@PathVariable("postID") String postID, @PathVariable("commentID") String commentID, @RequestParam("email") String email) {
        favoriteService.favoritePostComment(postID, commentID, email);
        return ResponseEntity.ok().build();
    }
}
