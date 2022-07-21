package ntou.notesharedevbackend.noteTest;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.noteModule.entity.Content;
import ntou.notesharedevbackend.noteModule.entity.Note;
import ntou.notesharedevbackend.noteModule.entity.VersionContent;
import ntou.notesharedevbackend.postModule.entity.Apply;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.PostRepository;
import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.hamcrest.core.IsNull;
import org.json.JSONArray;
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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoteTest {
    private HttpHeaders httpHeaders;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private PostRepository postRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Folder createFolder(String folderName, String path, String parent, String name) {
        Folder folder = new Folder();
        folder.setFolderName(folderName);
        folder.setFavorite(false);
        folder.setParent(parent);
        folder.setPath(path);
        folder.setNotes(new ArrayList<String>());
        folder.setChildren(new ArrayList<String>());
        folder.setPublic(false);
        folder.setCreatorName(name);
        folderRepository.insert(folder);
        return folder;
    }

    private AppUser createUser(String email, String name) {
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setActivate(true);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode("1234"));
        Folder buyFolder = createFolder("Buy", "/Buy", null, name);
        Folder favoriteFolder = createFolder("Favorite", "/Favorite", null, name);
        Folder collaborationFolder = createFolder("Collaboration", "/Collaboration", null, name);
        Folder OSFolder = createFolder("OS", "/OS", null, name);
        Folder tempRewardNoteFolder = createFolder("Temp Reward Note", "/Temp Reward Note", null, name);
        ArrayList<String> folderList = new ArrayList<>();
        folderList.add(buyFolder.getId());
        folderList.add(favoriteFolder.getId());
        folderList.add(collaborationFolder.getId());
        folderList.add(OSFolder.getId());
        folderList.add(tempRewardNoteFolder.getId());
        appUser.setFolders(folderList);
        appUser.setCoin(300);
        appUser.setNotification(new ArrayList<>());
        appUser.setUnreadMessageCount(0);
        appUser.setBelledBy(new ArrayList<>());
        appUser.setSubscribe(new ArrayList<>());
        appUser.setFans(new ArrayList<>());
        return appUser;
    }

    public Post createCollaborationPost() {
        Post post = new Post();
        post.setType("collaboration");
        ArrayList<String> email = new ArrayList<>();
        email.add("yitingwu.1030@gmail.com");
        email.add("user1@gmail.com");
        email.add("user2@gmail.com");
        post.setEmail(email);
        post.setAuthor("Ting");
        post.setDepartment("Computer Science");
        post.setSubject("Operation System");
        post.setSchool("NTOU");
        post.setProfessor("professor");
        post.setTitle("Interrupt vs trap");
        post.setContent("this is a post!");
        post.setCollabNoteAuthorNumber(post.getEmail().size());
        ArrayList<String> answers = new ArrayList<>();
        post.setAnswers(answers);
        post.setCollabApply(new ArrayList<Apply>());
        post.setPublic(true);
        post.setComments(new ArrayList<Comment>());
        post.setCommentCount(0);
        post = postRepository.insert(post);
        return post;
    }

    private Note createNormalNote() {
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
        note.setPublic(false);
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
        note.setSubmit(false);
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
        ArrayList<String> email = new ArrayList<String>();
        email.add("yitingwu.1030@gmail.com");
        post.setEmail(email);
        post.setDepartment("CS");
        post.setSubject("Python");
        post.setSchool("NTOU");
        post.setProfessor("Chin-Chun Chang");
        post.setTitle("Python iterator");
        post.setContent("iterator詳細介紹");
        post.setDate();
        post.setBestPrice(50);
        post.setReferencePrice(10);
        post.setReferenceNumber(2);
        post.setPublic(true);
        post.setComments(new ArrayList<Comment>());
        ArrayList<String> answers = new ArrayList<>();
        post.setAnswers(answers);
        return postRepository.insert(post);
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
        note.setReference(null);
        note.setBest(null);
        note.setManagerEmail("user1@gmail.com");
        Post post = createCollaborationPost();
        note.setPostID(post.getId());
        noteRepository.insert(note);
        return note;
    }

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        noteRepository.deleteAll();
        folderRepository.deleteAll();
        postRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        AppUser appUser1 = createUser("user1@gmail.com", "User1");
        AppUser appUser2 = createUser("user2@gmail.com", "User2");
        userRepository.insert(appUser);
        userRepository.insert(appUser1);
        userRepository.insert(appUser2);
        Note note = createCollaborationNote();
        Post post = postRepository.findById(note.getPostID()).get();
        post.getAnswers().add(note.getId());
        postRepository.save(post);
        Folder appUserFolder = folderRepository.findById(userRepository.findByEmail("yitingwu.1030@gmail.com").getFolders().get(2)).get();
        Folder appUser1Folder = folderRepository.findById(userRepository.findByEmail("user1@gmail.com").getFolders().get(2)).get();
        Folder appUser2Folder = folderRepository.findById(userRepository.findByEmail("user2@gmail.com").getFolders().get(2)).get();
        appUserFolder.getNotes().add(note.getId());
        appUser1Folder.getNotes().add(note.getId());
        appUser2Folder.getNotes().add(note.getId());
        folderRepository.save(appUserFolder);
        folderRepository.save(appUser1Folder);
        folderRepository.save(appUser2Folder);
        Note normalNote = createNormalNote();
        Folder appUserFolderForNormalNote = folderRepository.findById(userRepository.findByEmail("yitingwu.1030@gmail.com").getFolders().get(3)).get();
        appUserFolderForNormalNote.getNotes().add(normalNote.getId());
        folderRepository.save(appUserFolderForNormalNote);
    }

    @Test
    public void testGetNoteByID() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(2)).get();
        Note note = noteRepository.findById(folder.getNotes().get(0)).get();
        mockMvc.perform(get("/note/" + note.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(note.getId()))
                .andExpect(jsonPath("$.res.type").value(note.getType()))
                .andExpect(jsonPath("$.res.department").value(note.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(note.getSubject()))
                .andExpect(jsonPath("$.res.title").value(note.getTitle()))
                .andExpect(jsonPath("$.res.headerEmail").value(note.getHeaderEmail()))
                .andExpect(jsonPath("$.res.headerName").value(note.getHeaderName()))
                .andExpect(jsonPath("$.res.authorEmail.[0]").value(note.getAuthorEmail().get(0)))
                .andExpect(jsonPath("$.res.authorName.[0]").value(note.getAuthorName().get(0)))
                .andExpect(jsonPath("$.res.managerEmail").value(note.getManagerEmail()))
                .andExpect(jsonPath("$.res.professor").value(note.getProfessor()))
                .andExpect(jsonPath("$.res.school").value(note.getSchool()))
                .andExpect(jsonPath("$.res.likeCount").value(note.getLikeCount()))
                .andExpect(jsonPath("$.res.favoriteCount").value(note.getFavoriteCount()))
                .andExpect(jsonPath("$.res.unlockCount").value(note.getUnlockCount()))
                .andExpect(jsonPath("$.res.downloadable").value(note.getDownloadable()))
                .andExpect(jsonPath("$.res.commentCount").value(note.getCommentCount()))
                .andExpect(jsonPath("$.res.comments").value(note.getComments()))
                .andExpect(jsonPath("$.res.price").value(note.getPrice()))
                .andExpect(jsonPath("$.res.quotable").value(note.getQuotable()))
                .andExpect(jsonPath("$.res.tag").value(note.getTag()))
                .andExpect(jsonPath("$.res.hiddenTag").value(note.getHiddenTag()))
                .andExpect(jsonPath("$.res.version.[0].id").value(note.getVersion().get(0).getId()))
                .andExpect(jsonPath("$.res.version.[0].name").value(note.getVersion().get(0).getName()))
                .andExpect(jsonPath("$.res.version.[0].slug").value(note.getVersion().get(0).getSlug()))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_html").value(note.getVersion().get(0).getContent().get(0).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_components").value(note.getVersion().get(0).getContent().get(0).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_assets").value(note.getVersion().get(0).getContent().get(0).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_css").value(note.getVersion().get(0).getContent().get(0).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_styles").value(note.getVersion().get(0).getContent().get(0).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[0].content.[1].mycustom_html").value(note.getVersion().get(0).getContent().get(1).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[0].content.[1].mycustom_components").value(note.getVersion().get(0).getContent().get(1).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[0].content.[1].mycustom_assets").value(note.getVersion().get(0).getContent().get(1).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[0].content.[1].mycustom_css").value(note.getVersion().get(0).getContent().get(1).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[0].content.[1].mycustom_styles").value(note.getVersion().get(0).getContent().get(1).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[0].content.[2].mycustom_html").value(note.getVersion().get(0).getContent().get(2).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[0].content.[2].mycustom_components").value(note.getVersion().get(0).getContent().get(2).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[0].content.[2].mycustom_assets").value(note.getVersion().get(0).getContent().get(2).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[0].content.[2].mycustom_css").value(note.getVersion().get(0).getContent().get(2).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[0].content.[2].mycustom_styles").value(note.getVersion().get(0).getContent().get(2).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[0].picURL").value(note.getVersion().get(0).getPicURL()))
                .andExpect(jsonPath("$.res.version.[0].fileURL").value(note.getVersion().get(0).getFileURL()))
                .andExpect(jsonPath("$.res.version.[0].temp").value(note.getVersion().get(0).getTemp()))
                .andExpect(jsonPath("$.res.version.[1].id").value(note.getVersion().get(1).getId()))
                .andExpect(jsonPath("$.res.version.[1].name").value(note.getVersion().get(1).getName()))
                .andExpect(jsonPath("$.res.version.[1].slug").value(note.getVersion().get(1).getSlug()))
                .andExpect(jsonPath("$.res.version.[1].content.[0].mycustom_html").value(note.getVersion().get(1).getContent().get(0).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[1].content.[0].mycustom_components").value(note.getVersion().get(1).getContent().get(0).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[1].content.[0].mycustom_assets").value(note.getVersion().get(1).getContent().get(0).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[1].content.[0].mycustom_css").value(note.getVersion().get(1).getContent().get(0).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[1].content.[0].mycustom_styles").value(note.getVersion().get(1).getContent().get(0).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[1].content.[1].mycustom_html").value(note.getVersion().get(1).getContent().get(1).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[1].content.[1].mycustom_components").value(note.getVersion().get(1).getContent().get(1).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[1].content.[1].mycustom_assets").value(note.getVersion().get(1).getContent().get(1).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[1].content.[1].mycustom_css").value(note.getVersion().get(1).getContent().get(1).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[1].content.[1].mycustom_styles").value(note.getVersion().get(1).getContent().get(1).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[1].content.[2].mycustom_html").value(note.getVersion().get(1).getContent().get(2).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[1].content.[2].mycustom_components").value(note.getVersion().get(1).getContent().get(2).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[1].content.[2].mycustom_assets").value(note.getVersion().get(1).getContent().get(2).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[1].content.[2].mycustom_css").value(note.getVersion().get(1).getContent().get(2).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[1].content.[2].mycustom_styles").value(note.getVersion().get(1).getContent().get(2).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[1].picURL").value(note.getVersion().get(1).getPicURL()))
                .andExpect(jsonPath("$.res.version.[1].fileURL").value(note.getVersion().get(1).getFileURL()))
                .andExpect(jsonPath("$.res.version.[1].temp").value(note.getVersion().get(1).getTemp()))
                .andExpect(jsonPath("$.res.version.[2].id").value(note.getVersion().get(2).getId()))
                .andExpect(jsonPath("$.res.version.[2].name").value(note.getVersion().get(2).getName()))
                .andExpect(jsonPath("$.res.version.[2].slug").value(note.getVersion().get(2).getSlug()))
                .andExpect(jsonPath("$.res.version.[2].content.[0].mycustom_html").value(note.getVersion().get(2).getContent().get(0).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[2].content.[0].mycustom_components").value(note.getVersion().get(2).getContent().get(0).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[2].content.[0].mycustom_assets").value(note.getVersion().get(2).getContent().get(0).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[2].content.[0].mycustom_css").value(note.getVersion().get(2).getContent().get(0).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[2].content.[0].mycustom_styles").value(note.getVersion().get(2).getContent().get(0).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[2].content.[1].mycustom_html").value(note.getVersion().get(2).getContent().get(1).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[2].content.[1].mycustom_components").value(note.getVersion().get(2).getContent().get(1).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[2].content.[1].mycustom_assets").value(note.getVersion().get(2).getContent().get(1).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[2].content.[1].mycustom_css").value(note.getVersion().get(2).getContent().get(1).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[2].content.[1].mycustom_styles").value(note.getVersion().get(2).getContent().get(1).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[2].content.[2].mycustom_html").value(note.getVersion().get(2).getContent().get(2).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[2].content.[2].mycustom_components").value(note.getVersion().get(2).getContent().get(2).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[2].content.[2].mycustom_assets").value(note.getVersion().get(2).getContent().get(2).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[2].content.[2].mycustom_css").value(note.getVersion().get(2).getContent().get(2).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[2].content.[2].mycustom_styles").value(note.getVersion().get(2).getContent().get(2).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[2].picURL").value(note.getVersion().get(2).getPicURL()))
                .andExpect(jsonPath("$.res.version.[2].fileURL").value(note.getVersion().get(2).getFileURL()))
                .andExpect(jsonPath("$.res.version.[2].temp").value(note.getVersion().get(2).getTemp()))
                .andExpect(jsonPath("$.res.version.[3].id").value(note.getVersion().get(3).getId()))
                .andExpect(jsonPath("$.res.version.[3].name").value(note.getVersion().get(3).getName()))
                .andExpect(jsonPath("$.res.version.[3].slug").value(note.getVersion().get(3).getSlug()))
                .andExpect(jsonPath("$.res.version.[3].content.[0].mycustom_html").value(note.getVersion().get(3).getContent().get(0).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[3].content.[0].mycustom_components").value(note.getVersion().get(3).getContent().get(0).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[3].content.[0].mycustom_assets").value(note.getVersion().get(3).getContent().get(0).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[3].content.[0].mycustom_css").value(note.getVersion().get(3).getContent().get(0).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[3].content.[0].mycustom_styles").value(note.getVersion().get(3).getContent().get(0).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[3].content.[1].mycustom_html").value(note.getVersion().get(3).getContent().get(1).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[3].content.[1].mycustom_components").value(note.getVersion().get(3).getContent().get(1).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[3].content.[1].mycustom_assets").value(note.getVersion().get(3).getContent().get(1).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[3].content.[1].mycustom_css").value(note.getVersion().get(3).getContent().get(1).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[3].content.[1].mycustom_styles").value(note.getVersion().get(3).getContent().get(1).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[3].content.[2].mycustom_html").value(note.getVersion().get(3).getContent().get(2).getMycustom_html()))
                .andExpect(jsonPath("$.res.version.[3].content.[2].mycustom_components").value(note.getVersion().get(3).getContent().get(2).getMycustom_components()))
                .andExpect(jsonPath("$.res.version.[3].content.[2].mycustom_assets").value(note.getVersion().get(3).getContent().get(2).getMycustom_assets()))
                .andExpect(jsonPath("$.res.version.[3].content.[2].mycustom_css").value(note.getVersion().get(3).getContent().get(2).getMycustom_css()))
                .andExpect(jsonPath("$.res.version.[3].content.[2].mycustom_styles").value(note.getVersion().get(3).getContent().get(2).getMycustom_styles()))
                .andExpect(jsonPath("$.res.version.[3].picURL").value(note.getVersion().get(3).getPicURL()))
                .andExpect(jsonPath("$.res.version.[3].fileURL").value(note.getVersion().get(3).getFileURL()))
                .andExpect(jsonPath("$.res.version.[3].temp").value(note.getVersion().get(3).getTemp()))
                .andExpect(jsonPath("$.res.postID").value(note.getPostID()))
                .andExpect(jsonPath("$.res.reference").value(note.getReference()))
                .andExpect(jsonPath("$.res.best").value(note.getBest()))
                .andExpect(jsonPath("$.res.public").value(note.getPublic()))
                .andExpect(jsonPath("$.res.submit").value(note.getSubmit()))
                .andExpect(jsonPath("$.res.headerUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.liker").isEmpty())
                .andExpect(jsonPath("$.res.buyer").isEmpty())
                .andExpect(jsonPath("$.res.favoriter").isEmpty())
                .andExpect(jsonPath("$.res.contributors").isEmpty())
                .andExpect(jsonPath("$.res.likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.buyerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.favoriterUserObj").isEmpty())
                .andExpect(jsonPath("$.res.contributorUserObj").isEmpty())
        ;
    }

    @Test
    public void testGetNoteVersion() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(2)).get();
        Note note = noteRepository.findById(folder.getNotes().get(0)).get();
        mockMvc.perform(get("/note/" + note.getId() + "/3")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(note.getVersion().get(3).getId()))
                .andExpect(jsonPath("$.res.name").value(note.getVersion().get(3).getName()))
                .andExpect(jsonPath("$.res.slug").value(note.getVersion().get(3).getSlug()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_html").value(note.getVersion().get(3).getContent().get(0).getMycustom_html()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_components").value(note.getVersion().get(3).getContent().get(0).getMycustom_components()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_assets").value(note.getVersion().get(3).getContent().get(0).getMycustom_assets()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_css").value(note.getVersion().get(3).getContent().get(0).getMycustom_css()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_styles").value(note.getVersion().get(3).getContent().get(0).getMycustom_styles()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_html").value(note.getVersion().get(3).getContent().get(1).getMycustom_html()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_components").value(note.getVersion().get(3).getContent().get(1).getMycustom_components()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_assets").value(note.getVersion().get(3).getContent().get(1).getMycustom_assets()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_css").value(note.getVersion().get(3).getContent().get(1).getMycustom_css()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_styles").value(note.getVersion().get(3).getContent().get(1).getMycustom_styles()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_html").value(note.getVersion().get(3).getContent().get(2).getMycustom_html()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_components").value(note.getVersion().get(3).getContent().get(2).getMycustom_components()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_assets").value(note.getVersion().get(3).getContent().get(2).getMycustom_assets()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_css").value(note.getVersion().get(3).getContent().get(2).getMycustom_css()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_styles").value(note.getVersion().get(3).getContent().get(2).getMycustom_styles()))
                .andExpect(jsonPath("$.res.picURL").value(note.getVersion().get(3).getPicURL()))
                .andExpect(jsonPath("$.res.fileURL").value(note.getVersion().get(3).getFileURL()))
                .andExpect(jsonPath("$.res.temp").value(note.getVersion().get(3).getTemp()));
    }

    @Test
    public void testGetNoteTags() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(2)).get();
        Note note = noteRepository.findById(folder.getNotes().get(0)).get();
        mockMvc.perform(get("/note/tags/" + note.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags.[0]").value(note.getTag().get(0)))
                .andExpect(jsonPath("$.tags.[1]").value(note.getTag().get(1)))
                .andExpect(jsonPath("$.tags.[2]").value(note.getTag().get(2)));
    }

    @Test
    public void testCreateNote() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(3)).get();
        JSONArray authorEmails = new JSONArray();
        authorEmails.put(appUser.getEmail());
        JSONArray authorNames = new JSONArray();
        authorNames.put(appUser.getName());
        JSONArray versionContents = new JSONArray();
        JSONArray fileURLs = new JSONArray()
                .put("fileURL1")
                .put("fileURL2")
                .put("fileURL3");
        JSONArray picURLs = new JSONArray()
                .put("picURL1")
                .put("picURL2")
                .put("picURL3");
        JSONObject content = new JSONObject()
                .put("mycustom_assets", "string")
                .put("mycustom_components", "string")
                .put("mycustom_html", "string")
                .put("mycustom_styles", "string")
                .put("mycustom_css", "string");
        JSONArray contentArray = new JSONArray()
                .put(content);
        JSONObject v1 = new JSONObject()
                .put("name", "string")
                .put("slug", "string")
                .put("fileURL", fileURLs)
                .put("picURL", picURLs)
                .put("temp", true)
                .put("content", contentArray);
        versionContents.put(v1);
        JSONObject request = new JSONObject()
                .put("type", "normal")
                .put("department", "CS")
                .put("subject", "Java")
                .put("title", "Array")
                .put("headerEmail", appUser.getEmail())
                .put("headerName", appUser.getName())
                .put("authorEmail", authorEmails)
                .put("authorName", authorNames)
                .put("professor", "NoteShare")
                .put("school", "NTOU")
                .put("public", false)
                .put("price", 50)
                .put("downloadable", false)
                .put("quotable", false)
                .put("version", versionContents);
        mockMvc.perform(post("/note/" + appUser.getEmail() + "/" + folder.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.res.id").exists())
                .andExpect(jsonPath("$.res.type").value(request.get("type")))
                .andExpect(jsonPath("$.res.department").value(request.get("department")))
                .andExpect(jsonPath("$.res.subject").value(request.get("subject")))
                .andExpect(jsonPath("$.res.title").value(request.get("title")))
                .andExpect(jsonPath("$.res.headerEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.headerName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorEmail.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName.[0]").value(appUser.getName()))
                .andExpect(jsonPath("$.res.professor").value(request.get("professor")))
                .andExpect(jsonPath("$.res.school").value(request.get("school")))
                .andExpect(jsonPath("$.res.likeCount").value(0))
                .andExpect(jsonPath("$.res.favoriteCount").value(0))
                .andExpect(jsonPath("$.res.unlockCount").value(0))
                .andExpect(jsonPath("$.res.downloadable").value(request.get("downloadable")))
                .andExpect(jsonPath("$.res.commentCount").value(0))
                .andExpect(jsonPath("$.res.comments").isEmpty())
                .andExpect(jsonPath("$.res.price").value(request.get("price")))
                .andExpect(jsonPath("$.res.quotable").value(request.get("quotable")))
                .andExpect(jsonPath("$.res.tag").isEmpty())
                .andExpect(jsonPath("$.res.hiddenTag").isEmpty())
                .andExpect(jsonPath("$.res.version.[0].id").exists())
                .andExpect(jsonPath("$.res.version.[0].name").value(v1.get("name")))
                .andExpect(jsonPath("$.res.version.[0].slug").value(v1.get("slug")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_html").value(content.get("mycustom_html")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_components").value(content.get("mycustom_components")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_assets").value(content.get("mycustom_assets")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_css").value(content.get("mycustom_css")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_styles").value(content.get("mycustom_styles")))
                .andExpect(jsonPath("$.res.version.[0].picURL.[0]").value(picURLs.get(0)))
                .andExpect(jsonPath("$.res.version.[0].picURL.[1]").value(picURLs.get(1)))
                .andExpect(jsonPath("$.res.version.[0].picURL.[2]").value(picURLs.get(2)))
                .andExpect(jsonPath("$.res.version.[0].fileURL[0]").value(fileURLs.get(0)))
                .andExpect(jsonPath("$.res.version.[0].fileURL[1]").value(fileURLs.get(1)))
                .andExpect(jsonPath("$.res.version.[0].fileURL[2]").value(fileURLs.get(2)))
                .andExpect(jsonPath("$.res.version.[0].temp").value(v1.get("temp")))
                .andExpect(jsonPath("$.res.postID").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.reference").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.best").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.public").value(request.get("public")))
                .andExpect(jsonPath("$.res.submit").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.headerUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.liker").isEmpty())
                .andExpect(jsonPath("$.res.buyer").isEmpty())
                .andExpect(jsonPath("$.res.favoriter").isEmpty())
                .andExpect(jsonPath("$.res.contributors").isEmpty())
                .andExpect(jsonPath("$.res.likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.buyerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.favoriterUserObj").isEmpty())
                .andExpect(jsonPath("$.res.contributorUserObj").isEmpty());

        if (folderRepository.findById(folder.getId()).get().getNotes().isEmpty()) {
            throw new Exception("Note Test : new note does not add in folder");
        }
    }

    @Test
    public void testSaveNote() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(3)).get();//Normal Folder
        Note note = noteRepository.findById(folder.getNotes().get(0)).get();
        JSONArray authorEmails = new JSONArray();
        authorEmails.put(appUser.getEmail());
        JSONArray authorNames = new JSONArray();
        authorNames.put(appUser.getName());
        JSONArray versionContents = new JSONArray();
        JSONArray fileURLs = new JSONArray()
                .put("fileURL3");
        JSONArray picURLs = new JSONArray()
                .put("picURL3");
        JSONObject content = new JSONObject()
                .put("mycustom_assets", "newString")
                .put("mycustom_components", "newString")
                .put("mycustom_html", "newString")
                .put("mycustom_styles", "newString")
                .put("mycustom_css", "newString");
        JSONArray contentArray = new JSONArray()
                .put(content);
        JSONObject v1 = new JSONObject()
                .put("name", "newString")
                .put("slug", "newString")
                .put("fileURL", fileURLs)
                .put("picURL", picURLs)
                .put("temp", true)
                .put("content", contentArray);
        versionContents.put(v1);
        JSONArray tag = new JSONArray()
                .put("tag1");
        JSONArray liker = new JSONArray();
        for (String s : note.getLiker()) {
            liker.put(s);
        }
        JSONArray favoriter = new JSONArray();
        for (String s : note.getFavoriter()) {
            favoriter.put(s);
        }
        JSONArray buyer = new JSONArray();
        for (String s : note.getBuyer()) {
            buyer.put(s);
        }
        JSONArray hiddenTag = new JSONArray();
        for (String s : note.getHiddenTag()) {
            hiddenTag.put(s);
        }
        JSONArray contributors = new JSONArray();
        for (String s : note.getContributors()) {
            contributors.put(s);
        }
        JSONArray comments = new JSONArray();
        JSONObject comment = new JSONObject();
        for (Comment c : note.getComments()) {
            JSONArray commentLiker = new JSONArray();
            for (String s : c.getLiker()) {
                commentLiker.put(s);
            }
            JSONArray commentPicURL = new JSONArray();
            for (String s : c.getPicURL()) {
                commentPicURL.put(s);
            }
            comment.put("id", c.getId())
                    .put("author", c.getAuthor())
                    .put("email", c.getEmail())
                    .put("content", c.getContent())
                    .put("likeCount", c.getLikeCount())
                    .put("floor", c.getFloor())
                    .put("date", c.getDate())
                    .put("best", c.getBest())
                    .put("liker", commentLiker)
                    .put("picURL", commentPicURL);
            comments.put(c);
        }
        JSONObject request = new JSONObject()
                .put("id", note.getId())
                .put("type", note.getType())
                .put("department", "CSE")
                .put("subject", "Java")
                .put("title", "Array")
                .put("headerEmail", appUser.getEmail())
                .put("headerName", appUser.getName())
                .put("authorEmail", authorEmails)
                .put("authorName", authorNames)
                .put("professor", "Note")
                .put("school", "NTU")
                .put("liker", liker)
                .put("likeCount", note.getLikeCount())
                .put("favoriter", favoriter)
                .put("favoriteCount", note.getFavoriteCount())
                .put("buyer", buyer)
                .put("unlockCount", note.getUnlockCount())
                .put("commentCount", note.getCommentCount())
                .put("comments", comments)
                .put("tag", tag)
                .put("hiddenTag", hiddenTag)
                .put("contributors", contributors)
                .put("postID", note.getPostID())
                .put("publishDate", note.getPublishDate() == null ? null : note.getPublishDate())
                .put("description", note.getDescription() == null ? null : note.getDescription())
                .put("reference", note.getReference() == null ? null : note.getReference())
                .put("best", note.getBest() == null ? null : note.getBest())
                .put("submit", note.getSubmit() == null ? null : note.getSubmit())
                .put("public", true)
                .put("price", 500)
                .put("downloadable", true)
                .put("quotable", true)
                .put("version", versionContents)
                .put("managerEmail", note.getManagerEmail() == null ? null : note.getManagerEmail());
        mockMvc.perform(put("/note/" + note.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(note.getId()))
                .andExpect(jsonPath("$.res.type").value(request.get("type")))
                .andExpect(jsonPath("$.res.department").value(request.get("department")))
                .andExpect(jsonPath("$.res.subject").value(request.get("subject")))
                .andExpect(jsonPath("$.res.title").value(request.get("title")))
                .andExpect(jsonPath("$.res.headerEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.headerName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorEmail.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName.[0]").value(appUser.getName()))
                .andExpect(jsonPath("$.res.professor").value(request.get("professor")))
                .andExpect(jsonPath("$.res.school").value(request.get("school")))
                .andExpect(jsonPath("$.res.likeCount").value(0))
                .andExpect(jsonPath("$.res.favoriteCount").value(0))
                .andExpect(jsonPath("$.res.unlockCount").value(0))
                .andExpect(jsonPath("$.res.downloadable").value(request.get("downloadable")))
                .andExpect(jsonPath("$.res.commentCount").value(0))
                .andExpect(jsonPath("$.res.comments").isEmpty())
                .andExpect(jsonPath("$.res.price").value(request.get("price")))
                .andExpect(jsonPath("$.res.quotable").value(request.get("quotable")))
                .andExpect(jsonPath("$.res.tag.[0]").value(tag.get(0)))
                .andExpect(jsonPath("$.res.hiddenTag").isEmpty())
                .andExpect(jsonPath("$.res.version.[0].id").exists())
                .andExpect(jsonPath("$.res.version.[0].name").value(v1.get("name")))
                .andExpect(jsonPath("$.res.version.[0].slug").value(v1.get("slug")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_html").value(content.get("mycustom_html")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_components").value(content.get("mycustom_components")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_assets").value(content.get("mycustom_assets")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_css").value(content.get("mycustom_css")))
                .andExpect(jsonPath("$.res.version.[0].content.[0].mycustom_styles").value(content.get("mycustom_styles")))
                .andExpect(jsonPath("$.res.version.[0].picURL.[0]").value(picURLs.get(0)))
                .andExpect(jsonPath("$.res.version.[0].fileURL[0]").value(fileURLs.get(0)))
                .andExpect(jsonPath("$.res.version.[0].temp").value(v1.get("temp")))
                .andExpect(jsonPath("$.res.postID").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.reference").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.best").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.public").value(request.get("public")))
                .andExpect(jsonPath("$.res.submit").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.headerUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.liker").isEmpty())
                .andExpect(jsonPath("$.res.buyer").isEmpty())
                .andExpect(jsonPath("$.res.favoriter").isEmpty())
                .andExpect(jsonPath("$.res.likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.buyerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.favoriterUserObj").isEmpty())
                .andExpect(jsonPath("$.res.contributorUserObj").isEmpty())
        ;

    }

    @Test
    public void testUpdateNoteContent() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(3)).get();//Normal Folder
        Note note = noteRepository.findById(folder.getNotes().get(0)).get();
        JSONArray fileURLs = new JSONArray()
                .put("fileURL4")
                .put("fileURL5")
                .put("fileURL6")
                .put("fileURL7")
                .put("fileURL8")
                .put("fileURL9");
        JSONArray picURLs = new JSONArray()
                .put("picURL8");
        JSONObject content = new JSONObject()
                .put("mycustom_assets", "stringstringstring")
                .put("mycustom_components", "stringstringstring")
                .put("mycustom_html", "stringstringstring")
                .put("mycustom_styles", "stringstringstring")
                .put("mycustom_css", "stringstringstring");
        JSONArray contentArray = new JSONArray()
                .put(content);
        JSONObject v1 = new JSONObject()
                .put("name", "newName")
                .put("slug", "newSlug")
                .put("fileURL", fileURLs)
                .put("picURL", picURLs)
                .put("temp", false)
                .put("content", contentArray);
        mockMvc.perform(put("/note/" + note.getId() + "/2")
                        .headers(httpHeaders)
                        .content(v1.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res..id").hasJsonPath())
                .andExpect(jsonPath("$.res.name").value(v1.get("name")))
                .andExpect(jsonPath("$.res.slug").value(v1.get("slug")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_html").value(content.get("mycustom_html")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_components").value(content.get("mycustom_components")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_assets").value(content.get("mycustom_assets")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_css").value(content.get("mycustom_css")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_styles").value(content.get("mycustom_styles")))
                .andExpect(jsonPath("$.res.picURL.[0]").value(picURLs.get(0)))
                .andExpect(jsonPath("$.res.fileURL[0]").value(fileURLs.get(0)))
                .andExpect(jsonPath("$.res.fileURL[1]").value(fileURLs.get(1)))
                .andExpect(jsonPath("$.res.fileURL[2]").value(fileURLs.get(2)))
                .andExpect(jsonPath("$.res.fileURL[3]").value(fileURLs.get(3)))
                .andExpect(jsonPath("$.res.fileURL[4]").value(fileURLs.get(4)))
                .andExpect(jsonPath("$.res.fileURL[5]").value(fileURLs.get(5)))
                .andExpect(jsonPath("$.res.temp").value(v1.get("temp")));
    }

    @Test
    public void testAddNoteContent() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(3)).get();//Normal Folder
        Note note = noteRepository.findById(folder.getNotes().get(0)).get();
        JSONArray fileURLs = new JSONArray()
                .put("fileURL4")
                .put("fileURL5")
                .put("fileURL6")
                .put("fileURL7")
                .put("fileURL8")
                .put("fileURL9");
        JSONArray picURLs = new JSONArray()
                .put("picURL8");
        JSONObject content = new JSONObject()
                .put("mycustom_assets", "stringstringstring")
                .put("mycustom_components", "stringstringstring")
                .put("mycustom_html", "stringstringstring")
                .put("mycustom_styles", "stringstringstring")
                .put("mycustom_css", "stringstringstring");
        JSONArray contentArray = new JSONArray()
                .put(content);
        JSONObject v1 = new JSONObject()
                .put("name", "newName")
                .put("slug", "newSlug")
                .put("fileURL", fileURLs)
                .put("picURL", picURLs)
                .put("temp", false)
                .put("content", contentArray);
        mockMvc.perform(put("/note/" + note.getId() + "/4")
                        .headers(httpHeaders)
                        .content(v1.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res..id").hasJsonPath())
                .andExpect(jsonPath("$.res.name").value(v1.get("name")))
                .andExpect(jsonPath("$.res.slug").value(v1.get("slug")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_html").value(content.get("mycustom_html")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_components").value(content.get("mycustom_components")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_assets").value(content.get("mycustom_assets")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_css").value(content.get("mycustom_css")))
                .andExpect(jsonPath("$.res.content.[0].mycustom_styles").value(content.get("mycustom_styles")))
                .andExpect(jsonPath("$.res.picURL.[0]").value(picURLs.get(0)))
                .andExpect(jsonPath("$.res.fileURL[0]").value(fileURLs.get(0)))
                .andExpect(jsonPath("$.res.fileURL[1]").value(fileURLs.get(1)))
                .andExpect(jsonPath("$.res.fileURL[2]").value(fileURLs.get(2)))
                .andExpect(jsonPath("$.res.fileURL[3]").value(fileURLs.get(3)))
                .andExpect(jsonPath("$.res.fileURL[4]").value(fileURLs.get(4)))
                .andExpect(jsonPath("$.res.fileURL[5]").value(fileURLs.get(5)))
                .andExpect(jsonPath("$.res.temp").value(v1.get("temp")));
    }

    @Test
    public void testSetManager() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser manager = userRepository.findByEmail("user1@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(2)).get();
        Note note = noteRepository.findById(folder.getNotes().get(0)).get();
        mockMvc.perform(put("/note/admin/" + note.getId() + "/" + manager.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        if (!noteRepository.findById(note.getId()).get().getManagerEmail().equals(manager.getEmail())) {
            System.out.println(noteRepository.findById(note.getId()).get().getManagerEmail());
            throw new Exception("Note Test : note's manager does wrong");
        }
    }

    @Test
    public void testKickUserFromCollaboration() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser kickTarget = userRepository.findByEmail("user1@gmail.com");
        Folder folder = folderRepository.findById(appUser.getFolders().get(2)).get();
        Note note = noteRepository.findById(folder.getNotes().get(0)).get();
        Post post = postRepository.findById(note.getPostID()).get();
        mockMvc.perform(put("/note/kick/" + note.getId() + "/" + kickTarget.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (noteRepository.findById(note.getId()).get().getAuthorEmail().contains(kickTarget.getEmail())) {
            throw new Exception("Note Test : Note's author's email still contain kickTarget");
        }
        if (noteRepository.findById(note.getId()).get().getAuthorName().contains(kickTarget.getName())) {
            throw new Exception("Note Test : Note's author's email still contain kickTarget");
        }
        //踢人之後 共筆貼文的作者人數要減一
        if (!postRepository.findById(post.getId()).get().getCollabNoteAuthorNumber().equals(post.getCollabNoteAuthorNumber() - 1)) {
            throw new Exception("Note Test : Post's collabNoteAuthorNumber does not -1");
        }
        //若為管理員，note要移除管理員˙
        if (!(noteRepository.findById(note.getId()).get().getManagerEmail() == null)) {
            throw new Exception("Note Test : Note's manager email does not clear");
        }
    }

    @Test
    public void testCopyNoteToFolder() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder newFolder = createFolder("New", "/New", null, appUser.getName());
        appUser.getFolders().add(newFolder.getId());
        userRepository.save(appUser);
        Folder oldFolder = folderRepository.findById(appUser.getFolders().get(3)).get();
        Note note = noteRepository.findById(oldFolder.getNotes().get(0)).get();
        mockMvc.perform(put("/note/save/" + note.getId() + "/" + newFolder.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(newFolder.getId()))
                .andExpect(jsonPath("$.res.folderName").value(newFolder.getFolderName()))
                .andExpect(jsonPath("$.res.path").value(newFolder.getPath()))
                .andExpect(jsonPath("$.res.children").isEmpty())
                .andExpect(jsonPath("$.res.notes.[0].id").value(note.getId()))
                .andExpect(jsonPath("$.res.notes.[0].headerEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.notes.[0].headerName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.favorite").value(newFolder.getFavorite()))
                .andExpect(jsonPath("$.res.public").value(newFolder.getPublic()))
                .andExpect(jsonPath("$.res.creatorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.creatorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.creatorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()));

        if (!folderRepository.findById(oldFolder.getId()).get().getNotes().contains(note.getId())) {
            throw new Exception("Note Test : Old Folder remove note id");
        }
    }

    @Test
    public void testDeleteNoteToFolder() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder newFolder = createFolder("New", "/New", null, appUser.getName());
        appUser.getFolders().add(newFolder.getId());
        userRepository.save(appUser);
        Folder oldFolder = folderRepository.findById(appUser.getFolders().get(3)).get();
        Note note = noteRepository.findById(oldFolder.getNotes().get(0)).get();
        newFolder.getNotes().add(note.getId());
        folderRepository.save(newFolder);
        mockMvc.perform(put("/note/delete/" + note.getId() + "/" + oldFolder.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(oldFolder.getId()))
                .andExpect(jsonPath("$.res.folderName").value(oldFolder.getFolderName()))
                .andExpect(jsonPath("$.res.path").value(oldFolder.getPath()))
                .andExpect(jsonPath("$.res.children").isEmpty())
                .andExpect(jsonPath("$.res.notes").isEmpty())
                .andExpect(jsonPath("$.res.favorite").value(oldFolder.getFavorite()))
                .andExpect(jsonPath("$.res.public").value(oldFolder.getPublic()))
                .andExpect(jsonPath("$.res.creatorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.creatorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.creatorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()));

        if (!folderRepository.findById(newFolder.getId()).get().getNotes().contains(note.getId())) {
            throw new Exception("Note Test : new Folder's note be remove");
        }
    }

//    @Test
//    public void testDeleteNoteToEmptyFolder() throws Exception {
//        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
//        Folder newFolder = createFolder("New", "/New", null, appUser.getName());
//        appUser.getFolders().add(newFolder.getId());
//        userRepository.save(appUser);
//        Folder oldFolder = folderRepository.findById(appUser.getFolders().get(3)).get();
//        Note note = noteRepository.findById(oldFolder.getNotes().get(0)).get();
//        mockMvc.perform(put("/note/delete/" + note.getId() + "/" + newFolder.getId())
//                        .headers(httpHeaders))
//                .andExpect(status().isNoContent())
//                .andExpect(jsonPath("$.msg").value("This folder hasn't contains the note."));
//        if (!folderRepository.findById(oldFolder.getId()).get().getNotes().contains(note.getId())) {
//            throw new Exception("Note Test : old Folder's note be remove");
//        }
//    }

    @Test
    public void testModifyVersionPublishStatus() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder appUserFolderForNormalNote = folderRepository.findById(userRepository.findByEmail(appUser.getEmail()).getFolders().get(3)).get();
        Note note = noteRepository.findById(appUserFolderForNormalNote.getNotes().get(0)).get();//normal note

        mockMvc.perform(put("/note/publish/" + note.getId() + "/0")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(note.getVersion().get(0).getId()))
                .andExpect(jsonPath("$.res.name").value(note.getVersion().get(0).getName()))
                .andExpect(jsonPath("$.res.slug").value(note.getVersion().get(0).getSlug()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_html").value(note.getVersion().get(0).getContent().get(0).getMycustom_html()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_components").value(note.getVersion().get(0).getContent().get(0).getMycustom_components()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_assets").value(note.getVersion().get(0).getContent().get(0).getMycustom_assets()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_css").value(note.getVersion().get(0).getContent().get(0).getMycustom_css()))
                .andExpect(jsonPath("$.res.content.[0].mycustom_styles").value(note.getVersion().get(0).getContent().get(0).getMycustom_styles()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_html").value(note.getVersion().get(0).getContent().get(1).getMycustom_html()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_components").value(note.getVersion().get(0).getContent().get(1).getMycustom_components()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_assets").value(note.getVersion().get(0).getContent().get(1).getMycustom_assets()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_css").value(note.getVersion().get(0).getContent().get(1).getMycustom_css()))
                .andExpect(jsonPath("$.res.content.[1].mycustom_styles").value(note.getVersion().get(0).getContent().get(1).getMycustom_styles()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_html").value(note.getVersion().get(0).getContent().get(2).getMycustom_html()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_components").value(note.getVersion().get(0).getContent().get(2).getMycustom_components()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_assets").value(note.getVersion().get(0).getContent().get(2).getMycustom_assets()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_css").value(note.getVersion().get(0).getContent().get(2).getMycustom_css()))
                .andExpect(jsonPath("$.res.content.[2].mycustom_styles").value(note.getVersion().get(0).getContent().get(2).getMycustom_styles()))
                .andExpect(jsonPath("$.res.picURL").value(note.getVersion().get(0).getPicURL()))
                .andExpect(jsonPath("$.res.fileURL").value(note.getVersion().get(0).getFileURL()))
                .andExpect(jsonPath("$.res.temp").value(!note.getVersion().get(0).getTemp()));

        Note newNote = noteRepository.findById(note.getId()).get();
        if (!newNote.getVersion().get(0).getTemp().equals(!note.getVersion().get(0).getTemp())) {
            throw new Exception("Note Test : version isTemp does not update");
        }
    }

    @Test
    public void testPublishNote() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Folder appUserFolderForNormalNote = folderRepository.findById(userRepository.findByEmail(appUser.getEmail()).getFolders().get(3)).get();
        Note note = noteRepository.findById(appUserFolderForNormalNote.getNotes().get(0)).get();//normal note

        mockMvc.perform(put("/note/publish/" + note.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (noteRepository.findById(note.getId()).get().getPublishDate() == null) {
            throw new Exception("Note Test : note publishDate dose not change");
        }
    }

    @Test
    public void testSubmitRewardNote() throws Exception {
        AppUser appUser = userRepository.findByEmail("user1@gmail.com");
        Post post = createRewardPost();
        Note rewardNote = createRewardNote(appUser.getEmail(), appUser.getName());
        rewardNote.setPostID(post.getId());
        noteRepository.save(rewardNote);
        Folder tempRewardNote = folderRepository.findById(appUser.getFolders().get(4)).get();
        tempRewardNote.getNotes().add(rewardNote.getId());
        folderRepository.save(tempRewardNote);

        mockMvc.perform(put("/note/submit/" + rewardNote.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));


        //check post answer add noteID
        if (!postRepository.findById(post.getId()).get().getAnswers().contains(rewardNote.getId())) {
            throw new Exception("Note Test : post answer does not add reward noteID");
        }
    }

    @Test
    public void testWithdrawRewardNote() throws Exception {
        AppUser appUser = userRepository.findByEmail("user1@gmail.com");
        Post post = createRewardPost();
        Note rewardNote = createRewardNote(appUser.getEmail(), appUser.getName());
        rewardNote.setPostID(post.getId());
        rewardNote.setSubmit(true);
        noteRepository.save(rewardNote);
        post.getAnswers().add(rewardNote.getId());
        postRepository.save(post);
        Folder tempRewardNote = folderRepository.findById(appUser.getFolders().get(4)).get();
        tempRewardNote.getNotes().add(rewardNote.getId());
        folderRepository.save(tempRewardNote);

        mockMvc.perform(put("/note/withdraw/" + rewardNote.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));


        //check post answer remove noteID
        if (postRepository.findById(post.getId()).get().getAnswers().contains(rewardNote.getId())) {
            throw new Exception("Note Test : post answer does not remove reward noteID");
        }
    }

//    @Test
    public void testRemoveLastNoteFromFolder() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Note wantToDeleteNote = createNormalNote();
        Folder OSFolder = folderRepository.findById(appUser.getFolders().get(3)).get();
        OSFolder.getNotes().add(wantToDeleteNote.getId());
        folderRepository.save(OSFolder);

        mockMvc.perform(put("/note/delete/" + wantToDeleteNote.getId() + "/" + OSFolder.getId())
                        .headers(httpHeaders))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.res").value("Can't delete last note"));
    }

    @Test
    public void testRemoveNoteFromBuyFolder() throws Exception {
        AppUser appUser = userRepository.findByEmail("user1@gmail.com");
        Note wantToDeleteNote = createNormalNote();
        Folder buyFolder = folderRepository.findById(appUser.getFolders().get(0)).get();
        buyFolder.getNotes().add(wantToDeleteNote.getId());
        folderRepository.save(buyFolder);

        mockMvc.perform(put("/note/delete/" + wantToDeleteNote.getId() + "/" + buyFolder.getId())
                        .headers(httpHeaders))
                .andExpect(status().is(406))
                .andExpect(jsonPath("$.res").value("Can't delete note which in Buy Folder"));
    }

    @Test
    public void testRemoveNoteFromFolder() throws Exception {
        AppUser appUser = userRepository.findByEmail("user1@gmail.com");
        Note wantToDeleteNote = createNormalNote();
        Folder buyFolder = folderRepository.findById(appUser.getFolders().get(0)).get();
        buyFolder.getNotes().add(wantToDeleteNote.getId());
        Folder OSFolder = folderRepository.findById(appUser.getFolders().get(3)).get();
        OSFolder.getNotes().add(wantToDeleteNote.getId());
        folderRepository.save(buyFolder);
        folderRepository.save(OSFolder);

        mockMvc.perform(put("/note/delete/" + wantToDeleteNote.getId() + "/" + OSFolder.getId())
                        .headers(httpHeaders))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.res.id").value(OSFolder.getId()))
                .andExpect(jsonPath("$.res.notes").isEmpty());

        if (folderRepository.findById(OSFolder.getId()).get().getNotes().contains(wantToDeleteNote.getId())) {
            throw new Exception("Note Test : note does not remove from folder");
        }
        if (!folderRepository.findById(buyFolder.getId()).get().getNotes().contains(wantToDeleteNote.getId())) {
            throw new Exception("Note Test : note is removed from buy folder");
        }
    }

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
        noteRepository.deleteAll();
        folderRepository.deleteAll();
        postRepository.deleteAll();
    }
}
