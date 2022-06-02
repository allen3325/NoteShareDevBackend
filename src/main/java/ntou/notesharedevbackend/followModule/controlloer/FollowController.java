package ntou.notesharedevbackend.followModule.controlloer;

import io.swagger.v3.oas.annotations.*;
import ntou.notesharedevbackend.followModule.service.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/",produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowController {
    @Autowired
    private FollowService followService;

    @Operation(summary = "Get a user's followers by email")
    @GetMapping("/followers/{email}")
    public ResponseEntity<Map<String, String[]>> getFollowers(@PathVariable("email") String email) {
        String[] followers = followService.getFollowers(email);
        Map<String, String[]> map = new HashMap<>();
        map.put("followers", followers);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Get a user's following by email")
    @GetMapping("/following/{email}")
    public ResponseEntity<Map<String, String[]>> getFollowing(@PathVariable("email") String email) {
        String[] following = followService.getFollowing(email);
        Map<String, String[]> map = new HashMap<>();
        map.put("following", following);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Follow someone", description = "userEmail是指誰(email)要做follow這個動作，followEmail是指他要follow誰(email)")
    @PutMapping("/follow/{userEmail}/{followEmail}")
    public ResponseEntity<Map<String, String>> follow(@PathVariable("userEmail") String userEmail, @PathVariable("followEmail") String followEmail) {
        followService.follow(userEmail, followEmail);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Unfollow someone", description = "userEmail是指誰(email)要做unfollow這個動作，followEmail是指他要unfollow誰(email)")
    @PutMapping("/unfollow/{userEmail}/{unfollowEmail}")
    public ResponseEntity<Map<String, String>> unfollow(@PathVariable("userEmail") String userEmail, @PathVariable("unfollowEmail") String unfollowEmail) {
        followService.unfollow(userEmail, unfollowEmail);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");

        return ResponseEntity.ok(map);
    }
}
