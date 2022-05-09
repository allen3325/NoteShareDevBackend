package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface FolderRepository extends MongoRepository<Folder,String> {

}
