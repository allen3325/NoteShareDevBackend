package ntou.notesharedevbackend.tagGeneration;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.dict.DictionaryEntry;
import com.aliasi.dict.ExactDictionaryChunker;
import com.aliasi.dict.MapDictionary;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.tagGeneration.entity.Dictionary;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class LingPipe {
    private final MapDictionary<String> myDict = new MapDictionary<>();
    private Map<String, Integer> chunkResult;
    private HashSet<String> recommendWords;
    String homePath = System.getProperty("user.home");
    private final String dictPath = homePath + "/dict/lingpipe.dict";
    private final double CHUNK_SCORE = 1.0;
    private final String CLASS_NAME = "tags";

    public LingPipe() {
        chunkResult = new HashMap<>();
        recommendWords = new HashSet<>();
        loadDictionary();
    }

    public void loadDictionary() {
        try {
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader(dictPath));
            String currentLine = reader.readLine();

            while (currentLine != null) {
                myDict.addEntry(new DictionaryEntry<String>(currentLine, this.CLASS_NAME, this.CHUNK_SCORE));
                recommendWords.add(currentLine);

                currentLine = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWordToDictionary(String word) {
        //check if the word is already in dictionary
        if (checkWordInDict(word))
            return;

        word = word + "\n";
        myDict.addEntry(new DictionaryEntry<String>(word, this.CLASS_NAME, this.CHUNK_SCORE));
        recommendWords.add(word);
        try {
            Files.write(Paths.get(dictPath), word.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkWordInDict(String word) {
        for (String wordInDict : recommendWords) {
            if (wordInDict.equals(word.toLowerCase()))
                return true;
        }
        return false;
    }

    public List<String> showPossibleWord(String word) {
        List<String> wordList = new ArrayList<>();
        for (String set : recommendWords) {
            if (set.toLowerCase().contains(word.toLowerCase()))
                wordList.add(set);
        }

        return wordList;
    }

    public void chunk(ExactDictionaryChunker chunker, String text) {
        HashMap<String, Integer> countWordFreq = new HashMap<>();
        Chunking chunking = chunker.chunk(text);
        for (Chunk chunk : chunking.chunkSet()) {
            int start = chunk.start();
            int end = chunk.end();
            String phrase = text.substring(start,end).toLowerCase();
            if (countWordFreq.containsKey(phrase))
                countWordFreq.put(phrase, countWordFreq.get(phrase) + 1);
            else
                countWordFreq.put(phrase, 1);
        }

        //order: true -> ASC; false -> DES
        chunkResult = sortByValue(countWordFreq, false);
    }

    public void testChunkDictionary(String sentence) {
        //filter out Chinese words
        for (int i = 0; i < sentence.length(); i++) {
            if (Character.isIdeographic(sentence.charAt(i)))
                sentence = sentence.replace(sentence.charAt(i), ' ');
        }

        ExactDictionaryChunker dictionaryChunkerTF =
                new ExactDictionaryChunker(myDict, IndoEuropeanTokenizerFactory.INSTANCE, true, false);
        chunk(dictionaryChunkerTF, sentence);
    }

    public HashMap<String, Integer> wordSuggestion() {
        HashMap<String, Integer> wordMap = new HashMap<>();
        final int TOP_FREQ = 5;
        int i = 0;
        for (Map.Entry<String, Integer> map : chunkResult.entrySet()) {
            if (i == TOP_FREQ)
                break;
            wordMap.put(map.getKey(), map.getValue());
            i++;
        }

        return wordMap;
    }

    public Map<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean order) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    public void printMap() {
        System.out.println("詞頻統計: ");
        chunkResult.forEach((key, value) -> System.out.println("詞 : " + key + ", 次數 : " + value));
    }
}

