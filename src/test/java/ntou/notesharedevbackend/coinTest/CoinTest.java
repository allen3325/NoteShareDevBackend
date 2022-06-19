package ntou.notesharedevbackend.coinTest;

import io.swagger.v3.oas.models.security.SecurityScheme;
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

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private AppUser createUser(){
        AppUser appUser = new AppUser();
        appUser.setEmail("yitingwu.1030@gmail.com");
        appUser.setActivate(true);
        appUser.setName("Ting");
        appUser.setPassword(passwordEncoder.encode("1234"));
        appUser.setVerifyCode("1111");
        appUser.setFolders(new ArrayList<>());
        appUser.setCoin(300);
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

    }

    @Test
    public void testBuyNoteWithNotEnoughCoin() throws Exception{

    }

    @AfterEach
    public  void clear(){
        userRepository.deleteAll();
    }

}
