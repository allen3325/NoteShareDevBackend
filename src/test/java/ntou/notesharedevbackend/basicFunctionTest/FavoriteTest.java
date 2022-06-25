package ntou.notesharedevbackend.basicFunctionTest;

import ntou.notesharedevbackend.commentModule.entity.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.*;
import org.springframework.test.web.servlet.*;

import java.util.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteTest {
    private HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private NoteRepository noteRepository;

    public Note createNote() {
        Note note = new Note();
        note.setType("collaboration");
        note.setHeaderEmail("allen3325940072@gmail.com");
        note.setHeaderName("allen");
        return note;
    }

    public Post createPost() {
        Post post = new Post();
        post.setType("collaboration");
        ArrayList<String> email = new ArrayList<>();
        email.add("allen3325940072@gmail.com");
        post.setEmail(email);
        post.setAuthor("Ting");
        post.setDepartment("Computer Science");
        post.setSubject("Operation System");
        post.setTitle("Interrupt vs trap");
        post.setContent("this is a post!");
        post.setDate();
        post.setBestPrice(null);
        ArrayList<String> answers = new ArrayList<>();
        answers.add("note1's id");
        post.setAnswers(answers);
//        ArrayList<String> wantEnterUsersEmail = new ArrayList<>();
//        wantEnterUsersEmail.add("noteshare1030@gmail.com");
//        post.setWantEnterUsersEmail(wantEnterUsersEmail);
        Apply apply = new Apply("noteshare1030@gmail.com","comment");
        post.setPublic(true);
        return post;
    }

    public Comment createComment(boolean isFavorite) {
        Comment comment = new Comment();
        comment.setAuthor("Gene");
        comment.setEmail("genewang7@gmail.com");
        comment.setContent("讚");
        comment.setBest(false);
//        comment.setReference(false);
        comment.setLikeCount(0);

        ArrayList<String> liker = new ArrayList<>();
        if (!isFavorite)
            liker.add("dna@gmail.com");
        comment.setLiker(liker);
        return comment;
    }

    @BeforeEach
    public void init() {
        postRepository.deleteAll();
        noteRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void testFavoriteNoteComment() throws Exception {
        Note note = createNote();
        Comment comment = createComment(true);
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment);
        note.setComments(comments);
        noteRepository.insert(note);

        mockMvc.perform(put("/favorite/note/{noteID}/{commentID}/{email}", note.getId(), comment.getId(), "dna@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
    }

    @Test
    public void testUnFavoriteNoteComment() throws Exception {
        Note note = createNote();
        Comment comment = createComment(false);
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment);
        note.setComments(comments);
        noteRepository.insert(note);

        mockMvc.perform(put("/unFavorite/note/{noteID}/{commentID}/{email}", note.getId(), comment.getId(), "dna@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
    }

    @Test
    public void testFavoritePostComment() throws Exception {
        Post post = createPost();
        Comment comment = createComment(true);
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment);
        post.setComments(comments);
        postRepository.insert(post);

        mockMvc.perform(put("/favorite/post/{postID}/{commentID}/{email}", post.getId(), comment.getId(), "dna@gmail.com")
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
    }

    @Test
    public void testUnFavoritePostComment() throws Exception {
        Post post = createPost();
        Comment comment = createComment(false);
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment);
        post.setComments(comments);
        postRepository.insert(post);

        mockMvc.perform(put("/unFavorite/post/{postID}/{commentID}/{email}", post.getId(), comment.getId(), "dna@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
    }


}
