package ntou.notesharedevbackend.followTest;

import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FollowTest {
    private HttpHeaders httpHeaders;

    @Autowired
    private MockMvc mockMvc;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init(){
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        userRepository.deleteAll();
        AppUser appUser = createInitUser();
        userRepository.insert(appUser);
    }

    private AppUser createInitUser(){
        AppUser appUser = new AppUser();
        appUser.setEmail("yitingwu.1030@gmail.com");
        // 張哲瑋：因為我把appUser的建構子修改過，以下空陣列為原本建構子裡做的事情
        appUser.setStrength(new ArrayList<String>());
        appUser.setSubscribe(new ArrayList<String>());
        appUser.setBell(new ArrayList<String>());
        appUser.setFans(new ArrayList<String>());
        appUser.setHeadshotPhoto(null);
        appUser.setActivate(true);
        appUser.setName("Ting");
        appUser.setPassword(passwordEncoder.encode("1234"));
        appUser.setVerifyCode("1111");
        appUser.setFolders(new ArrayList<>());
        AppUser fans1 = createUser("alan@gmail.com", "Alan");
        fans1.getFans().add(appUser.getEmail());
        userRepository.insert(fans1);
        AppUser fans2 = createUser("eva@gmail.com","Eva");
        fans2.getFans().add(appUser.getEmail());
        userRepository.insert(fans2);
        AppUser fans3 = createUser("tara@gmail.com","Tara");
        userRepository.insert(fans3);
        ArrayList<String> fans = new ArrayList<>();
        fans.add(fans1.getEmail());
        fans.add(fans2.getEmail());
        fans.add(fans3.getEmail());
        appUser.setFans(fans);
        ArrayList<String> subscribers = new ArrayList<>();
        subscribers.add(fans1.getEmail());
        subscribers.add(fans2.getEmail());
        appUser.setSubscribe(subscribers);
        return appUser;
    }

    private AppUser createUser(String email, String name){
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setActivate(true);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode("1234"));
        appUser.setVerifyCode("1111");
        appUser.setFolders(new ArrayList<>());
        // 張哲瑋：因為我把appUser的建構子修改過，以下空陣列為原本建構子裡做的事情
        appUser.setStrength(new ArrayList<String>());
        appUser.setSubscribe(new ArrayList<String>());
        appUser.setBell(new ArrayList<String>());
        appUser.setFans(new ArrayList<String>());
        appUser.setHeadshotPhoto(null);
        return appUser;
    }
    @Test
    public void testGetAUserFollowersByEmail ()throws Exception{
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser fans1 = userRepository.findByEmail(appUser.getFans().get(0));
        AppUser fans2 = userRepository.findByEmail(appUser.getFans().get(1));
        AppUser fans3 = userRepository.findByEmail(appUser.getFans().get(2));
        mockMvc.perform(get("/followers/yitingwu.1030@gmail.com")
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followers.[0].userObjEmail").value(fans1.getEmail()))
                .andExpect(jsonPath("$.followers.[0].userObjName").value(fans1.getName()))
                .andExpect(jsonPath("$.followers.[0].userObjAvatar").value(fans1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.followers.[1].userObjEmail").value(fans2.getEmail()))
                .andExpect(jsonPath("$.followers.[1].userObjName").value(fans2.getName()))
                .andExpect(jsonPath("$.followers.[1].userObjAvatar").value(fans2.getHeadshotPhoto()))
                .andExpect(jsonPath("$.followers.[2].userObjEmail").value(fans3.getEmail()))
                .andExpect(jsonPath("$.followers.[2].userObjName").value(fans3.getName()))
                .andExpect(jsonPath("$.followers.[2].userObjAvatar").value(fans3.getHeadshotPhoto()));
    }

    @Test
    public void testGetAUserFollowingByEmail() throws Exception{
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser following1= userRepository.findByEmail(appUser.getSubscribe().get(0));
        AppUser following2 = userRepository.findByEmail(appUser.getSubscribe().get(1));
        mockMvc.perform(get("/following/yitingwu.1030@gmail.com")
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.following.[0].userObjEmail").value(following1.getEmail()))
                .andExpect(jsonPath("$.following.[0].userObjName").value(following1.getName()))
                .andExpect(jsonPath("$.following.[0].userObjAvatar").value(following1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.following.[1].userObjEmail").value(following2.getEmail()))
                .andExpect(jsonPath("$.following.[1].userObjName").value(following2.getName()))
                .andExpect(jsonPath("$.following.[1].userObjAvatar").value(following2.getHeadshotPhoto()));
    }

    @Test
    public void testFollow()throws Exception{
        AppUser follower = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beFollowed = userRepository.findByEmail("tara@gmail.com");
        mockMvc.perform(put("/follow/"+follower.getEmail()+"/"+beFollowed.getEmail())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        //檢查Follower's subscribe
        if(!userRepository.findByEmail(follower.getEmail()).getSubscribe().contains(beFollowed.getEmail())){
            throw new Exception();
        }
        //檢查BeFollowed's fans
        if(!userRepository.findByEmail(beFollowed.getEmail()).getFans().contains(follower.getEmail())){
            throw new Exception();
        }
    }

    @Test
    public void testUnFollow()throws Exception{
        AppUser wantUnFollow = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beUnFollowed = userRepository.findByEmail("alan@gmail.com");
        mockMvc.perform(put("/unfollow/"+wantUnFollow.getEmail()+"/"+ beUnFollowed.getEmail())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        if(userRepository.findByEmail(wantUnFollow.getEmail()).getSubscribe().contains(beUnFollowed.getEmail())){
            throw new Exception();
        }
        if(userRepository.findByEmail(beUnFollowed.getEmail()).getFans().contains(wantUnFollow.getEmail())){
            throw new Exception();
        }
    }
    @AfterEach
    public  void clear(){
        userRepository.deleteAll();
    }
}
