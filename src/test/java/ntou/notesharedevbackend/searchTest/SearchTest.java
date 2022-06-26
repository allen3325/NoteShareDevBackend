package ntou.notesharedevbackend.searchTest;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.noteNodule.entity.Content;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.entity.VersionContent;
import ntou.notesharedevbackend.postModule.entity.Apply;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.PostRepository;
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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        folder.setPublic(true);
        return folderRepository.insert(folder);
    }

    private AppUser createUser(String email, String name) {
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setActivate(true);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode("1234"));
        Folder buyFolder = createFolder("Buy", "/Buy", null);
        Folder favoriteFolder = createFolder("Favorite", "/Favorite", null);
        Folder OSFolder = createFolder("OS", "/OS", null);
        ArrayList<String> folderList = new ArrayList<>();
        folderList.add(buyFolder.getId());
        folderList.add(favoriteFolder.getId());
        folderList.add(OSFolder.getId());
        appUser.setFolders(folderList);
        appUser.setCoin(300);
        return appUser;
    }

    private Note createNormalNote(){
        Note note = new Note();
        note.setType("normal");
        note.setDepartment("CS");
        note.setSubject("OS");
        note.setTitle("Interrupt");
        note.setHeaderEmail("yitingwu.1030@gmail.com");
        note.setHeaderName("Ting");
        ArrayList<String> authorEmails = new ArrayList<>();
        authorEmails.add("yitingwu.1030@gmail.com");
        note.setAuthorEmail(authorEmails);
        ArrayList<String> authorNames = new ArrayList<>();
        authorNames.add("Ting");
        note.setAuthorName(authorNames);
        note.setProfessor("NoteShare");
        note.setSchool("NTOU");
        note.setPublic(true);
        note.setPrice(50);
        note.setLiker(new ArrayList<>());
        note.setLikeCount(null);
        note.setBuyer(new ArrayList<>());
        note.setFavoriter(new ArrayList<>());
        note.setFavoriteCount(0);
        note.setUnlockCount(3);
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

    public Post createQAPost() {
        Post post = new Post();
        post.setType("QA");
        post.setPublic(true);
        post.setAuthor("yitingwu.1030@gmail.com");
        post.setAuthorName("Ting");
        ArrayList<String> email = new ArrayList<>();
        email.add("yitingwu.1030@gmail.com");
        post.setEmail(email);
        post.setTitle("Interrupt vs trap");
        post.setDepartment("CS");
        post.setSubject("Operation System");
        post.setSchool("NTOU");
        post.setProfessor("Shang-Pin Ma");
        post.setContent("ArrayList 跟 List 一樣嗎");
        post.setBestPrice(20);
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
        post.setComments(comments);
        post.setCommentCount(2);
        post.setBestPrice(30);
        post.setAnswers(new ArrayList<>());
        post = postRepository.insert(post);
        return post;
    }

    private Note createRewardNote(String email, String name) {
        Note note = new Note();
        note.setType("reward");
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
        note.setFavoriteCount(0);
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

    public Post createRewardPost() {
        Post post = new Post();
        post.setType("reward");
        post.setAuthor("yitingwu.1030@gmail.com");
        post.setAuthorName("Ting");
        ArrayList<String> email = new ArrayList<String>();
        email.add("yitingwu.1030@gmail.com");
        post.setEmail(email);
        post.setDepartment("CS");
        post.setSubject("Operation System");
        post.setSchool("NTOU");
        post.setProfessor("Chin-Chun Chang");
        post.setTitle("Interrupt vs trap");
        post.setContent("iterator詳細介紹");
        post.setBestPrice(20);
        post.setReferencePrice(10);
        post.setReferenceNumber(2);
        post.setPublic(true);
        post.setComments(new ArrayList<Comment>());
        ArrayList<String> answers = new ArrayList<>();
        Note note = createRewardNote("user1@gmail.com", "User1");
        Note note1 = createRewardNote("user2@gmail.com", "User2");
        answers.add(note.getId());
        answers.add(note1.getId());
        post.setAnswers(answers);
        post = postRepository.insert(post);
        return post;
    }

    private Note createCollaborationNote() {
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
        note.setLiker(new ArrayList<>());
        note.setLikeCount(null);
        note.setBuyer(new ArrayList<>());
        note.setFavoriter(new ArrayList<>());
        note.setFavoriteCount(0);
        note.setUnlockCount(2);
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
        note = noteRepository.insert(note);
        return note;
    }

    public Post createCollaborationPost() {
        Post post = new Post();
        post.setType("collaboration");
        ArrayList<String> email = new ArrayList<>();
        email.add("yitingwu.1030@gmail.com");
        post.setEmail(email);
        post.setAuthor("yitingwu.1030@gmail.com");
        post.setAuthorName("Ting");
        post.setDepartment("CS");
        post.setSubject("Operation System");
        post.setSchool("NTOU");
        post.setProfessor("professor");
        post.setTitle("Interrupt vs trap");
        post.setContent("this is a post!");
        post.setCollabNoteAuthorNumber(post.getEmail().size());
        ArrayList<String> answers = new ArrayList<>();
        Note note = createCollaborationNote();
        answers.add(note.getId());
        post.setAnswers(answers);
        post.setCollabApply(new ArrayList<Apply>());
        post.setPublic(true);
        post.setCollabNoteAuthorNumber(1);
        post.setComments(new ArrayList<Comment>());
        post.setCommentCount(0);
        post.setBestPrice(0);
        post = postRepository.insert(post);
        return post;
    }

    @Test
    public void testSearchUser() throws Exception {
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
    public void testSearchNote() throws Exception {
        Post rewardPost = createRewardPost();
        Note rewardNote1 = noteRepository.findById(rewardPost.getAnswers().get(0)).get();
        Note rewardNote2 = noteRepository.findById(rewardPost.getAnswers().get(1)).get();
        rewardNote2.setQuotable(true);
        noteRepository.save(rewardNote2);
        Post collaborationPost = createCollaborationPost();
        Note collaborationNote = noteRepository.findById(collaborationPost.getAnswers().get(0)).get();
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(2)).get();
        Note normalNote = createNormalNote();
        folder.getNotes().add(normalNote.getId());

        String keyword = "Interrupt";
        int offset = 0;
        int pageSize = 10;

        mockMvc.perform(get("/search/note/"+keyword+"/"+offset+"/"+pageSize)
                .headers(httpHeaders)
                .param("school","NTOU")
                .param("subject","OS")
                .param("department","CS")
                .param("professor","NoteShare")
                .param("haveNormal","true")
                .param("haveCollaboration","true")
                .param("haveReward","true")
                .param("favoriteCount","0")
                .param("price","50")
                .param("quotable","false")
                .param("sortBy","unlockCount")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.search.items.[0].id").value(normalNote.getId()))
                .andExpect(jsonPath("$.search.items.[0].type").value(normalNote.getType()))
                .andExpect(jsonPath("$.search.items.[0].department").value(normalNote.getDepartment()))
                .andExpect(jsonPath("$.search.items.[0].subject").value(normalNote.getSubject()))
                .andExpect(jsonPath("$.search.items.[0].title").value(normalNote.getTitle()))
                .andExpect(jsonPath("$.search.items.[0].email").value(normalNote.getHeaderEmail()))
                .andExpect(jsonPath("$.search.items.[0].author.[0]").value(normalNote.getAuthorName().get(0)))
                .andExpect(jsonPath("$.search.items.[0].professor").value(normalNote.getProfessor()))
                .andExpect(jsonPath("$.search.items.[0].school").value(normalNote.getSchool()))
                .andExpect(jsonPath("$.search.items.[0].likeCount").value(normalNote.getLikeCount()))
                .andExpect(jsonPath("$.search.items.[0].favoriteCount").value(normalNote.getFavoriteCount()))
                .andExpect(jsonPath("$.search.items.[0].unlockCount").value(normalNote.getUnlockCount()))
                .andExpect(jsonPath("$.search.items.[0].downloadable").value(normalNote.getDownloadable()))
                .andExpect(jsonPath("$.search.items.[0].commentCount").value(normalNote.getCommentCount()))
                .andExpect(jsonPath("$.search.items.[0].price").value(normalNote.getPrice()))
                //TODO: 需要顯示嗎
//                .andExpect(jsonPath("$.search.items.[0].quotable").value(normalNote.getQuotable()))
                .andExpect(jsonPath("$.search.items.[0].tag.[0]").value(normalNote.getTag().get(0)))
                .andExpect(jsonPath("$.search.items.[0].tag.[1]").value(normalNote.getTag().get(1)))
                .andExpect(jsonPath("$.search.items.[0].tag.[2]").value(normalNote.getTag().get(2)))
                .andExpect(jsonPath("$.search.items.[1].id").value(collaborationNote.getId()))
                .andExpect(jsonPath("$.search.items.[1].type").value(collaborationNote.getType()))
                .andExpect(jsonPath("$.search.items.[1].department").value(collaborationNote.getDepartment()))
                .andExpect(jsonPath("$.search.items.[1].subject").value(collaborationNote.getSubject()))
                .andExpect(jsonPath("$.search.items.[1].title").value(collaborationNote.getTitle()))
                .andExpect(jsonPath("$.search.items.[1].email").value(collaborationNote.getHeaderEmail()))
                .andExpect(jsonPath("$.search.items.[1].author.[0]").value(collaborationNote.getAuthorName().get(0)))
                .andExpect(jsonPath("$.search.items.[1].author.[1]").value(collaborationNote.getAuthorName().get(1)))
                .andExpect(jsonPath("$.search.items.[1].author.[2]").value(collaborationNote.getAuthorName().get(2)))
                .andExpect(jsonPath("$.search.items.[1].professor").value(collaborationNote.getProfessor()))
                .andExpect(jsonPath("$.search.items.[1].school").value(collaborationNote.getSchool()))
                .andExpect(jsonPath("$.search.items.[1].likeCount").value(collaborationNote.getLikeCount()))
                .andExpect(jsonPath("$.search.items.[1].favoriteCount").value(collaborationNote.getFavoriteCount()))
                .andExpect(jsonPath("$.search.items.[1].unlockCount").value(collaborationNote.getUnlockCount()))
                .andExpect(jsonPath("$.search.items.[1].downloadable").value(collaborationNote.getDownloadable()))
                .andExpect(jsonPath("$.search.items.[1].commentCount").value(collaborationNote.getCommentCount()))
                .andExpect(jsonPath("$.search.items.[1].price").value(collaborationNote.getPrice()))
                //TODO: 需要顯示嗎
//                .andExpect(jsonPath("$.search.items.[0].quotable").value(normalNote.getQuotable()))
                .andExpect(jsonPath("$.search.items.[1].tag.[0]").value(collaborationNote.getTag().get(0)))
                .andExpect(jsonPath("$.search.items.[1].tag.[1]").value(collaborationNote.getTag().get(1)))
                .andExpect(jsonPath("$.search.items.[1].tag.[2]").value(collaborationNote.getTag().get(2)))
                .andExpect(jsonPath("$.search.items.[2].id").value(rewardNote1.getId()))
                .andExpect(jsonPath("$.search.items.[2].type").value(rewardNote1.getType()))
                .andExpect(jsonPath("$.search.items.[2].department").value(rewardNote1.getDepartment()))
                .andExpect(jsonPath("$.search.items.[2].subject").value(rewardNote1.getSubject()))
                .andExpect(jsonPath("$.search.items.[2].title").value(rewardNote1.getTitle()))
                .andExpect(jsonPath("$.search.items.[2].email").value(rewardNote1.getHeaderEmail()))
                .andExpect(jsonPath("$.search.items.[2].professor").value(rewardNote1.getProfessor()))
                .andExpect(jsonPath("$.search.items.[2].school").value(rewardNote1.getSchool()))
                .andExpect(jsonPath("$.search.items.[2].likeCount").value(rewardNote1.getLikeCount()))
                .andExpect(jsonPath("$.search.items.[2].favoriteCount").value(rewardNote1.getFavoriteCount()))
                .andExpect(jsonPath("$.search.items.[2].unlockCount").value(rewardNote1.getUnlockCount()))
                .andExpect(jsonPath("$.search.items.[2].downloadable").value(rewardNote1.getDownloadable()))
                .andExpect(jsonPath("$.search.items.[2].commentCount").value(rewardNote1.getCommentCount()))
                .andExpect(jsonPath("$.search.items.[2].price").value(rewardNote1.getPrice()))
                //TODO: 需要顯示嗎
//                .andExpect(jsonPath("$.search.items.[0].quotable").value(normalNote.getQuotable()))
                .andExpect(jsonPath("$.search.items.[2].tag.[0]").value(rewardNote1.getTag().get(0)))
                .andExpect(jsonPath("$.search.items.[2].tag.[1]").value(rewardNote1.getTag().get(1)))
                .andExpect(jsonPath("$.search.items.[2].tag.[2]").value(rewardNote1.getTag().get(2)));

    }

    @Test
    public void testSearchPost() throws Exception {
        Post QAPost = createQAPost();
        Post rewardPost = createRewardPost();
        Post collaborationPost = createCollaborationPost();

        String keyword = "trap";
        int offset = 0;
        int pageSize = 10;

        ;

        mockMvc.perform(get("/search/post/" + keyword + "/" + offset + "/" + pageSize)
                        .headers(httpHeaders)
                        .param("sortBy", "bestPrice")
                        .param("subject", "Operation System")
                        .param("department", "CS")
                        .param("author", "yitingwu.1030@gmail.com")
                        .param("haveQA", "true")
                        .param("haveCollaboration", "true")
                        .param("haveReward", "true")

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.search.items.[0].id").value(QAPost.getId()))
                .andExpect(jsonPath("$.search.items.[0].email.[0]").value(QAPost.getEmail().get(0)))
                .andExpect(jsonPath("$.search.items.[0].type").value(QAPost.getType()))
                .andExpect(jsonPath("$.search.items.[0].author").value(QAPost.getAuthor()))
                .andExpect(jsonPath("$.search.items.[0].authorName").value(QAPost.getAuthorName()))
                .andExpect(jsonPath("$.search.items.[0].department").value(QAPost.getDepartment()))
                .andExpect(jsonPath("$.search.items.[0].school").value(QAPost.getSchool()))
                .andExpect(jsonPath("$.search.items.[0].professor").value(QAPost.getProfessor()))
                .andExpect(jsonPath("$.search.items.[0].title").value(QAPost.getTitle()))
                .andExpect(jsonPath("$.search.items.[0].content").value(QAPost.getContent()))
                .andExpect(jsonPath("$.search.items.[0].date").value(QAPost.getDate()))
                .andExpect(jsonPath("$.search.items.[0].bestPrice").value(QAPost.getBestPrice()))
                .andExpect(jsonPath("$.search.items.[0].referencePrice").value(QAPost.getReferencePrice()))
                .andExpect(jsonPath("$.search.items.[0].referenceNumber").value(QAPost.getReferenceNumber()))
                .andExpect(jsonPath("$.search.items.[0].comments.[0].id").value(QAPost.getComments().get(0).getId()))
                .andExpect(jsonPath("$.search.items.[0].comments.[1].id").value(QAPost.getComments().get(1).getId()))
                .andExpect(jsonPath("$.search.items.[0].commentCount").value(QAPost.getCommentCount()))
                .andExpect(jsonPath("$.search.items.[0].answers").value(QAPost.getAnswers()))
                .andExpect(jsonPath("$.search.items.[0].publishDate").value(QAPost.getPublishDate()))
                .andExpect(jsonPath("$.search.items.[0].vote").value(QAPost.getVote()))
                .andExpect(jsonPath("$.search.items.[0].collabNoteAuthorNumber").value(QAPost.getCollabNoteAuthorNumber()))
                .andExpect(jsonPath("$.search.items.[0].collabApply").value(QAPost.getCollabApply()))
                .andExpect(jsonPath("$.search.items.[0].public").value(QAPost.getPublic()))
                .andExpect(jsonPath("$.search.items.[1].id").value(rewardPost.getId()))
                .andExpect(jsonPath("$.search.items.[1].email.[0]").value(rewardPost.getEmail().get(0)))
                .andExpect(jsonPath("$.search.items.[1].type").value(rewardPost.getType()))
                .andExpect(jsonPath("$.search.items.[1].author").value(rewardPost.getAuthor()))
                .andExpect(jsonPath("$.search.items.[1].authorName").value(rewardPost.getAuthorName()))
                .andExpect(jsonPath("$.search.items.[1].department").value(rewardPost.getDepartment()))
                .andExpect(jsonPath("$.search.items.[1].school").value(rewardPost.getSchool()))
                .andExpect(jsonPath("$.search.items.[1].professor").value(rewardPost.getProfessor()))
                .andExpect(jsonPath("$.search.items.[1].title").value(rewardPost.getTitle()))
                .andExpect(jsonPath("$.search.items.[1].content").value(rewardPost.getContent()))
                .andExpect(jsonPath("$.search.items.[1].date").value(rewardPost.getDate()))
                .andExpect(jsonPath("$.search.items.[1].bestPrice").value(rewardPost.getBestPrice()))
                .andExpect(jsonPath("$.search.items.[1].referencePrice").value(rewardPost.getReferencePrice()))
                .andExpect(jsonPath("$.search.items.[1].referenceNumber").value(rewardPost.getReferenceNumber()))
                .andExpect(jsonPath("$.search.items.[1].comments").value(rewardPost.getComments()))
                .andExpect(jsonPath("$.search.items.[1].commentCount").value(rewardPost.getCommentCount()))
                .andExpect(jsonPath("$.search.items.[1].answers").value(rewardPost.getAnswers()))
                .andExpect(jsonPath("$.search.items.[1].publishDate").value(rewardPost.getPublishDate()))
                .andExpect(jsonPath("$.search.items.[1].vote").value(rewardPost.getVote()))
                .andExpect(jsonPath("$.search.items.[1].collabNoteAuthorNumber").value(rewardPost.getCollabNoteAuthorNumber()))
                .andExpect(jsonPath("$.search.items.[1].collabApply").value(rewardPost.getCollabApply()))
                .andExpect(jsonPath("$.search.items.[1].public").value(rewardPost.getPublic()))
                .andExpect(jsonPath("$.search.items.[2].id").value(collaborationPost.getId()))
                .andExpect(jsonPath("$.search.items.[2].email.[0]").value(collaborationPost.getEmail().get(0)))
                .andExpect(jsonPath("$.search.items.[2].type").value(collaborationPost.getType()))
                .andExpect(jsonPath("$.search.items.[2].author").value(collaborationPost.getAuthor()))
                .andExpect(jsonPath("$.search.items.[2].authorName").value(collaborationPost.getAuthorName()))
                .andExpect(jsonPath("$.search.items.[2].department").value(collaborationPost.getDepartment()))
                .andExpect(jsonPath("$.search.items.[2].school").value(collaborationPost.getSchool()))
                .andExpect(jsonPath("$.search.items.[2].professor").value(collaborationPost.getProfessor()))
                .andExpect(jsonPath("$.search.items.[2].title").value(collaborationPost.getTitle()))
                .andExpect(jsonPath("$.search.items.[2].content").value(collaborationPost.getContent()))
                .andExpect(jsonPath("$.search.items.[2].date").value(collaborationPost.getDate()))
                .andExpect(jsonPath("$.search.items.[2].bestPrice").value(collaborationPost.getBestPrice()))
                .andExpect(jsonPath("$.search.items.[2].referencePrice").value(collaborationPost.getReferencePrice()))
                .andExpect(jsonPath("$.search.items.[2].referenceNumber").value(collaborationPost.getReferenceNumber()))
                .andExpect(jsonPath("$.search.items.[2].comments").value(collaborationPost.getComments()))
                .andExpect(jsonPath("$.search.items.[2].commentCount").value(collaborationPost.getCommentCount()))
                .andExpect(jsonPath("$.search.items.[2].answers").value(collaborationPost.getAnswers()))
                .andExpect(jsonPath("$.search.items.[2].publishDate").value(collaborationPost.getPublishDate()))
                .andExpect(jsonPath("$.search.items.[2].vote").value(collaborationPost.getVote()))
                .andExpect(jsonPath("$.search.items.[2].collabNoteAuthorNumber").value(collaborationPost.getCollabNoteAuthorNumber()))
                .andExpect(jsonPath("$.search.items.[2].collabApply").value(collaborationPost.getCollabApply()))
                .andExpect(jsonPath("$.search.items.[2].public").value(collaborationPost.getPublic()))
                .andExpect(jsonPath("$.search.totalPages").value(1));
    }

    @Test
    public void testSearchFolder() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder folder1 = createFolder("taldskfgjaldfjk", "/taldskfgjaldfjk", null);

        Folder folder2 = createFolder("taldskfgjaldfjk1111", "/taldskfgjaldfjk1111", null);

        Folder folder11 = createFolder("tSecond", "/taldskfgjaldfjk/tSecond", folder1.getId());

        Folder folder111 = createFolder("tThird", "/taldskfgjaldfjk/tSecond/Third", folder11.getId());

        String keyword = "t";
        int offset =0;
        int pageSize = 10;
        mockMvc.perform(get("/search/folder/"+keyword+"/"+offset+"/"+pageSize)
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.search.items.[0].id").value(folder1.getId()))
                .andExpect(jsonPath("$.search.items.[0].folderName").value(folder1.getFolderName()))
                .andExpect(jsonPath("$.search.items.[0].notes").value(folder1.getNotes()))
                .andExpect(jsonPath("$.search.items.[0].path").value(folder1.getPath()))
                .andExpect(jsonPath("$.search.items.[0].parent").value(folder1.getParent()))
                .andExpect(jsonPath("$.search.items.[0].children").value(folder1.getChildren()))
                .andExpect(jsonPath("$.search.items.[0].public").value(folder1.getPublic()))
                .andExpect(jsonPath("$.search.items.[0].favorite").value(folder1.getFavorite()))
                .andExpect(jsonPath("$.search.items.[1].id").value(folder2.getId()))
                .andExpect(jsonPath("$.search.items.[1].folderName").value(folder2.getFolderName()))
                .andExpect(jsonPath("$.search.items.[1].notes").value(folder2.getNotes()))
                .andExpect(jsonPath("$.search.items.[1].path").value(folder2.getPath()))
                .andExpect(jsonPath("$.search.items.[1].parent").value(folder2.getParent()))
                .andExpect(jsonPath("$.search.items.[1].children").value(folder2.getChildren()))
                .andExpect(jsonPath("$.search.items.[1].public").value(folder2.getPublic()))
                .andExpect(jsonPath("$.search.items.[1].favorite").value(folder2.getFavorite()))
                .andExpect(jsonPath("$.search.items.[2].id").value(folder11.getId()))
                .andExpect(jsonPath("$.search.items.[2].folderName").value(folder11.getFolderName()))
                .andExpect(jsonPath("$.search.items.[2].notes").value(folder11.getNotes()))
                .andExpect(jsonPath("$.search.items.[2].path").value(folder11.getPath()))
                .andExpect(jsonPath("$.search.items.[2].parent").value(folder11.getParent()))
                .andExpect(jsonPath("$.search.items.[2].children").value(folder11.getChildren()))
                .andExpect(jsonPath("$.search.items.[2].public").value(folder11.getPublic()))
                .andExpect(jsonPath("$.search.items.[2].favorite").value(folder11.getFavorite()))
                .andExpect(jsonPath("$.search.items.[3].id").value(folder111.getId()))
                .andExpect(jsonPath("$.search.items.[3].folderName").value(folder111.getFolderName()))
                .andExpect(jsonPath("$.search.items.[3].notes").value(folder111.getNotes()))
                .andExpect(jsonPath("$.search.items.[3].path").value(folder111.getPath()))
                .andExpect(jsonPath("$.search.items.[3].parent").value(folder111.getParent()))
                .andExpect(jsonPath("$.search.items.[3].children").value(folder111.getChildren()))
                .andExpect(jsonPath("$.search.items.[3].public").value(folder111.getPublic()))
                .andExpect(jsonPath("$.search.items.[3].favorite").value(folder111.getFavorite()));
    }

    @AfterEach
    public void clear() {
        //TODO:打開
//        postRepository.deleteAll();
//        userRepository.deleteAll();
//        folderRepository.deleteAll();
//        noteRepository.deleteAll();
    }
}
