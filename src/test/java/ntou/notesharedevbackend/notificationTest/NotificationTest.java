package ntou.notesharedevbackend.notificationTest;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.notificationModule.entity.Message;
import ntou.notesharedevbackend.repository.FolderRepository;
import ntou.notesharedevbackend.repository.NoteRepository;
import ntou.notesharedevbackend.repository.PostRepository;
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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NotificationTest {
    private HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void init(){
        userRepository.deleteAll();
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    private AppUser createUser(String email, String name) {
        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setActivate(true);
        appUser.setName(name);
        appUser.setPassword(passwordEncoder.encode("1234"));
        ArrayList<String> folderList = new ArrayList<>();
        appUser.setFolders(folderList);
        appUser.setCoin(300);
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(createMessage("first"));
        messages.add(createMessage("second"));
        messages.add(createMessage("third"));
        appUser.setNotification(messages);
        appUser.setUnreadMessageCount(2);
        return userRepository.insert(appUser);
    }

    private Message createMessage (String content){
        Message message = new Message();
        message.setContent(content);
        message.setTime("time");
        message.setUser("user1@gmail.com");
        return message;
    }

    @Test
    public void testGetNotification() throws Exception{
        AppUser appUser = createUser("yitingwu.1030@gmail.com","Ting");
//        userRepository.insert(appUser);
        AppUser appUser1 = createUser("user1@gmail.com","User1");
//        userRepository.insert(appUser1);
//        AppUser appUser = userRepository.findByEmail("yitingwu.1030@gmail.com");
        mockMvc.perform(get("/notification/"+appUser.getEmail())
                .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notification.[0].content").value(appUser.getNotification().get(0).getContent()))
                .andExpect(jsonPath("$.notification.[0].messageObj.userObjEmail").value(appUser1.getEmail()))
                .andExpect(jsonPath("$.notification.[0].messageObj.userObjName").value(appUser1.getName()))
                .andExpect(jsonPath("$.notification.[0].messageObj.userObjAvatar").value(appUser1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.notification.[0].time").value(appUser.getNotification().get(0).getTime()))
                .andExpect(jsonPath("$.notification.[1].content").value(appUser.getNotification().get(1).getContent()))
                .andExpect(jsonPath("$.notification.[1].messageObj.userObjEmail").value(appUser1.getEmail()))
                .andExpect(jsonPath("$.notification.[1].messageObj.userObjName").value(appUser1.getName()))
                .andExpect(jsonPath("$.notification.[1].messageObj.userObjAvatar").value(appUser1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.notification.[1].time").value(appUser.getNotification().get(1).getTime()))
                .andExpect(jsonPath("$.notification.[2].content").value(appUser.getNotification().get(2).getContent()))
                .andExpect(jsonPath("$.notification.[2].messageObj.userObjEmail").value(appUser1.getEmail()))
                .andExpect(jsonPath("$.notification.[2].messageObj.userObjName").value(appUser1.getName()))
                .andExpect(jsonPath("$.notification.[2].messageObj.userObjAvatar").value(appUser1.getHeadshotPhoto()))
                .andExpect(jsonPath("$.notification.[2].time").value(appUser.getNotification().get(2).getTime()));

        if(!userRepository.findById(appUser.getId()).get().getUnreadMessageCount().equals(0)){
            throw new Exception("Notification Test : user unreadMessageCount does not equal zero");
        }
    }

//    @AfterEach
    public void clear(){
        userRepository.deleteAll();
    }
}
