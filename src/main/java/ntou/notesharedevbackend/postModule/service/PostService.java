package ntou.notesharedevbackend.postModule.service;

import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.repository.PostRepository;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.schedulerModule.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {
    //TODO::::修好Post的schema CURD問題
    @Autowired
    private PostRepository postRepository;
    @Autowired
    @Lazy(value = true)
    private SchedulingService schedulingService;

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
                post.setTask(request.getTask());
                post.setVote(request.getVote());
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
    public Task schedulerPublishTime(String postID,Task request){
        Post post = getPostById(postID);
        if(post.getType().equals("collaboration")){//check post is collaboration
            Task task = new Task();
            task.setPostID(postID);
            task.setType("publish");
            task.setNoteIDOrVoteID(post.getAnswers().get(0));
            task.setYear(request.getYear());
            task.setMonth(request.getMonth());
            task.setDay(request.getDay());
            post.setTask(task);
            postRepository.save(post);
            schedulingService.addSchedule(task);
            return task;
        }else{
            return null;
        }
    }
    public Vote addVote(String postID, Vote request){
        Vote vote= new Vote();
        vote.setType(request.getType());
        vote.setTask(schedulerPublishTime(postID,request.getTask()));
        if(vote.getType().equals("kick")){
            vote.setKickTarget(request.getKickTarget());
        }
        Post post = getPostById(postID);
        ArrayList<Vote> voteArrayList = post.getVote();
        voteArrayList.add(vote);
        post.setVote(voteArrayList);
        postRepository.save(post);
        return vote;
    }
    public Task replacePublishTime (String postID, Task request){
        Post post = getPostById(postID);
        post.setTask(null);
        postRepository.save(post);
        return schedulerPublishTime(postID, request);
    }

    public Vote replaceVote(String postID, String voteID, Vote request){
        Post post = getPostById(postID);
        ArrayList<Vote> voteArrayList = post.getVote();
        Vote newVote = new Vote();
        for(Vote v : voteArrayList){
            if(v.getId().equals(voteID)){//find target vote
                newVote.setId(v.getId());
                newVote.setType(request.getType());
                if(!request.getTask().equals(v.getTask())){//task change
                    Task newTask  = new Task();
                    newTask.setPostID(postID);
                    newTask.setNoteIDOrVoteID(voteID);
                    newTask.setType(request.getTask().getType());
                    newTask.setYear(request.getTask().getYear());
                    newTask.setMonth(request.getTask().getMonth());
                    newTask.setDay(request.getTask().getDay());
                    newVote.setTask(newTask);
                    schedulingService.addSchedule(newVote.getTask());
                }
                newVote.setAgree(request.getAgree());
                newVote.setDisagree(request.getDisagree());
                newVote.setKickTarget(request.getKickTarget());
                newVote.setResult(request.getResult());
                voteArrayList.set(voteArrayList.indexOf(v), newVote);
                break;
            }
        }
        post.setVote(voteArrayList);
        postRepository.save(post);
        return newVote;
    }


}
