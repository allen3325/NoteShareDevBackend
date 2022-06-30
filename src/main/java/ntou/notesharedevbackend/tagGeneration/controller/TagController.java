package ntou.notesharedevbackend.tagGeneration.controller;

import ntou.notesharedevbackend.tagGeneration.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/note/tag",produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/wordSuggestion/{noteID}")
    public ResponseEntity<Object> getWordSuggestion(@PathVariable("noteID") String noteID) {

    }
//
//    @GetMapping("/showPossibleWord/{noteID}")
//    public ResponseEntity<Object> getPossibleInputWord(@PathVariable("email") String email,
//                                                    @PathVariable("noteID") String noteID) {
//
//    }
//
//    @PutMapping("/addWordToDict/{noteID}")
//    public ResponseEntity<Object> addWordToDict(@PathVariable("email") String email,
//                                                  @PathVariable("noteID") String noteID) {
//
//    }

}
