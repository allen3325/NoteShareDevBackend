package ntou.notesharedevbackend.notificationTest;

import ntou.notesharedevbackend.notificationModule.entity.Message;
import ntou.notesharedevbackend.notificationModule.entity.MessageReturn;
import ntou.notesharedevbackend.repository.UserRepository;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.entity.UserObj;
import org.bson.types.ObjectId;
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
public class NotificationTest {
    private HttpHeaders httpHeaders;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void init() {
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
        appUser.setNotification(new ArrayList<>());
        appUser.setUnreadMessageCount(2);
        return userRepository.insert(appUser);
    }

    private Message createMessage() {
        Message message = new Message();
        message.setMessage("content");
        message.setId(new ObjectId().toString());
        message.setType("type");
        message.setReceiverEmail("yitingwu.1030@gmail.com");
        AppUser appUser = userRepository.findByEmail("user1@gmail.com");
        UserObj userObj = new UserObj();
        userObj.setUserObjEmail(appUser.getEmail());
        userObj.setUserObjName(appUser.getName());
        userObj.setUserObjAvatar(appUser.getHeadshotPhoto());
        message.setUserObj(userObj);
        return message;
    }

    private MessageReturn messageTurnToMessageReturn(Message message) {
        return new MessageReturn(message);
    }

    @Test
    public void testGetNotification() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        AppUser sender = createUser("user1@gmail.com", "User1");
        MessageReturn message1 = messageTurnToMessageReturn(createMessage());
        MessageReturn message2 = messageTurnToMessageReturn(createMessage());
        MessageReturn message3 = messageTurnToMessageReturn(createMessage());
        appUser.getNotification().add(message1);
        appUser.getNotification().add(message2);
        appUser.getNotification().add(message3);
        appUser.setUnreadMessageCount(3);
        userRepository.save(appUser);
        mockMvc.perform(get("/notification/" + appUser.getEmail())
                        .headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notification.[0].message").value(message1.getMessage()))
                .andExpect(jsonPath("$.notification.[0].userObj.userObjEmail").value(sender.getEmail()))
                .andExpect(jsonPath("$.notification.[0].userObj.userObjName").value(sender.getName()))
                .andExpect(jsonPath("$.notification.[0].userObj.userObjAvatar").value(sender.getHeadshotPhoto()))
                .andExpect(jsonPath("$.notification.[0].date").hasJsonPath())
                .andExpect(jsonPath("$.notification.[1].message").value(message2.getMessage()))
                .andExpect(jsonPath("$.notification.[1].userObj.userObjEmail").value(sender.getEmail()))
                .andExpect(jsonPath("$.notification.[1].userObj.userObjName").value(sender.getName()))
                .andExpect(jsonPath("$.notification.[1].userObj.userObjAvatar").value(sender.getHeadshotPhoto()))
                .andExpect(jsonPath("$.notification.[1].date").hasJsonPath())
                .andExpect(jsonPath("$.notification.[2].message").value(message3.getMessage()
                ))
                .andExpect(jsonPath("$.notification.[2].userObj.userObjEmail").value(sender.getEmail()))
                .andExpect(jsonPath("$.notification.[2].userObj.userObjName").value(sender.getName()))
                .andExpect(jsonPath("$.notification.[2].userObj.userObjAvatar").value(sender.getHeadshotPhoto()))
                .andExpect(jsonPath("$.notification.[2].date").hasJsonPath());
    }

    @Test
    public void testClearUnreadMessage() throws Exception {
        AppUser appUser = createUser("yitingwu.1030@gmail.com", "Ting");
        AppUser sender = createUser("user1@gmail.com", "User1");
        MessageReturn message1 = messageTurnToMessageReturn(createMessage());
        MessageReturn message2 = messageTurnToMessageReturn(createMessage());
        MessageReturn message3 = messageTurnToMessageReturn(createMessage());
        appUser.getNotification().add(message1);
        appUser.getNotification().add(message2);
        appUser.getNotification().add(message3);
        appUser.setUnreadMessageCount(3);
        userRepository.save(appUser);
        mockMvc.perform(put("/notification/unreadMessage/" + appUser.getEmail())
                        .headers(httpHeaders))
                .andExpect(jsonPath("$.msg").value("Success"));

        if (!userRepository.findById(appUser.getId()).get().getUnreadMessageCount().equals(0)) {
            throw new Exception("Notification Test : user unreadMessageCount does not equal zero");
        }
    }

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
    }
}
