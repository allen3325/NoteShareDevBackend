package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.postModule.entity.Post;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PostRepository extends MongoRepository<Post,String> {
    List<Post> findAllByType(String postType);

    @Query(value = "{'title': {$regex : ?0, $options: 'i'}}")
    List<Post> findPostByTitleRegex(String title);
}
