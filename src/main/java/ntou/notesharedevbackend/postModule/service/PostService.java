package ntou.notesharedevbackend.postModule.service;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.postModule.entity.PostRequest;
import ntou.notesharedevbackend.repository.PostRepository;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.schedulerModule.service.SchedulingService;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private NoteService noteService;
    @Autowired
    @Lazy(value = true)
    private SchedulingService schedulingService;
    @Autowired
    @Lazy(value = true)
    private AppUserService appUserService;

    public Post[] getAllTypeOfPost(String postType) {
        List<Post> postList = postRepository.findAllByType(postType);
        return postList.toArray(new Post[0]);
    }

    public Post getPostById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Post createPost(String userEmail,PostRequest request) {
        AppUser appUser = appUserService.getUserByEmail(userEmail);
        ArrayList<String> email = new ArrayList<>();
        email.add(userEmail);
        Post post = new Post();
        post.setType(request.getType());
        post.setEmail(email);
        post.setAuthor(appUser.getName());
        post.setDepartment(request.getDepartment());
        post.setSubject(request.getSubject());
        post.setSchool(request.getSchool());
        post.setProfessor(request.getProfessor());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setDate();
        post.setBestPrice(request.getBestPrice());
        post.setReferencePrice(request.getReferencePrice());
        post.setReferenceNumber(request.getReferenceNumber());
        post.setPublic(request.getPublic());
        post.setComments(new ArrayList<Comment>());
        post.setCommentCount(0);
        post.setAnswers(new ArrayList<String>());
        post.setWantEnterUsersEmail(new ArrayList<String>());
        post.setPublishDate(request.getPublishDate());
        post.setVote(new ArrayList<Vote>());
        if(request.getType().equals("collaboration")){//若為共筆貼文，須建立共筆筆記
            Note note = new Note();
            note.setPrice(request.getPrice());
//            note.setCreatedAt(request.getCreatedAt());
            note.setTitle(request.getTitle());
            note.setType(request.getType());
            note.setPublic(false);
            note.setDepartment(request.getDepartment());
            note.setSubject(request.getSubject());
            note.setSchool(request.getSchool());
            note.setProfessor(request.getProfessor());
            note.setPrice(request.getPrice());
            note.setDownloadable(false);
            note.setPublic(false);
            note.setQuotable(false);
            Note createdNote = noteService.createNote(note,userEmail);
            ArrayList<String> answers = new ArrayList<String>();//把新增的共筆筆記ID存到共筆貼文內的answer
            answers.add(createdNote.getId());
            post.setAnswers(answers);
            Post newPost = postRepository.insert(post);
            noteService.collaborationNoteSetPostID(createdNote.getId(),newPost.getId());
            return newPost;
        }
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
        post.setSchool(request.getSchool());
        post.setProfessor(request.getProfessor());
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setDate();
        post.setBestPrice(request.getBestPrice());
        post.setReferencePrice(request.getReferencePrice());
        post.setReferenceNumber(request.getReferenceNumber());
        post.setPublic(request.getPublic());
        post.setComments(request.getComments());
        post.setCommentCount(post.getComments().size());
        post.setAnswers(request.getAnswers());
        post.setWantEnterUsersEmail(request.getWantEnterUsersEmail());
        post.setPublishDate(request.getDate());
        post.setVote(request.getVote());

        return postRepository.save(post);
    }
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    public void modifyPublishStatus(String id) {
        Post post = getPostById(id);
        post.setPublic(!post.getPublic());
        replacePost(post.getId(), post);
//        postRepository.save(post);
    }

    public void applyCollaboration(String id, String email) {
        Post post = getPostById(id);
        ArrayList<String> currentWantEnterUsersEmail = post.getWantEnterUsersEmail();
        if (currentWantEnterUsersEmail == null)
            currentWantEnterUsersEmail = new ArrayList<>();
        currentWantEnterUsersEmail.add(email);
        post.setWantEnterUsersEmail(currentWantEnterUsersEmail);

        replacePost(post.getId(), post);
//        postRepository.save(post);
    }

    // TODO: 記得要將email跟name(用email去拿user.name)加進note的author email,author name
    //  將id加進此post裡的answer裡存的筆記的author
    public void approveCollaboration(String id, String email) {
        Post post = getPostById(id);
        ArrayList<String> currentEmails = post.getEmail();
        currentEmails.add(email);
        post.setEmail(currentEmails);
        clearWantEnterUsersEmail(post, email);

        replacePost(post.getId(), post);
//        postRepository.save(post);
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
//            task.setType("publish");
            task.setVoteID(post.getAnswers().get(0));
            task.setYear(request.getYear());
            task.setMonth(request.getMonth());
            task.setDay(request.getDay());
//            post.setTask(task);
            replacePost(post.getId(), post);
//            postRepository.save(post);
            schedulingService.addSchedule(task);
            return task;
        }else{
            return null;
        }
    }

    public Task replacePublishTime (String postID, Task request){
        Post post = getPostById(postID);
//        post.setTask(null);
        replacePost(post.getId(), post);
//        postRepository.save(post);
        return schedulerPublishTime(postID, request);
    }

    public Vote addVote(String postID, Vote request){
        Vote vote= new Vote();
//        vote.setType(request.getType());
//        if(vote.getType().equals("kick")){
//            vote.setKickTarget(request.getKickTarget());
//        }
        //set Task
        Task task = new Task();
//        task.setType("vote");
        task.setVoteID(vote.getId());
        task.setYear(request.getTask().getYear());
        task.setMonth(request.getTask().getMonth());
        task.setDay(request.getTask().getDay());
        task.setPostID(postID);
        vote.setTask(task);
        schedulingService.addSchedule(vote.getTask());
        Post post = getPostById(postID);
        ArrayList<Vote> voteArrayList = new ArrayList<Vote>();
        voteArrayList.add(vote);
        post.setVote(voteArrayList);
        replacePost(post.getId(), post);
//        postRepository.save(post);
        return vote;
    }

    public Vote replaceVote(String postID, String voteID, Vote request){
        Post post = getPostById(postID);
        ArrayList<Vote> voteArrayList = post.getVote();
        Vote newVote = new Vote();
        for(Vote v : voteArrayList){
            if(v.getId().equals(voteID)){//find old vote
                schedulingService.cancelSchedule(v.getTask().getId());//cancel old task
                newVote.setId(v.getId());
//                newVote.setType(request.getType());
                Task newTask  = new Task();//set new task
                newTask.setPostID(postID);
                newTask.setVoteID(voteID);
//                newTask.setType("vote");
                newTask.setYear(request.getTask().getYear());
                newTask.setMonth(request.getTask().getMonth());
                newTask.setDay(request.getTask().getDay());
                newVote.setTask(newTask);//set vote's task
                schedulingService.addSchedule(newVote.getTask());
                newVote.setAgree(request.getAgree());
                newVote.setDisagree(request.getDisagree());
                newVote.setKickTarget(request.getKickTarget());
                newVote.setResult(request.getResult());
                voteArrayList.set(voteArrayList.indexOf(v), newVote);//更新vote
                break;
            }
        }
        post.setVote(voteArrayList);
        replacePost(post.getId(), post);
//        postRepository.save(post);
        return newVote;
    }

    public boolean voteCollaborationVote(String postID, String voteID,String email,String option){
        Post post = getPostById(postID);
        for(Vote v : post.getVote()){
            if(v.getId().equals(voteID)){
                if(v.getAgree().contains(email)){//原本投同意
                    if(option.equals("agree")){//取消同意
                        v.getAgree().remove(email);
                        post.getVote().set(post.getVote().indexOf(v),v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    }else{//改投不同意
                        v.getAgree().remove(email);
                        v.getDisagree().add(email);
                        post.getVote().set(post.getVote().indexOf(v),v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    }
                } else if (v.getDisagree().contains(email)) {//原本投不同意
                    if(option.equals("agree")){//改投同意
                        v.getDisagree().remove(email);
                        v.getAgree().add(email);
                        post.getVote().set(post.getVote().indexOf(v),v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    }else{//取消同意
                        v.getDisagree().remove(email);
                        post.getVote().set(post.getVote().indexOf(v),v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    }
                }else{//尚未投票
                    if(option.equals("agree")){//投同意
                        v.getAgree().add(email);
                        post.getVote().set(post.getVote().indexOf(v),v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    }else{//投不同意
                        v.getDisagree().add(email);
                        post.getVote().set(post.getVote().indexOf(v),v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean rewardChooseBestAnswer(String postID, String answerID, String email){
        Post post = getPostById(postID);
        if(post.getEmail().contains(email)){//確認為貼文作者
            //TODO 可以更改最佳解嗎 =>可以 點數變動問題
            noteService.rewardNoteBestAnswer(answerID,email);
            return true;
        }
        return false;
    }

    public boolean QAChooseBestAnswer(String postID, String commentID, String email){
        //TODO 點數增減
        Post post = getPostById(postID);
        if(post.getEmail().contains(email)){
            for(Comment c: post.getComments()){
                if(c.getId().equals(commentID)){
                    Comment bestComment = c;
                    bestComment.setBest(true);
                    post.getComments().set(post.getComments().indexOf(c),bestComment);
                    replacePost(post.getId(), post);
//                    postRepository.save(post);
                }else{
                    if(c.getBest()){
                        Comment notBestComment = c;
                        notBestComment.setBest(false);
                        post.getComments().set(post.getComments().indexOf(c),notBestComment);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean QAChooseReferenceAnswer(String postID, String commentID, String email){
        //TODO 參考解數量
        //TODO 點數增減
        Post post = getPostById(postID);
        if(post.getEmail().contains(email)){
            for(Comment c: post.getComments()){
                if(c.getId().equals(commentID)){
                    Comment referenceComment = c;
//                    referenceComment.setReference(true);
                    post.getComments().set(post.getComments().indexOf(c),referenceComment);
                    replacePost(post.getId(), post);
//                    postRepository.save(post);
                    return true;
                }
            }
        }
        return false;
    }
}
