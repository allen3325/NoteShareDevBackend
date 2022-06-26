package ntou.notesharedevbackend.verificationModule.controller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import ntou.notesharedevbackend.verificationModule.entity.AuthRequest;
import ntou.notesharedevbackend.verificationModule.entity.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/verification",produces = MediaType.APPLICATION_JSON_VALUE)
public class JWTController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    @Lazy
    private AppUserService appUserService;

    @Operation(summary = "sign up", description = "註冊,body裡，只需填email,name,password,headshotPhoto")
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody AppUser request){
        Map<String,Object> res = new HashMap<>();

        if(jwtService.signUp(request)){
            res.put("msg","Success");
            return ResponseEntity.status(201).body(res);
        }else{
            res.put("msg","Has the same email registered!");
            return ResponseEntity.status(409).body(res);
        }
    }

    @Operation(summary = "驗證", description = "")
    @PutMapping("/verify/{email}/{code}")
    public ResponseEntity<Object> verify(@PathVariable String email,@PathVariable String code){
        Map<String,Object> res = new HashMap<>();

        if(jwtService.verifyCode(email,code)){
            res.put("msg","Success");
            return ResponseEntity.ok(res);
        }else{
            res.put("msg","Verification code error.");
            return ResponseEntity.status(418).body(res);
        }
    }

    @Operation(summary = "登入", description = "")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> issueToken(@Valid @RequestBody AuthRequest request) {
        String token = jwtService.generateToken(request);
        Map<String, Object> response = new HashMap<>(Collections.singletonMap("token", token));

        Map<String, Object> user = jwtService.parseToken(response.get("token").toString());
        String userEmail = user.get("email").toString();
        response.put("activate",appUserService.getUserByEmail(userEmail).isActivate());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "前端用不到", description = "")
    @PostMapping("/parse")
    public ResponseEntity<Map<String, Object>> parseToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        Map<String, Object> response = jwtService.parseToken(token);

        return ResponseEntity.ok(response);
    }
}
