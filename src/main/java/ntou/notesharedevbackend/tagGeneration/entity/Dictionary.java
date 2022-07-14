package ntou.notesharedevbackend.tagGeneration.entity;

import org.springframework.data.mongodb.core.mapping.*;

@Document(collection = "dictionary")
public class Dictionary {
    private String id;
    private String type;    //jieba, lingpipe
    private String word;
    private Integer frequency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
