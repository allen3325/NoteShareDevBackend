package ntou.notesharedevbackend.tagGeneration.config;

import ntou.notesharedevbackend.repository.*;
import ntou.notesharedevbackend.tagGeneration.entity.*;
import ntou.notesharedevbackend.tagGeneration.entity.Dictionary;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Configuration
public class TagConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(DictionaryRepository dictionaryRepository) {
        return args -> {
            String dictPath = "././././././dict";
            List<Dictionary> jieba = dictionaryRepository.findAllByType("jieba");
            List<Dictionary> lingpipe = dictionaryRepository.findAllByType("lingpipe");
            if (!Files.exists(Paths.get(dictPath))) {
                Files.createDirectories(Paths.get(dictPath));
                Files.createFile(Paths.get(dictPath + "/jieba.dict"));
                Files.createFile(Paths.get(dictPath + "/lingpipe.dict"));

                try {
                    for (Dictionary dictionary : jieba) {
                        String word = dictionary.getWord() + " " + String.valueOf(dictionary.getFrequency()) + "\n";
                        Files.write(Paths.get(dictPath + "/jieba.dict"), word.getBytes(), StandardOpenOption.APPEND);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    for (Dictionary dictionary : lingpipe) {
                        String word = dictionary.getWord() + "\n";
                        Files.write(Paths.get(dictPath + "/lingpipe.dict"), word.getBytes(), StandardOpenOption.APPEND);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
