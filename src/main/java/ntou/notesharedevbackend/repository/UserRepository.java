package ntou.notesharedevbackend.repository;

import ntou.notesharedevbackend.userModule.entity.AppUser;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public interface UserRepository  extends MongoRepository<AppUser,String> {
    AppUser findByEmail(String email);
    List<AppUser> findByNameLike(String name);
    Page<AppUser> findByNameLike(String name, Pageable pageable);
    boolean existsByEmail(String email);
}
