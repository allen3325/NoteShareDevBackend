package ntou.notesharedevbackend.noteModule.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "plagiarismdictionary")
public class Plagiarismdictionary {
    private String word;
    private Integer frequency;

    public Plagiarismdictionary(String word, Integer frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
}
