package ntou.notesharedevbackend.verificationModule.controller;

import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.verificationModule.entity.AuthRequest;
import ntou.notesharedevbackend.verificationModule.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(value = "/verification",produces = MediaType.APPLICATION_JSON_VALUE)
public class JWTController {

    @Autowired
    private JWTService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody AppUser request){
        if(jwtService.signUp(request)){
            return ResponseEntity.ok("Successful.");
        }else{
            return ResponseEntity.status(409).body("Has the same email registered!");
        }
    }

    @PutMapping("/verify/{email}/{code}")
    public ResponseEntity<String> verify(@PathVariable String email,@PathVariable String code){
        if(jwtService.verifyCode(email,code)){
            return ResponseEntity.ok("Successful.");
        }else{
            return ResponseEntity.status(418).body("Verification code error.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> issueToken(@Valid @RequestBody AuthRequest request) {
        String token = jwtService.generateToken(request);
        Map<String, String> response = Collections.singletonMap("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/parse")
    public ResponseEntity<Map<String, Object>> parseToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        Map<String, Object> response = jwtService.parseToken(token);

        return ResponseEntity.ok(response);
    }
}
