package ntou.notesharedevbackend.followTest;

import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        userRepository.deleteAll();
        AppUser appUser = createInitUser();
        userRepository.insert(appUser);
    }

    private AppUser createInitUser() {
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
        fans1 = userRepository.insert(fans1);
        AppUser fans2 = createUser("eva@gmail.com", "Eva");
        fans2.getFans().add(appUser.getEmail());
        fans2 = userRepository.insert(fans2);
        AppUser fans3 = createUser("tara@gmail.com", "Tara");
        fans3 = userRepository.insert(fans3);
        ArrayList<String> fans = new ArrayList<>();
        fans.add(fans1.getEmail());
        fans.add(fans2.getEmail());
        fans.add(fans3.getEmail());
        appUser.setFans(fans);
        ArrayList<String> subscribers = new ArrayList<>();
        subscribers.add(fans1.getEmail());
        subscribers.add(fans2.getEmail());
        appUser.setSubscribe(subscribers);
        ArrayList<String> bells = new ArrayList<>();
        bells.add(fans1.getEmail());
        bells.add(fans2.getEmail());
        appUser.setBell(bells);
        fans1.getBelledBy().add(appUser.getEmail());
        userRepository.save(fans1);
        fans2.getBelledBy().add(appUser.getEmail());
        userRepository.save(fans2);
        return appUser;
    }

    private AppUser createUser(String email, String name) {
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
        appUser.setBelledBy(new ArrayList<>());
        appUser.setNotification(new ArrayList<>());
        appUser.setUnreadMessageCount(0);
        return appUser;
    }

    @Test
    public void testGetAUserFollowersByEmail() throws Exception {
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
    public void testGetAUserFollowingByEmail() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser following1 = userRepository.findByEmail(appUser.getSubscribe().get(0));
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
    public void testFollow() throws Exception {
        AppUser follower = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beFollowed = userRepository.findByEmail("tara@gmail.com");
        mockMvc.perform(put("/follow/" + follower.getEmail() + "/" + beFollowed.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        //檢查Follower's subscribe
        if (!userRepository.findByEmail(follower.getEmail()).getSubscribe().contains(beFollowed.getEmail())) {
            throw new Exception();
        }
        //檢查BeFollowed's fans
        if (!userRepository.findByEmail(beFollowed.getEmail()).getFans().contains(follower.getEmail())) {
            throw new Exception();
        }
    }

    @Test
    public void testUnFollow() throws Exception {
        AppUser wantUnFollow = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beUnFollowed = userRepository.findByEmail("alan@gmail.com");
        mockMvc.perform(put("/unfollow/" + wantUnFollow.getEmail() + "/" + beUnFollowed.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));
        if (userRepository.findByEmail(wantUnFollow.getEmail()).getSubscribe().contains(beUnFollowed.getEmail())) {
            throw new Exception();
        }
        if (userRepository.findByEmail(beUnFollowed.getEmail()).getFans().contains(wantUnFollow.getEmail())) {
            throw new Exception();
        }
    }

    @Test
    public void testBell() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beBell = userRepository.findByEmail("tara@gmail.com");
        mockMvc.perform(put("/bell/" + appUser.getEmail() + "/" + beBell.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (!userRepository.findByEmail(appUser.getEmail()).getBell().contains(beBell.getEmail())) {
            throw new Exception("Bell Test : user's bell does not update");
        }
        if (!userRepository.findByEmail(beBell.getEmail()).getBelledBy().contains(appUser.getEmail())) {
            throw new Exception("Bell Test : beBell's BellBy does not update");
        }
    }

    @Test
    public void testCancelBell() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser beCancelBell = userRepository.findByEmail("tara@gmail.com");
        appUser.getBell().add(beCancelBell.getEmail());
        beCancelBell.getBelledBy().add(appUser.getEmail());
        userRepository.save(beCancelBell);
        userRepository.save(appUser);
        mockMvc.perform(put("/cancelBell/" + appUser.getEmail() + "/" + beCancelBell.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Success"));

        if (userRepository.findByEmail(appUser.getEmail()).getBell().contains(beCancelBell.getEmail())) {
            throw new Exception("Bell Test : user's bell does not update");
        }
        if (userRepository.findByEmail(beCancelBell.getEmail()).getBelledBy().contains(appUser.getEmail())) {
            throw new Exception("Bell Test : beCancelBell's beBell does not update");
        }
    }

    @Test
    public void testGetUserBellByEmail() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser bell1 = userRepository.findByEmail(appUser.getBell().get(0));
        AppUser bell2 = userRepository.findByEmail(appUser.getBell().get(1));
        mockMvc.perform(get("/bell/yitingwu.1030@gmail.com")
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.[0].userObjEmail").value(bell1.getEmail()))
                .andExpect(jsonPath("$.res.[0].userObjName").value(bell1.getName()))
                .andExpect(jsonPath("$.res.[0].userObjAvatar").value(bell1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.res.[1].userObjEmail").value(bell2.getEmail()))
                .andExpect(jsonPath("$.res.[1].userObjName").value(bell2.getName()))
                .andExpect(jsonPath("$.res.[1].userObjAvatar").value(bell2.getHeadshotPhoto()));
    }

    @Test
    public void testGetUserBellByByEmail() throws Exception {
        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        AppUser bell1 = userRepository.findByEmail(appUser.getBell().get(0));
        mockMvc.perform(get("/bellBy/" + bell1.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res.[0].userObjEmail").value(appUser.getEmail()))
                .andExpect(jsonPath("$.res.[0].userObjName").value(appUser.getName()))
                .andExpect(jsonPath("$.res.[0].userObjAvatar").value(appUser.getHeadshotPhoto()));
    }

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
    }
}
