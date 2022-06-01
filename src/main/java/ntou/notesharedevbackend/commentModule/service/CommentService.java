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



    public ArrayList<Comment> getAllCommentsByID(String id){

        if(noteRepository.findById(id).isPresent()){
            Note note = noteService.getNote(id);
            return note.getComments();
        } else if (postRepository.findById(id).isPresent()) {
            Post post = postService.getPostById(id);
            if(post.getType().equals("QA")) {
                return post.getComments();
            }else{
                throw new NotFoundException("Wrong post type");
            }
        }
        else{
             throw new NotFoundException("Can't find any note or post matched id");
        }
    }

    public Comment getCommentByFloor(String id, int floor){
        ArrayList<Comment> commentArrayList = getAllCommentsByID(id);
        Comment comment = commentArrayList.get(floor);
        if(comment.getFloor()!=floor){//check floor
            for(Comment c:commentArrayList){
                if(c.getFloor()==floor){
                    comment = c;
                }
            }
        }
        return comment;
    }//需要去檢查每個comment中的floor嗎

    public Comment createComment(String id, Comment request){
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ");
//        Date date = calendar.getTime();
        Note note = new Note();
        Post post = new Post();
        String type="";
//        System.out.println(date);

        ArrayList<Comment> commentArrayList= new ArrayList<Comment>();
        if(noteRepository.findById(id).isPresent()){
            type="note";
            note = noteService.getNote(id);
            commentArrayList = note.getComments();
        } else if (postRepository.findById(id).isPresent()) {
            type="post";
            post = postService.getPostById(id);
            commentArrayList = post.getComments();
        }
        else{
            throw new NotFoundException("Can't find any note or post matched id");
        }

        Comment comment = new Comment();
        comment.setAuthor(request.getAuthor());
        comment.setContent(request.getContent());
        comment.setDate(new Date());
        comment.setEmail(request.getEmail());
        comment.setFloor(commentArrayList.size());
        comment.setReferenceNotesURL(request.getReferenceNotesURL());
        commentArrayList.add(comment);
        if(type.equals("note")){
            note.setComments(commentArrayList);
            noteRepository.save(note);
        }else{
            post.setComments(commentArrayList);
            postRepository.save(post);
        }
        return comment;
    }

    public Comment updateComment(String id, Integer floor, Comment request){
        //int index = 0;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
        Date date = calendar.getTime();
        ArrayList<Comment> commentArrayList= new ArrayList<Comment>();
        System.out.println(date);
        if(noteRepository.findById(id).isPresent()){
            Note note = noteService.getNote(id);
            commentArrayList = note.getComments();
            commentArrayList.get(floor).setBest(request.getBest());
            commentArrayList.get(floor).setReference(request.getReference());
            commentArrayList.get(floor).setReferenceNotesURL(request.getReferenceNotesURL());
            commentArrayList.get(floor).setLiker(request.getLiker());
            commentArrayList.get(floor).setLikeCount(request.getLikeCount());
            commentArrayList.get(floor).setContent(request.getContent());
            commentArrayList.get(floor).setDate(date);//要用new date嗎 讀現在時間

//            commentArrayList.get(floor).setAuthor(request.getAuthor());//不應該變
//            commentArrayList.get(floor).setEmail(request.getEmail());//不應該變

//            for(Comment c: commentArrayList){//by using commentID
//                if(c.getId() == commentID){
//                    index = commentArrayList.indexOf(c);
//                    c.setBest(request.getBest());
//                    c.setReferenceNotesURL((request.getReferenceNotesURL()));
//                    c.setReference(request.getReference());
//                    c.setDate(request.getDate());
//                    c.setMycustom_html(request.getMycustom_html());
//                    c.setLikeCount(request.getLikeCount());
//                    c.setLiker(request.getLiker());
////                    c.setEmail(request.getEmail());//不應該變
////                    c.setAuthor(request.getAuthor());//不應該變
//                    commentArrayList.set(index, c);
//                    break;
//                }
//            }
            note.setComments(commentArrayList);
            noteRepository.save(note);
            return commentArrayList.get(floor);
        } else if (postRepository.findById(id).isPresent()) {
            Post post = postService.getPostById(id);
            commentArrayList = post.getComments();
            commentArrayList.get(floor).setBest(request.getBest());
            commentArrayList.get(floor).setReference(request.getReference());
            commentArrayList.get(floor).setReferenceNotesURL(request.getReferenceNotesURL());
            commentArrayList.get(floor).setLiker(request.getLiker());
            commentArrayList.get(floor).setLikeCount(request.getLikeCount());
            commentArrayList.get(floor).setContent(request.getContent());
            commentArrayList.get(floor).setDate(date);//要用new date嗎 讀現在時間
//            commentArrayList.get(floor).setAuthor(request.getAuthor());//不應該變
//            commentArrayList.get(floor).setEmail(request.getEmail());//不應該變

//            for(Comment c: commentArrayList){//by using commentID
//                if(c.getId() == commentID){
//                    index = commentArrayList.indexOf(c);
//                    c.setBest(request.getBest());
//                    c.setReferenceNotesURL((request.getReferenceNotesURL()));
//                    c.setReference(request.getReference());
//                    c.setDate(request.getDate());
//                    c.setMycustom_html(request.getMycustom_html());
//                    c.setLikeCount(request.getLikeCount());
//                    c.setLiker(request.getLiker());
////                    c.setEmail(request.getEmail());//不應該變
////                    c.setAuthor(request.getAuthor());//不應該變
//                    commentArrayList.set(index, c);
//                    break;
//                }
//            }
            post.setComments(commentArrayList);
            postRepository.save(post);
            return commentArrayList.get(floor);
        }else{
            throw new NotFoundException("Can't find any note or post matched id");
        }
    }

    public Boolean deleteComment(String id, Integer floor){
        Note note = new Note();
        Post post = new Post();
        ArrayList<Comment> commentArrayList= new ArrayList<Comment>();
        if(noteRepository.findById(id).isPresent()){
            note = noteService.getNote(id);
            commentArrayList = note.getComments();
            commentArrayList.get(floor).setEmail(null);
            commentArrayList.get(floor).setAuthor(null);
            commentArrayList.get(floor).setDate(null);
            commentArrayList.get(floor).setContent(null);
            commentArrayList.get(floor).setLikeCount(null);
            commentArrayList.get(floor).setLiker(null);
            commentArrayList.get(floor).setReferenceNotesURL(null);
            commentArrayList.get(floor).setReference(null);
            commentArrayList.get(floor).setBest(null);
            note.setComments(commentArrayList);
            noteRepository.save(note);
            return true;
        } else if (postRepository.findById(id).isPresent()) {
            post = postService.getPostById(id);
            commentArrayList = post.getComments();
            commentArrayList.get(floor).setEmail(null);
            commentArrayList.get(floor).setAuthor(null);
            commentArrayList.get(floor).setDate(null);
            commentArrayList.get(floor).setContent(null);
            commentArrayList.get(floor).setLikeCount(null);
            commentArrayList.get(floor).setLiker(null);
            commentArrayList.get(floor).setReferenceNotesURL(null);
            commentArrayList.get(floor).setReference(null);
            commentArrayList.get(floor).setBest(null);
            post.setComments(commentArrayList);
            postRepository.save(post);
            return true;
        }
        else{
            return false;
        }
    }
}
