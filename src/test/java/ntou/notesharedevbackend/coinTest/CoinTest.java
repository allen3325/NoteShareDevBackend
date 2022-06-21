package ntou.notesharedevbackend.coinTest;

import io.swagger.v3.oas.models.security.SecurityScheme;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.noteNodule.entity.Note;
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
    private AppUser createUser(String email, String name, Integer coin){
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setActivate(true);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode("1234"));
        Folder buyFolder = createFolder("Buy","/Buy",null);
        Folder favoriteFolder = createFolder("Favorite","/Favorite",null);
        ArrayList<String> folderList = new ArrayList<>();
        folderList.add(buyFolder.getId());
        folderList.add(favoriteFolder.getId());
        appUser.setFolders(folderList);
        appUser.setCoin(coin);
        return appUser;
    }

    private Note createNote(){
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
        note.setLiker(null);
        note.setLikeCount(null);
        note.setBuyer(null);
        note.setFavoriter(null);
        note.setFavoriteCount(null);
        note.setUnlockCount(0);
        note.setDownloadable(false);
        note.setCommentCount(null);
        note.setComments(null);
        note.setSubmit(null);
        note.setQuotable(false);
        note.setTag(null);
        note.setHiddenTag(null);
        note.setVersion(null);
        note.setContributors(null);
        note.setPostID(null);
        note.setReference(null);
        note.setBest(null);
        note.setManagerEmail(null);
        noteRepository.insert(note);
        return note;
    }
    @BeforeEach
    public void init(){
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        userRepository.deleteAll();
        noteRepository.deleteAll();
        folderRepository.deleteAll();
        AppUser appUser = createUser("yitingwu.1030@gmail.com","Ting",300);
        userRepository.insert(appUser);
        Note note = createNote();
        Folder favoriteFolder = folderRepository.findById(userRepository.findByEmail("yitingwu.1030@gmail.com").getFolders().get(1)).get();
        favoriteFolder.getNotes().add(note.getId());
        folderRepository.save(favoriteFolder);
    }

    @Test
    public void testAddCoin() throws Exception{
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Integer newCoin = appUser.getCoin() +30;
        JSONObject request = new JSONObject()
                .put("coin","+30");
        mockMvc.perform(put("/coin/"+appUser.getEmail())
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

        if(!userRepository.findByEmail(appUser.getEmail()).getCoin().equals(newCoin)){
            throw new Exception("Coin Test Error");
        }
    }
    @Test
    public void testChangeCoinWithEnoughCoin() throws Exception{
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Integer newCoin = appUser.getCoin() -30;
        JSONObject request = new JSONObject()
                .put("coin","-30");
        mockMvc.perform(put("/coin/"+appUser.getEmail())
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

        if(!userRepository.findByEmail(appUser.getEmail()).getCoin().equals(newCoin)){
            throw new Exception("Coin Test Error");
        }
    }

    @Test
    public void testChangeCoinWithNotEnoughCoin() throws Exception{
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Integer newCoin = 0;
        JSONObject request = new JSONObject()
                .put("coin","-30000");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/coin/"+appUser.getEmail())
                        .headers(httpHeaders)
                                .content(request.toString());
        mockMvc.perform(requestBuilder)
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.res").value("money is not enough. so set this user's money to 0."));

        if(!userRepository.findByEmail(appUser.getEmail()).getCoin().equals(newCoin)){
            throw new Exception("Coin Test Error");
        }
    }
    @Test
    public void testBuyNoteWithEnoughCoin() throws Exception{
        AppUser buyer = createUser("alan@gmail.com","alan",300);
        userRepository.insert(buyer);
        AppUser noteAuthor = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Note note = noteRepository.findAll().get(0);
        Integer buyerNewCoin = 250;
        Integer authorNewCoin = 350;
        mockMvc.perform(put("/coin/note/"+buyer.getEmail()+"/"+note.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(note.getId()))
                .andExpect(jsonPath("$.res.type").value(note.getType()))
                .andExpect(jsonPath("$.res.department").value(note.getDepartment()))
                .andExpect(jsonPath("$.res.subject").value(note.getSubject()))
                .andExpect(jsonPath("$.res.title").value(note.getTitle()))
                .andExpect(jsonPath("$.res.headerEmail").value(note.getHeaderEmail()))
                .andExpect(jsonPath("$.res.headerName").value(note.getHeaderName()))
                .andExpect(jsonPath("$.res.authorEmail").value(note.getAuthorEmail()))
                .andExpect(jsonPath("$.res.authorName").value(note.getAuthorName()))
                .andExpect(jsonPath("$.res.managerEmail").value(note.getManagerEmail()))
                .andExpect(jsonPath("$.res.professor").value(note.getProfessor()))
                .andExpect(jsonPath("$.res.school").value(note.getSchool()))
                .andExpect(jsonPath("$.res.liker").value(note.getLiker()))
                .andExpect(jsonPath("$.res.buyer").value(note.getBuyer()))
                .andExpect(jsonPath("$.res.favoriter").value(note.getFavoriter()))
                .andExpect(jsonPath("$.res.likeCount").value(note.getLikeCount()))
                .andExpect(jsonPath("$.res.favoriteCount").value(note.getFavoriteCount()))
                //TODO: 跟張哲瑋說 CoinService 買成功的時候要增加解鎖次數 完成後 測試下行
//                .andExpect(jsonPath("$.res.unlockCount").value(note.getUnlockCount()+1))
                .andExpect(jsonPath("$.res.downloadable").value(note.getDownloadable()))
                .andExpect(jsonPath("$.res.commentCount").value(note.getCommentCount()))
                .andExpect(jsonPath("$.res.comments").value(note.getComments()))
                .andExpect(jsonPath("$.res.price").value(note.getPrice()))
                .andExpect(jsonPath("$.res.quotable").value(note.getQuotable()))
                .andExpect(jsonPath("$.res.tag").value(note.getTag()))
                .andExpect(jsonPath("$.res.hiddenTag").value(note.getHiddenTag()))
                .andExpect(jsonPath("$.res.version").value(note.getVersion()))
                .andExpect(jsonPath("$.res.contributors").value(note.getContributors()))
                .andExpect(jsonPath("$.res.postID").value(note.getPostID()))
                .andExpect(jsonPath("$.res.reference").value(note.getReference()))
                .andExpect(jsonPath("$.res.best").value(note.getBest()))
                .andExpect(jsonPath("$.res.public").value(note.getPublic()))
                .andExpect(jsonPath("$.res.submit").value(note.getSubmit()));
        //TODO:跟張哲瑋說:筆記作者們要拿到錢 完成後測試下面迴圈
//        for(String s: note.getAuthorEmail()){
//            if(!userRepository.findByEmail(s).getCoin().equals(authorNewCoin)){
//                throw new Exception("Coin Test : author's coin does not increase");
//            }
//        }

        if(!userRepository.findByEmail(buyer.getEmail()).getCoin().equals(buyerNewCoin)){
            throw new Exception("Coin Test : buyer's coin does not decrease");
        }

        if(!folderRepository.findById(userRepository.findByEmail(buyer.getEmail()).getFolders().get(0)).get().getNotes().contains(note.getId())){
            throw new Exception("Coin Test : buyer does not get note");
        }
    }

    @Test
    public void testBuyNoteWithNotEnoughCoin() throws Exception{
        AppUser buyer = createUser("alan@gmail.com","alan",10);
        userRepository.insert(buyer);
        AppUser noteAuthor = userRepository.findByEmail("yitingwu.1030@gmail.com");
        Note note = noteRepository.findAll().get(0);
        Integer buyerNewCoin = 10;
        Integer authorNewCoin = 300;
        mockMvc.perform(put("/coin/note/"+buyer.getEmail()+"/"+note.getId())
                .headers(httpHeaders))
                .andExpect(status().isPreconditionFailed())
                .andExpect(jsonPath("$.msg").value("money is not enough or bought the note."));

        if(!userRepository.findByEmail(noteAuthor.getEmail()).getCoin().equals(authorNewCoin)){
            throw new Exception("Coin Test : author's coin should not change");
        }
        if(!userRepository.findByEmail(buyer.getEmail()).getCoin().equals(buyerNewCoin)){
            throw new Exception("Coin Test : buyer's coin should not change");
        }
    }

    @AfterEach
    public  void clear(){
        userRepository.deleteAll();
        noteRepository.deleteAll();
        folderRepository.deleteAll();
    }

}
