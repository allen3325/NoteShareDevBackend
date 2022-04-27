package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends MongoRepository<AppUser,String> {
    List<AppUser> findByNameLike(String name);
    AppUser findByEmail(String email);
}
