package ntou.notesharedevbackend.followModule.controlloer;

import ntou.notesharedevbackend.followModule.service.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/",produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowController {
    @Autowired
    private FollowService followService;

    @GetMapping("/follow/{email}")
    public ResponseEntity<String[]> getFollowers(@PathVariable("email") String email) {
        String[] followers = followService.getFollowers(email);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following/{email}")
    public ResponseEntity<String[]> getFollowing(@PathVariable("email") String email) {
        String[] followers = followService.getFollowing(email);
        return ResponseEntity.ok(followers);
    }

    @PutMapping("/follow/{userEmail}/{followEmail}")
    public ResponseEntity<AppUser> follow(@PathVariable("userEmail") String userEmail, @PathVariable("followEmail") String followEmail) {
        followService.follow(userEmail, followEmail);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/unfollow/{userEmail}/{unfollowEmail}")
    public ResponseEntity<AppUser> unfollow(@PathVariable("userEmail") String userEmail, @PathVariable("unfollowEmail") String unfollowEmail) {
        followService.unfollow(userEmail, unfollowEmail);
        return ResponseEntity.ok().build();
    }
}
