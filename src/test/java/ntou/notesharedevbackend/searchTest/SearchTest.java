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
//        ArrayList<String> wantEnterUsersEmail = new ArrayList<>();
//        post.setWantEnterUsersEmail(wantEnterUsersEmail);
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
    public void testSearchPost() throws Exception{
        Post QAPost = createQAPost();
        Post rewardPost = createRewardPost();
        Post collaborationPost = createCollaborationPost();

        String keyword = "trap";
        int offset = 0;
        int pageSize = 10;

                ;

        mockMvc.perform(get("/search/post/"+keyword+"/"+offset+"/"+pageSize)
                .headers(httpHeaders)
                        .param("sortBy","bestPrice")
                                .param("subject","Operation System")
                                .param("department","CS")
                                .param("author","yitingwu.1030@gmail.com")
                                .param("haveQA","true")
                                .param("haveCollaboration","true")
                                .param("haveReward","true")

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
    public void testSearchFolder() throws Exception{

    }
    @AfterEach
    public void clear() {
//        postRepository.deleteAll();
//        userRepository.deleteAll();
//        folderRepository.deleteAll();
//        noteRepository.deleteAll();
    }
}
