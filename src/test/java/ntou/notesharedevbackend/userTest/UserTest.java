package ntou.notesharedevbackend.userTest;

import com.google.gson.JsonArray;
import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.folderTest.FolderTest;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

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

    private AppUser createUser(){
        AppUser appUser = new AppUser();
        appUser.setEmail("yitingwu.1030@gmail.com");
        appUser.setActivate(true);
        appUser.setName("Ting");
        appUser.setPassword("1234");
        appUser.setVerifyCode("1111");
//        Folder buyFolder = createFolder("Buy","/Buy",null);
//        Folder favoriteFolder = createFolder("Favorite","/Favorite",null);
//        ArrayList<String> folderList = new ArrayList<>();
//        folderList.add(buyFolder.getId());
//        folderList.add(favoriteFolder.getId());
        appUser.setFolders(new ArrayList<>());
        return appUser;
    }

    @BeforeEach
    public void init(){
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        userRepository.deleteAll();
        AppUser appUser = createUser();
        userRepository.insert(appUser);
    }


    @Test
    public void testCreateUser() throws Exception {
        JSONObject request = new JSONObject()
                .put("email","allen3325940072@gmail.com")
                .put("name","allen")
                .put("password","1234");

        RequestBuilder requestBuilder = post("/verification/signup")
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateSameEmailUser() throws Exception {
        JSONObject request = new JSONObject()
                .put("email","yitingwu.1030@gmail.com")
                .put("name","Ting")
                .put("password","1234");

        RequestBuilder requestBuilder = post("/verification/signup")
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void testVerifyCode() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/verification/verify/yitingwu.1030@gmail.com/1111")
                        .headers(httpHeaders))
                .andExpect(status().isOk());
        //TODO:response 格式
    }

    @Test
    public void testWrongVerifyCode() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/verification/verify/yitingwu.1030@gmail.com/2222")
                        .headers(httpHeaders))
                .andExpect(status().isIAmATeapot());
    }
    //@Test
    public void testRandomPassword() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/verification/randomPassword/yitingwu.1030@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk());
    }
    //TODO: mail can not send
    //@Test
    public void testResetPassword() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/verification/randomPassword/yitingwu.1030@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk());
    }
    //@Test
    public void testResendCode() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/verification/resendCode/yitingwu.1030@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk());
    }
    //@Test
    public void testLogin() throws Exception{
        JSONObject request = new JSONObject()
                .put("email","yitingwu.1030@gmail.com")
                .put("password","1234");
        mockMvc.perform(post("/verification/login")
                .headers(httpHeaders)
                .content(request.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
        //TODO: response $.res.token?
        //TODO: ERROR Encoded password does not look like BCrypt
    }

    @Test
    public void testGetUserByID() throws Exception{
        String id = userRepository.findByEmail("yitingwu.1030@gmail.com").getId();
        AppUser appUser =userRepository.findById(id).get();
        mockMvc.perform(get("/user/id/"+id)
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.email").value(appUser.getEmail()))
                .andExpect(jsonPath("$.name").value(appUser.getName()))
                .andExpect(jsonPath("$.verifyCode").value(appUser.getVerifyCode()))
                .andExpect(jsonPath("$.password").hasJsonPath())
                .andExpect(jsonPath("$.profile").value(appUser.getProfile()))
                .andExpect(jsonPath("$.strength").value(appUser.getStrength()))
                .andExpect(jsonPath("$.folders").value(appUser.getFolders()))
                .andExpect(jsonPath("$.subscribe").value(appUser.getSubscribe()))
                .andExpect(jsonPath("$.bell").value(appUser.getBell()))
                .andExpect(jsonPath("$.fans").value(appUser.getFans()))
                .andExpect(jsonPath("$.coin").value(appUser.getCoin()))
                .andExpect(jsonPath("$.headshotPhoto").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.admin").value(appUser.isAdmin()))
                .andExpect(jsonPath("$.activate").value(appUser.isActivate()));
    }
    @Test
    public void testGetUserByEmail() throws Exception{
        AppUser appUser =userRepository.findByEmail("yitingwu.1030@gmail.com");
        mockMvc.perform(get("/user/"+appUser.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appUser.getId()))
                .andExpect(jsonPath("$.email").value(appUser.getEmail()))
                .andExpect(jsonPath("$.name").value(appUser.getName()))
                .andExpect(jsonPath("$.verifyCode").value(appUser.getVerifyCode()))
                .andExpect(jsonPath("$.password").hasJsonPath())
                .andExpect(jsonPath("$.profile").value(appUser.getProfile()))
                .andExpect(jsonPath("$.strength").value(appUser.getStrength()))
                .andExpect(jsonPath("$.folders").value(appUser.getFolders()))
                .andExpect(jsonPath("$.subscribe").value(appUser.getSubscribe()))
                .andExpect(jsonPath("$.bell").value(appUser.getBell()))
                .andExpect(jsonPath("$.fans").value(appUser.getFans()))
                .andExpect(jsonPath("$.coin").value(appUser.getCoin()))
                .andExpect(jsonPath("$.headshotPhoto").value(appUser.getHeadshotPhoto()))
                .andExpect(jsonPath("$.admin").value(appUser.isAdmin()))
                .andExpect(jsonPath("$.activate").value(appUser.isActivate()));
    }

    //TODO:使用者可以編輯的資料
    //@Test
    public void testReplaceUser() throws Exception{
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        String newName = "TestNewName";
        String newPassword = "TestNewPassword";
        String newProfile = "Test Profile"; //自我介紹
        JSONArray newStrength=new JSONArray();//擅長科目
        newStrength.put("Operating System");
        newStrength.put("Java");
        newStrength.put("Discrete mathematics");
        JSONArray newSubscribe=new JSONArray();
        JSONArray newBell=new JSONArray();
        JSONArray newFans=new JSONArray();
        Integer coin;
        String headshotPhoto;
        ArrayList<String> newStrengthArray = new ArrayList<>(); //擅長科目
        newStrengthArray.add("Operating System");
        newStrengthArray.add("Java");
        newStrengthArray.add("Discrete mathematics");
        ArrayList<String> newSubscribeArray= new ArrayList<>();
        ArrayList<String> newBellArray = new ArrayList<>();
        ArrayList<String> newFansArray = new ArrayList<>();
    }

    @Test
    public void testModifyUserStrength() throws Exception{
        AppUser appUser =userRepository.findByEmail("yitingwu.1030@gmail.com");
        JSONArray newStrength =new JSONArray();
        newStrength.put("Operating System");
        newStrength.put("Java");
        newStrength.put("Discrete mathematics");
        ArrayList<String> newStrengthArray = new ArrayList<>();
        newStrengthArray.add("Operating System");
        newStrengthArray.add("Java");
        newStrengthArray.add("Discrete mathematics");
        JSONObject request = new JSONObject()
                .put("strength",newStrength);

        mockMvc.perform(put("/user/strength/"+appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk());
        //TODO:response 統一

//                .andExpect(jsonPath("$.id").value(appUser.getId()))
//                .andExpect(jsonPath("$.email").value(appUser.getEmail()))
//                .andExpect(jsonPath("$.name").value(appUser.getName()))
//                .andExpect(jsonPath("$.verifyCode").value(appUser.getVerifyCode()))
//                .andExpect(jsonPath("$.password").hasJsonPath())
//                .andExpect(jsonPath("$.profile").value(appUser.getProfile()))
//                .andExpect(jsonPath("$.strength").value(newStrength))
//                .andExpect(jsonPath("$.folders").value(appUser.getFolders()))
//                .andExpect(jsonPath("$.subscribe").value(appUser.getSubscribe()))
//                .andExpect(jsonPath("$.bell").value(appUser.getBell()))
//                .andExpect(jsonPath("$.fans").value(appUser.getFans()))
//                .andExpect(jsonPath("$.coin").value(appUser.getCoin()))
//                .andExpect(jsonPath("$.headshotPhoto").value(appUser.getHeadshotPhoto()))
//                .andExpect(jsonPath("$.admin").value(appUser.isAdmin()))
//                .andExpect(jsonPath("$.activate").value(appUser.isActivate()));
        if(!userRepository.findByEmail("yitingwu.1030@gmail.com").getStrength().equals(newStrengthArray)){
            throw new Exception("Modify User Strength : strength does not change");
        }
    }

    @Test
    public void testModifyUserProfile() throws Exception{
        AppUser appUser =userRepository.findByEmail("yitingwu.1030@gmail.com");
        String newProfile = "我是測試，我在測試，希望測試成功";
        JSONObject request = new JSONObject()
                .put("profile",newProfile);

        mockMvc.perform(put("/user/profile/"+appUser.getEmail())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.id").value(appUser.getId()))
//                .andExpect(jsonPath("$.email").value(appUser.getEmail()))
//                .andExpect(jsonPath("$.name").value(appUser.getName()))
//                .andExpect(jsonPath("$.verifyCode").value(appUser.getVerifyCode()))
//                .andExpect(jsonPath("$.password").hasJsonPath())
//                .andExpect(jsonPath("$.profile").value(newProfile))
//                .andExpect(jsonPath("$.strength").value(appUser.getStrength()))
//                .andExpect(jsonPath("$.folders").value(appUser.getFolders()))
//                .andExpect(jsonPath("$.subscribe").value(appUser.getSubscribe()))
//                .andExpect(jsonPath("$.bell").value(appUser.getBell()))
//                .andExpect(jsonPath("$.fans").value(appUser.getFans()))
//                .andExpect(jsonPath("$.coin").value(appUser.getCoin()))
//                .andExpect(jsonPath("$.headshotPhoto").value(appUser.getHeadshotPhoto()))
//                .andExpect(jsonPath("$.admin").value(appUser.isAdmin()))
//                .andExpect(jsonPath("$.activate").value(appUser.isActivate()));
        //TODO:response 統一
        if(!userRepository.findByEmail("yitingwu.1030@gmail.com").getProfile().equals(newProfile)){
            throw new Exception("Modify User Profile : profile does not change");
        }
    }
    @AfterEach
    public void clear(){
        userRepository.deleteAll();
    }
}