package ntou.notesharedevbackend.postModule.controller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.entity.NoteReturn;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.userModule.entity.*;
import ntou.notesharedevbackend.userModule.service.*;
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
    @Autowired
    private AppUserService appUserService;

    @Operation(summary = "get all post by type.(QA, reward, collaboration)")
    @GetMapping("/postType/{postType}")
    public ResponseEntity<Object> getAllTypeOfPost(@PathVariable("postType") String postType) {
        Post[] posts = postService.getAllTypeOfPost(postType);
        ArrayList<PostReturn> postReturns = new ArrayList<>();
        for (Post post : posts) {
            PostReturn postReturn = postService.getUserInfo(post);
            postReturns.add(postReturn);
        }

        Map<String, Object> res = new HashMap<>();

        res.put("res", postReturns);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get post by id.")
    @GetMapping("/{postID}")
    public ResponseEntity<Object> getPostById(@PathVariable("postID") String id) {
        Post post = postService.getPostById(id);
        PostReturn postReturn = postService.getUserInfo(post);
        Map<String, Object> res = new HashMap<>();

        res.put("res", postReturn);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get user's all post by type", description = "type為(QA, reward, collaboration)")
    @GetMapping("/{email}/{postType}")
    public ResponseEntity<Object> getUserAllPostByType(@PathVariable("email") String email, @PathVariable("postType") String postType) {
        ArrayList<Post> allPost = postService.getUserAllPostByType(email, postType);
        ArrayList<PostReturn> postReturns = new ArrayList<>();
        for (Post post : allPost) {
            PostReturn postReturn = postService.getUserInfo(post);
            postReturns.add(postReturn);
        }

        Map<String, Object> res = new HashMap<>();

        res.put("res", postReturns);
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

        PostReturn postReturn = postService.getUserInfo(post);
        Map<String, Object> res = new HashMap<>();

        res.put("res", postReturn);

        return ResponseEntity.status(201).body(res);
    }

    @Operation(summary = "update post.", description = "body should full complete.")
    @PutMapping("/{postID}")
    public ResponseEntity<Object> replacePost(@PathVariable("postID") String id, @RequestBody Post request) {
        Post post = postService.replacePost(id, request);
        PostReturn postReturn = postService.getUserInfo(post);
        Map<String, Object> res = new HashMap<>();

        res.put("res", postReturn);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "modify post is public.")
    @PutMapping("/publish/{postID}")
    public ResponseEntity<Object> modifyPublishStatus(@PathVariable("postID") String id) {
        Post post = postService.modifyPublishStatus(id);

        Map<String, Object> res = new HashMap<>();

        if (post != null) {
            PostReturn postReturn = postService.getUserInfo(post);
            res.put("res", postReturn);
        } else {
            res.put("msg", "can't change publish state before you got best answer.");
        }
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "apply become one of collaboration note's author.", description = "wantEnterUsersEmail為申請者,commentFromApplicant為留言")
    @PutMapping("/apply/{postID}")
    public ResponseEntity<Object> applyCollaboration(@PathVariable("postID") String id, @RequestBody Apply applicant) {
        Map<String, Object> res = new HashMap<>();
        if (postService.applyCollaboration(id, applicant)) {
            res.put("msg", "Success");
        } else {
            res.put("msg", "User already apply");
        }
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
                                                         @PathVariable("answerID") String answerID) {
        Map<String, Object> res = new HashMap<>();
        if (postService.rewardChooseBestAnswer(postID, answerID)) {
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


    @Operation(summary = "reward choose reference answer.", description = "填入postID,參考解筆記ID")
    @PutMapping("/reward/reference/{postID}/{answerID}")
    public ResponseEntity<Object> RewardChooseReferenceAnswer(@PathVariable("postID") String postID,
                                                              @PathVariable("answerID") String answerID) {
        Map<String, Object> res = new HashMap<>();
        if (postService.rewardChooseReferenceAnswer(postID, answerID)) {
            res.put("msg", "Success");
            return ResponseEntity.ok(res);
        } else {
            res.put("msg", "Reference Answer all be chosen");
            return ResponseEntity.status(409).body(res);
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

    @Operation(summary = "create reward note", description = "postID為要投稿的reward post ID，email要放投稿人email")
    @PostMapping(value = "/reward/{postID}/{email}")
    public ResponseEntity<Object> createRewardNote(@PathVariable("postID") String postID, @PathVariable("email") String email, @RequestBody Note request) {
        NoteReturn noteReturn = postService.createRewardNote(postID, email, request);
        Map<String, Object> res = new HashMap<>();
        res.put("res", noteReturn);
        return ResponseEntity.status(201).body(res);
    }

    @Operation(summary = "modify post archive status")
    @PutMapping("/archive/{postID}")
    public ResponseEntity<Object> modifyPostArchiveStatus(@PathVariable("postID") String postID) {
        Map<String, String> res = new HashMap<>();
        if (postService.archivePost(postID)) {
            res.put("res", "Success");
        } else {
            res.put("res", "can't change archive state before you got best answer.");
        }
        return ResponseEntity.ok().body(res);
    }
}
