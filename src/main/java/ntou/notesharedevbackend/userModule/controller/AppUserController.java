package ntou.notesharedevbackend.userModule.controller;

import io.swagger.v3.oas.annotations.*;
import ntou.notesharedevbackend.postModule.entity.Post;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.entity.ModifyUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
// TODO: add modify headShotPhoto api
public class AppUserController {
    @Autowired
    private AppUserService appUserService;

    @Operation(summary = "Get all users", description = "前端應該不會用到這個")
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        AppUser[] appUsers = appUserService.getAllUsers();
        Map<String, Object> res = new HashMap<>();

        res.put("res", appUsers);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get a user by id")
    @GetMapping("/id/{id}")
    public ResponseEntity<Object> getUserByID(@PathVariable("id") String id) {
        AppUser appUser = appUserService.getUserById(id);
        Map<String, Object> res = new HashMap<>();

        res.put("res", appUser);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get a user by email")
    @GetMapping("/{email}")
    public ResponseEntity<Object> getUserByEmail(@PathVariable("email") String email) {
        AppUser appUser = appUserService.getUserByEmail(email);
        Map<String, Object> res = new HashMap<>();

        res.put("res", appUser);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get user's headshotPhoto")
    @GetMapping("/head/{email}")
    public ResponseEntity<Object> getUserHeadshotPhoto(@PathVariable("email") String email) {
        String headshotPhoto = appUserService.getUserHeadshotPhoto(email);
        Map<String, Object> res = new HashMap<>();

        res.put("res", headshotPhoto);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get user's headshotPhoto")
    @PutMapping("/head/{email}/{headshotPhoto}")
    public ResponseEntity<Object> updateUserHeadshotPhoto(@PathVariable("email") String email, @PathVariable("headshotPhoto") String headshotPhoto) {
        AppUser appUser = appUserService.updateUserHeadshotPhoto(email,headshotPhoto);
        Map<String, Object> res = new HashMap<>();

        res.put("res", appUser);
        return ResponseEntity.ok(res);
    }

//    @PostMapping
//    public ResponseEntity createUser(@RequestBody AppUser request) {
//        AppUser appUser = appUserService.createUser(request);
//        if (appUser == null) {
//            return ResponseEntity.badRequest().body("can't have the same email.");
//        }
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(appUser.getId())
//                .toUri();
//
//        return ResponseEntity.created(location).body(appUser);
//    }

//    @Operation(summary = "Edit user information by email", description = "所有資料皆可編輯")
//    @PutMapping("/{email}")
//    public ResponseEntity<Object> replaceUser(@RequestBody AppUser request) {
//        AppUser appUser = appUserService.replaceUser(request);
//        Map<String,Object> res = new HashMap<>();
//
//        res.put("res",appUser);
//        return ResponseEntity.ok(res);
//    }

    @Operation(summary = "Edit user's strength by email", description = "只能編輯strength")
    @PutMapping("/strength/{email}")
    public ResponseEntity<Object> modifyUserStrength(@PathVariable("email") String email,
                                                     @RequestBody ModifyUser request) {
        appUserService.modifyStrength(email, request.getStrength());
        Map<String, Object> res = new HashMap<>();

        res.put("msg", "Success");
        return ResponseEntity.status(200).body(res);
    }

    @Operation(summary = "Edit user's Profile by email", description = "只能編輯自我介紹")
    @PutMapping("/profile/{email}")
    public ResponseEntity<Object> modifyUserProfile(@PathVariable("email") String email,
                                                    @RequestBody ModifyUser request) {
        appUserService.modifyProfile(email, request.getProfile());
        Map<String, Object> res = new HashMap<>();

        res.put("msg", "Success");
        return ResponseEntity.status(200).body(res);
    }
}
