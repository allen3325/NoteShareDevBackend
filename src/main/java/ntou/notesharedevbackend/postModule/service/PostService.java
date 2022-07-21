package ntou.notesharedevbackend.postModule.service;

import ntou.notesharedevbackend.coinModule.entity.Coin;
import ntou.notesharedevbackend.coinModule.service.CoinService;
import ntou.notesharedevbackend.commentModule.entity.*;
import ntou.notesharedevbackend.commentModule.service.*;
import ntou.notesharedevbackend.noteModule.entity.Note;
import ntou.notesharedevbackend.noteModule.entity.NoteReturn;
import ntou.notesharedevbackend.noteModule.service.NoteService;
import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.notificationModule.service.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.repository.PostRepository;
import ntou.notesharedevbackend.schedulerModule.entity.KickVoteRequest;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.schedulerModule.entity.VoteReturn;
import ntou.notesharedevbackend.schedulerModule.service.SchedulingService;
import ntou.notesharedevbackend.userModule.entity.*;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    @Lazy(value = true)
    private NoteService noteService;
    @Autowired
    @Lazy(value = true)
    private SchedulingService schedulingService;
    @Autowired
    @Lazy(value = true)
    private AppUserService appUserService;
    @Autowired
    @Lazy(value = true)
    private CoinService coinService;
    @Autowired
    @Lazy(value = true)
    private CommentService commentService;
    @Autowired
    @Lazy(value = true)
    private NotificationService notificationService;
    @Autowired
    @Lazy(value = true)
    private SimpMessagingTemplate messagingTemplate;

