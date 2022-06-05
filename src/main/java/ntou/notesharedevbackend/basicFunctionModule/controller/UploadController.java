package ntou.notesharedevbackend.basicFunctionModule.controller;

import io.swagger.v3.oas.annotations.*;
import ntou.notesharedevbackend.basicFunctionModule.entity.*;
import ntou.notesharedevbackend.basicFunctionModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadController {
    @Autowired
    UploadService uploadImageService;

    @Operation(summary = "Get images by note version", description = "thumbnailLink是這張圖片的url")
    @GetMapping("/picture/{noteID}/{version}")
    public ResponseEntity<Map<String, GetImageDTO[]>> getImage(@PathVariable("noteID") String noteID, @PathVariable("version") int version) {
        GetImageDTO[] images = uploadImageService.getImage(noteID, version);
        Map<String, GetImageDTO[]> map = new HashMap<>();
        map.put("images", images);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Upload image")
    @PostMapping("/picture")
    public ResponseEntity<Map<String, String[]>> uploadImage(@ModelAttribute UploadDTO uploadDTO) {
        String[] response = uploadImageService.uploadImage(uploadDTO.getFiles(), uploadDTO.getNoteID(), uploadDTO.getVersion());
        Map<String, String[]> map = new HashMap<>();
        map.put("imageID", response);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Upload file")
    @PostMapping("/file")
    public ResponseEntity<Map<String, String[]>> uploadFile(@ModelAttribute UploadDTO uploadDTO) {
        String[] response = uploadImageService.uploadFile(uploadDTO.getFiles(), uploadDTO.getNoteID(), uploadDTO.getVersion());
        Map<String, String[]> map = new HashMap<>();
        map.put("fileID", response);

        return ResponseEntity.ok(map);
    }
}