package ntou.notesharedevbackend.notificationModule.controller;

import io.swagger.v3.oas.annotations.*;
import ntou.notesharedevbackend.notificationModule.entity.*;
import ntou.notesharedevbackend.notificationModule.service.*;
import ntou.notesharedevbackend.userModule.entity.*;
import ntou.notesharedevbackend.userModule.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/notification",produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AppUserService appUserService;

    @Operation(summary = "get a user's all notifications")
    @GetMapping("/{email}")
    public ResponseEntity<Object> getNotification(@PathVariable("email") String email) {
        ArrayList<MessageReturn> notificationList = notificationService.getNotification(email);
        Map<String, Object> map = new HashMap<>();
        map.put("notification", notificationList);
        return ResponseEntity.ok(map);
    }

    @PutMapping("/unreadMessage/{email}")
    public ResponseEntity<Object> clearUnreadMessage(@PathVariable("email") String email) {
        notificationService.clearUnreadMessage(email);
        Map<String, Object> map = new HashMap<>();
        map.put("msg", "Success");
        return ResponseEntity.ok(map);
    }
}
