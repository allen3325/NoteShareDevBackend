package ntou.notesharedevbackend.commentModule.service;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.service.NoteService;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.service.PostService;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.PostRepository;
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

    public Comment createComment(String id, Comment request) {
        Note note = new Note();
        Post post = new Post();
        String type = "";

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
        comment.setAuthor(request.getAuthor());
        comment.setContent(request.getContent());
        comment.setDate();
        comment.setEmail(request.getEmail());
        comment.setFloor(commentArrayList.size());

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

    public Comment updateComment(String id, Integer floor, Comment request) {
        ArrayList<Comment> commentArrayList = new ArrayList<Comment>();

        if (noteRepository.findById(id).isPresent()) {
            Note note = noteService.getNote(id);
            commentArrayList = note.getComments();
            commentArrayList.get(floor).setBest(request.getBest());
            commentArrayList.get(floor).setLiker(request.getLiker());
            commentArrayList.get(floor).setLikeCount(request.getLiker().size());
            commentArrayList.get(floor).setContent(request.getContent());
            commentArrayList.get(floor).setDate();

            note.setComments(commentArrayList);
            noteService.replaceNote(note, note.getId());

            return commentArrayList.get(floor);
        } else if (postRepository.findById(id).isPresent()) {
            Post post = postService.getPostById(id);
            commentArrayList = post.getComments();
            commentArrayList.get(floor).setBest(request.getBest());
            commentArrayList.get(floor).setLiker(request.getLiker());
            commentArrayList.get(floor).setLikeCount(request.getLiker().size());
            commentArrayList.get(floor).setContent(request.getContent());
            commentArrayList.get(floor).setDate();

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
}
