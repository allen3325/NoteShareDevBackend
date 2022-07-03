package ntou.notesharedevbackend.schedulerModule.contorller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.exception.NotFoundException;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.postModule.entity.VoteRequest;
import ntou.notesharedevbackend.schedulerModule.entity.KickVoteRequest;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.entity.Vote;
import ntou.notesharedevbackend.schedulerModule.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
//TODO: 提醒使用者投票
public class ScheduleController {
    @Autowired
    private SchedulingService schedulingService;

//    @PostMapping("/publish/{postID}")
//    public ResponseEntity<Task> addPublishTask(@PathVariable(name = "postID") String postID, @RequestBody Task request) {
//        Task task = schedulingService.newPublishSchedule(postID, request);
//        if (task == null) {
//            throw new NotFoundException("Can not add task");
//        } else {
//            return ResponseEntity.ok().body(task);
//        }
//    }

    @Operation(summary = "creat vote",description = "時間要是今天以後")
    @PostMapping("/vote/{postID}")
    public ResponseEntity<Object> addVote(@PathVariable(name = "postID") String postID, @RequestBody KickVoteRequest request) {
        Vote vote = schedulingService.newVoteSchedule(postID, request);
        Map<String, Object> res = new HashMap<>();

        if(vote!=null){
            res.put("res",vote);
            return ResponseEntity.ok().body(res);
        }else {
            res.put("msg","can't set past time");
            return ResponseEntity.status(412).body(res);
        }
    }

//    @PutMapping("/modify/{postID}")
//    public ResponseEntity modifySchedule(@PathVariable(name = "postID") String postID, @RequestBody Task request) {
//        Task task = schedulingService.modifyPublishSchedule(postID, request);
//        return ResponseEntity.ok().body(task);
//    }
    @Operation(summary = "modify vote time", description = "year, month, day only,時間要是今天以後")
    @PutMapping("/modifyVote/{postID}/{voteID}")
    public ResponseEntity modifyVoteSchedule(@PathVariable(name = "postID") String postID, @PathVariable(name = "voteID") String voteID, @RequestBody KickVoteRequest request) {
        Vote vote = schedulingService.modifyVoteSchedule(postID, voteID, request);
        Map<String, Object> res = new HashMap<>();

        if(vote!=null){
            res.put("res",vote);
            return ResponseEntity.ok().body(res);
        }else {
            res.put("msg","can't set past time");
            return ResponseEntity.status(412).body(res);
        }
    }

    @DeleteMapping("/delete/{postID}/{voteID}")
    public ResponseEntity deleteVote(@PathVariable(name = "postID") String postID, @PathVariable(name = "voteID") String voteID){
        schedulingService.deleteVote(postID,voteID);
        Map<String, Object> res = new HashMap<>();
        res.put("msg", "Success");
        return ResponseEntity.status(204).body(res);
    }

    @Operation(summary = "後端自己用")
    @DeleteMapping("/cancelTask/{taskID}")
    public ResponseEntity cancelTask(@PathVariable(name = "taskID") String taskID) {
        schedulingService.cancelSchedule(taskID);
        return ResponseEntity.noContent().build();
    }

}
