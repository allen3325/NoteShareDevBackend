package ntou.notesharedevbackend.tagGeneration.service;

import ntou.notesharedevbackend.noteModule.entity.*;
import ntou.notesharedevbackend.noteModule.service.*;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.tagGeneration.*;
import ntou.notesharedevbackend.tagGeneration.entity.Dictionary;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class TagService {
    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private DictionaryRepository dictionaryRepository;

    public List<String> getWordSuggestion(String noteID) {
        Note note = noteService.getNote(noteID);
        ArrayList<VersionContent> versionContent = note.getVersion();
        int newestVersion = versionContent.size() - 1;
        ArrayList<Content> contents = versionContent.get(newestVersion).getContent();
        int newestContent = contents.size() - 1;
        String text = contents.get(newestContent).getMycustom_components();
        text = text.replaceAll("\"","");    // get rid of double quotation marks

        TagGeneration tagGeneration = new TagGeneration();
        List<String> generatedTags = tagGeneration.wordSuggestion(text);
        ArrayList<String> tags = note.getTag();
        ArrayList<String> hiddenTags = note.getHiddenTag();
        if (!tags.isEmpty()) {
            for (String generatedTag : generatedTags) {
                if (tags.contains(generatedTag))
                    continue;
                tags.add(generatedTag);
            }
        }
        if (!hiddenTags.isEmpty()) {
            for (String generatedTag : generatedTags) {
                if (hiddenTags.contains(generatedTag))
                    continue;
                hiddenTags.add(generatedTag);
            }
        }

        noteRepository.save(note);
        return generatedTags;
    }

    public List<String> getPossibleInputWord(String word) {
        TagGeneration tagGeneration = new TagGeneration();
        return tagGeneration.showPossibleWords(word);
    }

    public void addWordToDict(String word) {
        TagGeneration tagGeneration = new TagGeneration();
        tagGeneration.addWordToDict(word);

        Dictionary alreadyExist = dictionaryRepository.findByWord(word);
        if (alreadyExist == null) {
            Dictionary dictionary = new Dictionary();
            if (Character.isIdeographic(word.charAt(0)))
                dictionary.setType("jieba");
            else
                dictionary.setType("lingpipe");
            dictionary.setWord(word);
            dictionary.setFrequency(3);
            dictionaryRepository.insert(dictionary);
        }
        else {
            alreadyExist.setFrequency(alreadyExist.getFrequency() + 1);
            dictionaryRepository.save(alreadyExist);
        }
    }

    public void updateTags(ArrayList<String> tags, String noteID) {
        Note note = noteService.getNote(noteID);
        for (String tag : tags) {
            if (note.getTag().contains(tag))    //重複新增的tag不更改字典
                continue;
            addWordToDict(tag);
        }

        note.setTag(tags);
        noteRepository.save(note);
    }
}
