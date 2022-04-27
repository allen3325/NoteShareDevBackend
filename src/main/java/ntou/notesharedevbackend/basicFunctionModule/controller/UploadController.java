package ntou.notesharedevbackend.basicFunctionModule.controller;

import ntou.notesharedevbackend.basicFunctionModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {
    @Autowired
    UploadService uploadImageService;

    @PostMapping("/picture/{noteID}/{version}")
    public ResponseEntity<String[]> uploadImage(
            @PathVariable("noteID") String noteID, @PathVariable("version") String version, @RequestParam("files") MultipartFile[] uploadfiles) {
        String[] response = uploadImageService.uploadImage(uploadfiles, noteID, version);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/file/{noteID}/{version}")
    public ResponseEntity<String[]> uploadFile(
            @PathVariable("noteID") String noteID, @PathVariable("version") String version, @RequestParam("files") MultipartFile[] uploadfiles) {
        String[] response = uploadImageService.uploadFile(uploadfiles, noteID, version);

        return ResponseEntity.ok(response);
    }
}
