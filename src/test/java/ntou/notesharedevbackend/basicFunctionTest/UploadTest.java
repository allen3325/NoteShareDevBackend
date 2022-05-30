package ntou.notesharedevbackend.basicFunctionTest;

import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.postModule.entity.*;
import ntou.notesharedevbackend.repository.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.mock.web.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UploadTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private NoteRepository noteRepository;

    public void createNote() {
        Note note = new Note();

    }

    @Before
    public void init() {
        noteRepository.deleteAll();
    }

    @Test
    public void testUploadFile() throws Exception {

        MockMultipartFile file = new MockMultipartFile("files", "filename.txt", "text/plain", "some xml".getBytes());
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart("/file")
                        .file(file)
                        .param("noteID", "4"))
                .andExpect(status().isOk());
    }
}
