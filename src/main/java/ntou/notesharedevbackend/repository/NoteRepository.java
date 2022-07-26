package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.noteModule.entity.Content;
import ntou.notesharedevbackend.noteModule.entity.Note;
import ntou.notesharedevbackend.noteModule.entity.VersionContent;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {
    @Query(value = "{'title': {$regex : ?0, $options: 'i'}}")
    List<Note> findNoteByTitleRegex(String title, Sort sort);

    List<Note> findAllByHeaderEmail(String email);

    // "{\"_id\": {\"$oid\": \"629c22e636b09e5b52527f5b\"}}"
    @Query(value = "{ tag: { $all: ?0 } }", fields = "{_id : 1}")
    Set<String> findAllByTags(String[] tags);

    @Query(value = "{ hiddenTag: { $all: ?0 } }", fields = "{_id : 1}")
    List<String> findAllByHiddenTags(Set<String> tags);

    @Query(value = "{ _id: ?0 }", fields = "{_id: 0,'version' : 1}")
    Note findVersionById(String id);
}
