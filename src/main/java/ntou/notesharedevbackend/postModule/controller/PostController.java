package ntou.notesharedevbackend.postModule.controller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.entity.PostRequest;
import ntou.notesharedevbackend.postModule.service.PostService;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)

public class PostController {
    @Autowired
    private PostService postService;

    @Operation(summary = "get all post by type.(QA, reward, collaboration)")
    @GetMapping("/postType/{postType}")
    public ResponseEntity<Object> getAllTypeOfPost(@PathVariable("postType") String postType) {
        Post[] posts = postService.getAllTypeOfPost(postType);
        Map<String, Object> res = new HashMap<>();

        res.put("res", posts);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get post by id.")
    @GetMapping("/{postID}")
    public ResponseEntity<Object> getPostById(@PathVariable("postID") String id) {
        Post post = postService.getPostById(id);
        Map<String, Object> res = new HashMap<>();

        res.put("res", post);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "create post(QA, reward, collaboration).",description = "id,date,comments,commentCount," +
            "answers,wantEnterUserEmail,publishDate,vote,collabNoteAuthorNumber都不用填。若為共筆貼文，則會自動創建筆記放在此貼文的answer裡")
    @PostMapping("/{email}")
    public ResponseEntity<Object> createPost(@PathVariable("email") String email, @RequestBody PostRequest request) {
        Post post = postService.createPost(email, request);
//        System.out.println("creat post");
        //create collaboration post and set collaboration note publish time
//        if(post.getType().equals("collaboration")){
//            System.out.println("create post and set task");
//          post.setTask(postService.schedulerPublishTime(post.getId(),request.getTask()));
//        }
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{postID}")
//                .buildAndExpand(post.getId())
//                .toUri();

        Map<String, Object> res = new HashMap<>();

        res.put("res", post);

        return ResponseEntity.status(201).body(res);
    }

    @Operation(summary = "update post.",description = "body should full complete.")
    @PutMapping("/{postID}")
    public ResponseEntity<Object> replacePost(
            @PathVariable("postID") String id, @RequestBody Post request) {
        Post post = postService.replacePost(id, request);
        Map<String, Object> res = new HashMap<>();

        res.put("res", post);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "modify post is public.")
    @PutMapping("/publish/{postID}")
    public ResponseEntity<Object> modifyPublishStatus(@PathVariable("postID") String id) {
        Post post = postService.modifyPublishStatus(id);
        Map<String, Object> res = new HashMap<>();

        if(post != null) {
            res.put("res", post);
        }else{
            res.put("msg","can't change publish state before you got best answer.");
        }
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "apply become one of collaboration note's author.",description = "email為申請者")
    @PutMapping("/{postID}/{email}")
    public ResponseEntity<Object> applyCollaboration(@PathVariable("postID") String id, @PathVariable("email") String email) {
        postService.applyCollaboration(id, email);

        Map<String, Object> res = new HashMap<>();

        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "approve become one of collaboration note's author.",description = "email為申請通過者")
    @PutMapping("/add/{postID}/{email}")
    public ResponseEntity<Object> approveCollaboration(@PathVariable("postID") String id, @PathVariable("email") String email) {
        postService.approveCollaboration(id, email);

        Map<String, Object> res = new HashMap<>();

        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "user vote.",description = "第一個ID為postID,第二個ID為voteID,email為投票者")
    @PutMapping("/vote/{postID}/{voteID}/{email}")
    public ResponseEntity<Object> voteCollaborationVote(@PathVariable("postID") String postID,
    @PathVariable("voteID") String voteID, @PathVariable("email") String email, @RequestBody String option) {
        Map<String,Object> res = new HashMap<>();
        if (postService.voteCollaborationVote(postID, voteID, email, option)) {
            res.put("msg","Success");
            return ResponseEntity.ok(res);
        } else {
            res.put("msg","Fail");
            throw new NotFoundException("Can not vote");
        }
    }

    @Operation(summary = "reward choose best answer(note).",description = "填入postID,最佳解筆記ID")
    @PutMapping("/reward/best/{postID}/{answerID}")
    public ResponseEntity<Object> rewardChooseBestAnswer(@PathVariable("postID") String postID,
    @PathVariable("answerID") String answerID, @RequestBody String email) {
            Map<String,Object> res = new HashMap<>();
        if (postService.rewardChooseBestAnswer(postID, answerID, email)) {
            res.put("msg","Success");
            return ResponseEntity.ok(res);
        } else {
            res.put("msg","Fail");
            throw new NotFoundException("Can't not choose best answer");
        }
    }

    @Operation(summary = "qa choose best answer(comment)",description = "填入postID,最佳解留言ID")
    @PutMapping("/qa/best/{postID}/{commentID}")
    public ResponseEntity<Object> QAChooseBestAnswer(@PathVariable("postID") String postID,
    @PathVariable("commentID") String commentID) {
        Map<String,Object> res = new HashMap<>();
        if (postService.QAChooseBestAnswer(postID, commentID)) {
            res.put("msg","Success");
            return ResponseEntity.ok(res);
        } else {
            throw new NotFoundException("This post has best answer already.");
        }
    }

    //TODO: 改成 reward
    @Operation(summary = "reward choose reference answer.",description = "填入postID,最佳解留言ID")
    @PutMapping("/qa/reference/{postID}/{commentID}")
    public ResponseEntity<Object> QAChooseReferenceAnswer(@PathVariable("postID") String postID,
    @PathVariable("commentID") String commentID, @RequestBody String email) {
            Map<String,Object> res = new HashMap<>();
        if (postService.QAChooseReferenceAnswer(postID, commentID, email)) {
            res.put("msg","Success");
            return ResponseEntity.ok(res);
        } else {
            res.put("msg","Fail");
            throw new NotFoundException("Can't not choose reference answer");
        }
    }

    @Operation(summary = "delete post.")
    @DeleteMapping("/{postID}")
    public ResponseEntity<Object> deletePost(@PathVariable("postID") String id) {
        postService.deletePost(id);
        Map<String,Object> res = new HashMap<>();
        res.put("msg","Success");
        return ResponseEntity.status(204).body(res);
    }
}
