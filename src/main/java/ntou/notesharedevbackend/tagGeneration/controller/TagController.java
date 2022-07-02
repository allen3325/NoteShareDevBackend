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

//    @GetMapping("/wordSuggestion/{noteID}")
//    public ResponseEntity<Object> getWordSuggestion(@PathVariable("noteID") String noteID,
//                                                    @RequestBody Map<String, String> request) {
//
//        Map<String, Object> res = new HashMap<>();
//        res.put("generatedTags", possibleInputWord);
//        return ResponseEntity.ok(res);
//    }
//
    @GetMapping("/showPossibleWord")
    public ResponseEntity<Object> getPossibleInputWord(@RequestBody Map<String, String> request) {
        String word = request.get("word");
        List<String> possibleInputWord = tagService.getPossibleInputWord(word);
        Map<String, Object> res = new HashMap<>();
        res.put("possibleInputTags", possibleInputWord);
        return ResponseEntity.ok(res);
    }
//
    @PutMapping("/addWordToDict")
    public ResponseEntity<Object> addWordToDict(@RequestBody Map<String, String> request) {
        String word = request.get("word");
        tagService.addWordToDict(word);

        Map<String, Object> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

}
