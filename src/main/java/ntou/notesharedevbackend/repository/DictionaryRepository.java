package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.tagGeneration.entity.Dictionary;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface DictionaryRepository extends MongoRepository<Dictionary,String> {
    List<Dictionary> findAllByType(String type);
    Dictionary findByWord(String word);
}
