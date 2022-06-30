package ntou.notesharedevbackend.tagGeneration;


import org.manlier.analysis.jieba.JiebaSegmenter;
import org.manlier.analysis.jieba.SegToken;
import org.manlier.analysis.jieba.WordDictionary;
import org.manlier.analysis.jieba.dao.FileDictSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class BetterJieba {
    private JiebaSegmenter segmenter;
    private WordDictionary dict;    //jieba字典
    private Map<String, Integer> sortedCountWordFreq;   //jieba切字結果
    private Map<String, Integer> dictWordMap;   //自身字典Hashmap
    String dictPath = "./././././dict/jieba.dict";
    String sentence;

    public BetterJieba(String sentence) {
        this.sentence = removeAllMarks(sentence);
        segmenter = new JiebaSegmenter();
        dict = WordDictionary.getInstance();
        dictWordMap = new HashMap<>();

        try {
            dict.init(new FileDictSource(Paths.get(dictPath)));
            loadDictWordMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String removeAllMarks(String sentence) {
        return sentence.replaceAll("[~!@#$%^&*)(/_+=-]", "")
                .replaceAll("[<>。:：‧．、?？_；—｛｝「）（」…·，.,']", "")
                .replaceAll("[\\d]", "")
                .replaceAll("\\n", "")
                .replaceAll("\\t", "")
                .replaceAll("\\[", "")
                .replaceAll("\\]", "");
    }

    public void loadDictWordMap() {
        try {
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader(dictPath));
            String currentLine = reader.readLine();
            dictWordMap.clear();

            while (currentLine != null) {
                String[] strArr = currentLine.split(" ");
                if (strArr.length > 0) {
                    dictWordMap.put(strArr[0], Integer.parseInt(strArr[1]));
                    currentLine = reader.readLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWordToDict(String word) {
        if (!dict.containsWord(word))
            writeToDict(word + " 3" + "\n");
        else
            modifyWordFreq(word);

        //reload user dict
        try {
            dict.loadUserDict(new FileDictSource(Paths.get(dictPath)));
            loadDictWordMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToDict(String word) {
        try {
            Files.write(Paths.get(dictPath), word.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modifyWordFreq(String word) {
        dictWordMap.put(word, dictWordMap.get(word) + 1);

        try {
            String modifiedFileContent = "";
            BufferedWriter writer = new BufferedWriter(new FileWriter(dictPath));
            for (Map.Entry<String, Integer> map : dictWordMap.entrySet()) {
                String key = map.getKey();
                int value = map.getValue();
                String line = key + " " + value + System.lineSeparator();
                modifiedFileContent += line;
            }

            writer.write(modifiedFileContent);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> showPossibleWord(String word) {
        List<String> wordList = new ArrayList<>();
        for (Map.Entry<String, Integer> map : dictWordMap.entrySet()) {
            String key = map.getKey();
            if (key.contains(word))
                wordList.add(key);
        }

        return wordList;
    }

    public void countAndSort() {
        List<SegToken> newArr = segmenter.process(this.sentence, JiebaSegmenter.SegMode.INDEX);
        HashMap<String, Integer> countWordFreq = new HashMap<>();
        for (SegToken element : newArr) {
            String key = element.word;
            if (countWordFreq.containsKey(key))
                countWordFreq.put(key, countWordFreq.get(key) + 1);
            else
                countWordFreq.put(key, 1);
        }

        //order: true -> ASC; false -> DES
        sortedCountWordFreq = sortByValue(countWordFreq, false);
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

    public boolean checkWordInDict(String word) {
        for (Map.Entry<String, Integer> map : dictWordMap.entrySet()) {
            if (map.getKey().equals(word))
                return true;
        }
        return false;
    }

    public HashMap<String, Integer> wordSuggestion() {
        HashMap<String, Integer> wordMap = new HashMap<>();
        final int TOP_FREQ = 5;
        int i = 0;

        countAndSort();
        for (Map.Entry<String, Integer> map : sortedCountWordFreq.entrySet()) {
            if (i == TOP_FREQ)  //找到前五名
                break;

            String key = map.getKey();
            int value = map.getValue();
            if (key.length() <= 1)  //去除連接詞
                continue;
            if (checkWordInDict(key)) {
                wordMap.put(key, value);
                i++;
            }
        }

        return wordMap;
    }

    public void printMap() {
        System.out.println("詞頻統計: \n");
        sortedCountWordFreq.forEach((key, value) -> System.out.println("詞 : " + key + ", 次數 : " + value));
    }
}

