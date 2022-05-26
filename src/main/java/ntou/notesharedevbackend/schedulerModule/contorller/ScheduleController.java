package ntou.notesharedevbackend.schedulerModule.contorller;

import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.schedulerModule.entity.Task;
import ntou.notesharedevbackend.schedulerModule.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
@RequestMapping(value = "/schedule",produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleController {
    @Autowired
    private SchedulingService schedulingService;
    @PutMapping("/modify/{id}")
    public ResponseEntity modifySchedule(@PathVariable (name = "id") String id,@RequestBody Post request){
        schedulingService.modifySchedule(id,request.getTask());
        return ResponseEntity.ok().build();
    }
}
