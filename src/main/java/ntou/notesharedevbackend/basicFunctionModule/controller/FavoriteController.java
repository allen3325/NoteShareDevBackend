package ntou.notesharedevbackend.basicFunctionModule.controller;

import ntou.notesharedevbackend.basicFunctionModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

//    @PutMapping("/note/{noteID}")
//    public ResponseEntity favoriteNote(@PathVariable("noteID") String id) {
//        favoriteService.favoriteNote(id);
//        return ResponseEntity.ok().build();
//    }

    @PutMapping("/favorite/note/{noteID}/{commentID}/{email}")
    public ResponseEntity<Map<String, String>> favoriteNoteComment(@PathVariable("noteID") String noteID, @PathVariable("commentID") String commentID, @PathVariable("email") String email) {
        favoriteService.favoriteNoteComment(noteID, commentID, email);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");

        return ResponseEntity.ok(map);
    }

    @PutMapping("/favorite/post/{postID}/{commentID}/{email}")
    public ResponseEntity<Map<String, String>> favoritePostComment(@PathVariable("postID") String postID, @PathVariable("commentID") String commentID, @PathVariable("email") String email) {
        favoriteService.favoritePostComment(postID, commentID, email);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");

        return ResponseEntity.ok(map);
    }

    @PutMapping("/unFavorite/note/{noteID}/{commentID}/{email}")
    public ResponseEntity<Map<String, String>> unFavoriteNoteComment(@PathVariable("noteID") String noteID, @PathVariable("commentID") String commentID, @PathVariable("email") String email) {
        favoriteService.unFavoriteNoteComment(noteID, commentID, email);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");

        return ResponseEntity.ok(map);
    }

    @PutMapping("/unFavorite/post/{postID}/{commentID}/{email}")
    public ResponseEntity<Map<String, String>> unFavoritePostComment(@PathVariable("postID") String postID, @PathVariable("commentID") String commentID, @PathVariable("email") String email) {
        favoriteService.unFavoritePostComment(postID, commentID, email);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");

        return ResponseEntity.ok(map);
    }
}
