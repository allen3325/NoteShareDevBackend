package ntou.notesharedevbackend.basicFunctionTest;

import ntou.notesharedevbackend.commentModule.entity.*;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.noteModule.entity.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private FolderRepository folderRepository;

    public Note createNote() {
        Note note = new Note();
        note.setType("collaboration");
        note.setHeaderEmail("allen3325940072@gmail.com");
        note.setHeaderName("allen");
        note.setLiker(new ArrayList<String>());
        note.setBuyer(new ArrayList<String>());
        note.setFavoriter(new ArrayList<String>());
        note.setFavoriteCount(0);
        note.setComments(new ArrayList<Comment>());
        note.setTag(new ArrayList<String>());
        note.setHiddenTag(new ArrayList<String>());
        note.setVersion(new ArrayList<VersionContent>());
        note.setContributors(new ArrayList<String>());
        note.setClickDate(new ArrayList<>());
        note.setClickNum(0);
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
        Apply apply = new Apply("noteshare1030@gmail.com", "comment");
        post.setPublic(true);
        return post;
    }

    private Folder createFolder(String folderName, String path, String parent) {
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

    private AppUser createUser(String email, String name, Integer coin) {
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setActivate(true);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode("1234"));
        Folder buyFolder = createFolder("Buy", "/Buy", null);
        Folder favoriteFolder = createFolder("Favorite", "/Favorite", null);
        ArrayList<String> folderList = new ArrayList<>();
        folderList.add(buyFolder.getId());
        folderList.add(favoriteFolder.getId());
        appUser.setFolders(folderList);
        appUser.setCoin(coin);
        appUser.setNotification(new ArrayList<>());
        appUser.setSubscribe(new ArrayList<>());
        appUser.setBell(new ArrayList<>());
        appUser.setBelledBy(new ArrayList<>());
        appUser.setFans(new ArrayList<>());
        appUser.setHeadshotPhoto("headshotPhoto");
        appUser.setStrength(new ArrayList<>());
        appUser.setProfile("profile");
        appUser.setUnreadMessageCount(0);
        return appUser;
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
        comment.setLiker(liker);
        return comment;
    }

    @BeforeEach
    public void init() {
        postRepository.deleteAll();
        noteRepository.deleteAll();
        userRepository.deleteAll();
        folderRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @Test
    public void testFavoriteNote() throws Exception {
        Note note = createNote();
        noteRepository.insert(note);
        AppUser favoriter = createUser("yitingwu.1030@gmail.com", "Ting", 300);
        userRepository.insert(favoriter);
        // favorite
        mockMvc.perform(put("/favorite/note/{noteID}/{email}", note.getId(), favoriter.getEmail()).headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("success"));

        if (!noteRepository.findById(note.getId()).get().getFavoriter().contains(favoriter.getEmail())) {
            throw new Exception("Favorite Test : note's favoriter does not update.");
        }
        if (!noteRepository.findById(note.getId()).get().getFavoriteCount().equals(note.getFavoriteCount() + 1)) {
            throw new Exception("Favorite Test : note's favorite count does not update.");
        }
        if (!folderRepository.findById(userRepository.findByEmail(favoriter.getEmail()).getFolders().get(1)).get().getNotes().contains(note.getId())) {
            throw new Exception("Favorite Test : favoriter's favorite folder does not update.");
        }
    }

    @Test
    public void testUnFavoriteNote() throws Exception {
        Note note = createNote();
        note.getFavoriter().add("yitingwu.1030@gmail.com");
        note.setFavoriteCount(1);
        noteRepository.insert(note);
        AppUser favoriter = createUser("yitingwu.1030@gmail.com", "Ting", 300);
        userRepository.insert(favoriter);

        // unFavorite
        mockMvc.perform(put("/favorite/note/{noteID}/{email}", note.getId(), favoriter.getEmail()).headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("success"));
        if (noteRepository.findById(note.getId()).get().getFavoriter().contains(favoriter.getEmail())) {
            throw new Exception("Favorite Test (unFavorite) : note's favoriter does not update.");
        }
        if (noteRepository.findById(note.getId()).get().getFavoriteCount().equals(note.getFavoriteCount() + 1)) {
            throw new Exception("Favorite Test (unFavorite) : note's favorite count does not update.");
        }
        if (userRepository.findByEmail(favoriter.getEmail()).getFolders().get(1).contains(note.getId())) {
            throw new Exception("Favorite Test (unFavorite) : favoriter's favorite folder does not update.");
        }
    }

    @Test
    public void testFavoriteNoteComment() throws Exception {
        Note note = createNote();
        Comment comment = createComment(false);
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment);
        note.setComments(comments);
        noteRepository.insert(note);

        mockMvc.perform(put("/favorite/note/{noteID}/{commentID}/{email}", note.getId(), comment.getId(), "dna@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (!noteRepository.findById(note.getId()).get().getComments().get(0).getLiker().contains("dna@gmail.com")) {
            throw new Exception("Favorite Test : comment's liker does not update.");
        }
        if (!noteRepository.findById(note.getId()).get().getComments().get(0).getLikeCount().equals(comment.getLikeCount() + 1)) {
            throw new Exception("Favorite Test : comment's liker count does not update.");
        }
    }

    @Test
    public void testUnFavoriteNoteComment() throws Exception {
        Note note = createNote();
        Comment comment = createComment(true);
        ArrayList<Comment> comments = new ArrayList<>();
        comment.getLiker().add("dna@gmail.com");
        comment.setLikeCount(comment.getLiker().size());
        comments.add(comment);
        note.setComments(comments);
        noteRepository.insert(note);

        mockMvc.perform(put("/unFavorite/note/{noteID}/{commentID}/{email}", note.getId(), comment.getId(), "dna@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        if (noteRepository.findById(note.getId()).get().getComments().get(0).getLiker().contains("dna@gmail.com")) {
            throw new Exception("Favorite Test : comment's liker does not update.");
        }
        if (!noteRepository.findById(note.getId()).get().getComments().get(0).getLikeCount().equals(comment.getLikeCount() - 1)) {
            throw new Exception("Favorite Test : comment's liker count does not update.");
        }

    }

    @Test
    public void testFavoritePostComment() throws Exception {
        Post post = createPost();
        Comment comment = createComment(false);
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment);
        post.setComments(comments);
        postRepository.insert(post);

        mockMvc.perform(put("/favorite/post/{postID}/{commentID}/{email}", post.getId(), comment.getId(), "dna@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        Comment responseComment = postRepository.findById(post.getId()).get().getComments().get(0);
        if (!responseComment.getLiker().contains("dna@gmail.com")) {
            throw new Exception("Favorite Test : comment's liker does not update");
        }
        if (!responseComment.getLikeCount().equals(responseComment.getLiker().size())) {
            System.out.println(responseComment.getLiker().size());
            System.out.println(responseComment.getLikeCount());
            throw new Exception("Favorite Test : comment's like count does not update");
        }
    }

    @Test
    public void testUnFavoritePostComment() throws Exception {
        Post post = createPost();
        Comment comment = createComment(true);
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment);
        post.setComments(comments);
        postRepository.insert(post);

        mockMvc.perform(put("/unFavorite/post/{postID}/{commentID}/{email}", post.getId(), comment.getId(), "dna@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        if (postRepository.findById(post.getId()).get().getComments().get(0).getLiker().contains("dna@gmail.com")) {
            throw new Exception("Favorite Test : comment's liker does not update");
        }
        if (postRepository.findById(post.getId()).get().getComments().get(0).getLikeCount().equals(comment.getLikeCount() + 1)) {
            throw new Exception("Favorite Test : comment's like count does not update");
        }
    }


}
