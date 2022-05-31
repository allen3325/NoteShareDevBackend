package ntou.notesharedevbackend.basicFunctionModule.service;

import ntou.notesharedevbackend.commentModule.entity.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.noteNodule.service.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.postModule.service.*;
import ntou.notesharedevbackend.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class FavoriteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private NoteService noteService;
    @Autowired
    private PostService postService;

    public void favoriteNote(String id) {
        Note note = noteService.getNote(id);
        if (note.getFavorite() != null) {
            note.setFavorite(!note.getFavorite());
            noteRepository.save(note);
        }
    }

    public void favoriteNoteComment(String noteID, String commentID, String email) {
        Note note = noteService.getNote(noteID);
        ArrayList<Comment> comments = note.getComments();
        for (Comment comment : comments) {
            if (comment.getId().equals(commentID)) {
                ArrayList<String> liker = comment.getLiker();
                if (!liker.contains(email)) {
                    liker.add(email);
                    comment.setLiker(liker);
                    comment.setLikeCount(comment.getLikeCount() + 1);
                }
            }
        }
        note.setComments(comments);
        noteRepository.save(note);
    }

    public void favoritePostComment(String postID, String commentID, String email) {
        Post post = postService.getPostById(postID);
        ArrayList<Comment> comments = post.getComments();
        for (Comment comment : comments) {
            if (comment.getId().equals(commentID)) {
                ArrayList<String> liker = comment.getLiker();
                if (!liker.contains(email)) {
                    liker.add(email);
                    comment.setLiker(liker);
                    comment.setLikeCount(comment.getLikeCount() + 1);
                }
            }
        }
        post.setComments(comments);
        postRepository.save(post);
    }

    public void unFavoriteNoteComment(String noteID, String commentID, String email) {
        Note note = noteService.getNote(noteID);
        ArrayList<Comment> comments = note.getComments();
        for (Comment comment : comments) {
            if (comment.getId().equals(commentID)) {
                ArrayList<String> liker = comment.getLiker();
                if (liker.contains(email)) {
                    liker.remove(email);
                    comment.setLiker(liker);
                    comment.setLikeCount(comment.getLikeCount() - 1);
                }
            }
        }
        note.setComments(comments);
        noteRepository.save(note);
    }

    public void unFavoritePostComment(String postID, String commentID, String email) {
        Post post = postService.getPostById(postID);
        ArrayList<Comment> comments = post.getComments();
        for (Comment comment : comments) {
            if (comment.getId().equals(commentID)) {
                ArrayList<String> liker = comment.getLiker();
                if (liker.contains(email)) {
                    liker.remove(email);
                    comment.setLiker(liker);
                    comment.setLikeCount(comment.getLikeCount() - 1);
                }
            }
        }
        post.setComments(comments);
        postRepository.save(post);
    }
}
