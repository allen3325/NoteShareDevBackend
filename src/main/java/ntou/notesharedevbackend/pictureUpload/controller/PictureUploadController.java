package ntou.notesharedevbackend.pictureUpload.controller;

import ntou.notesharedevbackend.pictureUpload.entity.PicDTO;
import ntou.notesharedevbackend.pictureUpload.entity.ReceivePicUpload;
import ntou.notesharedevbackend.pictureUpload.service.PictureUploadService;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/picture/imgur", produces = MediaType.APPLICATION_JSON_VALUE)
public class PictureUploadController {
    @Autowired
    private PictureUploadService pictureUploadService;

    @PutMapping
    public ResponseEntity<Object> uploadPicture(@RequestBody PicDTO request) {
        ReceivePicUpload response = pictureUploadService.uploadPicture(request);

        Map<String, Object> res = new HashMap<>();

        res.put("link", response.getData().getLink());
        res.put("success", response.getSuccess());
        res.put("status", response.getStatus());

        return ResponseEntity.status(response.getStatus()).body(res);
    }
}
