package ntou.notesharedevbackend.tagGeneration.controller;

import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.tagGeneration.entity.*;
import ntou.notesharedevbackend.tagGeneration.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/note/tag",produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    @Autowired
    private TagService tagService;
    @Autowired
    private DictionaryRepository dictionaryRepository;

//    @PostMapping("/tmp")
//    public void initData(@RequestBody Dictionary dictionary) {
//        dictionaryRepository.insert(dictionary);
//    }

//    @GetMapping("/wordSuggestion/{noteID}")
//    public ResponseEntity<Object> getWordSuggestion(@PathVariable("noteID") String noteID) {
//        Map<String, Object> map = new HashMap<>();
//
//        return ResponseEntity.ok(map);
//    }
//
//    @GetMapping("/showPossibleWord/{noteID}")
//    public ResponseEntity<Object> getPossibleInputWord(@PathVariable("noteID") String noteID) {
//
//    }
//
//    @PutMapping("/addWordToDict")
//    public ResponseEntity<Object> addWordToDict(@RequestBody String word) {
//
//    }

}