//    protected final SimpMessagingTemplate messagingTemplate;
//
//    @Autowired
//    protected PostService(SimpMessagingTemplate messagingTemplate) { this.messagingTemplate = messagingTemplate; }

    public Post[] getAllTypeOfPost(String postType) {
        List<Post> postList = postRepository.findAllByType(postType);
        return postList.toArray(new Post[0]);
    }

    public Post getPostById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find post."));
    }

    public Post createPost(String userEmail, PostRequest request) {
        AppUser appUser = appUserService.getUserByEmail(userEmail);
        ArrayList<String> email = new ArrayList<>();
        email.add(userEmail);
        Post post = new Post();
        post.setType(request.getType());
        post.setEmail(email);
        post.setAuthor(userEmail);
        post.setAuthorName(appUser.getName());
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
        post.setCollabApply(new ArrayList<Apply>());
        post.setPublishDate(request.getPublishDate());
        post.setVote(new ArrayList<Vote>());
        post.setArchive(false);
        post.setApplyEmail(new ArrayList<String>());
        if (request.getType().equals("collaboration")) {//若為共筆貼文，須建立共筆筆記
            post.setCollabNoteAuthorNumber(post.getEmail().size());
            Note note = new Note();
//            note.setCreatedAt(request.getCreatedAt());
            note.setTitle(request.getTitle());
            note.setType(request.getType());
            note.setPublic(false);
            note.setDepartment(request.getDepartment());
            note.setSubject(request.getSubject());
            note.setSchool(request.getSchool());
            note.setProfessor(request.getProfessor());
            note.setPrice(request.getBestPrice());
            note.setDownloadable(false);
            note.setPublic(false);
            note.setQuotable(false);
            Note createdNote = noteService.createNote(note, userEmail);
            ArrayList<String> answers = new ArrayList<String>();//把新增的共筆筆記ID存到共筆貼文內的answer
            answers.add(createdNote.getId());
            post.setAnswers(answers);
            Post newPost = postRepository.insert(post);
            noteService.collaborationNoteSetPostID(createdNote.getId(), newPost.getId());
            return newPost;
        }
        return postRepository.insert(post);
    }

    public Post replacePost(String id, Post request) {
        Post oldPost = getPostById(id);

        Post post = new Post();
        post.setId(oldPost.getId());
        post.setType(request.getType());
        post.setEmail(request.getEmail());
        post.setAuthor(request.getAuthor());
        post.setAuthorName(request.getAuthorName());
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
        post.setCollabApply(request.getCollabApply());
        post.setPublishDate(request.getDate());
        post.setVote(request.getVote());
        post.setCollabNoteAuthorNumber(post.getEmail().size());
        post.setApplyEmail(request.getApplyEmail());
        post.setArchive(request.getArchive());
        return postRepository.save(post);
    }

    public boolean deletePost(String id) {
        Post post = getPostById(id);
        if (post.getType().equals("reward")) {
            //已選最佳解
            if (post.getAnswers().size() != 0 && noteService.rewardNoteHaveAnswer(post.getAnswers())) {
                postRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } else { //QA跟共筆不能刪
            return false;
        }
    }

    public Post modifyPublishStatus(String id) {
        Post post = getPostById(id);
        if (post.getPublic()) {//想改成非公開
            if (post.getType().equals("reward")) {//懸賞判斷有無best answer
                if (post.getAnswers().size() != 0 && noteService.rewardNoteHaveAnswer(post.getAnswers())) {
                    post.setPublic(!post.getPublic());
                    //歸還非最佳解且非參考解的筆記
                    replacePost(post.getId(), post);
                    noteService.returnRewardNoteToAuthor(post.getId(), post.getAnswers());
                } else {
                    System.out.println("can't change publish state before you got best answer.");
                    return null;
                }
            } else if (post.getType().equals("QA")) {//QA判斷有無best answer
                if (QAhaveBestAnswer(post.getComments())) {
                    post.setPublic(!post.getPublic());
                    replacePost(post.getId(), post);
                } else {
                    System.out.println("can't change publish state before you got best answer.");
                    return null;
                }
            } else if (post.getType().equals("collaboration")) {//共筆無條件
                post.setPublic(!post.getPublic());
                replacePost(post.getId(), post);
            }
        } else {
            post.setPublic(!post.getPublic());
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
            post.setPublishDate(calendar.getTime());
            replacePost(post.getId(), post);

            //傳送訊息給開啟bell的使用者
            List<String> emails = post.getEmail();
            for (String email : emails) {
                MessageReturn messageReturn = notificationService.getMessageReturn(email, "發布了貼文", post.getType().toLowerCase(), id);
                messagingTemplate.convertAndSend("/topic/bell-messages/" + email, messageReturn);
                notificationService.saveNotificationBell(email, messageReturn);
            }
        }

        return getPostById(id);
//        postRepository.save(post);
    }

    public boolean QAhaveBestAnswer(ArrayList<Comment> comments) {
        for (Comment c : comments) {
            if (c.getBest()) return true;
        }
        return false;
    }

    public Boolean applyCollaboration(String id, Apply applicant) {
        Post post = getPostById(id);
//        if (post.getApplyEmail().contains(applicant.getWantEnterUsersEmail())) {
//            return false;
//        }
//        post.getApplyEmail().add(applicant.getWantEnterUsersEmail());
        // get all apply
        ArrayList<Apply> allApply = post.getCollabApply();
        allApply.add(applicant);
        // update apply in post
        post.setCollabApply(allApply);
        replacePost(post.getId(), post);

        //傳送通知給管理員&共筆發起人
        ArrayList<String> emails = post.getEmail();
        for (String email : emails) {
            MessageReturn messageReturn = notificationService.getMessageReturn(applicant.getWantEnterUsersEmail(), "向你申請了共筆", "collaboration", id);
            messagingTemplate.convertAndSendToUser(email, "/topic/private-messages", messageReturn);
            notificationService.saveNotificationPrivate(email, messageReturn);
        }

        return true;
//        postRepository.save(post);
    }

    public void approveCollaboration(String id, String email) {
        // get author's name by email
        String name = appUserService.getUserByEmail(email).getName();
        // update post
        Post post = getPostById(id);
        ArrayList<String> currentEmails = post.getEmail();
        currentEmails.add(email);
        post.setEmail(currentEmails);
        // update note's author name and author email
        String noteID = post.getAnswers().get(0);
        Note note = noteService.getNote(noteID);
        // update author email
        ArrayList<String> authorEmail = note.getAuthorEmail();
        authorEmail.add(email);
        note.setAuthorEmail(authorEmail);
        // update author name
        ArrayList<String> authorName = note.getAuthorName();
        authorName.add(name);
        note.setAuthorName(authorName);
        // update note
        noteService.replaceNote(note, note.getId());
        // update want enter queue
        clearWantEnterUsersEmail(post, email);
        replacePost(post.getId(), post);
    }

    public void clearWantEnterUsersEmail(Post post, String email) {
        // get current all applicant and remove the target
        ArrayList<Apply> currentApplicant = post.getCollabApply();
        currentApplicant.removeIf(applicant -> applicant.getWantEnterUsersEmail().equals(email));
        // update apply in post
        post.setCollabApply(currentApplicant);
        replacePost(post.getId(), post);
    }

//    public Task schedulerPublishTime(String postID, Task request) {
//        Post post = getPostById(postID);
//        if (post.getType().equals("collaboration")) {//check post is collaboration
//            Task task = new Task();
//            task.setPostID(postID);
////            task.setType("publish");
//            task.setVoteID(post.getAnswers().get(0));
//            task.setYear(request.getYear());
//            task.setMonth(request.getMonth());
//            task.setDay(request.getDay());
////            post.setTask(task);
//            replacePost(post.getId(), post);
////            postRepository.save(post);
//            schedulingService.addSchedule(task);
//            return task;
//        } else {
//            return null;
//        }
//    }

//    public Task replacePublishTime(String postID, Task request) {
//        Post post = getPostById(postID);
////        post.setTask(null);
//        replacePost(post.getId(), post);
////        postRepository.save(post);
//        return schedulerPublishTime(postID, request);
//    }

    public Vote addVote(String postID, KickVoteRequest request) {
        Vote vote = new Vote();
        vote.setKickTarget(request.getKickTargetEmail());
        //set Task
        Task task = new Task();
        task.setVoteID(vote.getId());
        task.setYear(request.getYear());
        task.setMonth(request.getMonth());
        task.setDay(request.getDay());
        task.setPostID(postID);
        vote.setTask(task);
        schedulingService.addSchedule(vote.getTask());
        Post post = getPostById(postID);
        ArrayList<Vote> voteArrayList = post.getVote();
        voteArrayList.add(vote);
        post.setVote(voteArrayList);
        replacePost(post.getId(), post);
//        postRepository.save(post);

        //傳送通知給所有共筆作者
        MessageReturn messageReturn = new MessageReturn();
        messageReturn.setMessage("有人在共筆內發起了投票");
        UserObj userObj = new UserObj();
        userObj.setUserObjEmail("noteshare@gmail.com");
        userObj.setUserObjName("NoteShare System");
        userObj.setUserObjAvatar("https://i.imgur.com/5V1waq3.png");
        messageReturn.setUserObj(userObj);
        messageReturn.setType("collaboration");
        messageReturn.setId(postID);
        messageReturn.setDate(new Date());
        for (String author : post.getEmail()) {
            messagingTemplate.convertAndSendToUser(author, "/topic/private-messages", messageReturn);
            notificationService.saveNotificationPrivate(author, messageReturn);
        }

        return vote;
    }

    public Vote replaceVote(String postID, String voteID, KickVoteRequest request) {
        Post post = getPostById(postID);
        ArrayList<Vote> voteArrayList = post.getVote();
        Vote newVote = new Vote();
        for (Vote v : voteArrayList) {
            //find old vote
            if (v.getId().equals(voteID)) {
                //取消原本的排程
                schedulingService.cancelSchedule(v.getTask().getId());
//                newVote.setId(v.getId());
//                newVote.setType(request.getType());
                //set new task 設定新的排程
                Task newTask = new Task();
                newTask.setPostID(postID);
                newTask.setVoteID(voteID);
                newTask.setYear(request.getYear());
                newTask.setMonth(request.getMonth());
                newTask.setDay(request.getDay());
                v.setTask(newTask);
//                newVote.setTask(newTask);//set vote's task
                schedulingService.addSchedule(v.getTask());
//                schedulingService.addSchedule(newVote.getTask());
//                newVote.setAgree(request.getAgree());
//                newVote.setDisagree(request.getDisagree());
//                newVote.setKickTarget(request.getKickTarget());
//                newVote.setResult(request.getResult());
//                voteArrayList.set(voteArrayList.indexOf(v), newVote);//更新vote
                newVote = v;
                break;
            }
        }
//        post.setVote(voteArrayList);
        replacePost(post.getId(), post);
//        postRepository.save(post);
        return newVote;
    }

    public boolean voteCollaborationVote(String postID, String voteID, String email, VoteRequest request) {
        Post post = getPostById(postID);
        //檢查是否為此共筆作者
        if (!post.getEmail().contains(email)) {
            return false;
        }
        for (Vote v : post.getVote()) {
            if (v.getId().equals(voteID)) {
                if (v.getAgree().contains(email)) {//原本投同意
                    if (request.getOption().equals("agree")) {//取消同意
                        v.getAgree().remove(email);
                        post.getVote().set(post.getVote().indexOf(v), v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    } else {//改投不同意
                        v.getAgree().remove(email);
                        v.getDisagree().add(email);
                        post.getVote().set(post.getVote().indexOf(v), v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    }
                } else if (v.getDisagree().contains(email)) {//原本投不同意
                    if (request.getOption().equals("agree")) {//改投同意
                        v.getDisagree().remove(email);
                        v.getAgree().add(email);
                        post.getVote().set(post.getVote().indexOf(v), v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    } else {//取消同意
                        v.getDisagree().remove(email);
                        post.getVote().set(post.getVote().indexOf(v), v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    }
                } else {//尚未投票
                    if (request.getOption().equals("agree")) {//投同意
                        v.getAgree().add(email);
                        post.getVote().set(post.getVote().indexOf(v), v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    } else {//投不同意
                        v.getDisagree().add(email);
                        post.getVote().set(post.getVote().indexOf(v), v);
                        replacePost(post.getId(), post);
//                        postRepository.save(post);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean rewardChooseBestAnswer(String postID, String answerID) {
        Post post = getPostById(postID);
        AppUser appUser = appUserService.getUserByEmail(post.getAuthor());//懸賞人
        String bestPrice = String.valueOf(post.getBestPrice());
        Coin postAuthorCoin = new Coin();
        postAuthorCoin.setCoin('-' + bestPrice);
        coinService.changeCoin(appUser.getEmail(), postAuthorCoin);
        noteService.rewardNoteBestAnswer(answerID, appUser.getEmail(), bestPrice);
        //檢查是否選完最佳解和參考解
        if (noteService.rewardNoteHaveAnswer(post.getAnswers()) && post.getReferenceNumber().equals(0)) {
            //選完歸還剩餘筆記
            noteService.returnRewardNoteToAuthor(post.getId(), post.getAnswers());
        }
        //傳送通知給懸賞筆記contributor
        MessageReturn messageReturn = notificationService.getMessageReturn(appUser.getEmail(), "將你的懸賞筆記設為最佳解", "reward", postID);
        Note note = noteService.getNote(answerID);
        String contributor = note.getAuthorEmail().get(0);
        messagingTemplate.convertAndSendToUser(contributor, "/topic/private-messages", messageReturn);
        notificationService.saveNotificationPrivate(contributor, messageReturn);

        return true;
    }

    public boolean QAChooseBestAnswer(String postID, String commentID) {
        Post post = getPostById(postID);
        Coin coin = new Coin();
        // change price to String
        String price = String.valueOf(post.getBestPrice());
        if (post.getAnswers().isEmpty()) {
            ArrayList<String> answer = new ArrayList<>();
            ArrayList<Comment> allComments = post.getComments();
            for (Comment comment : allComments) {
                if (comment.getId().equals(commentID)) {
                    comment.setBest(true);
                    answer.add(commentID);
                    post.setAnswers(answer);
                    // add comment's author's money.
                    coin.setCoin("+" + price);
                    coinService.changeCoin(comment.getEmail(), coin);
                    // minus post's author's money.
                    coin.setCoin("-" + price);
                    coinService.changeCoin(post.getEmail().get(0), coin);

                    //傳送通知給QA最佳解使用者
                    MessageReturn messageReturn = notificationService.getMessageReturn(post.getAuthor(), "將你的答案設為最佳解", "qa", postID);
                    messagingTemplate.convertAndSendToUser(comment.getEmail(), "/topic/private-messages", messageReturn);
                    notificationService.saveNotificationPrivate(comment.getEmail(), messageReturn);
                }
            }
            post.setComments(allComments);
            replacePost(post.getId(), post);

        } else {
            return false;
        }
        return true;
    }

    public boolean rewardChooseReferenceAnswer(String postID, String answerID) {
        Post post = getPostById(postID);
        AppUser appUser = appUserService.getUserByEmail(post.getAuthor());
        String referencePrice = String.valueOf(post.getReferencePrice());
        if (post.getReferenceNumber() > 0) {
            post.setReferenceNumber(post.getReferenceNumber() - 1);
            post = replacePost(postID, post);
            Coin postAuthorCoin = new Coin();
            postAuthorCoin.setCoin('-' + referencePrice);
            coinService.changeCoin(appUser.getEmail(), postAuthorCoin);//作者扣點
            noteService.rewardNoteReferenceAnswer(answerID, appUser.getEmail(), referencePrice);
            //檢查是否選完最佳解和參考解
            if (noteService.rewardNoteHaveAnswer(post.getAnswers()) && post.getReferenceNumber().equals(0)) {
                //選完歸還剩餘筆記
                noteService.returnRewardNoteToAuthor(post.getId(), post.getAnswers());
            }
            //傳送通知給懸賞筆記contributor
            MessageReturn messageReturn = notificationService.getMessageReturn(appUser.getEmail(), "將你的懸賞筆記設為參考解", "reward", postID);
            Note note = noteService.getNote(answerID);
            String contributor = note.getAuthorEmail().get(0);
            messagingTemplate.convertAndSendToUser(contributor, "/topic/private-messages", messageReturn);
            notificationService.saveNotificationPrivate(contributor, messageReturn);

            return true;
        }
        return false;
    }

    public ArrayList<Post> getUserAllPostByType(String email, String postType) {
        List<Post> allPost = postRepository.findAllByEmailContainingAndType(email, postType);
        return new ArrayList<Post>(allPost);
    }

    public void kickUserFromCollaboration(String postID, String email) {
        Post post = getPostById(postID);
        ArrayList<String> emails = post.getEmail();
        emails.remove(email);
        post.setEmail(emails);
        post.setCollabNoteAuthorNumber(post.getCollabNoteAuthorNumber() - 1);
        replacePost(postID, post);
    }

    public void denyCollaboration(String postID, String denyEmail) {
        Post post = getPostById(postID);
        ArrayList<Apply> allApply = post.getCollabApply();
        // find deny in all apply
        for (Apply applicant : allApply) {
            if (applicant.getWantEnterUsersEmail().equals(denyEmail)) {
                // find deny
                allApply.remove(applicant);
                break;
            }
        }
        post.setCollabApply(allApply);
        replacePost(postID, post);
    }

    public void deleteVote(String postID, String voteID) {
        Post post = getPostById(postID);
        ArrayList<Vote> voteArrayList = post.getVote();
        for (Vote vote : voteArrayList) {
            if (vote.getId().equals(voteID)) {
                voteArrayList.remove(vote);
                break;
            }
        }
        post.setVote(voteArrayList);
        replacePost(postID, post);
    }

    public PostReturn getUserInfo(Post post) {
        PostReturn postReturn = new PostReturn();
        postReturn.setId(post.getId());
        postReturn.setType(post.getType());
        postReturn.setDepartment(post.getDepartment());
        postReturn.setSubject(post.getSubject());
        postReturn.setSchool(post.getSchool());
        postReturn.setProfessor(post.getProfessor());
        postReturn.setTitle(post.getTitle());
        postReturn.setContent(post.getContent());
        postReturn.setDate(post.getDate());
        postReturn.setBestPrice(post.getBestPrice());
        postReturn.setReferencePrice(post.getReferencePrice());
        postReturn.setReferenceNumber(post.getReferenceNumber());
        postReturn.setPublic(post.getPublic());
        postReturn.setComments(post.getComments());
        ArrayList<CommentReturn> commentReturnArrayList = new ArrayList<>();
        for (Comment comment : post.getComments()) {
            CommentReturn commentReturn = commentService.getUserInfo(comment);
            commentReturnArrayList.add(commentReturn);
        }
        postReturn.setCommentsUserObj(commentReturnArrayList);

        postReturn.setCommentCount(post.getCommentCount());
        postReturn.setAnswers(post.getAnswers());
        postReturn.setPublishDate(post.getPublishDate());
        postReturn.setVote(post.getVote());
        ArrayList<VoteReturn> voteReturnArrayList = new ArrayList<>();
        for (Vote vote : post.getVote()) {
            voteReturnArrayList.add(schedulingService.getUserInfo(vote));
        }
        postReturn.setVoteUserObj(voteReturnArrayList);
        postReturn.setCollabNoteAuthorNumber(post.getCollabNoteAuthorNumber());
        ArrayList<ApplyReturn> collabApplyArrayList = new ArrayList<>();
        if (post.getCollabApply() != null) {
            for (Apply apply : post.getCollabApply()) {
                ApplyReturn applyReturn = new ApplyReturn();
                applyReturn.setUserObj(appUserService.getUserInfo(apply.getWantEnterUsersEmail()));
                applyReturn.setCommentFromApplicant(apply.getCommentFromApplicant());
                collabApplyArrayList.add(applyReturn);
            }
            postReturn.setCollabApply(post.getCollabApply());
        }
        postReturn.setCollabApplyUserObj(collabApplyArrayList);
        UserObj userObj = appUserService.getUserInfo(post.getAuthor());
        postReturn.setAuthorUserObj(userObj);
        postReturn.setAuthorName(post.getAuthorName());
        postReturn.setAuthor(post.getAuthor());
        ArrayList<UserObj> emailUserObj = new ArrayList<>();
        for (String email : post.getEmail()) {
            UserObj userInfo = appUserService.getUserInfo(email);
            emailUserObj.add(userInfo);
        }
        postReturn.setEmailUserObj(emailUserObj);
        postReturn.setEmail(post.getEmail());
        postReturn.setArchive(post.getArchive());
        ArrayList<NotePostReturn> answersUserObj = new ArrayList<>();
        if (post.getAnswers() != null) {
            if (post.getType().equals("QA") && !post.getAnswers().isEmpty()) {//為QA且有最佳解
                NotePostReturn notePostReturn = new NotePostReturn();
                String answerComment = post.getAnswers().get(0);
                notePostReturn.setId(answerComment);
                for (Comment comment : post.getComments()) {
                    if (comment.getId().equals(answerComment)) {
                        notePostReturn.setDate(comment.getDate());
                        UserObj userObj1 = appUserService.getUserInfo(comment.getEmail());
                        notePostReturn.setUserObj(userObj1);
                        notePostReturn.setBest(true);
                        notePostReturn.setReference(false);
                        answersUserObj.add(notePostReturn);
                        break;
                    }
                }
            } else {
                for (String noteID : post.getAnswers()) {
                    NotePostReturn notePostReturn = new NotePostReturn();
//                    System.out.println(noteID);
                    notePostReturn.setId(noteID);
                    Note note = noteService.getNote(noteID);
                    notePostReturn.setDate(note.getPublishDate());
                    UserObj userObj1 = appUserService.getUserInfo(note.getHeaderEmail());
                    notePostReturn.setUserObj(userObj1);
                    if (note.getReference() != null && note.getReference()) {
                        notePostReturn.setReference(true);
                    } else {
                        notePostReturn.setReference(false);
                    }
                    if (note.getBest() != null && note.getBest()) {
                        notePostReturn.setBest(true);
                    } else {
                        notePostReturn.setBest(false);
                    }
                    answersUserObj.add(notePostReturn);
                }

            }
        }
        postReturn.setAnswersUserObj(answersUserObj);
//        ArrayList<UserObj> applyUserObj = new ArrayList<>();
//        for (String applyEmail : post.getApplyEmail()) {
//            UserObj userObjInfo = appUserService.getUserInfo(applyEmail);
//            applyUserObj.add(userObjInfo);
//        }
//        postReturn.setApplyUserObj(applyUserObj);
//        postReturn.setApplyEmail(post.getApplyEmail());
        return postReturn;
    }

    public NoteReturn createRewardNote(String postID, String email, Note request) {
        Note note = noteService.createRewardNote(postID, email, request);

        return noteService.getUserinfo(note);
    }

    public Boolean archivePost(String postID) {
        Post post = getPostById(postID);
        if (!post.getArchive()) {//想封存
            if (post.getType().equals("reward")) {//懸賞判斷有無best answer
                if (post.getAnswers().size() != 0 && noteService.rewardNoteHaveAnswer(post.getAnswers())) {
                    post.setArchive(!post.getArchive());
                    replacePost(postID, post);
                    //歸還非最佳非參考的筆記
                    noteService.returnRewardNoteToAuthor(post.getId(), post.getAnswers());
                    return true;
                } else {
                    System.out.println("can't change publish state before you got best answer.");
                    return false;
                }
            } else if (post.getType().equals("QA")) {//QA判斷有無best answer
                if (QAhaveBestAnswer(post.getComments())) {
                    post.setArchive(!post.getArchive());
                    replacePost(postID, post);
                    return true;
                } else {
                    System.out.println("can't change publish state before you got best answer.");
                    return false;
                }
            } else if (post.getType().equals("collaboration")) {//共筆無條件
                post.setArchive(!post.getArchive());
                replacePost(postID, post);
                return true;
            }
        } else {//想解除封存
            post.setArchive(!post.getArchive());
            replacePost(postID, post);
            return true;
        }
        return true;
    }

}
