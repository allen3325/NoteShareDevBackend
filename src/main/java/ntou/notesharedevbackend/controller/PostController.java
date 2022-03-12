package ntou.notesharedevbackend.controller;

import ntou.notesharedevbackend.entity.Post;
import ntou.notesharedevbackend.service.PostService;
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

    @GetMapping("/{postID}")
    public ResponseEntity<Post> getPost(@PathVariable("postID") String id){
        Post post = postService.getPost(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post request) {
        Post post = postService.createPost(request);

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

    @DeleteMapping("/{postID}")
    public ResponseEntity deletePost(@PathVariable("postID") String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
