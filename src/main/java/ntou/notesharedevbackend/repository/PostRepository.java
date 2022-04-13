package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.postModule.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends MongoRepository<Post,String> {
}
