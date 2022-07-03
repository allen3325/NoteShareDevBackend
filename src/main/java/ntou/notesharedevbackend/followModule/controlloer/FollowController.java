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
    public ResponseEntity<Object> getFollowers(@PathVariable("email") String email) {
        String[] followers = followService.getFollowers(email);
        Map<String, Object> map = new HashMap<>();
        ArrayList<UserObj> followUserObj = new ArrayList<>();
        for(String followerEmail : followers){
            followUserObj.add(followService.getUserInfo(followerEmail));
        }
        map.put("followers", followUserObj);

        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Get a user's following by email")
    @GetMapping("/following/{email}")
    public ResponseEntity<Object> getFollowing(@PathVariable("email") String email) {
        String[] following = followService.getFollowing(email);
        Map<String, Object> map = new HashMap<>();
        ArrayList<UserObj> followingUserObj = new ArrayList<>();
        for(String followingEmail : following){
            followingUserObj.add(followService.getUserInfo(followingEmail));
        }
        map.put("followers", followingUserObj);

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
