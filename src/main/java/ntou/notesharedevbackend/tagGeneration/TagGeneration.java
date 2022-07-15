package ntou.notesharedevbackend.tagGeneration;

import java.util.*;

public class TagGeneration {
    private final BetterJieba betterJieba;
    private final LingPipe lingPipe;

    public TagGeneration() {
        betterJieba = new BetterJieba();
        lingPipe = new LingPipe();
    }

    public void addWordToDict(String word) {
        //Chinese
        if (Character.isIdeographic(word.charAt(0))) {
            betterJieba.addWordToDict(word);
        } else {
            lingPipe.addWordToDictionary(word);
        }
    }

    public List<String> showPossibleWords(String word) {
        List<String> englishAndChinesePossibleWords = new ArrayList<>();
        List<String> chinesePossibleWords = betterJieba.showPossibleWord(word);
        List<String> englishPossibleWords = lingPipe.showPossibleWord(word);
        englishAndChinesePossibleWords.addAll(chinesePossibleWords);
        englishAndChinesePossibleWords.addAll(englishPossibleWords);

        return englishAndChinesePossibleWords;
    }

    public void chunkDictionary(String text) {
        lingPipe.testChunkDictionary(text);
        betterJieba.countAndSort(text);
    }

    public List<String> wordSuggestion(String text) {
        chunkDictionary(text);

        HashMap<String, Integer> chineseWordSuggestion = betterJieba.wordSuggestion(text);
        HashMap<String, Integer> englishWordSuggestion = lingPipe.wordSuggestion();
        TreeMap<String, Integer> chineseAndEnglishWordSuggestion = new TreeMap<>();
        chineseAndEnglishWordSuggestion.putAll(chineseWordSuggestion);
        chineseAndEnglishWordSuggestion.putAll(englishWordSuggestion);

        Map<String, Integer> sortedMap = sortByValues(chineseAndEnglishWordSuggestion);

        List<String> wordList = new ArrayList<>();
        final int TOP_FREQ = 5;
        int i = 0;
        for (Map.Entry<String, Integer> map : sortedMap.entrySet()) {
            if (i == TOP_FREQ)
                break;
            wordList.add(map.getKey());

            i++;
        }

        return wordList;
    }

    public static <K, V extends Comparable<V>> Map<K, V>
    sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator =
                new Comparator<K>() {
                    public int compare(K k1, K k2) {
                        int compare =
                                map.get(k2).compareTo(map.get(k1));
                        if (compare == 0)
                            return 1;
                        else
                            return compare;
                    }
                };

        Map<K, V> sortedByValues =
                new TreeMap<K, V>(valueComparator);
        sortedByValues.putAll(map);
        return sortedByValues;
    }
}
