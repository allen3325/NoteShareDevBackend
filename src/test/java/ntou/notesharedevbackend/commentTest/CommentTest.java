package ntou.notesharedevbackend.commentTest;

import ntou.notesharedevbackend.commentModule.entity.Comment;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.noteNodule.entity.Content;
import ntou.notesharedevbackend.noteNodule.entity.Note;
import ntou.notesharedevbackend.noteNodule.entity.VersionContent;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentTest {
    private HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private PostRepository postRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void init() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        folderRepository.deleteAll();
        noteRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        AppUser appUser = createUser("yitingwu.1030@gmail.com","Ting");
        AppUser appUser1 = createUser("user1@gmail.com","User1");
        AppUser appUser2 = createUser("user2@gmail.com","User2");
        userRepository.insert(appUser);
        userRepository.insert(appUser1);
        userRepository.insert(appUser2);
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
        Folder OSFolder = createFolder("OS","/OS",null);
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
        note.setFavoriteCount(null);
        note.setUnlockCount(0);
        note.setDownloadable(false);
        note.setCommentCount(2);
        ArrayList<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setEmail("user1@gmail.com");
        comment.setAuthor("User1");
        comment.getLiker().add("user2@gmail.com");
        comment.getLiker().add("yitingwu.1030@gmail.com");
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
        return noteRepository.insert(note);
    }

    public Post createQAPost(){
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

    @Test
    public void testGetAllCommentByNoteID() throws Exception{
        Note note = createNormalNote();
        AppUser commentAuthor = userRepository.findByEmail("user1@gmail.com");
        AppUser commentAuthor1 = userRepository.findByEmail("user2@gmail.com");//commentLiker
        AppUser commentLiker = userRepository.findByEmail("yitingwu.1030@gmail.com");
        mockMvc.perform(get("/comment/"+note.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.[0].id").value(note.getComments().get(0).getId()))
//                .andExpect(jsonPath("$.res.[0].author").value(note.getComments().get(0).getAuthor()))
//                .andExpect(jsonPath("$.res.[0].email").value(note.getComments().get(0).getEmail()))
                .andExpect(jsonPath("$.res.[0].content").value(note.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res.[0].likeCount").value(note.getComments().get(0).getLikeCount()))
//                .andExpect(jsonPath("$.res.[0].floor").value(note.getComments().get(0).getFloor()))
//                .andExpect(jsonPath("$.res.[0].liker").value(note.getComments().get(0).getLiker()))
                .andExpect(jsonPath("$.res.[0].date").value(note.getComments().get(0).getDate()))
                .andExpect(jsonPath("$.res.[0].picURL").value(note.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res.[0].best").value(note.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res.[0].userObj.userObjEmail").value(commentAuthor.getEmail()))
                .andExpect(jsonPath("$.res.[0].userObj.userObjName").value(commentAuthor.getName()))
                .andExpect(jsonPath("$.res.[0].userObj.userObjAvatar").value(commentAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[0].likerUserObj.[0].userObjEmail").value(commentAuthor1.getEmail()))
                .andExpect(jsonPath("$.res.[0].likerUserObj.[0].userObjName").value(commentAuthor1.getName()))
                .andExpect(jsonPath("$.res.[0].likerUserObj.[0].userObjAvatar").value(commentAuthor1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[0].likerUserObj.[1].userObjEmail").value(commentLiker.getEmail()))
                .andExpect(jsonPath("$.res.[0].likerUserObj.[1].userObjName").value(commentLiker.getName()))
                .andExpect(jsonPath("$.res.[0].likerUserObj.[1].userObjAvatar").value(commentLiker.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[1].id").value(note.getComments().get(1).getId()))
//                .andExpect(jsonPath("$.res.[1].author").value(note.getComments().get(1).getAuthor()))
//                .andExpect(jsonPath("$.res.[1].email").value(note.getComments().get(1).getEmail()))
                .andExpect(jsonPath("$.res.[1].content").value(note.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res.[1].likeCount").value(note.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res.[1].floor").value(note.getComments().get(1).getFloor()))
//                .andExpect(jsonPath("$.res.[1].liker").value(note.getComments().get(1).getLiker()))
                .andExpect(jsonPath("$.res.[1].date").value(note.getComments().get(1).getDate()))
                .andExpect(jsonPath("$.res.[1].picURL").value(note.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res.[1].best").value(note.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res.[1].userObj.userObjEmail").value(commentAuthor1.getEmail()))
                .andExpect(jsonPath("$.res.[1].userObj.userObjName").value(commentAuthor1.getName()))
                .andExpect(jsonPath("$.res.[1].userObj.userObjAvatar").value(commentAuthor1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[1].likerUserObj").isEmpty());;
    }

    @Test
    public void testGetAllCommentByQAPostID() throws Exception{
        Post post = createQAPost();
        AppUser commentAuthor = userRepository.findByEmail("user1@gmail.com");
        AppUser commentAuthor1 = userRepository.findByEmail("user2@gmail.com");
        mockMvc.perform(get("/comment/"+post.getId())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.[0].id").value(post.getComments().get(0).getId()))
//                .andExpect(jsonPath("$.res.[0].author").value(post.getComments().get(0).getAuthor()))
//                .andExpect(jsonPath("$.res.[0].email").value(post.getComments().get(0).getEmail()))
                .andExpect(jsonPath("$.res.[0].content").value(post.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res.[0].likeCount").value(post.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res.[0].floor").value(post.getComments().get(0).getFloor()))
//                .andExpect(jsonPath("$.res.[0].liker").value(post.getComments().get(0).getLiker()))
                .andExpect(jsonPath("$.res.[0].date").value(post.getComments().get(0).getDate()))
                .andExpect(jsonPath("$.res.[0].picURL").value(post.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res.[0].best").value(post.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res.[0].userObj.userObjEmail").value(commentAuthor.getEmail()))
                .andExpect(jsonPath("$.res.[0].userObj.userObjName").value(commentAuthor.getName()))
                .andExpect(jsonPath("$.res.[0].userObj.userObjAvatar").value(commentAuthor.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[0].likerUserObj").isEmpty())
                .andExpect(jsonPath("$.res.[1].id").value(post.getComments().get(1).getId()))
//                .andExpect(jsonPath("$.res.[1].author").value(post.getComments().get(1).getAuthor()))
//                .andExpect(jsonPath("$.res.[1].email").value(post.getComments().get(1).getEmail()))
                .andExpect(jsonPath("$.res.[1].content").value(post.getComments().get(1).getContent()))
                .andExpect(jsonPath("$.res.[1].likeCount").value(post.getComments().get(1).getLikeCount()))
                .andExpect(jsonPath("$.res.[1].floor").value(post.getComments().get(1).getFloor()))
//                .andExpect(jsonPath("$.res.[1].liker").value(post.getComments().get(1).getLiker()))
                .andExpect(jsonPath("$.res.[1].date").value(post.getComments().get(1).getDate()))
                .andExpect(jsonPath("$.res.[1].picURL").value(post.getComments().get(1).getPicURL()))
                .andExpect(jsonPath("$.res.[1].best").value(post.getComments().get(1).getBest()))
                .andExpect(jsonPath("$.res.[1].userObj.userObjEmail").value(commentAuthor1.getEmail()))
                .andExpect(jsonPath("$.res.[1].userObj.userObjName").value(commentAuthor1.getName()))
                .andExpect(jsonPath("$.res.[1].userObj.userObjAvatar").value(commentAuthor1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[1].likerUserObj").isEmpty());
    }

    @Test
    public void testGetCommentByFloor() throws Exception{
        Post post = createQAPost();
        Comment comment = post.getComments().get(0);
        AppUser appUser = userRepository.findByEmail(comment.getEmail());
        mockMvc.perform(get("/comment/"+post.getId()+"/0")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(post.getComments().get(0).getId()))
//                .andExpect(jsonPath("$.res.author").value(post.getComments().get(0).getAuthor()))
//                .andExpect(jsonPath("$.res.email").value(post.getComments().get(0).getEmail()))
                .andExpect(jsonPath("$.res.content").value(post.getComments().get(0).getContent()))
                .andExpect(jsonPath("$.res.likeCount").value(post.getComments().get(0).getLikeCount()))
                .andExpect(jsonPath("$.res.floor").value(post.getComments().get(0).getFloor()))
//                .andExpect(jsonPath("$.res.liker").value(post.getComments().get(0).getLiker()))
                .andExpect(jsonPath("$.res.date").value(post.getComments().get(0).getDate()))
                .andExpect(jsonPath("$.res.picURL").value(post.getComments().get(0).getPicURL()))
                .andExpect(jsonPath("$.res.best").value(post.getComments().get(0).getBest()))
                .andExpect(jsonPath("$.res.userObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.userObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.userObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.likerUserObj").isEmpty());
    }

    @Test
    public void testCreateComment() throws Exception{
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Post post = createQAPost();
        JSONArray picURLs = new JSONArray()
                .put("picurl1")
                .put("picurl2")
                .put("picurl3");
        JSONObject request = new JSONObject()
                .put("email","yitingwu.1030@gmail.com")
                .put("picURL",picURLs)
                .put("content","Nice");
        mockMvc.perform(post("/comment/"+post.getId())
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").hasJsonPath())
//                .andExpect(jsonPath("$.res.author").value(request.get("author")))
//                .andExpect(jsonPath("$.res.email").value(request.get("email")))
                .andExpect(jsonPath("$.res.likeCount").value(0))
//                .andExpect(jsonPath("$.res.liker").isEmpty())
                .andExpect(jsonPath("$.res.floor").value(post.getComments().size()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.picURL.[0]").value(picURLs.get(0)))
                .andExpect(jsonPath("$.res.picURL.[1]").value(picURLs.get(1)))
                .andExpect(jsonPath("$.res.picURL.[2]").value(picURLs.get(2)))
                .andExpect(jsonPath("$.res.best").value(false))
                .andExpect(jsonPath("$.res.content").value(request.get("content")))
                .andExpect(jsonPath("$.res.userObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.userObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.userObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.likerUserObj").isEmpty());
    }

    @Test
    public void testUpdateComment() throws Exception{
        Post post = createQAPost();
        Comment comment = post.getComments().get(0);
        AppUser appUser = userRepository.findByEmail(comment.getEmail());
        JSONObject request = new JSONObject()
                .put("email",comment.getEmail())
                .put("picURL",null)
                .put("content","NewContent");
        mockMvc.perform(put("/comment/"+post.getId()+"/0")
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(comment.getId()))
//                .andExpect(jsonPath("$.res.author").value(request.get("author")))
//                .andExpect(jsonPath("$.res.email").value(request.get("email")))
                .andExpect(jsonPath("$.res.likeCount").value(0))
//                .andExpect(jsonPath("$.res.liker").isEmpty())
                .andExpect(jsonPath("$.res.floor").value(comment.getFloor()))
                .andExpect(jsonPath("$.res.date").hasJsonPath())
                .andExpect(jsonPath("$.res.best").value(comment.getBest()))
                .andExpect(jsonPath("$.res.content").value(request.get("content")))
                .andExpect(jsonPath("$.res.picURL").value(IsNull.nullValue()))
                .andExpect(jsonPath("$.res.userObj.userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.userObj.userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.userObj.userObjAvatar").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.likerUserObj").isEmpty());
    }

    @Test
    public void testDeleteComment() throws Exception{
        Post post = createQAPost();
        mockMvc.perform(delete("/comment/"+post.getId()+"/0")
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        post = postRepository.findById(post.getId()).get();
        Comment comment = post.getComments().get(0);
        if(comment.getId().isEmpty()){
            throw new Exception("Comment Test :　comment' id should not remove");
        }
        if(comment.getEmail()!=null){
            throw new Exception("Comment Test : comment's email should be null");
        }
        if(comment.getAuthor()!=null){
            throw new Exception("Comment Test : comment's author should be null");
        }
        if(comment.getContent()!=null){
            throw new Exception("Comment Test : comment's content should be null");
        }
        if(comment.getLikeCount()!=0){
            throw new Exception("Comment Test : comment's likeCount should be null");
        }
        if(comment.getBest()!=false){
            throw new Exception("Comment Test : comment's best should be null");
        }
    }
    @AfterEach
    public void clear(){
        postRepository.deleteAll();
        userRepository.deleteAll();
        folderRepository.deleteAll();
        noteRepository.deleteAll();
    }
}
