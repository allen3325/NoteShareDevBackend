package ntou.notesharedevbackend.postModule.controller;

import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping("/postType/{postType}")
    public ResponseEntity<Post[]> getAllTypeOfPost(@PathVariable("postType") String postType) {
        Post[] posts = postService.getAllTypeOfPost(postType);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postID}")
    public ResponseEntity<Post> getPostById(@PathVariable("postID") String id){
        Post post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post request) {
        Post post = postService.createPost(request);
        System.out.println("creat post");
        //create collaboration post and set collaboration note publish time
        if(post.getType().equals("collaboration")){
            System.out.println("create post and set task");
          post.setTask(postService.schedulerPublishTime(post.getId(),request.getTask()));
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{postID}")
                .buildAndExpand(post.getId())
                .toUri();

        return ResponseEntity.created(location).body(post);
    }

    @PutMapping("/{postID}")
    public ResponseEntity<Post> replacePost(
            @PathVariable("postID") String id, @RequestBody Post request) {
        Post post = postService.replacePost(id, request);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/publish/{postID}")
    public ResponseEntity modifyPublishStatus(@PathVariable("postID") String id) {
        postService.modifyPublishStatus(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{postID}/{email}")
    public ResponseEntity applyCollaboration(@PathVariable("postID") String id, @PathVariable("email") String email) {
        postService.applyCollaboration(id, email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/add/{postID}/{email}")
    public ResponseEntity approveCollaboration(@PathVariable("postID") String id, @PathVariable("email") String email) {
        postService.approveCollaboration(id, email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/vote/{postID}/{voteID}/{email}")
    public ResponseEntity voteCollaborationVote(@PathVariable("postID") String postID,@PathVariable ("voteID") String voteID,@PathVariable("email") String email, @RequestBody String option){
        if(postService.voteCollaborationVote(postID,voteID, email,option)){
            return ResponseEntity.ok().build();
        }else{
            throw new NotFoundException("Can not vote");
        }
    }

    @DeleteMapping("/{postID}")
    public ResponseEntity deletePost(@PathVariable("postID") String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
