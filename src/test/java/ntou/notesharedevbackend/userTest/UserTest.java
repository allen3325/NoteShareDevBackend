package ntou.notesharedevbackend.userTest;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

    private HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FolderRepository folderRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
        appUser.setVerifyCode("1111");
        Folder buyFolder = createFolder("Buy", "/Buy", null);
        Folder favoriteFolder = createFolder("Favorite", "/Favorite", null);
        Folder folderFolder = createFolder("Folder", "/Folder", null);
        ArrayList<String> folderList = new ArrayList<>();
        folderList.add(buyFolder.getId());
        folderList.add(favoriteFolder.getId());
        folderList.add(folderFolder.getId());
        appUser.setFolders(folderList);
        appUser.setHeadshotPhoto("HeadshotPhoto");
        appUser.setProfile("profile");
        ArrayList<String> strengths = new ArrayList<>();
        strengths.add("strength1");
        strengths.add("strength2");
        strengths.add("strength3");
        appUser.setStrength(strengths);
        appUser.setBell(new ArrayList<>());
        appUser.setSubscribe(new ArrayList<>());
        appUser.setFans(new ArrayList<>());
        appUser.setNotification(new ArrayList<>());
        appUser.setUnreadMessageCount(0);
        appUser.setBelledBy(new ArrayList<>());
        return userRepository.insert(appUser);
    }

    @BeforeEach
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        userRepository.deleteAll();
        folderRepository.deleteAll();
    }


    @Test
    public void testCreateUser() throws Exception {
        JSONObject request = new JSONObject()
                .put("email", "00853129@email.ntou.edu.tw")
                .put("name", "wu")
                .put("password", "1234");

        RequestBuilder requestBuilder = post("/verification/signup")
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateSameEmailUser() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        JSONObject request = new JSONObject()
                .put("email", "yitingwu.1030@gmail.com")
                .put("name", "Ting")
                .put("password", "1234");

        RequestBuilder requestBuilder = post("/verification/signup")
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void testCreateSameNameUser() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        JSONObject request = new JSONObject()
                .put("email", "a8257tsa1030@gmail.com")
                .put("name", "Ting")
                .put("password", "1234");

        RequestBuilder requestBuilder = post("/verification/signup")
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotAcceptable());
    }


    @Test
    public void testVerifyCode() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        mockMvc.perform(MockMvcRequestBuilders.put("/verification/verify/yitingwu.1030@gmail.com/1111")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
    }

    @Test
    public void testWrongVerifyCode() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        mockMvc.perform(MockMvcRequestBuilders.put("/verification/verify/yitingwu.1030@gmail.com/2222")
                        .headers(httpHeaders))
                .andExpect(status().isIAmATeapot());
    }

    @Test
    public void testRandomPassword() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        mockMvc.perform(MockMvcRequestBuilders.post("/verification/randomPassword/yitingwu.1030@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
    }

    @Test
    public void testResetPassword() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        JSONObject request = new JSONObject()
                .put("email", "yitingwu.1030@gmail.com")
                .put("password", "1234")
                .put("newPassword", "1111");
        mockMvc.perform(MockMvcRequestBuilders.post("/verification/resetPassword")
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
    }

    @Test
    public void testResendCode() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        mockMvc.perform(MockMvcRequestBuilders.post("/verification/resendCode/yitingwu.1030@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
    }

    @Test
    public void testLogin() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        JSONObject request = new JSONObject()
                .put("email", "yitingwu.1030@gmail.com")
                .put("password", "1234");
        mockMvc.perform(post("/verification/login")
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testGetUserByID() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        String id = appUser.getId();
        AppUser fans = createUser("user1@gmail.com", "User1");
        appUser.getFans().add(fans.getEmail());
        fans.getSubscribe().add(appUser.getEmail());
        AppUser following = createUser("user2@gmail.com", "User2");
        appUser.getSubscribe().add(following.getEmail());
        following.getFans().add(appUser.getEmail());
        AppUser bells = createUser("user3@gmail.com0", "User3");
        appUser.getBell().add(bells.getEmail());
        bells.getBelledBy().add(appUser.getEmail());
        fans.getBell().add(appUser.getEmail());
        appUser.getBelledBy().add(fans.getEmail());
        userRepository.save(fans);
        userRepository.save(following);
        userRepository.save(bells);
        userRepository.save(appUser);
        mockMvc.perform(get("/user/id/" + id)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(id))
                .andExpect(jsonPath("$.res.email").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.name").value(appUser.getName()))
                .andExpect(jsonPath("$.res.verifyCode").value(appUser.getVerifyCode()))
                .andExpect(jsonPath("$.res.password").hasJsonPath())
                .andExpect(jsonPath("$.res.profile").value(appUser.getProfile()))
                .andExpect(jsonPath("$.res.strength").value(appUser.getStrength()))
                .andExpect(jsonPath("$.res.folders.[0]").value(appUser.getFolders().get(0)))
                .andExpect(jsonPath("$.res.folders.[1]").value(appUser.getFolders().get(1)))
                .andExpect(jsonPath("$.res.folders.[2]").value(appUser.getFolders().get(2)))
                .andExpect(jsonPath("$.res.fansUserObj.[0].userObjEmail").value(fans.getEmail()))
                .andExpect(jsonPath("$.res.fansUserObj.[0].userObjName").value(fans.getName()))
                .andExpect(jsonPath("$.res.fansUserObj.[0].userObjAvatar").value(fans.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.subscribeUserObj.[0].userObjEmail").value(following.getEmail()))
                .andExpect(jsonPath("$.res.subscribeUserObj.[0].userObjName").value(following.getName()))
                .andExpect(jsonPath("$.res.subscribeUserObj.[0].userObjAvatar").value(following.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.bellUserObj.[0].userObjEmail").value(bells.getEmail()))
                .andExpect(jsonPath("$.res.bellUserObj.[0].userObjName").value(bells.getName()))
                .andExpect(jsonPath("$.res.bellUserObj.[0].userObjAvatar").value(bells.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.belledByUserObj.[0].userObjEmail").value(fans.getEmail()))
                .andExpect(jsonPath("$.res.belledByUserObj.[0].userObjName").value(fans.getName()))
                .andExpect(jsonPath("$.res.belledByUserObj.[0].userObjAvatar").value(fans.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.coin").value(appUser.getCoin()))
                .andExpect(jsonPath("$.res.headshotPhoto").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.admin").value(appUser.isAdmin()))
                .andExpect(jsonPath("$.res.activate").value(appUser.isActivate()));
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        mockMvc.perform(get("/user/" + appUser.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(appUser.getId()))
                .andExpect(jsonPath("$.res.email").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.name").value(appUser.getName()))
                .andExpect(jsonPath("$.res.verifyCode").value(appUser.getVerifyCode()))
                .andExpect(jsonPath("$.res.password").hasJsonPath())
                .andExpect(jsonPath("$.res.profile").value(appUser.getProfile()))
                .andExpect(jsonPath("$.res.strength").value(appUser.getStrength()))
                .andExpect(jsonPath("$.res.folders").value(appUser.getFolders()))
                .andExpect(jsonPath("$.res.subscribeUserObj").value(appUser.getSubscribe()))
                .andExpect(jsonPath("$.res.bellUserObj").value(appUser.getBell()))
                .andExpect(jsonPath("$.res.fansUserObj").value(appUser.getFans()))
                .andExpect(jsonPath("$.res.coin").value(appUser.getCoin()))
                .andExpect(jsonPath("$.res.headshotPhoto").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.admin").value(appUser.isAdmin()))
                .andExpect(jsonPath("$.res.activate").value(appUser.isActivate()));
    }


    @Test
    public void testGetUserHeadShotPhoto() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        mockMvc.perform(get("/user/head/" + appUser.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res").value(appUser.getHeadshotPhoto()));
    }

    @Test
    public void testGetUserProfile() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        mockMvc.perform(get("/user/profile/" + appUser.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res").value(appUser.getProfile()));
    }

    @Test
    public void testGetUserStrength() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        mockMvc.perform(get("/user/strength/" + appUser.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.[0]").value(appUser.getStrength().get(0)))
                .andExpect(jsonPath("$.res.[1]").value(appUser.getStrength().get(1)))
                .andExpect(jsonPath("$.res.[2]").value(appUser.getStrength().get(2)));
    }

    @Test
    public void testGetUserName() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        mockMvc.perform(get("/user/name/" + appUser.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res").value(appUser.getName()));
    }

    @Test
    public void testUpdateUserName() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        String newName = "newTime";
        mockMvc.perform(put("/user/name/" + appUser.getEmail() + "/" + newName)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res").value(newName));
    }

    @Test
    public void testUpdateHeadshotPhoto() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        String newHeadshotPhoto = "newHeadshotPhoto";
        JSONObject request = new JSONObject()
                .put("headshotPhoto", newHeadshotPhoto);
        mockMvc.perform(put("/user/head/" + appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.id").value(appUser.getId()))
                .andExpect(jsonPath("$.res.email").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.name").value(appUser.getName()))
                .andExpect(jsonPath("$.res.verifyCode").value(appUser.getVerifyCode()))
                .andExpect(jsonPath("$.res.password").hasJsonPath())
                .andExpect(jsonPath("$.res.profile").value(appUser.getProfile()))
                .andExpect(jsonPath("$.res.strength.[0]").value(appUser.getStrength().get(0)))
                .andExpect(jsonPath("$.res.strength.[1]").value(appUser.getStrength().get(1)))
                .andExpect(jsonPath("$.res.strength.[2]").value(appUser.getStrength().get(2)))
                .andExpect(jsonPath("$.res.folders.[0]").value(appUser.getFolders().get(0)))
                .andExpect(jsonPath("$.res.folders.[1]").value(appUser.getFolders().get(1)))
                .andExpect(jsonPath("$.res.folders.[2]").value(appUser.getFolders().get(2)))
                .andExpect(jsonPath("$.res.subscribeUserObj").value(appUser.getSubscribe()))
                .andExpect(jsonPath("$.res.bellUserObj").value(appUser.getBell()))
                .andExpect(jsonPath("$.res.fansUserObj").value(appUser.getFans()))
                .andExpect(jsonPath("$.res.coin").value(appUser.getCoin()))
                .andExpect(jsonPath("$.res.headshotPhoto").value(newHeadshotPhoto))
                .andExpect(jsonPath("$.res.admin").value(appUser.isAdmin()))
                .andExpect(jsonPath("$.res.activate").value(appUser.isActivate()));
    }

    @Test
    public void testModifyUserStrength() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        JSONArray newStrength = new JSONArray();
        newStrength.put("Operating System");
        newStrength.put("Java");
        newStrength.put("Discrete mathematics");
        ArrayList<String> newStrengthArray = new ArrayList<>();
        newStrengthArray.add("Operating System");
        newStrengthArray.add("Java");
        newStrengthArray.add("Discrete mathematics");
        JSONObject request = new JSONObject()
                .put("strength", newStrength);

        mockMvc.perform(put("/user/strength/" + appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (!userRepository.findByEmail("yitingwu.1030@gmail.com").getStrength().equals(newStrengthArray)) {
            throw new Exception("Modify User Strength : strength does not change");
        }
    }

    @Test
    public void testModifyUserProfile() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        String newProfile = "我是測試，我在測試，希望測試成功";
        JSONObject request = new JSONObject()
                .put("profile", newProfile);

        mockMvc.perform(put("/user/profile/" + appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (!userRepository.findByEmail("yitingwu.1030@gmail.com").getProfile().equals(newProfile)) {
            throw new Exception("Modify User Profile : profile does not change");
        }
    }

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
        folderRepository.deleteAll();
    }
}