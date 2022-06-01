package ntou.notesharedevbackend.postTest;

import com.fasterxml.jackson.databind.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.postModule.service.*;
import ntou.notesharedevbackend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PostTest {
    private HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;

    @BeforeEach
    public void init() {
        postRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
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
        post.setDate(new Date());
        post.setPrice(null);
        post.setComments(null);
        ArrayList<String> answers = new ArrayList<>();
        answers.add("note1's id");
        post.setAnswers(answers);
        ArrayList<String> wantEnterUsersEmail = new ArrayList<>();
        wantEnterUsersEmail.add("noteshare1030@gmail.com");
        post.setWantEnterUsersEmail(wantEnterUsersEmail);
        post.setPublic(true);
        return post;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetPost() throws Exception {
        Post post = createPost();
        postRepository.insert(post);

        mockMvc.perform(get("/post/" + post.getId())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.type").value(post.getType()))
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.author").value(post.getAuthor()))
                .andExpect(jsonPath("$.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andExpect(jsonPath("$.date").hasJsonPath())
                .andExpect(jsonPath("$.price").value(post.getPrice()))
                .andExpect(jsonPath("$.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.wantEnterUsersEmail").value(post.getWantEnterUsersEmail()))
                .andExpect(jsonPath("$.public").value(post.getPublic()));
    }

    @Test
    public void testGetAllTypesOfPost() throws Exception {
        Post post = createPost();
        postRepository.insert(post);

        mockMvc.perform(get("/post/postType/" + post.getType())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(post.getId()))
                .andExpect(jsonPath("$[0].type").value(post.getType()))
                .andExpect(jsonPath("$[0].email").value(post.getEmail()))
                .andExpect(jsonPath("$[0].author").value(post.getAuthor()))
                .andExpect(jsonPath("$[0].department").value(post.getDepartment()))
                .andExpect(jsonPath("$[0].subject").value(post.getSubject()))
                .andExpect(jsonPath("$[0].title").value(post.getTitle()))
                .andExpect(jsonPath("$[0].content").value(post.getContent()))
                .andExpect(jsonPath("$[0].date").hasJsonPath())
                .andExpect(jsonPath("$[0].price").value(post.getPrice()))
                .andExpect(jsonPath("$[0].answers").value(post.getAnswers()))
                .andExpect(jsonPath("$[0].wantEnterUsersEmail").value(post.getWantEnterUsersEmail()))
                .andExpect(jsonPath("$[0].public").value(post.getPublic()));
    }

    @Test
    public void testCreatePost() throws Exception {
        Post post = createPost();

        mockMvc.perform(post("/post")
                        .headers(httpHeaders)
                        .content(asJsonString(post)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value(post.getType()))
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.author").value(post.getAuthor()))
                .andExpect(jsonPath("$.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andExpect(jsonPath("$.date").hasJsonPath())
                .andExpect(jsonPath("$.price").value(post.getPrice()))
                .andExpect(jsonPath("$.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.wantEnterUsersEmail").value(post.getWantEnterUsersEmail()))
                .andExpect(jsonPath("$.public").value(post.getPublic()));
    }

    @Test
    public void testPutPost() throws Exception {
        Post post = createPost();
        postRepository.insert(post);

        post.setAuthor("Gene");
        post.setTitle("TITLE");

        mockMvc.perform(put("/post/" + post.getId())
                        .headers(httpHeaders)
                        .content(asJsonString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()));
    }

    @Test
    public void testDeletePost() throws Exception {
        Post post = createPost();
        postRepository.insert(post);

        mockMvc.perform(delete("/post/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testModifyPublishStatus() throws Exception {
        Post post = createPost();
        postRepository.insert(post);

        mockMvc.perform(put("/post/publish/" + post.getId())
                .headers(httpHeaders))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testApplyCollaboration() throws Exception {
        Post post = createPost();
        postRepository.insert(post);

        mockMvc.perform(put("/post/{postID}/{email}", post.getId(), "genewang7@gmail.com")
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    public void testApproveCollaboration() throws Exception {
        Post post = createPost();
        postRepository.insert(post);

        mockMvc.perform(put("/post/add/{postID}/{email}", post.getId(), "genewang7@gmail.com")
                .headers(httpHeaders))
                .andExpect(status().isOk());
    }
}
