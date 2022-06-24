package ntou.notesharedevbackend.notificationModule.controller;

import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.notificationModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/notification",produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{email}")
    public ResponseEntity<Object> getNotification(@PathVariable("email") String email) {
        ArrayList<Message> notification = notificationService.getNotification(email);
        Map<String, Object> map = new HashMap<>();
        map.put("notification", notification);
        return ResponseEntity.ok(map);
    }

    @PutMapping("/unreadMessageCount/{email}")
    public ResponseEntity<Object> clearUnreadMessageCount(@PathVariable("email") String email) {
        notificationService.clearUnreadMessageCount(email);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "Success");
        return ResponseEntity.ok(map);
    }
}
