package ntou.notesharedevbackend.searchTest;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.PostRepository;
import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SearchTest {
    private HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private NoteRepository noteRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void init() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        folderRepository.deleteAll();
        noteRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        AppUser appUser1 = createUser("user1@gmail.com", "User1");
        AppUser appUser2 = createUser("user2@gmail.com", "User2");
        userRepository.insert(appUser);
        userRepository.insert(appUser1);
        userRepository.insert(appUser2);
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

    private AppUser createUser(String email, String name) {
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setActivate(true);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode("1234"));
        Folder buyFolder = createFolder("Buy", "/Buy", null);
        Folder favoriteFolder = createFolder("Favorite", "/Favorite", null);
        Folder collaborationFolder = createFolder("Collaboration", "/Collaboration", null);
        Folder OSFolder = createFolder("OS", "/OS", null);
        ArrayList<String> folderList = new ArrayList<>();
        folderList.add(buyFolder.getId());
        folderList.add(favoriteFolder.getId());
        folderList.add(collaborationFolder.getId());
        folderList.add(OSFolder.getId());
        appUser.setFolders(folderList);
        appUser.setCoin(300);
        return appUser;
    }

    @Test
    public void testSearchUser() throws Exception{
        AppUser user1 = userRepository.findByEmail("user1@gmail.com");
        AppUser user2 = userRepository.findByEmail("user2@gmail.com");
        mockMvc.perform(get("/search/user/user/0/5")
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.search.items.[0].id").value(user2.getId()))
                .andExpect(jsonPath("$.search.items.[0].email").value(user2.getEmail()))
                .andExpect(jsonPath("$.search.items.[0].name").value(user2.getName()))
                .andExpect(jsonPath("$.search.items.[0].verifyCode").value(user2.getVerifyCode()))
                .andExpect(jsonPath("$.search.items.[0].profile").value(user2.getProfile()))
                .andExpect(jsonPath("$.search.items.[0].strength").value(user2.getStrength()))
                .andExpect(jsonPath("$.search.items.[0].folders.[0]").value(user2.getFolders().get(0)))
                .andExpect(jsonPath("$.search.items.[0].folders.[1]").value(user2.getFolders().get(1)))
                .andExpect(jsonPath("$.search.items.[0].folders.[2]").value(user2.getFolders().get(2)))
                .andExpect(jsonPath("$.search.items.[0].subscribe").value(user2.getSubscribe()))
                .andExpect(jsonPath("$.search.items.[0].bell").value(user2.getBell()))
                .andExpect(jsonPath("$.search.items.[0].fans").value(user2.getFans()))
                .andExpect(jsonPath("$.search.items.[0].coin").value(user2.getCoin()))
                .andExpect(jsonPath("$.search.items.[0].headshotPhoto").value(user2.getHeadshotPhoto()))
                .andExpect(jsonPath("$.search.items.[0].notification").value(user2.getNotification()))
                .andExpect(jsonPath("$.search.items.[0].unreadMessageCount").value(user2.getUnreadMessageCount()))
                .andExpect(jsonPath("$.search.items.[0].admin").value(user2.isAdmin()))
                .andExpect(jsonPath("$.search.items.[0].activate").value(user2.isActivate()))
                .andExpect(jsonPath("$.search.items.[1].id").value(user1.getId()))
                .andExpect(jsonPath("$.search.items.[1].email").value(user1.getEmail()))
                .andExpect(jsonPath("$.search.items.[1].name").value(user1.getName()))
                .andExpect(jsonPath("$.search.items.[1].verifyCode").value(user1.getVerifyCode()))
                .andExpect(jsonPath("$.search.items.[1].profile").value(user1.getProfile()))
                .andExpect(jsonPath("$.search.items.[1].strength").value(user1.getStrength()))
                .andExpect(jsonPath("$.search.items.[1].folders.[0]").value(user1.getFolders().get(0)))
                .andExpect(jsonPath("$.search.items.[1].folders.[1]").value(user1.getFolders().get(1)))
                .andExpect(jsonPath("$.search.items.[1].folders.[2]").value(user1.getFolders().get(2)))
                .andExpect(jsonPath("$.search.items.[1].subscribe").value(user1.getSubscribe()))
                .andExpect(jsonPath("$.search.items.[1].bell").value(user1.getBell()))
                .andExpect(jsonPath("$.search.items.[1].fans").value(user1.getFans()))
                .andExpect(jsonPath("$.search.items.[1].coin").value(user1.getCoin()))
                .andExpect(jsonPath("$.search.items.[1].headshotPhoto").value(user1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.search.items.[1].notification").value(user1.getNotification()))
                .andExpect(jsonPath("$.search.items.[1].unreadMessageCount").value(user1.getUnreadMessageCount()))
                .andExpect(jsonPath("$.search.items.[1].admin").value(user1.isAdmin()))
                .andExpect(jsonPath("$.search.items.[1].activate").value(user1.isActivate()))
                .andExpect(jsonPath("$.search.totalPages").value(1));
    }

    @Test
    public void testSearchNote() throws Exception{}

    @Test
    public void testSearchPost() throws Exception{}

    @Test
    public void testSearchFolder() throws Exception{}
    @AfterEach
    public void clear() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        folderRepository.deleteAll();
        noteRepository.deleteAll();
    }
}
