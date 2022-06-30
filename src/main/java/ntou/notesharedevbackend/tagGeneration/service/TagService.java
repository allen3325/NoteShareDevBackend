package ntou.notesharedevbackend.tagGeneration.service;

import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.noteNodule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class TagService {
    @Autowired
    private NoteService noteService;

//    public List<String> getWordSuggestion(String noteID) {
//        Note note = noteService.getNote(noteID);
//        ArrayList<VersionContent> versionContents = note.getVersion();
//        int latestVersion = versionContents.size() - 1;
//        VersionContent versionContent = versionContents.get(latestVersion);
//    }

//    public List<String> getPossibleInputWord(String noteID) {
//
//    }
//
    public void addWordToDict(String word) {

    }
}
