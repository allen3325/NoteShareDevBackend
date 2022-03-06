package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post,String> {
}
