package ntou.notesharedevbackend.schedulerModule.contorller;

import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.schedulerModule.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
//TODO: 提醒使用者投票
public class ScheduleController {
    @Autowired
    private SchedulingService schedulingService;

    @PostMapping("/publish/{postID}")
    public ResponseEntity<Task> addPublishTask(@PathVariable(name = "postID") String postID, @RequestBody Task request) {
        Task task = schedulingService.newPublishSchedule(postID, request);
        if (task == null) {
            throw new NotFoundException("Can not add task");
        } else {
            return ResponseEntity.ok().body(task);
        }
    }

    @PostMapping("/vote/{postID}")
    public ResponseEntity<Vote> addVote(@PathVariable(name = "postID") String postID, @RequestBody Vote request) {
        Vote vote = schedulingService.newVoteSchedule(postID, request);
        return ResponseEntity.ok().body(vote);

    }

    @PutMapping("/modify/{postID}")
    public ResponseEntity modifySchedule(@PathVariable(name = "postID") String postID, @RequestBody Task request) {
        Task task = schedulingService.modifyPublishSchedule(postID, request);
        return ResponseEntity.ok().body(task);

    }

    @PutMapping("/modifyVote/{postID}/{voteID}")
    public ResponseEntity modifyVoteSchedule(@PathVariable(name = "postID") String postID, @PathVariable(name = "voteID") String voteID, @RequestBody Vote request) {
        Vote vote = schedulingService.modifyVoteSchedule(postID, voteID, request);
        return ResponseEntity.ok().body(vote);
    }

    @DeleteMapping("/cancelTask/{taskID}")
    public ResponseEntity cancelTask(@PathVariable(name = "taskID") String taskID) {
        schedulingService.cancelSchedule(taskID);
        return ResponseEntity.noContent().build();
    }

}
