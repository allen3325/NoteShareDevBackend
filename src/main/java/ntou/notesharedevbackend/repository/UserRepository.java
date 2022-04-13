package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.userModule.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends MongoRepository<User,String> {
}
