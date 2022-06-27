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
        ArrayList<String> email = new ArrayList<>();
        email.add("yitingwu.1030@gmail.com");
        post.setEmail(email);
        post.setTitle("Java Array");
        post.setDepartment("CS");
        post.setSubject("Java");
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
        post.setBestPrice(10);
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
        ArrayList<String> email = new ArrayList<String>();
        email.add("yitingwu.1030@gmail.com");
        post.setEmail(email);
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
        post.setDepartment("Computer Science");
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

        mockMvc.perform(get("/post/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email").value(post.getEmail()))
                .andExpect(jsonPath("$.res.author").value(post.getAuthor()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.school").value(post.getSchool()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.bestPrice").value(post.getBestPrice()))
                .andExpect(jsonPath("$.res.public").value(post.getPublic()))
                .andExpect(jsonPath("$.res.comments.[0].id").value(post.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res.comments.[0].author").value(post.getComments().get(0).getAuthor()))
                .andExpect(jsonPath("$.res.comments.[0].email").value(post.getComments().get(0).getEmail()))
                .andExpect(jsonPath("$.res.comments.[0].content").value(post.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res.comments.[0].likeCount").value(post.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res.comments.[0].floor").value(post.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res.comments.[0].liker").value(post.getComments().get(0).getLiker()))
                .andExpect(jsonPath("$.res.comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[0].picURL").value(post.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res.comments.[0].best").value(post.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res.comments.[1].id").value(post.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res.comments.[1].author").value(post.getComments().get(1).getAuthor()))
                .andExpect(jsonPath("$.res.comments.[1].email").value(post.getComments().get(1).getEmail()))
                .andExpect(jsonPath("$.res.comments.[1].content").value(post.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res.comments.[1].likeCount").value(post.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res.comments.[1].floor").value(post.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res.comments.[1].liker").value(post.getComments().get(1).getLiker()))
                .andExpect(jsonPath("$.res.comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[1].picURL").value(post.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res.comments.[1].best").value(post.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res.commentCount").value(post.getCommentCount()));
    }

    @Test
    public void testGetRewardPost() throws Exception {
        Post post = createRewardPost();

        mockMvc.perform(get("/post/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email").value(post.getEmail()))
                .andExpect(jsonPath("$.res.author").value(post.getAuthor()))
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
                .andExpect(jsonPath("$.res.public").value(post.getPublic()));
    }

    @Test
    public void testGetCollaborationPost() throws Exception {
        Post post = createCollaborationPost();

        mockMvc.perform(get("/post/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email").value(post.getEmail()))
                .andExpect(jsonPath("$.res.author").value(post.getAuthor()))
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
                .andExpect(jsonPath("$.res.public").value(post.getPublic()));
    }

    @Test
    public void testGetAllTypesOfPost() throws Exception {
        Post post1 = createQAPost();
        Post post2 = createQAPost();
        Post post3 = createQAPost();
        mockMvc.perform(get("/post/postType/QA")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res[0].id").value(post1.getId()))
                .andExpect(jsonPath("$.res[0].type").value(post1.getType()))
                .andExpect(jsonPath("$.res[0].email").value(post1.getEmail()))
                .andExpect(jsonPath("$.res[0].author").value(post1.getAuthor()))
                .andExpect(jsonPath("$.res[0].department").value(post1.getDepartment()))
                .andExpect(jsonPath("$.res[0].subject").value(post1.getSubject()))
                .andExpect(jsonPath("$.res[0].school").value(post1.getSchool()))
                .andExpect(jsonPath("$.res[0].professor").value(post1.getProfessor()))
                .andExpect(jsonPath("$.res[0].title").value(post1.getTitle()))
                .andExpect(jsonPath("$.res[0].content").value(post1.getContent()))
                .andExpect(jsonPath("$.res[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res[0].bestPrice").value(post1.getBestPrice()))
                .andExpect(jsonPath("$.res[0].comments.[0].id").value(post1.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res[0].comments.[0].author").value(post1.getComments().get(0).getAuthor()))
                .andExpect(jsonPath("$.res[0].comments.[0].email").value(post1.getComments().get(0).getEmail()))
                .andExpect(jsonPath("$.res[0].comments.[0].content").value(post1.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res[0].comments.[0].likeCount").value(post1.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res[0].comments.[0].floor").value(post1.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res[0].comments.[0].liker").value(post1.getComments().get(0).getLiker()))
                .andExpect(jsonPath("$.res[0].comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res[0].comments.[0].picURL").value(post1.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res[0].comments.[0].best").value(post1.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res[0].comments.[1].id").value(post1.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res[0].comments.[1].author").value(post1.getComments().get(1).getAuthor()))
                .andExpect(jsonPath("$.res[0].comments.[1].email").value(post1.getComments().get(1).getEmail()))
                .andExpect(jsonPath("$.res[0].comments.[1].content").value(post1.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res[0].comments.[1].likeCount").value(post1.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res[0].comments.[1].floor").value(post1.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res[0].comments.[1].liker").value(post1.getComments().get(1).getLiker()))
                .andExpect(jsonPath("$.res[0].comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res[0].comments.[1].picURL").value(post1.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res[0].comments.[1].best").value(post1.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res[0].commentCount").value(post1.getCommentCount()))
                .andExpect(jsonPath("$.res[0].public").value(post1.getPublic()))
                .andExpect(jsonPath("$.res[1].id").value(post2.getId()))
                .andExpect(jsonPath("$.res[1].type").value(post2.getType()))
                .andExpect(jsonPath("$.res[1].email").value(post2.getEmail()))
                .andExpect(jsonPath("$.res[1].author").value(post2.getAuthor()))
                .andExpect(jsonPath("$.res[1].department").value(post2.getDepartment()))
                .andExpect(jsonPath("$.res[1].subject").value(post2.getSubject()))
                .andExpect(jsonPath("$.res[1].school").value(post2.getSchool()))
                .andExpect(jsonPath("$.res[1].professor").value(post2.getProfessor()))
                .andExpect(jsonPath("$.res[1].title").value(post2.getTitle()))
                .andExpect(jsonPath("$.res[1].content").value(post2.getContent()))
                .andExpect(jsonPath("$.res[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res[1].bestPrice").value(post2.getBestPrice()))
                .andExpect(jsonPath("$.res[1].comments.[0].id").value(post2.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res[1].comments.[0].author").value(post2.getComments().get(0).getAuthor()))
                .andExpect(jsonPath("$.res[1].comments.[0].email").value(post2.getComments().get(0).getEmail()))
                .andExpect(jsonPath("$.res[1].comments.[0].content").value(post2.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res[1].comments.[0].likeCount").value(post2.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res[1].comments.[0].floor").value(post2.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res[1].comments.[0].liker").value(post2.getComments().get(0).getLiker()))
                .andExpect(jsonPath("$.res[1].comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res[1].comments.[0].picURL").value(post2.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res[1].comments.[0].best").value(post2.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res[1].comments.[1].id").value(post2.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res[1].comments.[1].author").value(post2.getComments().get(1).getAuthor()))
                .andExpect(jsonPath("$.res[1].comments.[1].email").value(post2.getComments().get(1).getEmail()))
                .andExpect(jsonPath("$.res[1].comments.[1].content").value(post2.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res[1].comments.[1].likeCount").value(post2.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res[1].comments.[1].floor").value(post2.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res[1].comments.[1].liker").value(post2.getComments().get(1).getLiker()))
                .andExpect(jsonPath("$.res[1].comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res[1].comments.[1].picURL").value(post2.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res[1].comments.[1].best").value(post2.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res[1].commentCount").value(post2.getCommentCount()))
                .andExpect(jsonPath("$.res[1].public").value(post2.getPublic()))
                .andExpect(jsonPath("$.res[2].id").value(post3.getId()))
                .andExpect(jsonPath("$.res[2].type").value(post3.getType()))
                .andExpect(jsonPath("$.res[2].email").value(post3.getEmail()))
                .andExpect(jsonPath("$.res[2].author").value(post3.getAuthor()))
                .andExpect(jsonPath("$.res[2].department").value(post3.getDepartment()))
                .andExpect(jsonPath("$.res[2].subject").value(post3.getSubject()))
                .andExpect(jsonPath("$.res[2].school").value(post3.getSchool()))
                .andExpect(jsonPath("$.res[2].professor").value(post3.getProfessor()))
                .andExpect(jsonPath("$.res[2].title").value(post3.getTitle()))
                .andExpect(jsonPath("$.res[2].content").value(post3.getContent()))
                .andExpect(jsonPath("$.res[2].date").hasJsonPath())
                .andExpect(jsonPath("$.res[2].bestPrice").value(post3.getBestPrice()))
                .andExpect(jsonPath("$.res[2].comments.[0].id").value(post3.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res[2].comments.[0].author").value(post3.getComments().get(0).getAuthor()))
                .andExpect(jsonPath("$.res[2].comments.[0].email").value(post3.getComments().get(0).getEmail()))
                .andExpect(jsonPath("$.res[2].comments.[0].content").value(post3.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res[2].comments.[0].likeCount").value(post3.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res[2].comments.[0].floor").value(post3.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res[2].comments.[0].liker").value(post3.getComments().get(0).getLiker()))
                .andExpect(jsonPath("$.res[2].comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res[2].comments.[0].picURL").value(post3.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res[2].comments.[0].best").value(post3.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res[2].comments.[1].id").value(post3.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res[2].comments.[1].author").value(post3.getComments().get(1).getAuthor()))
                .andExpect(jsonPath("$.res[2].comments.[1].email").value(post3.getComments().get(1).getEmail()))
                .andExpect(jsonPath("$.res[2].comments.[1].content").value(post3.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res[2].comments.[1].likeCount").value(post3.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res[2].comments.[1].floor").value(post3.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res[2].comments.[1].liker").value(post3.getComments().get(1).getLiker()))
                .andExpect(jsonPath("$.res[2].comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res[2].comments.[1].picURL").value(post3.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res[2].comments.[1].best").value(post3.getComments().get(1).getBest()))
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
                .andExpect(jsonPath("$.res.email").value(email.get(0)))
                .andExpect(jsonPath("$.res.author").value(request.get("author")))
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
                .andExpect(jsonPath("$.res.commentCount").value(0));
    }

    @Test
    public void testPutPost() throws Exception {
        Post post = createQAPost();

        post.setContent("newContent");
        post.setSubject("Java");
        post.setTitle("TITLE");

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
                .put("department", post.getDepartment())
                .put("subject", post.getSubject())
                .put("professor", post.getProfessor())
                .put("title", post.getTitle())
                .put("content", post.getContent())
                .put("date", post.getDate())
                .put("comments", comments)
                .put("commentCount", post.getCommentCount())
                .put("public", true)
                .put("email", new JSONArray().put("yitingwu.1030@gmail.com"));

        mockMvc.perform(put("/post/" + post.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.author").value(post.getAuthor()))
                .andExpect(jsonPath("$.res.department").value(post.getDepartment()))
                .andExpect(jsonPath("$.res.professor").value(post.getProfessor()))
                .andExpect(jsonPath("$.res.title").value(post.getTitle()))
                .andExpect(jsonPath("$.res.content").value(post.getContent()))
                .andExpect(jsonPath("$.res.subject").value(post.getSubject()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.public").value(true))
                .andExpect(jsonPath("$.res.comments.[0].id").value(post.getComments().get(0).getId()))
                .andExpect(jsonPath("$.res.comments.[0].author").value(post.getComments().get(0).getAuthor()))
                .andExpect(jsonPath("$.res.comments.[0].email").value(post.getComments().get(0).getEmail()))
                .andExpect(jsonPath("$.res.comments.[0].content").value(post.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res.comments.[0].likeCount").value(post.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res.comments.[0].floor").value(post.getComments().get(0).getFloor()))
                .andExpect(jsonPath("$.res.comments.[0].liker").value(post.getComments().get(0).getLiker()))
                .andExpect(jsonPath("$.res.comments.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[0].picURL").value(post.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res.comments.[0].best").value(post.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res.comments.[1].id").value(post.getComments().get(1).getId()))
                .andExpect(jsonPath("$.res.comments.[1].author").value(post.getComments().get(1).getAuthor()))
                .andExpect(jsonPath("$.res.comments.[1].email").value(post.getComments().get(1).getEmail()))
                .andExpect(jsonPath("$.res.comments.[1].content").value(post.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res.comments.[1].likeCount").value(post.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res.comments.[1].floor").value(post.getComments().get(1).getFloor()))
                .andExpect(jsonPath("$.res.comments.[1].liker").value(post.getComments().get(1).getLiker()))
                .andExpect(jsonPath("$.res.comments.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.res.comments.[1].picURL").value(post.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res.comments.[1].best").value(post.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res.commentCount").value(post.getCommentCount()));
    }

    @Test
    public void testApplyCollaboration() throws Exception {
        AppUser applicant = createUser("user@email.com", "user");
        userRepository.insert(applicant);
        Post post = createCollaborationPost();


        JSONObject request = new JSONObject();
//      "wantEnterUsersEmail": "genewang7@gmail.com",
//      "commentFromApplicant": "我想加入口以嗎？二"
        request.put("wantEnterUsersEmail",applicant.getEmail());
        request.put("commentFromApplicant","我是留言");

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
        mockMvc.perform(put("/post/vote/" + post.getId() + "/" + vote.getId() + "/" + appUser.getEmail())
                        .headers(httpHeaders)
                        .content("agree"))
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
        if(!userRepository.findById(contributor.getId()).get().getCoin().equals(contributor.getCoin()+post.getBestPrice())){
            throw new Exception("Post Test : best answer author's coin does not get");
        }
        if(!userRepository.findById(postAuthor.getId()).get().getCoin().equals(postAuthor.getCoin()-post.getBestPrice())){
            throw new Exception("Post Test : post author's coin does not reduce");
        }
    }

    @Test
    public void testRewardChooseReferenceAnswer() throws Exception{
        AppUser contributor = userRepository.findByEmail("user2@gmail.com");
        Post post = createRewardPost();
        String answerID = post.getAnswers().get(1);
        AppUser postAuthor = userRepository.findByEmail(post.getAuthor());
        mockMvc.perform(put("/post/reward/reference/"+post.getId()+"/"+answerID)
                        .content(postAuthor.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if(!noteRepository.findById(answerID).get().getReference().equals(true)){
            throw new Exception("Post Test : note isBest does not change to true");
        }
        if(!userRepository.findById(contributor.getId()).get().getCoin().equals(contributor.getCoin()+post.getReferencePrice())){
            throw new Exception("Post Test : best answer author's coin does not get");
        }
        if(!userRepository.findById(postAuthor.getId()).get().getCoin().equals(postAuthor.getCoin()-post.getReferencePrice())){
            throw new Exception("Post Test : post author's coin does not reduce");
        }
        if(!postRepository.findById(post.getId()).get().getReferenceNumber().equals(post.getReferenceNumber()-1)){
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

//TODO:若為QA post 檢查要看comment 而非answer ， QA post之answer會為null
    //TODO:service function 應判斷是否選解 不是有沒有人回答
//    @Test
//    public void testModifyPublishStatusBeforeChooseBestAnswer() throws Exception {
//        Post post = createRewardPost();
//
//        mockMvc.perform(put("/post/publish/" + post.getId())
//                        .headers(httpHeaders))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.msg").value("can't change publish state before you got best answer."));
//
//        if(postRepository.findById(post.getId()).get().getPublic().equals(false)){
//            throw new Exception("Post Test : post's public should be true");
//        }
//    }


    @Test
    public void testModifyPublishStatusAfterChooseBestAnswer() throws Exception {
        Post post = createRewardPost();
        Note answerNote = noteRepository.findById(post.getAnswers().get(0)).get();
        answerNote.setBest(true);
        noteRepository.save(answerNote);

        mockMvc.perform(put("/post/publish/" + post.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getId()))
                .andExpect(jsonPath("$.res.type").value(post.getType()))
                .andExpect(jsonPath("$.res.email").value(post.getEmail()))
                .andExpect(jsonPath("$.res.author").value(post.getAuthor()))
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
                .andExpect(jsonPath("$.res.public").value(false));
        if (postRepository.findById(post.getId()).get().getPublic().equals(true)) {
            throw new Exception("Post Test: post is still public");
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
