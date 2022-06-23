package ntou.notesharedevbackend.postTest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.postModule.service.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private UserRepository userRepository;
    @Autowired
    private FolderRepository folderRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void init() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        folderRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    private Folder createFolder(String folderName, String path, String parent){
        Folder folder = new Folder();
        folder.setFolderName(folderName);
        folder.setFavorite(false);
        folder.setParent(parent);
        folder.setPath(path);
        folder.setNotes(new ArrayList<String>());
        folder.setChildren(new ArrayList<String>());
        folder.setPublic(false);
        folderRepository.insert(folder);
        return folder;
    }
    private AppUser createUser(String email, String name){
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setActivate(true);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode("1234"));
        Folder buyFolder = createFolder("Buy","/Buy",null);
        Folder favoriteFolder = createFolder("Favorite","/Favorite",null);
        Folder collaborationFolder = createFolder("Collaboration","/Collaboration",null);
        Folder OSFolder = createFolder("OS","/OS",null);
        ArrayList<String> folderList = new ArrayList<>();
        folderList.add(buyFolder.getId());
        folderList.add(favoriteFolder.getId());
        folderList.add(collaborationFolder.getId());
        folderList.add(OSFolder.getId());
        appUser.setFolders(folderList);
        appUser.setCoin(300);
        return appUser;
    }

    public Post createQAPost(){
        Post post = new Post();
        post.setType("QA");
        post.setPublic(true);
        post.setAuthor("yitingwu.1030@gmail.com");
        post.setTitle("Java Array");
        post.setDepartment("CS");
        post.setSubject("Java");
        post.setSchool("NTOU");
        post.setProfessor("Shang-Pin Ma");
        post.setContent("ArrayList 跟 List 一樣嗎");
        post.setBestPrice(20);
        ArrayList<Comment> comments = new ArrayList<>();
        post.setComments(comments);
        post.setCommentCount(0);
        post = postRepository.insert(post);
        return post;
    }

    public Post createRewardPost(){
        Post post = new Post();
        post.setType("reward");
        post.setAuthor("yitingwu.1030@gmail.com");
        post.setDepartment("CS");
        post.setSubject("Python");
        post.setSchool("NTOU");
        post.setProfessor("Chin-Chun Chang");
        post.setTitle("Python iterator");
        post.setContent("iterator詳細介紹");
        post.setBestPrice(50);
        post.setReferencePrice(10);
        post.setReferenceNumber(2);
        post.setPublic(true);
        ArrayList<String> answers = new ArrayList<>();
        post.setAnswers(answers);
        post = postRepository.insert(post);
        return post;
    }
    public Post createPost() {
        Post post = new Post();
        post.setType("collaboration");
        ArrayList<String> email = new ArrayList<>();
        email.add("yitingwu.1030@gmail.com");
        post.setEmail(email);
        post.setAuthor("Ting");
        post.setDepartment("Computer Science");
        post.setSubject("Operation System");
        post.setTitle("Interrupt vs trap");
        post.setContent("this is a post!");
        post.setComments(new ArrayList<Comment>());
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
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email").value(post.getEmail()))
                .andExpect(jsonPath("$.res.author").value(post.getAuthor()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.res.wantEnterUsersEmail").value(post.getWantEnterUsersEmail()))
                .andExpect(jsonPath("$.res.public").value(post.getPublic()));
    }

    @Test
    public void testGetAllTypesOfPost() throws Exception {
        Post post = createPost();
        postRepository.insert(post);

        mockMvc.perform(get("/post/postType/" + post.getType())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res[0].id").value(post.getId()))
                .andExpect(jsonPath("$.res[0].type").value(post.getType()))
                .andExpect(jsonPath("$.res[0].email").value(post.getEmail()))
                .andExpect(jsonPath("$.res[0].author").value(post.getAuthor()))
                .andExpect(jsonPath("$.res[0].department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res[0].subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res[0].title").value(post.getTitle()))
                .andExpect(jsonPath("$.res[0].content").value(post.getContent()))
                .andExpect(jsonPath("$.res[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res[0].bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res[0].answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.res[0].wantEnterUsersEmail").value(post.getWantEnterUsersEmail()))
                .andExpect(jsonPath("$.res[0].public").value(post.getPublic()));
    }

    // TODO: fix this.
    @Test
    public void testCreatePost() throws Exception {
//        Post post = createPost();
//
//        mockMvc.perform(post("/post")
//                        .headers(httpHeaders)
//                        .content(asJsonString(post)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.type").value(post.getType()))
//                .andExpect(jsonPath("$.email").value(post.getEmail()))
//                .andExpect(jsonPath("$.author").value(post.getAuthor()))
//                .andExpect(jsonPath("$.department").value(post.getDepartment()))
//                .andExpect(jsonPath("$.subject").value(post.getSubject()))
//                .andExpect(jsonPath("$.title").value(post.getTitle()))
//                .andExpect(jsonPath("$.content").value(post.getContent()))
//                .andExpect(jsonPath("$.date").hasJsonPath())
//                .andExpect(jsonPath("$.price").value(post.getPrice()))
//                .andExpect(jsonPath("$.answers").value(post.getAnswers()))
//                .andExpect(jsonPath("$.wantEnterUsersEmail").value(post.getWantEnterUsersEmail()))
//                .andExpect(jsonPath("$.public").value(post.getPublic()));
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
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()));
    }

    @Test
    public void testApplyCollaboration() throws Exception{

    }

    @Test
    public void testApproveCollaboration() throws Exception{

    }

    @Test
    public void testVoteCollaborationVote() throws Exception{

    }

    @Test
    public void testRewardChooseBestAnswer() throws Exception{

    }

    @Test
    public void testRewardChooseReferenceAnswer() throws Exception{

    }

    @Test
    public void testQAChooseBestAnswer() throws Exception{

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
                .andExpect(status().isOk());
    }

    //TODO: 需要先創建用戶再加(BeforeEach)，例如底下的genewang7@gmail.com，不然在post裡面會抓不到名字。
//    @Test
//    public void testApproveCollaboration() throws Exception {
//        Post post = createPost();
//        postRepository.insert(post);
//
//        mockMvc.perform(put("/post/add/{postID}/{email}", post.getId(), "genewang7@gmail.com")
//                        .headers(httpHeaders))
//                .andExpect(status().isOk());
//    }

    @AfterEach
    public void clear(){

    }
}
