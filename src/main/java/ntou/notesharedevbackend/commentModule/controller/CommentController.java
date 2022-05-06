package ntou.notesharedevbackend.commentModule.controller;

import ntou.notesharedevbackend.commentModule.entity.Comment;
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
import java.util.List;

@RestController
@RequestMapping(value = "/comment",produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{ID}")
    public ResponseEntity<ArrayList<Comment>> getAllCommentByID(@PathVariable("ID") String id){
        ArrayList<Comment> commentArrayList = commentService.getAllCommentsByID(id);

        if(commentArrayList.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok().body(commentArrayList);
        }
    }

    @GetMapping("/{ID}/{floor}")
    public ResponseEntity<Comment> getCommentByFloor(@PathVariable("ID") String id ,@PathVariable("floor") int floor){
        Comment comment = commentService.getCommentByFloor(id,floor);
        return ResponseEntity.ok().body(comment);
    }

    @PostMapping("/{ID}")
    public ResponseEntity<Comment> createComment(@PathVariable("ID") String id, @RequestBody Comment request){
        Comment comment = commentService.createComment(id, request);
        return ResponseEntity.ok().body(comment);
    }

    @PutMapping("{ID}/{floor}")
    public ResponseEntity<Comment> updateComment(@PathVariable("ID") String id, @PathVariable("floor") Integer floor, @RequestBody Comment request){
        Comment comment = commentService.updateComment(id, floor, request);
        return ResponseEntity.ok().body(comment);
    }

    @DeleteMapping("{ID}/{floor}")
    public ResponseEntity deleteComment(@PathVariable("ID") String id, @PathVariable("floor") Integer floor){
        if(commentService.deleteComment(id,floor)){
            return ResponseEntity.ok().build();
        }else{
            throw new NotFoundException("Can not find comment");
        }
    }
}
