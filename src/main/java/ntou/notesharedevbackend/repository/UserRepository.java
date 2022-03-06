package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository  extends MongoRepository<User,String> {
}
