package ntou.notesharedevbackend.basicFunctionModule.service;

import ntou.notesharedevbackend.commentModule.entity.*;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderModule.service.FolderService;
import ntou.notesharedevbackend.noteModule.entity.*;
import ntou.notesharedevbackend.noteModule.service.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.postModule.service.*;
import ntou.notesharedevbackend.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Lazy;
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
    @Autowired
    @Lazy
    private FolderService folderService;

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
        noteService.replaceNote(note, note.getId());
//        noteRepository.save(note);
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

    public void favoriteNote(String noteID, String email) {
        boolean isRemove = false;
        if (noteRepository.existsById(noteID)) {
            // update note's favorite info.
            Note note = noteService.getNote(noteID);
            if (note.getFavoriter() != null) {
                ArrayList<String> noteFavoriter = note.getFavoriter();
                if (noteFavoriter.contains(email)) {
                    noteFavoriter.remove(email);
                    isRemove = true;
                } else {
                    noteFavoriter.add(email);
                }
                noteService.replaceNote(note, note.getId());
            }
            Folder favFolder = folderService.getFavoriteFolderByUserEmail(email);
            ArrayList<String> favNotes = favFolder.getNotes();
            if (isRemove) {
                // remove the note from user's favorite folder.
                favNotes.remove(noteID);
            } else {
                // put the note into user's favorite folder.
                favNotes.add(noteID);
            }
            favFolder.setNotes(favNotes);
            folderService.replaceFolder(favFolder);
        } else {
            throw new NotFoundException("note is not found");
        }
    }

    public void likeNote(String id, String email) {
        Note note = noteService.getNote(id);
        ArrayList<String> liker = note.getLiker();
        liker.add(email);
        note.setLiker(liker);
        note.setLikeCount(liker.size());
        noteRepository.save(note);
    }

    public void unlikeNote(String id, String email) {
        Note note = noteService.getNote(id);
        ArrayList<String> liker = note.getLiker();
        liker.remove(email);
        note.setLiker(liker);
        note.setLikeCount(liker.size());
        noteRepository.save(note);
    }
}
