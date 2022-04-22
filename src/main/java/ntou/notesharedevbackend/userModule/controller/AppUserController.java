package ntou.notesharedevbackend.userModule.controller;

import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/user",produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController {
    @Autowired
    private AppUserService appUserService;

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable("id") String id){
        AppUser appUser = appUserService.getUserById(id);
        return ResponseEntity.ok(appUser);
    }

    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser request){
        AppUser appUser = appUserService.createUser(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(appUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(appUser);
    }
}
