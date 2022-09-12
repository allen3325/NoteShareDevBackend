package ntou.notesharedevbackend.followTest;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.noteModule.entity.Content;
import ntou.notesharedevbackend.noteModule.entity.Note;
import ntou.notesharedevbackend.noteModule.entity.VersionContent;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.repository.NoteRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FollowTest {
    private HttpHeaders httpHeaders;

    @Autowired
    private MockMvc mockMvc;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        userRepository.deleteAll();
        noteRepository.deleteAll();
        folderRepository.deleteAll();
        AppUser appUser = createInitUser();
        userRepository.insert(appUser);
    }

    private AppUser createInitUser() {
        AppUser appUser = new AppUser();
        appUser.setEmail("yitingwu.1030@gmail.com");
        // 張哲瑋：因為我把appUser的建構子修改過，以下空陣列為原本建構子裡做的事情
        appUser.setStrength(new ArrayList<String>());
        appUser.setSubscribe(new ArrayList<String>());
        appUser.setBell(new ArrayList<String>());
        appUser.setFans(new ArrayList<String>());
        appUser.setHeadshotPhoto(null);
        appUser.setActivate(true);
        appUser.setName("Ting");
        appUser.setPassword(passwordEncoder.encode("1234"));
        appUser.setVerifyCode("1111");
        appUser.setFolders(new ArrayList<>());
        AppUser fans1 = createUser("alan@gmail.com", "Alan");
        fans1.getFans().add(appUser.getEmail());
        fans1 = userRepository.insert(fans1);
        AppUser fans2 = createUser("eva@gmail.com", "Eva");
        fans2.getFans().add(appUser.getEmail());
        fans2 = userRepository.insert(fans2);
        AppUser fans3 = createUser("tara@gmail.com", "Tara");
        fans3 = userRepository.insert(fans3);
        ArrayList<String> fans = new ArrayList<>();
        fans.add(fans1.getEmail());
        fans.add(fans2.getEmail());
        fans.add(fans3.getEmail());
        appUser.setFans(fans);
        ArrayList<String> subscribers = new ArrayList<>();
        subscribers.add(fans1.getEmail());
        subscribers.add(fans2.getEmail());
        appUser.setSubscribe(subscribers);
        ArrayList<String> bells = new ArrayList<>();
        bells.add(fans1.getEmail());
        bells.add(fans2.getEmail());
        appUser.setBell(bells);
        fans1.getBelledBy().add(appUser.getEmail());
        userRepository.save(fans1);
        fans2.getBelledBy().add(appUser.getEmail());
        userRepository.save(fans2);
        return appUser;
    }

    private AppUser createUser(String email, String name) {
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setActivate(true);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode("1234"));
        appUser.setVerifyCode("1111");
        Folder folder = new Folder();
        folder.setCreatorName(name);
        folder.setFolderName("Folders");
        folder.setChildren(new ArrayList<>());
        folder.setNotes(new ArrayList<>());
        folder.setParent(null);
        folder.setPath("/Folders");
        folder.setFavorite(false);
        folder.setPublic(false);
        folder.getNotes().add(createNormalNote(email, name).getId());
        ArrayList<String> folderArrayList = new ArrayList<>();
        folderArrayList.add(folder.getId());
        folderRepository.insert(folder);
        appUser.setFolders(folderArrayList);
        // 張哲瑋：因為我把appUser的建構子修改過，以下空陣列為原本建構子裡做的事情
        appUser.setStrength(new ArrayList<String>());
        appUser.setSubscribe(new ArrayList<String>());
        appUser.setBell(new ArrayList<String>());
        appUser.setFans(new ArrayList<String>());
        appUser.setHeadshotPhoto(null);
        appUser.setBelledBy(new ArrayList<>());
        appUser.setNotification(new ArrayList<>());
        appUser.setUnreadMessageCount(0);
        return appUser;
    }

    private Note createNormalNote(String email, String name) {
        Note note = new Note();
        note.setType("normal");
        note.setDepartment("CS");
        note.setSubject("OS");
        note.setTitle("Interrupt");
        note.setHeaderEmail(email);
        note.setHeaderName(name);
        ArrayList<String> authorEmails = new ArrayList<>();
        authorEmails.add(email);
        note.setAuthorEmail(authorEmails);
        ArrayList<String> authorNames = new ArrayList<>();
        authorNames.add(name);
        note.setAuthorName(authorNames);
        note.setProfessor("NoteShare");
        note.setSchool("NTOU");
        note.setPublic(true);
        note.setPrice(50);
        note.setLiker(new ArrayList<>());
        note.setLikeCount(null);
        note.setBuyer(new ArrayList<>());
        note.setFavoriter(new ArrayList<>());
        note.setFavoriteCount(null);
        note.setUnlockCount(0);
        note.setDownloadable(false);
        note.setCommentCount(null);
        note.setComments(new ArrayList<>());
        note.setSubmit(null);
        note.setQuotable(false);
        ArrayList<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tags.add("tag3");
        note.setTag(tags);
        note.setHiddenTag(new ArrayList<>());
        ArrayList<VersionContent> versionContents = new ArrayList<>();
        VersionContent v1 = new VersionContent();
        v1.setName("v1");
        v1.setSlug("string");
        ArrayList<String> fileURLs = new ArrayList<>();
        fileURLs.add("fileURL1");
        fileURLs.add("fileURL2");
        fileURLs.add("fileURL3");
        v1.setFileURL(fileURLs);
        ArrayList<String> picURLs = new ArrayList<>();
        picURLs.add("picURL1");
        picURLs.add("picURL2");
        picURLs.add("picURL3");
        v1.setPicURL(picURLs);
        v1.setTemp(true);
        Content content1 = new Content();
        content1.setMycustom_assets("string");
        content1.setMycustom_components("string");
        content1.setMycustom_css("string");
        content1.setMycustom_html("string");
        content1.setMycustom_styles("string");
        Content content2 = new Content();
        content2.setMycustom_assets("string");
        content2.setMycustom_components("string");
        content2.setMycustom_css("string");
        content2.setMycustom_html("string");
        content2.setMycustom_styles("string");
        Content content3 = new Content();
        content3.setMycustom_assets("string");
        content3.setMycustom_components("string");
        content3.setMycustom_css("string");
        content3.setMycustom_html("string");
        content3.setMycustom_styles("string");
        ArrayList<Content> contents = new ArrayList<>();
        contents.add(content1);
        contents.add(content2);
        contents.add(content3);
        v1.setContent(contents);
        VersionContent v2 = new VersionContent();
        v2.setSlug("String");
        v2.setFileURL(fileURLs);
        v2.setPicURL(picURLs);
        v2.setContent(contents);
        v2.setName("v2");
        v2.setTemp(false);
        VersionContent v3 = new VersionContent();
        v3.setSlug("String");
        v3.setFileURL(fileURLs);
        v3.setPicURL(picURLs);
        v3.setContent(contents);
        v3.setTemp(true);
        v3.setName("v3");
        VersionContent v4 = new VersionContent();
        v4.setSlug("String");
        v4.setFileURL(fileURLs);
        v4.setPicURL(picURLs);
        v4.setContent(contents);
        v4.setName("v4");
        v4.setTemp(false);
        versionContents.add(v1);
        versionContents.add(v2);
        versionContents.add(v3);
        versionContents.add(v4);
        note.setVersion(versionContents);
        note.setContributors(new ArrayList<>());
        note.setPostID(null);
        note.setReference(null);
        note.setBest(null);
        note.setManagerEmail(null);
        noteRepository.insert(note);
        return note;
    }

    @Test
    public void testGetAUserFollowersByEmail() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser fans1 = userRepository.findByEmail(appUser.getFans().get(0));
        AppUser fans2 = userRepository.findByEmail(appUser.getFans().get(1));
        AppUser fans3 = userRepository.findByEmail(appUser.getFans().get(2));
        mockMvc.perform(get("/followers/yitingwu.1030@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followers.[0].userObjEmail").value(fans1.getEmail()))
                .andExpect(jsonPath("$.followers.[0].userObjName").value(fans1.getName()))
                .andExpect(jsonPath("$.followers.[0].userObjAvatar").value(fans1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.followers.[1].userObjEmail").value(fans2.getEmail()))
                .andExpect(jsonPath("$.followers.[1].userObjName").value(fans2.getName()))
                .andExpect(jsonPath("$.followers.[1].userObjAvatar").value(fans2.getHeadshotPhoto()))
                .andExpect(jsonPath("$.followers.[2].userObjEmail").value(fans3.getEmail()))
                .andExpect(jsonPath("$.followers.[2].userObjName").value(fans3.getName()))
                .andExpect(jsonPath("$.followers.[2].userObjAvatar").value(fans3.getHeadshotPhoto()));
    }

    @Test
    public void testGetAUserFollowingByEmail() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser following1 = userRepository.findByEmail(appUser.getSubscribe().get(0));
        AppUser following2 = userRepository.findByEmail(appUser.getSubscribe().get(1));
        mockMvc.perform(get("/following/yitingwu.1030@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.following.[0].userObjEmail").value(following1.getEmail()))
                .andExpect(jsonPath("$.following.[0].userObjName").value(following1.getName()))
                .andExpect(jsonPath("$.following.[0].userObjAvatar").value(following1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.following.[1].userObjEmail").value(following2.getEmail()))
                .andExpect(jsonPath("$.following.[1].userObjName").value(following2.getName()))
                .andExpect(jsonPath("$.following.[1].userObjAvatar").value(following2.getHeadshotPhoto()));
    }

    @Test
    public void testFollow() throws Exception {
        AppUser follower = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beFollowed = userRepository.findByEmail("tara@gmail.com");
        mockMvc.perform(put("/follow/" + follower.getEmail() + "/" + beFollowed.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        //檢查Follower's subscribe
        if (!userRepository.findByEmail(follower.getEmail()).getSubscribe().contains(beFollowed.getEmail())) {
            throw new Exception();
        }
        //檢查BeFollowed's fans
        if (!userRepository.findByEmail(beFollowed.getEmail()).getFans().contains(follower.getEmail())) {
            throw new Exception();
        }
    }

    @Test
    public void testUnFollow() throws Exception {
        AppUser wantUnFollow = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beUnFollowed = userRepository.findByEmail("alan@gmail.com");
        mockMvc.perform(put("/unfollow/" + wantUnFollow.getEmail() + "/" + beUnFollowed.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        if (userRepository.findByEmail(wantUnFollow.getEmail()).getSubscribe().contains(beUnFollowed.getEmail())) {
            throw new Exception("Follow Test : wantUnFollow's subscribe doesn't update");
        }
        if (userRepository.findByEmail(beUnFollowed.getEmail()).getFans().contains(wantUnFollow.getEmail())) {
            throw new Exception("Follow Test : beUnFollowed's fans doesn't update");
        }
    }

    @Test
    public void testBell() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beBell = userRepository.findByEmail("tara@gmail.com");
        mockMvc.perform(put("/bell/" + appUser.getEmail() + "/" + beBell.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (!userRepository.findByEmail(appUser.getEmail()).getBell().contains(beBell.getEmail())) {
            throw new Exception("Bell Test : user's bell does not update");
        }
        if (!userRepository.findByEmail(beBell.getEmail()).getBelledBy().contains(appUser.getEmail())) {
            throw new Exception("Bell Test : beBell's BellBy does not update");
        }
    }

    @Test
    public void testCancelBell() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beCancelBell = userRepository.findByEmail("tara@gmail.com");
        appUser.getBell().add(beCancelBell.getEmail());
        beCancelBell.getBelledBy().add(appUser.getEmail());
        userRepository.save(beCancelBell);
        userRepository.save(appUser);
        mockMvc.perform(put("/cancelBell/" + appUser.getEmail() + "/" + beCancelBell.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (userRepository.findByEmail(appUser.getEmail()).getBell().contains(beCancelBell.getEmail())) {
            throw new Exception("Bell Test : user's bell does not update");
        }
        if (userRepository.findByEmail(beCancelBell.getEmail()).getBelledBy().contains(appUser.getEmail())) {
            throw new Exception("Bell Test : beCancelBell's beBell does not update");
        }
    }

    @Test
    public void testGetUserBellByEmail() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser bell1 = userRepository.findByEmail(appUser.getBell().get(0));
        AppUser bell2 = userRepository.findByEmail(appUser.getBell().get(1));
        mockMvc.perform(get("/bell/yitingwu.1030@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.[0].userObjEmail").value(bell1.getEmail()))
                .andExpect(jsonPath("$.res.[0].userObjName").value(bell1.getName()))
                .andExpect(jsonPath("$.res.[0].userObjAvatar").value(bell1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[1].userObjEmail").value(bell2.getEmail()))
                .andExpect(jsonPath("$.res.[1].userObjName").value(bell2.getName()))
                .andExpect(jsonPath("$.res.[1].userObjAvatar").value(bell2.getHeadshotPhoto()));
    }

    @Test
    public void testGetUserBellByByEmail() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser bell1 = userRepository.findByEmail(appUser.getBell().get(0));
        mockMvc.perform(get("/bellBy/" + bell1.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.[0].userObjAvatar").value(appUser.getHeadshotPhoto()));
    }

    @Test
    public void testGetFollowingNotes() throws Exception {
        AppUser following1 = userRepository.findByEmail("alan@gmail.com");
        AppUser following2 = userRepository.findByEmail("eva@gmail.com");
        Folder folderOfF1 = folderRepository.findById(following1.getFolders().get(0)).get();
        Note noteOfF1 = noteRepository.findById(folderOfF1.getNotes().get(0)).get();
        Folder folderOfF2 = folderRepository.findById(following2.getFolders().get(0)).get();
        Note noteOfF2 = noteRepository.findById(folderOfF2.getNotes().get(0)).get();
        String email = "yitingwu.1030@gmail.com";
        int offset = 0;
        int pageSize = 10;
        mockMvc.perform(get("/note/following/" + email + "/" + offset + "/" + pageSize)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.items.[0].id").value(noteOfF1.getId()))
                .andExpect(jsonPath("$.res.items.[0].headerEmail").value(following1.getEmail()))
                .andExpect(jsonPath("$.res.items.[1].id").value(noteOfF2.getId()))
                .andExpect(jsonPath("$.res.items.[1].headerEmail").value(following2.getEmail()))
        ;

    }

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
        noteRepository.deleteAll();
        folderRepository.deleteAll();
    }
}
