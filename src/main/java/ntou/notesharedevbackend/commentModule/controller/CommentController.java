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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/comment",produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{ID}")
    public ResponseEntity<Object> getAllCommentByID(@PathVariable("ID") String id){
        ArrayList<Comment> commentArrayList = commentService.getAllCommentsByID(id);
        Map<String,Object> res = new HashMap<>();
        if(commentArrayList.isEmpty()){
            res.put("msg","comment is empty.");
            return ResponseEntity.status(404).body(res);
        }else {
            res.put("res",commentArrayList);
            return ResponseEntity.ok().body(res);
        }
    }

    @GetMapping("/{ID}/{floor}")
    public ResponseEntity<Object> getCommentByFloor(@PathVariable("ID") String id ,@PathVariable("floor") int floor){
        Comment comment = commentService.getCommentByFloor(id,floor);
        Map<String,Object> res = new HashMap<>();
        res.put("res",comment);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/{ID}")
    public ResponseEntity<Object> createComment(@PathVariable("ID") String id, @RequestBody Comment request){
        Comment comment = commentService.createComment(id, request);
        Map<String,Object> res = new HashMap<>();
        res.put("res",comment);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("{ID}/{floor}")
    public ResponseEntity<Object> updateComment(@PathVariable("ID") String id, @PathVariable("floor") Integer floor, @RequestBody Comment request){
        Comment comment = commentService.updateComment(id, floor, request);
        Map<String,Object> res = new HashMap<>();
        res.put("res",comment);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("{ID}/{floor}")
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
