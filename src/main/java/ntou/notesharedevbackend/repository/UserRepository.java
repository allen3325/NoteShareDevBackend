package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends MongoRepository<AppUser,String> {
}
