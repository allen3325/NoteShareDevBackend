package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends MongoRepository<AppUser,String> {
    AppUser findByEmail(String email);
    List<AppUser> findByNameLike(String name);
    @Query("{'folders.id': ?0}")
    AppUser findFolderById(String id);
    boolean existsByEmail(String email);
}
