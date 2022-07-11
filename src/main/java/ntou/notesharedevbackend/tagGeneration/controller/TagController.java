package ntou.notesharedevbackend.tagGeneration.controller;

import com.fasterxml.jackson.databind.*;
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

    @PutMapping("/wordSuggestion/{noteID}")
    public ResponseEntity<Object> getWordSuggestion(@PathVariable("noteID") String noteID,
                                                    @RequestBody Map<String, String> request) {
        String text = request.get("text");
        List<String> generatedTags = tagService.getWordSuggestion(noteID, text);
        Map<String, Object> res = new HashMap<>();
        res.put("generatedTags", generatedTags);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/showPossibleWord")
    public ResponseEntity<Object> getPossibleInputWord(@RequestParam(value = "word", defaultValue = "") String word) {
        List<String> possibleInputWord = tagService.getPossibleInputWord(word);
        Map<String, Object> res = new HashMap<>();
        res.put("possibleInputTags", possibleInputWord);
        return ResponseEntity.ok(res);
    }
//
    @PutMapping("/addWordToDict")
    public ResponseEntity<Object> addWordToDict(@RequestBody Map<String, String[]> request) {
        String[] words = request.get("words");
        for (String word: words)
            tagService.addWordToDict(word);

        Map<String, Object> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.ok(res);
    }

}
