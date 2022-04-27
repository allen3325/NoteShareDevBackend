package ntou.notesharedevbackend.postModule.controller;

import ntou.notesharedevbackend.postModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/publish/post", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublishPostController {
    @Autowired
    private PublishPostService publishPostService;

    @PutMapping("/{postID}")
    public ResponseEntity modifyPublishStatus(@PathVariable("postID") String id) {
        publishPostService.modifyPublishStatus(id);
        return ResponseEntity.noContent().build();
    }
}
