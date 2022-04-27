package ntou.notesharedevbackend.postModule.service;

import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class PublishPostService {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    public void modifyPublishStatus(String id) {
        Post post = postService.getPostById(id);
        post.setPublic(!post.getPublic());
        postRepository.save(post);
    }
}
