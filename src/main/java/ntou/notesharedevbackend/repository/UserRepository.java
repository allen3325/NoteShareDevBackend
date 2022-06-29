package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.*;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public interface UserRepository  extends MongoRepository<AppUser,String> {
    AppUser findByName(String name);
    AppUser findByEmail(String email);
    @Query(value = "{'name': {$regex : ?0, $options: 'i'}}")
    Page<AppUser> findByNameRegex(String name, Pageable pageable);
    boolean existsByEmail(String email);
}
