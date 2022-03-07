package ntou.notesharedevbackend.service;

import ntou.notesharedevbackend.entity.Post;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Post getPost(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Post createPost(Post request) {
        Post post = new Post();
        post.setType(request.getType());
        post.setEmail(request.getEmail());
        post.setAuthor(request.getAuthor());
        post.setDepartment(request.getDepartment());
        post.setSubject(request.getSubject());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setDate(request.getDate());
        switch (request.getType()) {
            case "QA":
                post.setPrice(request.getPrice());
                post.setComments(request.getComments());
                break;
            case "collaboration":
                post.setWantEnterUsersEmail(request.getWantEnterUsersEmail());
                break;
            case "reward":
                post.setPrice(request.getPrice());
                post.setAnswers(request.getAnswers());
                break;
        }
        return postRepository.insert(post);
    }

    public Post replacePost(String id, Post request) {
        Post oldPost = getPost(id);

        Post post = new Post();
        post.setId(oldPost.getId());
        post.setType(request.getType());
        post.setEmail(request.getEmail());
        post.setAuthor(request.getAuthor());
        post.setDepartment(request.getDepartment());
        post.setSubject(request.getSubject());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setDate(request.getDate());
        switch (request.getType()) {
            case "QA":
                post.setPrice(request.getPrice());
                post.setComments(request.getComments());
                break;
            case "collaboration":
                post.setWantEnterUsersEmail(request.getWantEnterUsersEmail());
                break;
            case "reward":
                post.setPrice(request.getPrice());
                post.setAnswers(request.getAnswers());
                break;
        }
        return postRepository.save(post);
    }
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

}
