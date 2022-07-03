package ntou.notesharedevbackend.postModule.controller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.postModule.entity.Apply;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.entity.PostRequest;
import ntou.notesharedevbackend.postModule.entity.VoteRequest;
import ntou.notesharedevbackend.postModule.service.PostService;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
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

    @Operation(summary = "get user's all post by type", description = "type為(QA, reward, collaboration)")
    @GetMapping("/{email}/{postType}")
    public ResponseEntity<Object> getUserAllPostByType(@PathVariable("email") String email, @PathVariable("postType") String postType) {
        ArrayList<Post> allPost = postService.getUserAllPostByType(email,postType);
        Map<String, Object> res = new HashMap<>();

        res.put("res", allPost);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "create post(QA, reward, collaboration).", description = "id, date, authorName, comments, " +
            "commentCount," +
            " answers, wantEnterUserEmail, publishDate, vote, collabNoteAuthorNumber," +
            " collabApply都不用填，author填Email，authorName我們會抓。若為共筆貼文，則會自動創建筆記放在此貼文的answer裡")
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

    @Operation(summary = "update post.", description = "body should full complete.")
    @PutMapping("/{postID}")
    public ResponseEntity<Object> replacePost(@PathVariable("postID") String id, @RequestBody Post request) {
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

        if (post != null) {
            res.put("res", post);
        } else {
            res.put("msg", "can't change publish state before you got best answer.");
        }
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "apply become one of collaboration note's author.", description = "wantEnterUsersEmail為申請者,commentFromApplicant為留言")
    @PutMapping("/apply/{postID}")
    public ResponseEntity<Object> applyCollaboration(@PathVariable("postID") String id, @RequestBody Apply applicant) {
        postService.applyCollaboration(id, applicant);

        Map<String, Object> res = new HashMap<>();

        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "deny someone become one of collaboration note's author.", description = "wantEnterUsersEmail為申請者,commentFromApplicant為留言")
    @DeleteMapping("/apply/{postID}/{denyEmail}")
    public ResponseEntity<Object> denyCollaboration(@PathVariable("postID") String id, @PathVariable("denyEmail") String denyEmail) {
        postService.denyCollaboration(id, denyEmail);
        Map<String, Object> res = new HashMap<>();

        res.put("msg", "Success");
        return ResponseEntity.status(204).body(res);
    }

    @Operation(summary = "approve become one of collaboration note's author.", description = "email為申請通過者")
    @PutMapping("/add/{postID}/{email}")
    public ResponseEntity<Object> approveCollaboration(@PathVariable("postID") String id, @PathVariable("email") String email) {
        postService.approveCollaboration(id, email);

        Map<String, Object> res = new HashMap<>();

        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "user vote.", description = "第一個ID為postID, 第二個ID為voteID, email為投票者,body放agree/disagree")
    @PutMapping("/vote/{postID}/{voteID}/{email}")
    public ResponseEntity<Object> voteCollaborationVote(@PathVariable("postID") String postID,
                                                        @PathVariable("voteID") String voteID, @PathVariable("email") String email, @RequestBody VoteRequest request) {
        Map<String, Object> res = new HashMap<>();
        if (postService.voteCollaborationVote(postID, voteID, email, request)) {
            res.put("msg", "Success");
            return ResponseEntity.ok(res);
        } else {
            res.put("msg", "Fail");
            throw new NotFoundException("Can not vote");
        }
    }

    @Operation(summary = "reward choose best answer(note).", description = "填入postID,最佳解筆記ID")
    @PutMapping("/reward/best/{postID}/{answerID}")
    public ResponseEntity<Object> rewardChooseBestAnswer(@PathVariable("postID") String postID,
                                                         @PathVariable("answerID") String answerID, @RequestBody String email) {
        Map<String, Object> res = new HashMap<>();
        if (postService.rewardChooseBestAnswer(postID, answerID, email)) {
            res.put("msg", "Success");
            return ResponseEntity.ok(res);
        } else {
            res.put("msg", "Fail");
            throw new NotFoundException("Can't not choose best answer");
        }
    }

    @Operation(summary = "qa choose best answer(comment)", description = "填入postID,最佳解留言ID")
    @PutMapping("/qa/best/{postID}/{commentID}")
    public ResponseEntity<Object> QAChooseBestAnswer(@PathVariable("postID") String postID,
                                                     @PathVariable("commentID") String commentID) {
        Map<String, Object> res = new HashMap<>();
        if (postService.QAChooseBestAnswer(postID, commentID)) {
            res.put("msg", "Success");
            return ResponseEntity.ok(res);
        } else {
            throw new NotFoundException("This post has best answer already.");
        }
    }


    @Operation(summary = "reward choose reference answer.", description = "填入postID,最佳解留言ID")
    @PutMapping("/reward/reference/{postID}/{answerID}")
    public ResponseEntity<Object> QAChooseReferenceAnswer(@PathVariable("postID") String postID,
                                                          @PathVariable("answerID") String answerID, @RequestBody String email) {
        Map<String, Object> res = new HashMap<>();
        if (postService.rewardChooseReferenceAnswer(postID, answerID, email)) {
            res.put("msg", "Success");
            return ResponseEntity.ok(res);
        } else {
            res.put("msg", "Fail");
            throw new NotFoundException("Can't not choose reference answer");
        }
    }

    @Operation(summary = "delete post.")
    @DeleteMapping("/{postID}")
    public ResponseEntity<Object> deletePost(@PathVariable("postID") String id) {
        postService.deletePost(id);
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.status(204).body(res);
    }
}
