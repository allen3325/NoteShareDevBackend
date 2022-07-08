package ntou.notesharedevbackend.postTest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.noteNodule.entity.Content;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.entity.VersionContent;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.postModule.service.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.checkerframework.checker.units.qual.A;
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
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
        Folder tempRewardNote = createFolder("Temp Reward Note", "/TempRewardNote", null, name);
        ArrayList<String> folderList = new ArrayList<>();
        folderList.add(buyFolder.getId());
        folderList.add(favoriteFolder.getId());
        folderList.add(collaborationFolder.getId());
        folderList.add(OSFolder.getId());
        folderList.add(tempRewardNote.getId());
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
        post.setTitle("Java Array");
        post.setDepartment("CS");
        post.setSubject("Java");
        post.setSchool("NTOU");
        post.setProfessor("Shang-Pin Ma");
        post.setContent("ArrayList 跟 List 一樣嗎");
        post.setDate();
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
        post.setBestPrice(10);
        post.setAnswers(new ArrayList<>());
        post.setArchive(false);
        post.setApplyEmail(new ArrayList<>());

        return postRepository.insert(post);
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
        Note note = createRewardNote("user1@gmail.com", "User1");
        Note note1 = createRewardNote("user2@gmail.com", "User2");
        answers.add(note.getId());
        answers.add(note1.getId());
        post.setAnswers(answers);
        post.setArchive(false);
        post.setApplyEmail(new ArrayList<>());
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
//        authorEmails.add("user1@gmail.com");
//        authorEmails.add("user2@gmail.com");
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
        post.setDepartment("Computer Science");
        post.setSubject("Operation System");
        post.setSchool("NTOU");
        post.setProfessor("professor");
        post.setTitle("Interrupt vs trap");
        post.setContent("this is a post!");
        post.setDate();
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
        post.setArchive(false);
        post.setApplyEmail(new ArrayList<>());
        post = postRepository.insert(post);
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
    public void testGetQAPost() throws Exception {
        Post post = createQAPost();
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));
        AppUser commentAuthor = userRepository.findByEmail(post.getComments().get(0).getEmail());
        AppUser commentAuthor1 = userRepository.findByEmail(post.getComments().get(1).getEmail());

        mockMvc.perform(get("/post/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.public").value(post.getPublic()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[0].id").value(post.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res.comments.[0].likeCount").value(0))
                .andExpect(jsonPath("$.res.comments.[0].floor").value(post.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res.comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[0].best").value(post.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res.comments.[0].content").value(post.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res.comments.[0].picURL").isEmpty())
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjEmail").value(commentAuthor.getEmail()))
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjName").value(commentAuthor.getName()))
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjAvatar").value(commentAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[0].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.comments.[1].id").value(post.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res.comments.[1].likeCount").value(0))
                .andExpect(jsonPath("$.res.comments.[1].floor").value(post.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res.comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[1].best").value(post.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res.comments.[1].content").value(post.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res.comments.[1].picURL").isEmpty())
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjEmail").value(commentAuthor1.getEmail()))
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjName").value(commentAuthor1.getName()))
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjAvatar").value(commentAuthor1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[1].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.commentCount").value(post.getCommentCount()));
    }

    @Test
    public void testGetRewardPost() throws Exception {
        Post post = createRewardPost();
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));
        mockMvc.perform(get("/post/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.referencePrice").value(post.getReferencePrice()))
                .andExpect(jsonPath("$.res.referenceNumber").value(post.getReferenceNumber()))
                .andExpect(jsonPath("$.res.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.res.public").value(post.getPublic()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()));
    }

    @Test
    public void testGetCollaborationPost() throws Exception {
        Post post = createCollaborationPost();
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));
        mockMvc.perform(get("/post/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.collabNoteAuthorNumber").value(post.getCollabNoteAuthorNumber()))
                .andExpect(jsonPath("$.res.answers").value(post.getAnswers()))
