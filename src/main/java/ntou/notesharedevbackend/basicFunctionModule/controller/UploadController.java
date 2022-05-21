package ntou.notesharedevbackend.basicFunctionModule.controller;

import ntou.notesharedevbackend.basicFunctionModule.entity.*;
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

    @PostMapping("/picture")
    public ResponseEntity<String[]> uploadImage(@ModelAttribute UploadDTO uploadDTO) {
        String[] response = uploadImageService.uploadImage(uploadDTO.getFiles(), uploadDTO.getNoteID(), uploadDTO.getVersion());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/file")
    public ResponseEntity<String[]> uploadFile(@ModelAttribute UploadDTO uploadDTO) {
        String[] response = uploadImageService.uploadFile(uploadDTO.getFiles(), uploadDTO.getNoteID(), uploadDTO.getVersion());

        return ResponseEntity.ok(response);
    }
}