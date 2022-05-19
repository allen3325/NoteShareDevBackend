package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.noteNodule.entity.Note;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface NoteRepository extends MongoRepository<Note,String> {
    List<Note> findNoteByTitleLike(String title);
    Page<Note> findNoteByTitleLike(String title, Pageable pageable);
}
