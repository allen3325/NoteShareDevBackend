package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.noteNodule.entity.Note;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface NoteRepository extends MongoRepository<Note,String> {
    @Query(value = "{'title': {$regex : ?0, $options: 'i'}}")
    Page<Note> findNoteByTitleRegex(String title, Pageable pageable);
}
