package ntou.notesharedevbackend.schedulerModule.repository;

import ntou.notesharedevbackend.schedulerModule.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
}
