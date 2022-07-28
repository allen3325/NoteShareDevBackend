package ntou.notesharedevbackend.basicFunctionModule.controller;

import io.swagger.v3.oas.annotations.*;
import ntou.notesharedevbackend.basicFunctionModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
//TODO: favoriter in note update.
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Operation(summary = "Favorite a note",description = "email給此用戶")
    @PutMapping("/favorite/note/{noteID}/{email}")
    public ResponseEntity<Object> favoriteNote(@PathVariable("noteID") String id,@PathVariable("email") String email) {
        favoriteService.favoriteNote(id,email);
        Map<String,Object> res = new HashMap<>();

        res.put("msg","success");
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "like a note",description = "email給此用戶")
    @PutMapping("/like/note/{noteID}/{email}")
    public ResponseEntity<Object> likeNote(@PathVariable("noteID") String id,@PathVariable("email") String email) {
        favoriteService.likeNote(id,email);
        Map<String,Object> res = new HashMap<>();

        res.put("msg","success");
        return ResponseEntity.ok().body(res);
    }

    @Operation(summary = "Favorite a comment from a note", description = "email是傳誰做favorite的動作")
    @PutMapping("/favorite/note/{noteID}/{commentID}/{email}")
    public ResponseEntity<Map<String, String>> favoriteNoteComment(@PathVariable("noteID") String noteID, @PathVariable("commentID") String commentID, @PathVariable("email") String email) {
        favoriteService.favoriteNoteComment(noteID, commentID, email);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Favorite a comment from a post", description = "email是傳誰做favorite的動作")
    @PutMapping("/favorite/post/{postID}/{commentID}/{email}")
    public ResponseEntity<Map<String, String>> favoritePostComment(@PathVariable("postID") String postID, @PathVariable("commentID") String commentID, @PathVariable("email") String email) {
        favoriteService.favoritePostComment(postID, commentID, email);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "UnFavorite a comment from a note", description = "email是傳誰做unFavorite的動作")
    @PutMapping("/unFavorite/note/{noteID}/{commentID}/{email}")
    public ResponseEntity<Map<String, String>> unFavoriteNoteComment(@PathVariable("noteID") String noteID, @PathVariable("commentID") String commentID, @PathVariable("email") String email) {
        favoriteService.unFavoriteNoteComment(noteID, commentID, email);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");
        return ResponseEntity.ok(map);
    }

    @Operation(summary = "UnFavorite a comment from a post", description = "email是傳誰做unFavorite的動作")
    @PutMapping("/unFavorite/post/{postID}/{commentID}/{email}")
    public ResponseEntity<Map<String, String>> unFavoritePostComment(@PathVariable("postID") String postID, @PathVariable("commentID") String commentID, @PathVariable("email") String email) {
        favoriteService.unFavoritePostComment(postID, commentID, email);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "unlike a note",description = "email給此用戶")
    @PutMapping("/unlike/note/{noteID}/{email}")
    public ResponseEntity<Object> unlikeNote(@PathVariable("noteID") String id,@PathVariable("email") String email) {
        favoriteService.unlikeNote(id,email);
        Map<String,Object> res = new HashMap<>();

        res.put("msg","success");
        return ResponseEntity.ok().body(res);
    }
}
