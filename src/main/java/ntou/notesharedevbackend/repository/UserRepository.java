package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.folderModule.entity.Folder;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends MongoRepository<AppUser,String> {
    AppUser findByEmail(String email);
    List<AppUser> findAppUserByNameLike(String name);

    Page<AppUser> findAppUserByNameLike(String name, Pageable pageable);
    @Query("{'folders.id': ?0}")
    AppUser findFolderById(String id);
    boolean existsByEmail(String email);
}
