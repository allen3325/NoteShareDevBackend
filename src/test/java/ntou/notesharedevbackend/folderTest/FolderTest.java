package ntou.notesharedevbackend.folderTest;


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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FolderTest {
    private HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;
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
        folderRepository.deleteAll();
        userRepository.deleteAll();
        noteRepository.deleteAll();
    }

    @Test
    public void testCreateFolder() throws Exception {
        AppUser appUser = createUser();
        userRepository.save(appUser);

        JSONObject request = new JSONObject()
                .put("folderName", "OS")
                .put("public", true)
                .put("path", "/Favorite/OS")
                .put("parent", appUser.getFolders().get(1));//Favorite folder's id

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/folder/yitingwu.1030@gmail.com")
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.res.id").hasJsonPath())
                .andExpect(jsonPath("$.res.folderName").value(request.getString("folderName")))
                .andExpect(jsonPath("$.res.public").value(request.getBoolean("public")))
                .andExpect(jsonPath("$.res.path").value(request.getString("path")))
                .andExpect(jsonPath("$.res.parent").value(request.getString("parent")))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        //檢查user+(檢查parentFolder)
        String folderID = userRepository.findByEmail("yitingwu.1030@gmail.com").getFolders().get(2);
        if (!folderRepository.findById(folderID).isPresent()) {
            throw new Exception("Create Folder: folder does not in DB");
        }
        if (!folderRepository.findById(request.getString("parent")).get().getChildren().contains(folderID)) {
            throw new Exception("Create Folder: folder id is not put into parent folder's children");
        }
    }

    private AppUser createUser() {
        AppUser appUser = new AppUser();
        appUser.setEmail("yitingwu.1030@gmail.com");
        appUser.setActivate(true);
        appUser.setName("Ting");
        appUser.setPassword("1234");
        Folder buyFolder = createFolder("Buy", "/Buy", null);
        Folder favoriteFolder = createFolder("Favorite", "/Favorite", null);
        ArrayList<String> folderList = new ArrayList<>();
        folderList.add(buyFolder.getId());
        folderList.add(favoriteFolder.getId());
        appUser.setFolders(folderList);
        return appUser;
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

    private Note createNote() {
        Note note = new Note();
        noteRepository.insert(note);
        return note;
    }

    @Test
    public void testGetAllFoldersFromUser() throws Exception {
        //建立user，建立多個folder
        Folder folder3 = createFolder("TingTest", "/TingTest", null);
        Folder folder33 = createFolder("Ting", "/TingTest/Ting", folder3.getId());
        folder3.getChildren().add(folder33.getId());
        folderRepository.save(folder3);
        AppUser appUser = createUser();
        ArrayList<String> folderList = appUser.getFolders();
        folderList.add(folder3.getId());
        folderList.add(folder33.getId());
        appUser.setFolders(folderList);
        userRepository.insert(appUser);

        //檢查回傳
        mockMvc.perform(get("/folder/all/" + appUser.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.[0].id").value(appUser.getFolders().get(0)))
                .andExpect(jsonPath("$.res.[0].folderName").value("Buy"))
                .andExpect(jsonPath("$.res.[0].parent").value(folderRepository.findById(appUser.getFolders().get(0)).get().getParent()))
                .andExpect(jsonPath("$.res.[0].path").value("/Buy"))
                .andExpect(jsonPath("$.res.[0].public").value(false))
                .andExpect(jsonPath("$.res.[1].id").value(appUser.getFolders().get(1)))
                .andExpect(jsonPath("$.res.[1].folderName").value("Favorite"))
                .andExpect(jsonPath("$.res.[1].parent").value(folderRepository.findById(appUser.getFolders().get(1)).get().getParent()))
                .andExpect(jsonPath("$.res.[1].path").value("/Favorite"))
                .andExpect(jsonPath("$.res.[1].public").value(false))
                .andExpect(jsonPath("$.res.[2].id").value(folder3.getId()))
                .andExpect(jsonPath("$.res.[2].folderName").value(folder3.getFolderName()))
                .andExpect(jsonPath("$.res.[2].parent").value(folder3.getParent()))
                .andExpect(jsonPath("$.res.[2].path").value(folder3.getPath()))
                .andExpect(jsonPath("$.res.[2].public").value(folder3.getPublic()))
                .andExpect(jsonPath("$.res.[3].id").value(folder33.getId()))
                .andExpect(jsonPath("$.res.[3].folderName").value(folder33.getFolderName()))
                .andExpect(jsonPath("$.res.[3].parent").value(folder33.getParent()))
                .andExpect(jsonPath("$.res.[3].path").value(folder33.getPath()))
                .andExpect(jsonPath("$.res.[3].public").value(folder33.getPublic()));
    }

    @Test
    public void testGetAllFoldersAndNotesUnderUserSpecificFolder() throws Exception {
        //建立 parent folder and children folder
        Folder parentFolder = createFolder("Buy", "/Buy", null);
        Folder firstChildrenFolder = createFolder("Ting", "/Buy/Ting", parentFolder.getId());
        Folder secondChildrenFolder = createFolder("Ting2", "/Buy/Ting2", parentFolder.getId());
        //建立note
        Note firstNote = createNote();
        Note secondNote = createNote();
        //note放入 parent folder
        parentFolder.getNotes().add(firstNote.getId());
        parentFolder.getNotes().add(secondNote.getId());
        //children放入parent
        parentFolder.getChildren().add(firstChildrenFolder.getId());
        parentFolder.getChildren().add(secondChildrenFolder.getId());
        folderRepository.save(parentFolder);
        //建立user
        AppUser appUser = createUser();
        appUser.getFolders().add(parentFolder.getId());
        userRepository.insert(appUser);
        mockMvc.perform(get("/folder/" + parentFolder.getId())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(parentFolder.getId()))
                .andExpect(jsonPath("$.res.folderName").value(parentFolder.getFolderName()))
                .andExpect(jsonPath("$.res.path").value(parentFolder.getPath()))
                .andExpect(jsonPath("$.res.children.[0].id").value(firstChildrenFolder.getId()))
                .andExpect(jsonPath("$.res.children.[1].id").value(secondChildrenFolder.getId()))
                .andExpect(jsonPath("$.res.notes.[0].id").value(firstNote.getId()))
                .andExpect(jsonPath("$.res.notes.[1].id").value(secondNote.getId()));
    }

    @Test
    public void testRenameFirstFloorFolder() throws Exception {
        //建folder
        Folder folder = createFolder("Ting", "/Ting", null);
        //建User
        AppUser appUser = createUser();
        appUser.getFolders().add(folder.getId());
        userRepository.insert(appUser);
        //改名
        String newName = "Yi";
        String newPath = "/Yi";
        mockMvc.perform(put("/folder/rename/" + appUser.getEmail() + "/" + folder.getId() + "/" + newName)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(folder.getId()))
                .andExpect(jsonPath("$.res.folderName").value(newName))
                .andExpect(jsonPath("$.res.path").value(newPath))
                .andExpect(jsonPath("$.res.parent").value(folder.getParent()));
        //檢查
        if (!folderRepository.findById(folder.getId()).get().getFolderName().equals(newName)) {
            throw new Exception("Folder Rename: target folder's name does not change => " + folderRepository.findById(folder.getId()).get().getFolderName());
        }
        if (!folderRepository.findById(folder.getId()).get().getPath().equals(newPath)) {
            throw new Exception("Folder Rename: target folder's path does not change => " + folderRepository.findById(folder.getId()).get().getPath());
        }

        ;
    }

    @Test
    public void testRenameSecondFloorFolder() throws Exception {
        //建User
        AppUser appUser = createUser();
        //建folder
        Folder folder = createFolder("Ting", "/Buy/Ting", appUser.getFolders().get(0));
        Folder childrenFolder = createFolder("Alan", "/Buy/Ting/Alan", folder.getId());
        Folder children2Folder = createFolder("Eva", "/Buy/Ting/Eva", folder.getId());
        folder.getChildren().add(childrenFolder.getId());
        folder.getChildren().add(children2Folder.getId());
        folderRepository.save(folder);
        appUser.getFolders().add(folder.getId());
        appUser.getFolders().add(childrenFolder.getId());
        appUser.getFolders().add(children2Folder.getId());
        userRepository.insert(appUser);
        //改名
        String newName = "Yi";
        String newPath = "/Buy/Yi";
        mockMvc.perform(put("/folder/rename/" + appUser.getEmail() + "/" + folder.getId() + "/" + newName)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(folder.getId()))
                .andExpect(jsonPath("$.res.folderName").value(newName))
                .andExpect(jsonPath("$.res.path").value(newPath))
                .andExpect(jsonPath("$.res.parent").value(folder.getParent()));
        //檢查
        //target's name
        if (!folderRepository.findById(folder.getId()).get().getFolderName().equals("Yi")) {
            throw new Exception("Folder Rename: target folder's name does not change => " + folder.getFolderName());
        }
        //target's path
        if (!folderRepository.findById(folder.getId()).get().getPath().equals("/Buy/Yi")) {
            throw new Exception("Folder Rename: target folder's path does not change => " + folder.getPath());
        }
        //children's path
        if (!folderRepository.findById(childrenFolder.getId()).get().getPath().equals("/Buy/Yi/Alan")) {
            throw new Exception("Folder Rename: children folder's path does not change => " + childrenFolder.getPath());
        }
        //children's path
        if (!folderRepository.findById(children2Folder.getId()).get().getPath().equals("/Buy/Yi/Eva")) {
            throw new Exception("Folder Rename: children folder's path does not change => " + children2Folder.getPath());
        }
    }

    @Test
    public void testChangePathByID() throws Exception {
        //建立user
        AppUser appUser = createUser();
        //建立folder
        Folder parentFolder = createFolder("Ting", "/Buy/Ting", appUser.getFolders().get(0));
        Folder folder = createFolder("Alan", "/Buy/Ting/Alan", parentFolder.getId());
        parentFolder.getChildren().add(folder.getId());
        folderRepository.save(parentFolder);
        Folder childrenFolder = createFolder("Eva", "/Buy/Ting/Alan/Eva", folder.getId());
        folder.getChildren().add(childrenFolder.getId());
        folderRepository.save(folder);
        appUser.getFolders().add(parentFolder.getId());
        appUser.getFolders().add(folder.getId());
        appUser.getFolders().add(childrenFolder.getId());
        //儲存user
        userRepository.insert(appUser);
        //修改位置
        JSONObject request = new JSONObject()
                .put("path", "/Buy/Alan")
                .put("parent", appUser.getFolders().get(0));
        mockMvc.perform(put("/folder/save/" + appUser.getEmail() + "/" + folder.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(folder.getId()))
                .andExpect(jsonPath("$.res.folderName").value(folder.getFolderName()))
                .andExpect(jsonPath("$.res.path").value("/Buy/Alan"))
                .andExpect(jsonPath("$.res.parent").value(appUser.getFolders().get(0)));
        //檢查
        if (folderRepository.findById(parentFolder.getId()).get().getChildren().contains(folder.getId())) {
            throw new Exception("Folder Change Path: parentFolder's children does not remove folder");
        }
        if (!folderRepository.findById(folder.getId()).get().getPath().equals("/Buy/Alan")) {
            throw new Exception("Folder Change Path: target folder's path does not change");
        }
        if (!folderRepository.findById(folder.getId()).get().getParent().equals(appUser.getFolders().get(0))) {
            throw new Exception("Folder Change Path: target folder's parent does not change");
        }
        if (!folderRepository.findById(childrenFolder.getId()).get().getPath().equals("/Buy/Alan/Eva")) {
            throw new Exception("Folder Change Path: childrenFolder's path does not change." + childrenFolder.getPath());
        }
    }

    //TODO: testChangePathToTopByID 移到最上層 parent = null 會有問題
    @Test
    public void testChangePathToTopByID() throws Exception {
        //建立user
        AppUser appUser = createUser();
        //建立folder
        Folder parentFolder = createFolder("Ting", "/Buy/Ting", appUser.getFolders().get(0));
        Folder folder = createFolder("Alan", "/Buy/Ting/Alan", parentFolder.getId());
        parentFolder.getChildren().add(folder.getId());
        folderRepository.save(parentFolder);
        Folder childrenFolder = createFolder("Eva", "/Buy/Ting/Alan/Eva", folder.getId());
        folder.getChildren().add(childrenFolder.getId());
        folderRepository.save(folder);
        appUser.getFolders().add(parentFolder.getId());
        appUser.getFolders().add(folder.getId());
        appUser.getFolders().add(childrenFolder.getId());
        //儲存user
        userRepository.insert(appUser);
        //修改位置
        JSONObject request = new JSONObject()
                .put("path", "/Alan")
                .put("parent", null);
        mockMvc.perform(put("/folder/save/" + appUser.getEmail() + "/" + folder.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(folder.getId()))
                .andExpect(jsonPath("$.res.folderName").value(folder.getFolderName()))
                .andExpect(jsonPath("$.res.path").value("/Alan"))
                .andExpect(jsonPath("$.res.parent").doesNotExist());
        //檢查
        if (folderRepository.findById(parentFolder.getId()).get().getChildren().contains(folder.getId())) {
            throw new Exception("Folder Change Path: parentFolder's children does not remove folder");
        }
        if (!folderRepository.findById(folder.getId()).get().getPath().equals("/Alan")) {
            throw new Exception("Folder Change Path: target folder's path does not change");
        }
        if (!(folderRepository.findById(folder.getId()).get().getParent() == null)) {
            throw new Exception("Folder Change Path: target folder's parent does not change");
        }
        if (!folderRepository.findById(childrenFolder.getId()).get().getPath().equals("/Alan/Eva")) {
            throw new Exception("Folder Change Path: childrenFolder's path does not change." + childrenFolder.getPath());
        }
    }

    //TODO:改變收藏狀態 會移出嗎，Response $.res.msg?
    @Test
    public void testPutIntoFavoriteFolder() throws Exception {
        //建User
        AppUser appUser = createUser();
        //建folder
        Folder folder = createFolder("Ting", "/Buy/Ting", appUser.getFolders().get(0));
        Folder childrenFolder = createFolder("Alan", "/Buy/Ting/Alan", folder.getId());
        Folder children2Folder = createFolder("Eva", "/Buy/Ting/Eva", folder.getId());
        folder.getChildren().add(childrenFolder.getId());
        folder.getChildren().add(children2Folder.getId());
        folderRepository.save(folder);
        appUser.getFolders().add(folder.getId());
        appUser.getFolders().add(childrenFolder.getId());
        appUser.getFolders().add(children2Folder.getId());
        userRepository.insert(appUser);
        //收藏
        mockMvc.perform(put("/folder/favorite/" + appUser.getEmail() + "/" + folder.getId())
                        .headers(httpHeaders))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value("success"));
        //檢查
        String favoriteFolderID = userRepository.findByEmail("yitingwu.1030@gmail.com").getFolders().get(1);
        if (!folderRepository.findById(favoriteFolderID).get().getChildren().contains(folder.getId())) {
            throw new Exception("Folder Change Favorite State : folder does not put into favorite folder's children");
        }
    }
//TODO:merge後測試

//    @Test
//    public void testTakeOutFromFavoriteFolder() throws Exception {
//        //建User
//        AppUser appUser = createUser();
//        //建folder
//        Folder folder = createFolder("Ting", "/Buy/Ting", appUser.getFolders().get(0));
//        Folder childrenFolder = createFolder("Alan", "/Buy/Ting/Alan", folder.getId());
//        Folder children2Folder = createFolder("Eva", "/Buy/Ting/Eva", folder.getId());
//        folder.getChildren().add(childrenFolder.getId());
//        folder.getChildren().add(children2Folder.getId());
//        folderRepository.save(folder);
//        //放進Favorite folder
//        Folder favoriteFolder = folderRepository.findById(appUser.getFolders().get(1)).get();
//        favoriteFolder.getChildren().add(folder.getId());
//        folderRepository.save(favoriteFolder);
//        appUser.getFolders().add(folder.getId());
//        appUser.getFolders().add(childrenFolder.getId());
//        appUser.getFolders().add(children2Folder.getId());
//        userRepository.insert(appUser);
//        //收藏
//        mockMvc.perform(put("/folder/favorite/" + appUser.getEmail() + "/" + folder.getId())
//                        .headers(httpHeaders))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.msg").value("success"));
//        //檢查
//        String favoriteFolderID = userRepository.findByEmail("yitingwu.1030@gmail.com").getFolders().get(1);
    //TODO: 這裡是不是少個!(好像寫反了)

//        if (folderRepository.findById(favoriteFolderID).get().getChildren().contains(folder.getId())) {
//            throw new Exception("Folder Change Favorite State : folder does not put into favorite folder's children");
//        }
//    }

    @Test
    public void testDeleteFolderByID() throws Exception {
        //建User
        AppUser appUser = createUser();
        //建folder
        Folder folder = createFolder("Ting", "/Buy/Ting", appUser.getFolders().get(0));
        Folder childrenFolder = createFolder("Alan", "/Buy/Ting/Alan", folder.getId());
        Folder children2Folder = createFolder("Eva", "/Buy/Ting/Eva", folder.getId());
        folder.getChildren().add(childrenFolder.getId());
        folder.getChildren().add(children2Folder.getId());
        folderRepository.save(folder);
        //放到Favorite folder
        Folder favoriteFolder = folderRepository.findById(appUser.getFolders().get(1)).get();
        favoriteFolder.getChildren().add(folder.getId());
        folderRepository.save(favoriteFolder);
        appUser.getFolders().add(folder.getId());
        appUser.getFolders().add(childrenFolder.getId());
        appUser.getFolders().add(children2Folder.getId());
        userRepository.insert(appUser);

        mockMvc.perform(delete("/folder/" + appUser.getEmail() + "/" + folder.getId())
                        .headers(httpHeaders))
                .andExpect(status().isNoContent());
        //檢查
        //folder
        if (folderRepository.findById(folder.getId()).isPresent()) {
            throw new Exception("Delete Folder: folder doesn't not delete");
        }
        //children folder
        if (folderRepository.findById(childrenFolder.getId()).isPresent()) {
            throw new Exception("Delete Folder: children folder doesn't not delete");
        }
        //children folder
        if (folderRepository.findById(children2Folder.getId()).isPresent()) {
            throw new Exception("Delete Folder: children folder doesn't not delete");
        }
        //parent folder's children
        if (folderRepository.findById(folder.getParent()).get().getChildren().contains(folder.getId())) {
            throw new Exception("Delete Folder: parent folder's children doesn't not remove folder's id");
        }
        //favorite
        //TODO : 跟張哲瑋說要檢查favorite的folder
        // 張哲瑋說：測試好了也過了:D
        if (folderRepository.findById(appUser.getFolders().get(1)).get().getChildren().contains(folder.getId())) {
            throw new Exception("Delete Folder: favorite folder's children doesn't not remove folder's id");
        }

    }

    @AfterEach
    public void clear() {
        folderRepository.deleteAll();
        userRepository.deleteAll();
        noteRepository.deleteAll();
    }
}