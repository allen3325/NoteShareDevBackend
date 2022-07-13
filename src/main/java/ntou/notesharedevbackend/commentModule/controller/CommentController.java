package ntou.notesharedevbackend.commentModule.controller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.commentModule.entity.CommentRequest;
import ntou.notesharedevbackend.commentModule.entity.CommentReturn;
import ntou.notesharedevbackend.commentModule.service.CommentService;
import ntou.notesharedevbackend.exception.NotFoundException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/comment",produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{ID}")
    @Operation(summary = "get all comments by note or post id.", description = "ID填筆記或是貼文的都可以")
    public ResponseEntity<Object> getAllCommentByID(@PathVariable("ID") String id){
        ArrayList<Comment> commentArrayList = commentService.getAllCommentsByID(id);
        Map<String,Object> res = new HashMap<>();
        if(commentArrayList.isEmpty()){
            res.put("msg","comment is empty.");
            return ResponseEntity.status(404).body(res);
        }else {
            ArrayList<CommentReturn> commentReturnArrayList = new ArrayList<>();
            for(Comment comment : commentArrayList){
                CommentReturn commentReturn = commentService.getUserInfo(comment);
                commentReturnArrayList.add(commentReturn);
            }
            res.put("res",commentReturnArrayList);
            return ResponseEntity.ok().body(res);
        }
    }

    @GetMapping("/{ID}/{floor}")
    @Operation(summary = "get comment by floor in the note or post", description = "ID填筆記或是貼文的都可以")
    public ResponseEntity<Object> getCommentByFloor(@PathVariable("ID") String id ,@PathVariable("floor") int floor){
        Comment comment = commentService.getCommentByFloor(id,floor);
        Map<String,Object> res = new HashMap<>();
        CommentReturn commentReturn = commentService.getUserInfo(comment);
        res.put("res",commentReturn);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/{ID}")
    @Operation(summary = "create comment in the note or post", description = "ID填筆記或是貼文的都可以，body傳入一個author,email," +
            "content,picURL")
    public ResponseEntity<Object> createComment(@PathVariable("ID") String id, @RequestBody CommentRequest request){
        Comment comment = commentService.createComment(id, request);
        Map<String,Object> res = new HashMap<>();
        CommentReturn commentReturn = commentService.getUserInfo(comment);
        res.put("res",commentReturn);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("{ID}/{floor}")
    @Operation(summary = "update comment by floor in the note or post", description = "ID為筆記或是貼文的，在傳入幾樓想要修改")
    public ResponseEntity<Object> updateComment(@PathVariable("ID") String id, @PathVariable("floor") Integer floor, @RequestBody CommentRequest request){
        Comment comment = commentService.updateComment(id, floor, request);
        Map<String,Object> res = new HashMap<>();
        CommentReturn commentReturn = commentService.getUserInfo(comment);
        res.put("res",commentReturn);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("{ID}/{floor}")
    @Operation(summary = "delete comment by floor in the note or post", description = "ID為筆記或是貼文的，在傳入幾樓想要刪除")
    public ResponseEntity<Object> deleteComment(@PathVariable("ID") String id, @PathVariable("floor") Integer floor){
        Map<String,Object> res = new HashMap<>();
        if(commentService.deleteComment(id,floor)){
            res.put("msg","Success");
            return ResponseEntity.ok(res);
        }else{
            throw new NotFoundException("Can not find comment");
        }
    }
}
