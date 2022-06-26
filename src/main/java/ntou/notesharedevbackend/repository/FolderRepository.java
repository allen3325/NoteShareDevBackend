package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FolderRepository extends MongoRepository<Folder,String> {
    @Query(value = "{'folderName': {$regex : ?0, $options: 'i'}}")
    List<Folder> findByFolderNameRegex(String title);
}
