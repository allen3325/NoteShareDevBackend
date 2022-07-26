package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.noteModule.entity.Plagiarismdictionary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlagiarismDictionaryRepository extends MongoRepository<Plagiarismdictionary, String> {
    Plagiarismdictionary findFirstByWord(String word);
    List<Plagiarismdictionary> findAllByWordLike(String word);
}
