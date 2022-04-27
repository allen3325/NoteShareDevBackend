package ntou.notesharedevbackend.basicFunctionModule.controller;

import ntou.notesharedevbackend.basicFunctionModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping(value = "/picture", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadFileController {
    @Autowired
    UploadFileService uploadImageService;

    @PostMapping("/{noteID}/{version}")
    public ResponseEntity<String[]> uploadImage(
            @PathVariable("noteID") String noteID, @PathVariable("version") String version, @RequestParam("files") MultipartFile[] uploadfiles) {
        String[] response = uploadImageService.uploadImage(uploadfiles, noteID, version);

        return ResponseEntity.ok(response);
    }
}
