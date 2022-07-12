package ntou.notesharedevbackend.commentModule.service;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.commentModule.entity.CommentRequest;
import ntou.notesharedevbackend.commentModule.entity.CommentReturn;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import ntou.notesharedevbackend.notificationModule.entity.Message;
import ntou.notesharedevbackend.notificationModule.entity.MessageReturn;
import ntou.notesharedevbackend.notificationModule.service.NotificationService;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.PostRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.entity.UserObj;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteService noteService;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private NotificationService notificationService;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public CommentService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public ArrayList<Comment> getAllCommentsByID(String id) {

        if (noteRepository.findById(id).isPresent()) {
            Note note = noteService.getNote(id);
            return note.getComments();
        } else if (postRepository.findById(id).isPresent()) {
            Post post = postService.getPostById(id);
//            if(post.getType().equals("QA")) {
//                return post.getComments();
//            }else{
//                throw new NotFoundException("Wrong post type");
//            }
            return post.getComments();
        } else {
            throw new NotFoundException("Can't find any note or post matched id");
        }
    }

    public Comment getCommentByFloor(String id, int floor) {
        ArrayList<Comment> commentArrayList = getAllCommentsByID(id);
        Comment comment = commentArrayList.get(floor);
        if (comment.getFloor() != floor) {//check floor
            for (Comment c : commentArrayList) {
                if (c.getFloor() == floor) {
                    comment = c;
                }
            }
        }
        return comment;
    }//需要去檢查每個comment中的floor嗎

    public Comment createComment(String id, CommentRequest request) {
        Note note = new Note();
        Post post = new Post();
        String type = "";
        AppUser appUser = appUserService.getUserByEmail(request.getEmail());
        //尋找筆記或貼文
        ArrayList<Comment> commentArrayList = new ArrayList<>();
        if (noteRepository.findById(id).isPresent()) {
            type = "note";
            note = noteService.getNote(id);
            commentArrayList = note.getComments();
        } else if (postRepository.findById(id).isPresent()) {
            type = "post";
            post = postService.getPostById(id);
            commentArrayList = post.getComments();
        } else {
            throw new NotFoundException("Can't find any note or post matched id");
        }
        //更新留言
        Comment comment = new Comment();
        comment.setAuthor(appUser.getName());
        comment.setContent(request.getContent());
        comment.setDate();
        comment.setEmail(request.getEmail());
        comment.setFloor(commentArrayList.size());
        if (request.getPicURL() != null) {
            comment.setPicURL(request.getPicURL());
        } else {
            comment.setPicURL(new ArrayList<String>());
        }
        //更新筆記或貼文
        UserObj userObj = appUserService.getUserInfo(comment.getEmail());
        commentArrayList.add(comment);
        if (type.equals("note")) {
            note.setComments(commentArrayList);
            noteService.replaceNote(note, note.getId());
            //通知筆記作者
            MessageReturn messageReturn = new MessageReturn();
            messageReturn.setDate(new Date());
            messageReturn.setMessage(comment.getAuthor() + "在你的筆記內留言");
            messageReturn.setId(note.getId());
            messageReturn.setType("note");
            messageReturn.setUserObj(userObj);
            if (note.getType().equals("collaboration")) {//共筆要寄給所有作者
                messagingTemplate.convertAndSend("/topic/group-messages/" + note.getId(), messageReturn);
                notificationService.saveNotificationGroup(note.getId(), messageReturn);
            } else {
                messagingTemplate.convertAndSendToUser(note.getHeaderEmail(), "/topic/private-messages", messageReturn);
                notificationService.saveNotificationPrivate(note.getHeaderEmail(), messageReturn);
            }
            ArrayList<String> taggedEmails;
            if ((taggedEmails = tagOtherCommentAuthor(request.getContent(), commentArrayList)) != null) {//可在留言內@多人
                //通知被@的人
                MessageReturn messageReturnToCommentAuthor = new MessageReturn();
                messageReturnToCommentAuthor.setDate(new Date());
                messageReturnToCommentAuthor.setMessage(comment.getAuthor() + "在筆記留言中提及你");
                messageReturnToCommentAuthor.setId(note.getId());
                messageReturnToCommentAuthor.setType("note");
                messageReturnToCommentAuthor.setUserObj(userObj);
                for (String taggedEmail : taggedEmails) {
                    messagingTemplate.convertAndSendToUser(taggedEmail, "/topic/private-messages", messageReturnToCommentAuthor);
                    notificationService.saveNotificationPrivate(taggedEmail, messageReturnToCommentAuthor);
                }
            }

        } else {
            post.setComments(commentArrayList);
            postService.replacePost(post.getId(), post);
            //通知貼文作者
            MessageReturn messageReturn = new MessageReturn();
            messageReturn.setDate(new Date());
            messageReturn.setMessage(comment.getAuthor() + "在你的貼文內留言");
            messageReturn.setId(post.getId());
            messageReturn.setType(post.getType());
            messageReturn.setUserObj(userObj);
            if (post.getType().equals("collaboration")) {//共筆要寄給所有作者
                Note collaborationNote = noteService.getNote(post.getAnswers().get(0));//從共筆筆記拿到所有作者
                messagingTemplate.convertAndSend("/topic/group-messages/" + collaborationNote.getId(), messageReturn);
                notificationService.saveNotificationGroup(collaborationNote.getId(), messageReturn);
            } else {
                messagingTemplate.convertAndSendToUser(post.getAuthor(), "/topic/private-messages", messageReturn);
                notificationService.saveNotificationPrivate(post.getAuthor(), messageReturn);
            }
            ArrayList<String> taggedEmails;
            if ((taggedEmails = tagOtherCommentAuthor(request.getContent(), commentArrayList)) != null) {//可提及多人
                MessageReturn messageReturnToCommentAuthor = new MessageReturn();
                messageReturnToCommentAuthor.setDate(new Date());
                //通知被@的留言作者
                messageReturnToCommentAuthor.setMessage(comment.getAuthor() + "在貼文留言中提及你");
                messageReturnToCommentAuthor.setId(post.getId());
                messageReturnToCommentAuthor.setType(post.getType());
                messageReturnToCommentAuthor.setUserObj(userObj);
                for (String taggedEmail : taggedEmails) {
                    messagingTemplate.convertAndSendToUser(taggedEmail, "/topic/private-messages", messageReturnToCommentAuthor);
                    notificationService.saveNotificationPrivate(taggedEmail, messageReturnToCommentAuthor);
                }
            }
        }
        return comment;
    }

    public Comment updateComment(String id, Integer floor, CommentRequest request) {
        ArrayList<Comment> commentArrayList = new ArrayList<Comment>();

        if (noteRepository.findById(id).isPresent()) {
            Note note = noteService.getNote(id);
            commentArrayList = note.getComments();
            commentArrayList.get(floor).setContent(request.getContent());
            commentArrayList.get(floor).setDate();
            commentArrayList.get(floor).setPicURL(request.getPicURL());
            note.setComments(commentArrayList);
            noteService.replaceNote(note, note.getId());
            ArrayList<String> taggedEmails;//可在留言內@多人
            if ((taggedEmails = tagOtherCommentAuthor(request.getContent(), commentArrayList)) != null) {//檢查有無提及人
                //通知被@的人
                MessageReturn messageReturnToCommentAuthor = new MessageReturn();
                messageReturnToCommentAuthor.setDate(new Date());
                messageReturnToCommentAuthor.setMessage(commentArrayList.get(floor).getAuthor() + "在筆記留言中提及你");
                messageReturnToCommentAuthor.setId(note.getId());
                messageReturnToCommentAuthor.setType("note");
                UserObj userObj = appUserService.getUserInfo(commentArrayList.get(floor).getEmail());
                messageReturnToCommentAuthor.setUserObj(userObj);
                for (String taggedEmail : taggedEmails) {
                    messagingTemplate.convertAndSendToUser(taggedEmail, "/topic/private-messages", messageReturnToCommentAuthor);
                    notificationService.saveNotificationPrivate(taggedEmail, messageReturnToCommentAuthor);
                }
            }
            return commentArrayList.get(floor);
        } else if (postRepository.findById(id).isPresent()) {
            Post post = postService.getPostById(id);
            commentArrayList = post.getComments();
            commentArrayList.get(floor).setContent(request.getContent());
            commentArrayList.get(floor).setDate();
            commentArrayList.get(floor).setPicURL(request.getPicURL());
            post.setComments(commentArrayList);
            postService.replacePost(post.getId(), post);
            ArrayList<String> taggedEmails;//可在留言內@多人
            if ((taggedEmails = tagOtherCommentAuthor(request.getContent(), commentArrayList)) != null) {//檢查有無提及人
                //通知被@的人
                MessageReturn messageReturnToCommentAuthor = new MessageReturn();
                messageReturnToCommentAuthor.setDate(new Date());
                messageReturnToCommentAuthor.setMessage(commentArrayList.get(floor).getAuthor() + "在貼文留言中提及你");
                messageReturnToCommentAuthor.setId(post.getId());
                messageReturnToCommentAuthor.setType(post.getType());
                UserObj userObj = appUserService.getUserInfo(commentArrayList.get(floor).getEmail());
                messageReturnToCommentAuthor.setUserObj(userObj);
                for (String taggedEmail : taggedEmails) {
                    messagingTemplate.convertAndSendToUser(taggedEmail, "/topic/private-messages", messageReturnToCommentAuthor);
                    notificationService.saveNotificationPrivate(taggedEmail, messageReturnToCommentAuthor);
                }
            }
            return commentArrayList.get(floor);
        } else {
            throw new NotFoundException("Can't find any note or post matched id");
        }
    }

    public Boolean deleteComment(String id, Integer floor) {
        Note note = new Note();
        Post post = new Post();
        ArrayList<Comment> commentArrayList = new ArrayList<Comment>();
        if (noteRepository.findById(id).isPresent()) {
            note = noteService.getNote(id);
            commentArrayList = note.getComments();
            commentArrayList.get(floor).setEmail(null);
            commentArrayList.get(floor).setAuthor(null);
            commentArrayList.get(floor).setDate();
            commentArrayList.get(floor).setContent(null);
            commentArrayList.get(floor).setLikeCount(null);
            commentArrayList.get(floor).setLiker(null);
            commentArrayList.get(floor).setBest(null);
            note.setComments(commentArrayList);
            noteService.replaceNote(note, note.getId());
            return true;
        } else if (postRepository.findById(id).isPresent()) {
            post = postService.getPostById(id);
            commentArrayList = post.getComments();
            commentArrayList.get(floor).setEmail(null);
            commentArrayList.get(floor).setAuthor(null);
            commentArrayList.get(floor).setDate();
            commentArrayList.get(floor).setContent(null);
            commentArrayList.get(floor).setLikeCount(null);
            commentArrayList.get(floor).setLiker(null);
            commentArrayList.get(floor).setBest(null);
            post.setComments(commentArrayList);
            postService.replacePost(post.getId(), post);
            return true;
        } else {
            return false;
        }
    }

    public CommentReturn getUserInfo(Comment comment) {
        CommentReturn commentReturn = new CommentReturn();
        commentReturn.setId(comment.getId());
        if (comment.getEmail() == null) {
            commentReturn.setAuthor(null);
            commentReturn.setEmail(null);
            commentReturn.setContent(null);
            commentReturn.setLikeCount(null);
            commentReturn.setLiker(null);
            commentReturn.setFloor(null);
            commentReturn.setDate(null);
            commentReturn.setPicURL(null);
            commentReturn.setBest(null);
            UserObj userObj = new UserObj();
            userObj.setUserObjAvatar(null);
            userObj.setUserObjEmail(null);
            userObj.setUserObjName(null);
            commentReturn.setUserObj(userObj);
            ArrayList<UserObj> likerUserObj = new ArrayList<>();
            commentReturn.setLikerUserObj(null);
        } else {
            commentReturn.setAuthor(comment.getAuthor());
            commentReturn.setEmail(comment.getEmail());
            commentReturn.setContent(comment.getContent());
            commentReturn.setLikeCount(comment.getLikeCount());
            commentReturn.setLiker(comment.getLiker());
            commentReturn.setFloor(comment.getFloor());
            commentReturn.setDate(comment.getDate());
            commentReturn.setPicURL(comment.getPicURL());
            commentReturn.setBest(comment.getBest());
            UserObj userObj = appUserService.getUserInfo(comment.getEmail());
            commentReturn.setUserObj(userObj);
            ArrayList<UserObj> likerUserObj = new ArrayList<>();
            for (String likerEmail : comment.getLiker()) {
                UserObj userObj1 = appUserService.getUserInfo(likerEmail);
                likerUserObj.add(userObj1);
            }
            commentReturn.setLikerUserObj(likerUserObj);
        }

        return commentReturn;
    }

    public ArrayList<String> tagOtherCommentAuthor(String content, ArrayList<Comment> commentArrayList) {
        if (content.contains("@")) {//有tag人
            ArrayList<String> tagEmails = new ArrayList<>();
            String name = "";
            String email = "";
            int index = content.indexOf("@");//可@很多人
            while (index >= 0) {
                name = "";
                for (int i = index + 1; i < content.length(); i++) {//切出名字
                    if (content.charAt(i) == ' ') break;
                    name = name + content.charAt(i);
                }
                for (Comment comment : commentArrayList) {//判斷是否為commentList內的用戶
                    if (name.equals(comment.getAuthor())) {
                        email = comment.getEmail();
                        if (!tagEmails.contains(email)) {//檢查是否tag過
                            tagEmails.add(email);
                        }
                        break;
                    }
                }
                index = content.indexOf("@", index + 1);
            }
            return tagEmails;
        } else {
            return null;
        }
    }
}
