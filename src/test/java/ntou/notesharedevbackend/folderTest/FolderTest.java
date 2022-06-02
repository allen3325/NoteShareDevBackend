package ntou.notesharedevbackend.folderTest;


import ntou.notesharedevbackend.repository.FolderRepository;
import org.json.JSONObject;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FolderTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FolderRepository folderRepository;

    @BeforeEach
    public void init(){
        folderRepository.deleteAll();
    }

    @Test
    public void testCreateFolder() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        JSONObject request = new JSONObject()
                .put("folderName","OS")
                .put("public",true)
                .put("path","/Favorite/OS")
                .put("parent","6290ca8d924d661c092f3f6e");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/folder/allen3325940072@gmail.com")
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.folderName").value(request.getString("folderName")))
                .andExpect(jsonPath("$.public").value(request.getBoolean("public")))
                .andExpect(jsonPath("$.path").value(request.getString("path")))
                .andExpect(jsonPath("$.parent").value(request.getString("parent")))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

}