package ntou.notesharedevbackend.postModule.service;

import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public Post[] getAllTypeOfPost(String postType) {
        List<Post> postList = postRepository.findAllByType(postType);
        return postList.toArray(new Post[0]);
    }

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

    public void modifyPublishStatus(String id) {
        Post post = getPostById(id);
        post.setPublic(!post.getPublic());
        postRepository.save(post);
    }

    public void applyCollaboration(String id, String email) {
        Post post = getPostById(id);
        ArrayList<String> currentWantEnterUsersEmail = post.getWantEnterUsersEmail();
        if (currentWantEnterUsersEmail == null)
            currentWantEnterUsersEmail = new ArrayList<>();
        currentWantEnterUsersEmail.add(email);
        post.setWantEnterUsersEmail(currentWantEnterUsersEmail);

        postRepository.save(post);
    }

    //TODO: 記得要將email跟name(用email去拿user.name)加進note的author email,author name
    public void approveCollaboration(String id, String email) {
        Post post = getPostById(id);
        ArrayList<String> currentEmails = post.getEmail();
        currentEmails.add(email);
        post.setEmail(currentEmails);
        clearWantEnterUsersEmail(post, email);

        postRepository.save(post);
    }

    public void clearWantEnterUsersEmail(Post post, String email) {
        ArrayList<String> currentWantEnterUsersEmail = post.getWantEnterUsersEmail();
        currentWantEnterUsersEmail.remove(email);
    }
}
