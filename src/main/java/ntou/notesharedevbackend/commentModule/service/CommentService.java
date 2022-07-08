package ntou.notesharedevbackend.commentModule.service;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.commentModule.entity.CommentRequest;
import ntou.notesharedevbackend.commentModule.entity.CommentReturn;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.PostRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.entity.UserObj;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

        ArrayList<Comment> commentArrayList;
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

        commentArrayList.add(comment);
        if (type.equals("note")) {
            note.setComments(commentArrayList);
            noteService.replaceNote(note, note.getId());
        } else {
            post.setComments(commentArrayList);
            postService.replacePost(post.getId(), post);
        }
        return comment;
    }

    public Comment updateComment(String id, Integer floor, CommentRequest request) {
        ArrayList<Comment> commentArrayList = new ArrayList<Comment>();

        if (noteRepository.findById(id).isPresent()) {
            Note note = noteService.getNote(id);
            commentArrayList = note.getComments();
//            commentArrayList.get(floor).setBest(request.getBest());
//            commentArrayList.get(floor).setLiker(request.getLiker());
//            commentArrayList.get(floor).setLikeCount(request.getLiker().size());
            commentArrayList.get(floor).setContent(request.getContent());
            commentArrayList.get(floor).setDate();
            commentArrayList.get(floor).setPicURL(request.getPicURL());
            note.setComments(commentArrayList);
            noteService.replaceNote(note, note.getId());

            return commentArrayList.get(floor);
        } else if (postRepository.findById(id).isPresent()) {
            Post post = postService.getPostById(id);
            commentArrayList = post.getComments();
//            commentArrayList.get(floor).setBest(request.getBest());
//            commentArrayList.get(floor).setLiker(request.getLiker());
//            commentArrayList.get(floor).setLikeCount(request.getLiker().size());
            commentArrayList.get(floor).setContent(request.getContent());
            commentArrayList.get(floor).setDate();
            commentArrayList.get(floor).setPicURL(request.getPicURL());
            post.setComments(commentArrayList);
            postService.replacePost(post.getId(), post);

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
}
