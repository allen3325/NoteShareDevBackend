package ntou.notesharedevbackend.followModule.controlloer;

import io.swagger.v3.oas.annotations.*;
import ntou.notesharedevbackend.followModule.service.*;
import ntou.notesharedevbackend.noteNodule.entity.*;
import ntou.notesharedevbackend.searchModule.entity.*;
import ntou.notesharedevbackend.userModule.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class FollowController {
    @Autowired
    private FollowService followService;

    @Operation(summary = "Get a user's followers by email")
    @GetMapping("/followers/{email}")
    public ResponseEntity<Object> getFollowers(@PathVariable("email") String email) {
        String[] followers = followService.getFollowers(email);
        Map<String, Object> map = new HashMap<>();
        ArrayList<UserObj> followUserObj = new ArrayList<>();
        for (String followerEmail : followers) {
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
        for (String followingEmail : following) {
            followingUserObj.add(followService.getUserInfo(followingEmail));
        }
        map.put("following", followingUserObj);

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

    @Operation(summary = "Bell someone", description = "userEmail是做開啟小鈴鐺的人，bellEmail是被開啟小鈴鐺的人")
    @PutMapping("/bell/{userEmail}/{bellEmail}")
    public ResponseEntity<Object> bell(@PathVariable("userEmail") String userEmail,
                                       @PathVariable("bellEmail") String bellEmail) {
        followService.bell(userEmail, bellEmail);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");
        return ResponseEntity.ok(map);
    }

    @Operation(summary = "Cancel bell someone", description = "userEmail是做關閉小鈴鐺的人，cancelBellEmail是被取消小鈴鐺的人")
    @PutMapping("/cancelBell/{userEmail}/{cancelBellEmail}")
    public ResponseEntity<Object> cancelBell(@PathVariable("userEmail") String userEmail,
                                             @PathVariable("cancelBellEmail") String cancelBellEmail) {
        followService.cancelBell(userEmail, cancelBellEmail);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "Success");
        return ResponseEntity.ok(map);
    }

    @Operation(summary = "get a user's bell by email")
    @GetMapping("/bell/{email}")
    public ResponseEntity<Object> getBell(@PathVariable("email") String email) {
        ArrayList<String> bells = followService.getBell(email);
        Map<String, Object> res = new HashMap<>();
        ArrayList<UserObj> bellUserObj = new ArrayList<>();
        for (String bellEmail : bells) {
            bellUserObj.add(followService.getUserInfo(bellEmail));
        }
        res.put("res", bellUserObj);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get a user's bellBy by email")
    @GetMapping("/bellBy/{email}")
    public ResponseEntity<Object> getBellBy(@PathVariable("email") String email) {
        ArrayList<String> bellBys = followService.getBellBy(email);
        Map<String, Object> res = new HashMap<>();
        ArrayList<UserObj> bellByUserObj = new ArrayList<>();
        for (String bellByEmail : bellBys) {
            bellByUserObj.add(followService.getUserInfo(bellByEmail));
        }
        res.put("res", bellByUserObj);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get a user's following notes")
    @GetMapping("/note/following/{email}/{offset}/{pageSize}")
    public ResponseEntity<Object> getFollowingNotes(@PathVariable("email") String email,
                                                    @PathVariable("offset") int offset,
                                                    @PathVariable("pageSize") int pageSize) {
        Pages noteLists = followService.getFollowingNotes(email, offset, pageSize);
        Map<String, Object> res = new HashMap<>();
        res.put("res", noteLists);
        return ResponseEntity.ok(res);
    }
}
