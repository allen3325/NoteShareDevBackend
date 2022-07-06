package ntou.notesharedevbackend.coinTest;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.entity.VersionContent;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CoinTest {
    private HttpHeaders httpHeaders;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private FolderRepository folderRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        return appUser;
    }

    private Note createNote() {
        Note note = new Note();
        note.setType("collaboration");
        note.setDepartment("CS");
        note.setSubject("OS");
        note.setTitle("Interrupt");
        note.setHeaderEmail("yitingwu.1030@gmail.com");
        note.setHeaderName("Ting");
        ArrayList<String> authorEmails = new ArrayList<>();
        authorEmails.add("yitingwu.1030@gmail.com");
        authorEmails.add("user1@gmail.com");
        authorEmails.add("user2@gmail.com");
        note.setAuthorEmail(authorEmails);
        ArrayList<String> authorNames = new ArrayList<>();
        authorNames.add("Ting");
        authorNames.add("User1");
        authorNames.add("User2");
        note.setAuthorName(authorNames);
        note.setProfessor("NoteShare");
        note.setSchool("NTOU");
        note.setPublic(true);
        note.setPrice(50);
        note.setLikeCount(0);
        note.setLiker(new ArrayList<String>());
        note.setBuyer(new ArrayList<String>());
        note.setFavoriter(new ArrayList<String>());
        note.setFavoriteCount(0);
        note.setUnlockCount(0);
        note.setDownloadable(false);
        note.setCommentCount(2);
        ArrayList<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setEmail("user1@gmail.com");
        comment.setAuthor("User1");
        comment.setFloor(0);
        comment.setContent("comment Content");
        Comment comment1 = new Comment();
        comment1.setEmail("user2@gmail.com");
        comment1.setAuthor("User2");
        comment1.setFloor(1);
        comment1.setContent("comment Content");
        comments.add(comment);
        comments.add(comment1);
        note.setComments(comments);
        note.setSubmit(null);
        note.setQuotable(false);
        note.setTag(new ArrayList<String>());
        note.setHiddenTag(new ArrayList<String>());
        note.setVersion(new ArrayList<VersionContent>());
        note.setContributors(new ArrayList<String>());
        note.setPostID(null);
        note.setReference(null);
        note.setBest(null);
        note.setManagerEmail(null);
        noteRepository.insert(note);
        return note;
    }

    @BeforeEach
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        userRepository.deleteAll();
        noteRepository.deleteAll();
        folderRepository.deleteAll();
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting", 300);
        userRepository.insert(appUser);
        AppUser appUser1 = createUser("user1@gmail.com", "User1", 300);
        userRepository.insert(appUser1);
        AppUser appUser2 = createUser("user2@gmail.com", "User2", 300);
        userRepository.insert(appUser2);
        Note note = createNote();
        Folder favoriteFolder = folderRepository.findById(userRepository.findByEmail("yitingwu.1030@gmail.com").getFolders().get(1)).get();
        favoriteFolder.getNotes().add(note.getId());
        folderRepository.save(favoriteFolder);
    }

    @Test
    public void testAddCoin() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Integer newCoin = appUser.getCoin() + 30;
        JSONObject request = new JSONObject()
                .put("coin", "+30");
        mockMvc.perform(put("/coin/" + appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.res.id").value(appUser.getId()))
                .andExpect(jsonPath("$.res.email").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.name").value(appUser.getName()))
                .andExpect(jsonPath("$.res.profile").value(appUser.getProfile()))
                .andExpect(jsonPath("$.res.strength").value(appUser.getStrength()))
                .andExpect(jsonPath("$.res.folders").value(appUser.getFolders()))
                .andExpect(jsonPath("$.res.subscribe").value(appUser.getSubscribe()))
                .andExpect(jsonPath("$.res.bell").value(appUser.getBell()))
                .andExpect(jsonPath("$.res.fans").value(appUser.getFans()))
                .andExpect(jsonPath("$.res.coin").value(newCoin))
                .andExpect(jsonPath("$.res.headshotPhoto").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.admin").value(appUser.isAdmin()))
                .andExpect(jsonPath("$.res.activate").value(appUser.isActivate()));

        if (!userRepository.findByEmail(appUser.getEmail()).getCoin().equals(newCoin)) {
            throw new Exception("Coin Test Error");
        }
    }

    @Test
    public void testChangeCoinWithEnoughCoin() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Integer newCoin = appUser.getCoin() - 30;
        JSONObject request = new JSONObject()
                .put("coin", "-30");
        mockMvc.perform(put("/coin/" + appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.res.id").value(appUser.getId()))
                .andExpect(jsonPath("$.res.email").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.name").value(appUser.getName()))
                .andExpect(jsonPath("$.res.profile").value(appUser.getProfile()))
                .andExpect(jsonPath("$.res.strength").value(appUser.getStrength()))
                .andExpect(jsonPath("$.res.folders").value(appUser.getFolders()))
                .andExpect(jsonPath("$.res.subscribe").value(appUser.getSubscribe()))
                .andExpect(jsonPath("$.res.bell").value(appUser.getBell()))
                .andExpect(jsonPath("$.res.fans").value(appUser.getFans()))
                .andExpect(jsonPath("$.res.coin").value(newCoin))
                .andExpect(jsonPath("$.res.headshotPhoto").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.admin").value(appUser.isAdmin()))
                .andExpect(jsonPath("$.res.activate").value(appUser.isActivate()));

        if (!userRepository.findByEmail(appUser.getEmail()).getCoin().equals(newCoin)) {
            throw new Exception("Coin Test Error");
        }
    }

    @Test
    public void testChangeCoinWithNotEnoughCoin() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Integer newCoin = 0;
        JSONObject request = new JSONObject()
                .put("coin", "-30000");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/coin/" + appUser.getEmail())
                .headers(httpHeaders)
                .content(request.toString());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.res").value("money is not enough. so set this user's money to 0."));

        if (!userRepository.findByEmail(appUser.getEmail()).getCoin().equals(newCoin)) {
            throw new Exception("Coin Test Error");
        }
    }

    @Test
    public void testBuyNoteWithEnoughCoin() throws Exception {
        AppUser buyer = createUser("alan@gmail.com", "alan", 300);
        userRepository.insert(buyer);
        AppUser noteAuthor = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser noteAuthor1 = userRepository.findByEmail("user1@gmail.com");
        AppUser noteAuthor2 = userRepository.findByEmail("user2@gmail.com");
        Note note = noteRepository.findAll().get(0);
        AppUser commentAuthor = userRepository.findByEmail(note.getComments().get(0).getEmail());
        AppUser commentAuthor1 = userRepository.findByEmail(note.getComments().get(1).getEmail());
        Integer notePrice = 50;
        Integer buyerNewCoin = 250;
        Integer authorNewCoin = 350;
        mockMvc.perform(put("/coin/note/" + buyer.getEmail() + "/" + note.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(note.getId()))
                .andExpect(jsonPath("$.res.type").value(note.getType()))
                .andExpect(jsonPath("$.res.department").value(note.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(note.getSubject()))
                .andExpect(jsonPath("$.res.title").value(note.getTitle()))
                .andExpect(jsonPath("$.res.professor").value(note.getProfessor()))
                .andExpect(jsonPath("$.res.school").value(note.getSchool()))
                .andExpect(jsonPath("$.res.likeCount").value(note.getLikeCount()))
                .andExpect(jsonPath("$.res.favoriteCount").value(note.getFavoriteCount()))
                .andExpect(jsonPath("$.res.unlockCount").value(note.getUnlockCount() + 1))
                .andExpect(jsonPath("$.res.downloadable").value(note.getDownloadable()))
                .andExpect(jsonPath("$.res.commentCount").value(note.getCommentCount()))
                .andExpect(jsonPath("$.res.comments.[0].id").value(note.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res.comments.[0].likeCount").value(note.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res.comments.[0].floor").value(note.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res.comments.[0].date").value(note.getComments().get(0).getDate()))
                .andExpect(jsonPath("$.res.comments.[0].best").value(note.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res.comments.[0].content").value(note.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res.comments.[0].picURL").value(note.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjEmail").value(commentAuthor.getEmail()))
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjName").value(commentAuthor.getName()))
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjAvatar").value(commentAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[0].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.comments.[1].id").value(note.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res.comments.[1].likeCount").value(note.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res.comments.[1].floor").value(note.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res.comments.[1].date").value(note.getComments().get(1).getDate()))
                .andExpect(jsonPath("$.res.comments.[1].best").value(note.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res.comments.[1].content").value(note.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res.comments.[1].picURL").value(note.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjEmail").value(commentAuthor1.getEmail()))
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjName").value(commentAuthor1.getName()))
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjAvatar").value(commentAuthor1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[1].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.price").value(note.getPrice()))
                .andExpect(jsonPath("$.res.quotable").value(note.getQuotable()))
                .andExpect(jsonPath("$.res.tag").value(note.getTag()))
                .andExpect(jsonPath("$.res.hiddenTag").value(note.getHiddenTag()))
                .andExpect(jsonPath("$.res.version").value(note.getVersion()))
                .andExpect(jsonPath("$.res.postID").value(note.getPostID()))
                .andExpect(jsonPath("$.res.reference").value(note.getReference()))
                .andExpect(jsonPath("$.res.best").value(note.getBest()))
                .andExpect(jsonPath("$.res.public").value(note.getPublic()))
                .andExpect(jsonPath("$.res.submit").value(note.getSubmit()))
                .andExpect(jsonPath("$.res.headerUserObj.userObjEmail").value(noteAuthor.getEmail()))
                .andExpect(jsonPath("$.res.headerUserObj.userObjName").value(noteAuthor.getName()))
                .andExpect(jsonPath("$.res.headerUserObj.userObjAvatar").value(noteAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjEmail").value(noteAuthor.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjName").value(noteAuthor.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjAvatar").value(noteAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.authorUserObj.[1].userObjEmail").value(noteAuthor1.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.[1].userObjName").value(noteAuthor1.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.[1].userObjAvatar").value(noteAuthor1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.authorUserObj.[2].userObjEmail").value(noteAuthor2.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.[2].userObjName").value(noteAuthor2.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.[2].userObjAvatar").value(noteAuthor2.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.buyerUserObj.[0].userObjEmail").value(buyer.getEmail()))
                .andExpect(jsonPath("$.res.buyerUserObj.[0].userObjName").value(buyer.getName()))
                .andExpect(jsonPath("$.res.buyerUserObj.[0].userObjAvatar").value(buyer.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.favoriterUserObj").isEmpty())
                .andExpect(jsonPath("$.res.contributorUserObj").isEmpty())
        ;
        for (String s : note.getAuthorEmail()) {
            if (!userRepository.findByEmail(s).getCoin().equals(authorNewCoin)) {
                throw new Exception("Coin Test : author's coin does not increase");
            }
        }

        if (!userRepository.findByEmail(buyer.getEmail()).getCoin().equals(buyerNewCoin)) {
            throw new Exception("Coin Test : buyer's coin does not decrease");
        }

        if (!folderRepository.findById(userRepository.findByEmail(buyer.getEmail()).getFolders().get(0)).get().getNotes().contains(note.getId())) {
            throw new Exception("Coin Test : buyer does not get note");
        }
    }

    @Test
    public void testBuyNoteWithNotEnoughCoin() throws Exception {
        AppUser buyer = createUser("alan@gmail.com", "alan", 10);
        userRepository.insert(buyer);
        AppUser noteAuthor = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Note note = noteRepository.findAll().get(0);
        Integer buyerNewCoin = 10;
        Integer authorNewCoin = 300;
        mockMvc.perform(put("/coin/note/" + buyer.getEmail() + "/" + note.getId())
                        .headers(httpHeaders))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.msg").value("money is not enough or bought the note."));

        if (!userRepository.findByEmail(noteAuthor.getEmail()).getCoin().equals(authorNewCoin)) {
            throw new Exception("Coin Test : author's coin should not change");
        }
        if (!userRepository.findByEmail(buyer.getEmail()).getCoin().equals(buyerNewCoin)) {
            throw new Exception("Coin Test : buyer's coin should not change");
        }
    }

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
        noteRepository.deleteAll();
        folderRepository.deleteAll();
    }

}