//                .andExpect(jsonPath("$.res.wantEnterUsersEmail").value(post.getWantEnterUsersEmail()))
                .andExpect(jsonPath("$.res.public").value(post.getPublic()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.collabApply").isEmpty())
                .andExpect(jsonPath("$.res.applyEmail").isEmpty())
                .andExpect(jsonPath("$.res.applyUserObj").isEmpty())
        ;
    }

    @Test
    public void testGetAllQAPost() throws Exception {
        Post post1 = createQAPost();
        Post post2 = createQAPost();
        Post post3 = createQAPost();
        mockMvc.perform(get("/post/postType/QA")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res[0].id").value(post1.getId()))
                .andExpect(jsonPath("$.res[0].type").value(post1.getType()))
                .andExpect(jsonPath("$.res[0].email.[0]").value(userRepository.findByEmail(post1.getEmail().get(0)).getEmail()))
                .andExpect(jsonPath("$.res[0].author").value(userRepository.findByEmail(post1.getEmail().get(0)).getEmail()))
                .andExpect(jsonPath("$.res[0].authorName").value(userRepository.findByEmail(post1.getEmail().get(0)).getName()))
                .andExpect(jsonPath("$.res[0].department").value(post1.getDepartment()))
                .andExpect(jsonPath("$.res[0].subject").value(post1.getSubject()))
                .andExpect(jsonPath("$.res[0].school").value(post1.getSchool()))
                .andExpect(jsonPath("$.res[0].professor").value(post1.getProfessor()))
                .andExpect(jsonPath("$.res[0].title").value(post1.getTitle()))
                .andExpect(jsonPath("$.res[0].content").value(post1.getContent()))
                .andExpect(jsonPath("$.res[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res[0].bestPrice").value(post1.getBestPrice()))
                .andExpect(jsonPath("$.res[0].comments.[0].id").value(post1.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res[0].comments.[0].content").value(post1.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res[0].comments.[0].likeCount").value(post1.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res[0].comments.[0].floor").value(post1.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res[0].comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res[0].comments.[0].picURL").value(post1.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res[0].comments.[0].best").value(post1.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res[0].comments.[0].userObj.userObjEmail").value(userRepository.findByEmail(post1.getComments().get(0).getEmail()).getEmail()))
                .andExpect(jsonPath("$.res[0].comments.[0].userObj.userObjName").value(userRepository.findByEmail(post1.getComments().get(0).getEmail()).getName()))
                .andExpect(jsonPath("$.res[0].comments.[0].userObj.userObjAvatar").value(userRepository.findByEmail(post1.getComments().get(0).getEmail()).getHeadshotPhoto()))
                .andExpect(jsonPath("$.res[0].comments.[0].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res[0].comments.[1].id").value(post1.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res[0].comments.[1].content").value(post1.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res[0].comments.[1].likeCount").value(post1.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res[0].comments.[1].floor").value(post1.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res[0].comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res[0].comments.[1].picURL").value(post1.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res[0].comments.[1].best").value(post1.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res[0].comments.[1].userObj.userObjEmail").value(userRepository.findByEmail(post1.getComments().get(1).getEmail()).getEmail()))
                .andExpect(jsonPath("$.res[0].comments.[1].userObj.userObjName").value(userRepository.findByEmail(post1.getComments().get(1).getEmail()).getName()))
                .andExpect(jsonPath("$.res[0].comments.[1].userObj.userObjAvatar").value(userRepository.findByEmail(post1.getComments().get(1).getEmail()).getHeadshotPhoto()))
                .andExpect(jsonPath("$.res[0].comments.[1].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res[0].commentCount").value(post1.getCommentCount()))
                .andExpect(jsonPath("$.res[0].public").value(post1.getPublic()))
                .andExpect(jsonPath("$.res[1].id").value(post2.getId()))
                .andExpect(jsonPath("$.res[1].type").value(post2.getType()))
                .andExpect(jsonPath("$.res[1].email.[0]").value(userRepository.findByEmail(post2.getEmail().get(0)).getEmail()))
                .andExpect(jsonPath("$.res[1].author").value(userRepository.findByEmail(post2.getEmail().get(0)).getEmail()))
                .andExpect(jsonPath("$.res[1].authorName").value(userRepository.findByEmail(post2.getEmail().get(0)).getName()))
                .andExpect(jsonPath("$.res[1].department").value(post2.getDepartment()))
                .andExpect(jsonPath("$.res[1].subject").value(post2.getSubject()))
                .andExpect(jsonPath("$.res[1].school").value(post2.getSchool()))
                .andExpect(jsonPath("$.res[1].professor").value(post2.getProfessor()))
                .andExpect(jsonPath("$.res[1].title").value(post2.getTitle()))
                .andExpect(jsonPath("$.res[1].content").value(post2.getContent()))
                .andExpect(jsonPath("$.res[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res[1].bestPrice").value(post2.getBestPrice()))
                .andExpect(jsonPath("$.res[1].comments.[0].id").value(post2.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res[1].comments.[0].content").value(post2.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res[1].comments.[0].likeCount").value(post2.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res[1].comments.[0].floor").value(post2.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res[1].comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res[1].comments.[0].picURL").value(post2.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res[1].comments.[0].best").value(post2.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res[1].comments.[0].userObj.userObjEmail").value(userRepository.findByEmail(post2.getComments().get(0).getEmail()).getEmail()))
                .andExpect(jsonPath("$.res[1].comments.[0].userObj.userObjName").value(userRepository.findByEmail(post2.getComments().get(0).getEmail()).getName()))
                .andExpect(jsonPath("$.res[1].comments.[0].userObj.userObjAvatar").value(userRepository.findByEmail(post2.getComments().get(0).getEmail()).getHeadshotPhoto()))
                .andExpect(jsonPath("$.res[1].comments.[0].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res[1].comments.[1].id").value(post2.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res[1].comments.[1].content").value(post2.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res[1].comments.[1].likeCount").value(post2.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res[1].comments.[1].floor").value(post2.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res[1].comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res[1].comments.[1].picURL").value(post2.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res[1].comments.[1].best").value(post2.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res[1].comments.[1].userObj.userObjEmail").value(userRepository.findByEmail(post2.getComments().get(1).getEmail()).getEmail()))
                .andExpect(jsonPath("$.res[1].comments.[1].userObj.userObjName").value(userRepository.findByEmail(post2.getComments().get(1).getEmail()).getName()))
                .andExpect(jsonPath("$.res[1].comments.[1].userObj.userObjAvatar").value(userRepository.findByEmail(post2.getComments().get(1).getEmail()).getHeadshotPhoto()))
                .andExpect(jsonPath("$.res[1].comments.[1].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res[1].commentCount").value(post2.getCommentCount()))
                .andExpect(jsonPath("$.res[1].public").value(post2.getPublic()))
                .andExpect(jsonPath("$.res[2].id").value(post3.getId()))
                .andExpect(jsonPath("$.res[2].type").value(post3.getType()))
                .andExpect(jsonPath("$.res[2].email.[0]").value(userRepository.findByEmail(post3.getEmail().get(0)).getEmail()))
                .andExpect(jsonPath("$.res[2].author").value(userRepository.findByEmail(post3.getEmail().get(0)).getEmail()))
                .andExpect(jsonPath("$.res[2].authorName").value(userRepository.findByEmail(post3.getEmail().get(0)).getName()))
                .andExpect(jsonPath("$.res[2].department").value(post3.getDepartment()))
                .andExpect(jsonPath("$.res[2].subject").value(post3.getSubject()))
                .andExpect(jsonPath("$.res[2].school").value(post3.getSchool()))
                .andExpect(jsonPath("$.res[2].professor").value(post3.getProfessor()))
                .andExpect(jsonPath("$.res[2].title").value(post3.getTitle()))
                .andExpect(jsonPath("$.res[2].content").value(post3.getContent()))
                .andExpect(jsonPath("$.res[2].date").hasJsonPath())
                .andExpect(jsonPath("$.res[2].bestPrice").value(post3.getBestPrice()))
                .andExpect(jsonPath("$.res[2].comments.[0].id").value(post3.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res[2].comments.[0].content").value(post3.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res[2].comments.[0].likeCount").value(post3.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res[2].comments.[0].floor").value(post3.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res[2].comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res[2].comments.[0].picURL").value(post3.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res[2].comments.[0].best").value(post3.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res[2].comments.[0].userObj.userObjEmail").value(userRepository.findByEmail(post3.getComments().get(0).getEmail()).getEmail()))
                .andExpect(jsonPath("$.res[2].comments.[0].userObj.userObjName").value(userRepository.findByEmail(post3.getComments().get(0).getEmail()).getName()))
                .andExpect(jsonPath("$.res[2].comments.[0].userObj.userObjAvatar").value(userRepository.findByEmail(post3.getComments().get(0).getEmail()).getHeadshotPhoto()))
                .andExpect(jsonPath("$.res[2].comments.[0].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res[2].comments.[1].id").value(post3.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res[2].comments.[1].content").value(post3.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res[2].comments.[1].likeCount").value(post3.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res[2].comments.[1].floor").value(post3.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res[2].comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res[2].comments.[1].picURL").value(post3.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res[2].comments.[1].best").value(post3.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res[2].comments.[1].userObj.userObjEmail").value(userRepository.findByEmail(post3.getComments().get(1).getEmail()).getEmail()))
                .andExpect(jsonPath("$.res[2].comments.[1].userObj.userObjName").value(userRepository.findByEmail(post3.getComments().get(1).getEmail()).getName()))
                .andExpect(jsonPath("$.res[2].comments.[1].userObj.userObjAvatar").value(userRepository.findByEmail(post3.getComments().get(1).getEmail()).getHeadshotPhoto()))
                .andExpect(jsonPath("$.res[2].comments.[1].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res[2].commentCount").value(post3.getCommentCount()))
                .andExpect(jsonPath("$.res[2].public").value(post3.getPublic()));

    }

    @Test
    public void testCreatePost() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        JSONArray email = new JSONArray()
                .put("yitingwu.1030@gmail.com");
        JSONObject request = new JSONObject()
                .put("type", "QA")
                .put("email", email)
                .put("author", "yitingwu.1030@gmail.com")
                .put("department", "CS")
                .put("subject", "Java")
                .put("school", "NTOU")
                .put("professor", "NoteShare")
                .put("title", "Java Array")
                .put("content", "Content")
                .put("bestPrice", 5)
                .put("public", true);

        mockMvc.perform(post("/post/" + appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.res.id").hasJsonPath())
                .andExpect(jsonPath("$.res.type").value(request.get("type")))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(request.get("department")))
                .andExpect(jsonPath("$.res.subject").value(request.get("subject")))
                .andExpect(jsonPath("$.res.school").value(request.get("school")))
                .andExpect(jsonPath("$.res.professor").value(request.get("professor")))
                .andExpect(jsonPath("$.res.title").value(request.get("title")))
                .andExpect(jsonPath("$.res.content").value(request.get("content")))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(request.get("bestPrice")))
                .andExpect(jsonPath("$.res.public").value(request.get("public")))
                .andExpect(jsonPath("$.res.comments").isEmpty())
                .andExpect(jsonPath("$.res.commentCount").value(0))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()));
    }

    @Test
    public void testPutPost() throws Exception {
        Post post = createQAPost();
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));
        AppUser commentAuthor = userRepository.findByEmail(post.getComments().get(0).getEmail());
        AppUser commentAuthor1 = userRepository.findByEmail(post.getComments().get(1).getEmail());
        post.setContent("newContent");
        post.setSubject("Java");
        post.setTitle("TITLE");

        JSONArray applyEmails = new JSONArray();
        JSONArray comments = new JSONArray();
        for (Comment c : post.getComments()) {
            JSONArray likers = new JSONArray();
            for (String s : c.getLiker()) {
                likers.put(s);
            }
            JSONArray picURLs = new JSONArray();
            for (String s : c.getPicURL()) {
                picURLs.put(s);
            }
            JSONObject comment = new JSONObject()
                    .put("id", c.getId())
                    .put("author", c.getAuthor())
                    .put("email", c.getEmail())
                    .put("likeCount", c.getLikeCount())
                    .put("liker", likers)
                    .put("floor", c.getFloor())
                    .put("date", c.getDate())
                    .put("picURL", picURLs)
                    .put("best", c.getBest())
                    .put("content", c.getContent());
            comments.put(comment);
        }


        JSONObject request = new JSONObject()
                .put("id", post.getId())
                .put("type", post.getType())
                .put("author", post.getAuthor())
                .put("authorName",appUser.getName())
                .put("department", post.getDepartment())
                .put("subject", post.getSubject())
                .put("professor", post.getProfessor())
                .put("title", post.getTitle())
                .put("content", post.getContent())
                .put("comments", comments)
                .put("commentCount", post.getCommentCount())
                .put("public", true)
                .put("email", new JSONArray().put("yitingwu.1030@gmail.com"))
                .put("applyEmail", applyEmails);

        mockMvc.perform(put("/post/" + post.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.public").value(true))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[0].id").value(post.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res.comments.[0].likeCount").value(0))
                .andExpect(jsonPath("$.res.comments.[0].floor").value(post.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res.comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[0].best").value(post.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res.comments.[0].content").value(post.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res.comments.[0].picURL").isEmpty())
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjEmail").value(commentAuthor.getEmail()))
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjName").value(commentAuthor.getName()))
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjAvatar").value(commentAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[0].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.comments.[1].id").value(post.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res.comments.[1].likeCount").value(0))
                .andExpect(jsonPath("$.res.comments.[1].floor").value(post.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res.comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[1].best").value(post.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res.comments.[1].content").value(post.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res.comments.[1].picURL").isEmpty())
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjEmail").value(commentAuthor1.getEmail()))
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjName").value(commentAuthor1.getName()))
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjAvatar").value(commentAuthor1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[1].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.commentCount").value(post.getCommentCount()));
    }

    @Test
    public void testApplyCollaboration() throws Exception {
        AppUser applicant = createUser("user@email.com", "user");
        userRepository.insert(applicant);
        Post post = createCollaborationPost();


        JSONObject request = new JSONObject();
        request.put("wantEnterUsersEmail", applicant.getEmail());
        request.put("commentFromApplicant", "我是留言");

        mockMvc.perform(put("/post/apply/" + post.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
//        if(!postRepository.findById(post.getId()).get().getWantEnterUsersEmail().contains(applicant.getEmail())){
//            throw new Exception("Post Test : applicant does not enter want enter list");
//        }
    }

    @Test
    public void testApproveCollaboration() throws Exception {
        AppUser applicant = createUser("user@email.com", "user");
        userRepository.insert(applicant);
        Post post = createCollaborationPost();
        post.getCollabApply().add(new Apply(applicant.getEmail(), "comment"));
        post.setComments(new ArrayList<>());
        postRepository.save(post);

        mockMvc.perform(put("/post/add/" + post.getId() + "/" + applicant.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
//        if(postRepository.findById(post.getId()).get().getWantEnterUsersEmail().contains(applicant.getEmail())){
//            throw new Exception("Post Test : applicant still in want enter list.");
//        }
        if (!postRepository.findById(post.getId()).get().getCollabNoteAuthorNumber().equals(post.getCollabNoteAuthorNumber() + 1)) {
            throw new Exception("Post Test : collabNoteAuthorNumber does not plus one");
        }

        if (!noteRepository.findById(post.getAnswers().get(0)).get().getAuthorEmail().contains(applicant.getEmail())) {
            throw new Exception("Post Test : applicant email does not enter note author email");
        }
        if (!noteRepository.findById(post.getAnswers().get(0)).get().getAuthorName().contains(applicant.getName())) {
            throw new Exception("Post Test : applicant email does not enter note author email");
        }


    }

    @Test
    public void testVoteCollaborationVote() throws Exception {
        Post post = createCollaborationPost();
        Vote vote = new Vote();
        ArrayList<Vote> voteArrayList = new ArrayList<>();
        voteArrayList.add(vote);
        post.setVote(voteArrayList);
        postRepository.save(post);
        AppUser appUser = userRepository.findByEmail("user1@gmail.com");
        JSONObject request = new JSONObject()
                .put("option", "agree");
        mockMvc.perform(put("/post/vote/" + post.getId() + "/" + vote.getId() + "/" + appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        post = postRepository.findById(post.getId()).get();
        if (!post.getVote().get(0).getAgree().contains(appUser.getEmail())) {
            throw new Exception("Post Test : voter does not enter queue");
        }
    }

    @Test
    public void testRewardChooseBestAnswer() throws Exception {
        AppUser contributor = userRepository.findByEmail("user2@gmail.com");
        Post post = createRewardPost();
        String answerID = post.getAnswers().get(1);
        AppUser postAuthor = userRepository.findByEmail(post.getAuthor());
        mockMvc.perform(put("/post/reward/best/" + post.getId() + "/" + answerID)
                        .content(postAuthor.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (!noteRepository.findById(answerID).get().getBest().equals(true)) {
            throw new Exception("Post Test : note isBest does not change to true");
        }
        if (!noteRepository.findById(answerID).get().getContributors().contains(contributor.getEmail())) {
            throw new Exception("Post Test : note's contributor does not add contributor");
        }
        if (!userRepository.findById(contributor.getId()).get().getCoin().equals(contributor.getCoin() + post.getBestPrice())) {
            throw new Exception("Post Test : best answer author's coin does not get");
        }
        if (!userRepository.findById(postAuthor.getId()).get().getCoin().equals(postAuthor.getCoin() - post.getBestPrice())) {
            throw new Exception("Post Test : post author's coin does not reduce");
        }
    }

    @Test
    public void testRewardChooseReferenceAnswer() throws Exception {
        AppUser contributor = userRepository.findByEmail("user2@gmail.com");
        Post post = createRewardPost();
        String answerID = post.getAnswers().get(1);
        AppUser postAuthor = userRepository.findByEmail(post.getAuthor());
        mockMvc.perform(put("/post/reward/reference/" + post.getId() + "/" + answerID)
                        .content(postAuthor.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (!noteRepository.findById(answerID).get().getReference().equals(true)) {
            throw new Exception("Post Test : note isBest does not change to true");
        }
        if (!userRepository.findById(contributor.getId()).get().getCoin().equals(contributor.getCoin() + post.getReferencePrice())) {
            throw new Exception("Post Test : best answer author's coin does not get");
        }
        if (!userRepository.findById(postAuthor.getId()).get().getCoin().equals(postAuthor.getCoin() - post.getReferencePrice())) {
            throw new Exception("Post Test : post author's coin does not reduce");
        }
        if (!postRepository.findById(post.getId()).get().getReferenceNumber().equals(post.getReferenceNumber() - 1)) {
            throw new Exception("Post Test : post reference number does not reduce");
        }
    }

    @Test
    public void testQAChooseBestAnswer() throws Exception {
        Post post = createQAPost();
        String bestCommentID = post.getComments().get(1).getId();
        AppUser commentAuthor = userRepository.findByEmail(post.getComments().get(1).getEmail());
        AppUser postAuthor = userRepository.findByEmail(post.getAuthor());
        mockMvc.perform(put("/post/qa/best/" + post.getId() + "/" + bestCommentID)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (!postRepository.findById(post.getId()).get().getComments().get(1).getBest().equals(true)) {
            throw new Exception("Post Test : QA post best comment's isBest does not set true");
        }
        //點數增減
        if (!userRepository.findById(commentAuthor.getId()).get().getCoin().equals(commentAuthor.getCoin() + post.getBestPrice())) {
            throw new Exception("Post Test : best answer author's coin does not get");
        }
        if (!userRepository.findById(postAuthor.getId()).get().getCoin().equals(postAuthor.getCoin() - post.getBestPrice())) {
            throw new Exception("Post Test : post author's coin does not reduce");
        }


    }

    @Test
    public void testDeletePost() throws Exception {
        Post post = createQAPost();

        mockMvc.perform(delete("/post/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.msg").value("Success"));
    }

    @Test
    public void testRewardPostModifyPublishStatusBeforeChooseBestAnswer() throws Exception {
        Post post = createRewardPost();

        mockMvc.perform(put("/post/publish/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("can't change publish state before you got best answer."));

        if (postRepository.findById(post.getId()).get().getPublic().equals(false)) {
            throw new Exception("Post Test : post's public should be true");
        }
    }

    @Test
    public void testQAPostModifyPublishStatusBeforeChooseBestAnswer() throws Exception {
        Post post = createQAPost();

        mockMvc.perform(put("/post/publish/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("can't change publish state before you got best answer."));

        if (postRepository.findById(post.getId()).get().getPublic().equals(false)) {
            throw new Exception("Post Test : post's public should be true");
        }
    }


    @Test
    public void testRewardPostModifyPublishStatusAfterChooseBestAnswer() throws Exception {
        Post post = createRewardPost();
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));
        Note answerNote = noteRepository.findById(post.getAnswers().get(0)).get();
        answerNote.setBest(true);
        noteRepository.save(answerNote);

        mockMvc.perform(put("/post/publish/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.referencePrice").value(post.getReferencePrice()))
                .andExpect(jsonPath("$.res.referenceNumber").value(post.getReferenceNumber()))
                .andExpect(jsonPath("$.res.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.res.public").value(false))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()));
        if (postRepository.findById(post.getId()).get().getPublic().equals(true)) {
            throw new Exception("Post Test: post is still public");
        }
    }

    @Test
    public void testQAPostModifyPublishStatusAfterChooseBestAnswer() throws Exception {
        Post post = createQAPost();
        post.getComments().get(0).setBest(true);
        postRepository.save(post);
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));

        mockMvc.perform(put("/post/publish/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.referencePrice").value(post.getReferencePrice()))
                .andExpect(jsonPath("$.res.referenceNumber").value(post.getReferenceNumber()))
                .andExpect(jsonPath("$.res.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.res.public").value(false))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()));
        if (postRepository.findById(post.getId()).get().getPublic().equals(true)) {
            throw new Exception("Post Test: post is still public");
        }
    }

    @Test
    public void testRewardPostModifyPublishStatusToPublic() throws Exception {
        Post post = createRewardPost();
        post.setPublic(false);
        postRepository.save(post);
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));

        mockMvc.perform(put("/post/publish/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.referencePrice").value(post.getReferencePrice()))
                .andExpect(jsonPath("$.res.referenceNumber").value(post.getReferenceNumber()))
                .andExpect(jsonPath("$.res.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.res.public").value(true))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()));
        if (postRepository.findById(post.getId()).get().getPublic().equals(false)) {
            throw new Exception("Post Test: post is still public");
        }
    }

    @Test
    public void testQAPostModifyPublishStatusToPublic() throws Exception {
        Post post = createQAPost();
        post.setPublic(false);
        postRepository.save(post);
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));
        AppUser commentAuthor = userRepository.findByEmail(post.getComments().get(0).getEmail());
        AppUser commentAuthor1 = userRepository.findByEmail(post.getComments().get(1).getEmail());
        mockMvc.perform(put("/post/publish/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.referencePrice").value(post.getReferencePrice()))
                .andExpect(jsonPath("$.res.referenceNumber").value(post.getReferenceNumber()))
                .andExpect(jsonPath("$.res.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.res.public").value(true))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[0].id").value(post.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res.comments.[0].likeCount").value(0))
                .andExpect(jsonPath("$.res.comments.[0].floor").value(post.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res.comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[0].best").value(post.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res.comments.[0].content").value(post.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res.comments.[0].picURL").isEmpty())
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjEmail").value(commentAuthor.getEmail()))
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjName").value(commentAuthor.getName()))
                .andExpect(jsonPath("$.res.comments.[0].userObj.userObjAvatar").value(commentAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[0].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.comments.[1].id").value(post.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res.comments.[1].likeCount").value(0))
                .andExpect(jsonPath("$.res.comments.[1].floor").value(post.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res.comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[1].best").value(post.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res.comments.[1].content").value(post.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res.comments.[1].picURL").isEmpty())
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjEmail").value(commentAuthor1.getEmail()))
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjName").value(commentAuthor1.getName()))
                .andExpect(jsonPath("$.res.comments.[1].userObj.userObjAvatar").value(commentAuthor1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.comments.[1].likerUserObj").isEmpty())
        ;
        if (postRepository.findById(post.getId()).get().getPublic().equals(false)) {
            throw new Exception("Post Test: post is still public");
        }
    }

    @Test
    public void testCreateRewardNote() throws Exception {
        Post post = createRewardPost();
        AppUser appUser = userRepository.findByEmail("user1@gmail.com");
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
                .put("type", "reward")
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
        mockMvc.perform(post("/post/reward/" + post.getId() + "/" + appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.res.id").exists())
                .andExpect(jsonPath("$.res.type").value(request.get("type")))
                .andExpect(jsonPath("$.res.headerEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorEmail.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName.[0]").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(request.get("department")))
                .andExpect(jsonPath("$.res.subject").value(request.get("subject")))
                .andExpect(jsonPath("$.res.title").value(request.get("title")))
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
                .andExpect(jsonPath("$.res.postID").value(post.getId()))
                .andExpect(jsonPath("$.res.reference").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.best").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.public").value(request.get("public")))
                .andExpect(jsonPath("$.res.submit").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.headerUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.buyerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.favoriterUserObj").isEmpty())
                .andExpect(jsonPath("$.res.contributorUserObj").isEmpty());

        Folder tempRewardNote = folderRepository.findById(appUser.getFolders().get(4)).get();
        if (tempRewardNote.getNotes().isEmpty()) {
            throw new Exception("Post Test : new reward note does enter tempRewardNote Folder");
        }

    }

    @Test
    public void testCollaborationPostModifyPublishStatusToUnPublish() throws Exception {
        Post post = createCollaborationPost();
        AppUser appUser = userRepository.findByEmail(post.getAuthor());

        mockMvc.perform(put("/post/publish/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.referencePrice").value(post.getReferencePrice()))
                .andExpect(jsonPath("$.res.referenceNumber").value(post.getReferenceNumber()))
                .andExpect(jsonPath("$.res.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.res.public").value(false))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()));
        if (postRepository.findById(post.getId()).get().getPublic().equals(post.getPublic())) {
            throw new Exception("Post Test : publish does not update");
        }
    }

    @Test
    public void testCollaborationPostModifyPublishStatusToPublish() throws Exception {
        Post post = createCollaborationPost();
        post.setPublic(false);
        postRepository.save(post);
        AppUser appUser = userRepository.findByEmail(post.getAuthor());

        mockMvc.perform(put("/post/publish/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.author").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.referencePrice").value(post.getReferencePrice()))
                .andExpect(jsonPath("$.res.referenceNumber").value(post.getReferenceNumber()))
                .andExpect(jsonPath("$.res.answers").value(post.getAnswers()))
                .andExpect(jsonPath("$.res.public").value(true))
                .andExpect(jsonPath("$.res.authorUserObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.authorUserObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.emailUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()));
        if (postRepository.findById(post.getId()).get().getPublic().equals(post.getPublic())) {
            throw new Exception("Post Test : publish does not update");
        }
    }

    @Test
    public void testApplyCollaborationPostAgain() throws Exception {
        Post post = createCollaborationPost();
        post.getApplyEmail().add("user1@gmail.com");
        postRepository.save(post);

        JSONObject request = new JSONObject();
        request.put("wantEnterUsersEmail", "user1@gmail.com");
        request.put("commentFromApplicant", "我是留言");

        mockMvc.perform(put("/post/apply/" + post.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("User already apply"));

    }

    @Test
    public void testGetUserAllCollaborationPost() throws Exception {
        AppUser appUser = userRepository.findByEmail("user1@gmail.com");
        Post post = createCollaborationPost();
        AppUser postAuthor = userRepository.findByEmail(post.getAuthor());
        post.getApplyEmail().add(appUser.getEmail());
        post.getEmail().add(appUser.getEmail());
        postRepository.save(post);
        mockMvc.perform(get("/post/" + appUser.getEmail() + "/collaboration")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.[0].id").value(post.getId()))
                .andExpect(jsonPath("$.res.[0].type").value(post.getType()))
                .andExpect(jsonPath("$.res.[0].email.[0]").value(postAuthor.getEmail()))
                .andExpect(jsonPath("$.res.[0].author").value(postAuthor.getEmail()))
                .andExpect(jsonPath("$.res.[0].authorName").value(postAuthor.getName()))
                .andExpect(jsonPath("$.res.[0].department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.[0].subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.[0].school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.[0].professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.[0].title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.[0].content").value(post.getContent()))
                .andExpect(jsonPath("$.res.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res.[0].bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.[0].commentCount").value(post.getCommentCount()))
                .andExpect(jsonPath("$.res.[0].public").value(post.getPublic()))
                .andExpect(jsonPath("$.res.[0].archive").value(post.getArchive()))
                .andExpect(jsonPath("$.res.[0].applyEmail.[0]").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.[0].applyUserObj.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.[0].applyUserObj.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.[0].applyUserObj.[0].userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[0].authorUserObj.userObjEmail").value(postAuthor.getEmail()))
                .andExpect(jsonPath("$.res.[0].authorUserObj.userObjName").value(postAuthor.getName()))
                .andExpect(jsonPath("$.res.[0].authorUserObj.userObjAvatar").value(postAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[0].emailUserObj.[0].userObjEmail").value(postAuthor.getEmail()))
                .andExpect(jsonPath("$.res.[0].emailUserObj.[0].userObjName").value(postAuthor.getName()))
                .andExpect(jsonPath("$.res.[0].emailUserObj.[0].userObjAvatar").value(postAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[0].emailUserObj.[1].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.[0].emailUserObj.[1].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.[0].emailUserObj.[1].userObjAvatar").value(appUser.getHeadshotPhoto()))
        ;
    }

    @Test
    public void testRewardPostModifyArchiveStatusBeforeChooseBestAnswer() throws Exception {
        Post post = createRewardPost();

        mockMvc.perform(put("/post/archive/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res").value("can't change archive state before you got best answer."));

        if (postRepository.findById(post.getId()).get().getArchive().equals(true)) {
            throw new Exception("Post Test : post's archive should be false");
        }
    }

    @Test
    public void testQAPostModifyArchiveStatusBeforeChooseBestAnswer() throws Exception {
        Post post = createQAPost();

        mockMvc.perform(put("/post/archive/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res").value("can't change archive state before you got best answer."));

        if (postRepository.findById(post.getId()).get().getArchive().equals(true)) {
            throw new Exception("Post Test : post's archive should be false");
        }
    }


    @Test
    public void testRewardPostModifyArchiveStatusAfterChooseBestAnswer() throws Exception {
        Post post = createRewardPost();
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));
        Note answerNote = noteRepository.findById(post.getAnswers().get(0)).get();
        answerNote.setBest(true);
        noteRepository.save(answerNote);

        mockMvc.perform(put("/post/archive/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res").value("Success"));
        if (postRepository.findById(post.getId()).get().getArchive().equals(false)) {
            throw new Exception("Post Test: post does not archive");
        }
    }

    @Test
    public void testQAPostModifyArchiveStatusAfterChooseBestAnswer() throws Exception {
        Post post = createQAPost();
        post.getComments().get(0).setBest(true);
        postRepository.save(post);
        AppUser appUser = userRepository.findByEmail(post.getEmail().get(0));

        mockMvc.perform(put("/post/archive/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res").value("Success"));
        if (postRepository.findById(post.getId()).get().getArchive().equals(false)) {
            throw new Exception("Post Test: post does not archive");
        }
    }

    @Test
    public void testCollaborationPostModifyArchiveStatus() throws Exception {
        Post post = createCollaborationPost();
        mockMvc.perform(put("/post/archive/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res").value("Success"));
        if (postRepository.findById(post.getId()).get().getArchive().equals(false)) {
            throw new Exception("Post Test: post does not archive");
        }
    }


    @AfterEach
    public void clear() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        folderRepository.deleteAll();
        noteRepository.deleteAll();
    }
}
