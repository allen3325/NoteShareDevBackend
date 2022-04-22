package ntou.notesharedevbackend.postModule.service;

import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    //TODO::::修好Post的schema CURD問題
    @Autowired
    private PostRepository postRepository;

    public Post getPostById(String id) {
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
        post.setPublic(request.getPublic());
        post.setPrice(request.getPrice());
        post.setComments(request.getComments());
        post.setAnswers(request.getAnswers());
        post.setWantEnterUsersEmail(request.getWantEnterUsersEmail());
//        switch (request.getType()) {
//            case "QA":
//                post.setPrice(request.getPrice());
//                post.setComments(request.getComments());
//                post.setAnswers(request.getAnswers());
//                break;
//            case "collaboration":
//                post.setWantEnterUsersEmail(request.getWantEnterUsersEmail());
//                break;
//            case "reward":
//                post.setPrice(request.getPrice());
//                post.setAnswers(request.getAnswers());
//                break;
//        }
        return postRepository.insert(post);
    }

    public Post replacePost(String id, Post request) {
        Post oldPost = getPostById(id);

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
