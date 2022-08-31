package ntou.notesharedevbackend.verificationModule.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.userModule.entity.AppUser;
import ntou.notesharedevbackend.userModule.service.AppUserService;
import ntou.notesharedevbackend.verificationModule.entity.AuthRequest;
import ntou.notesharedevbackend.verificationModule.service.JWTService;
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
@RequestMapping(value = "/verification", produces = MediaType.APPLICATION_JSON_VALUE)
public class JWTController {

    @Autowired
    private JWTService jwtService;
    @Autowired
    @Lazy
    private AppUserService appUserService;

    @Operation(summary = "sign up", description = "註冊,body裡，只需填email,name,password,headshotPhoto 406->重複名字，409->重複信箱，201->註冊成功")
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody AppUser request) {
        Map<String, Object> res = new HashMap<>();
        String msg = jwtService.signUp(request);
        if (msg.equals("Name")) {
            res.put("res", "Has the same name registered!");
            return ResponseEntity.status(406).body(res);
        } else if (msg.equals("Email")) {
            res.put("res", "Has the same email registered!");
            return ResponseEntity.status(409).body(res);
        } else {
            res.put("res", "Success");
            return ResponseEntity.status(201).body(res);
        }
    }

    @Operation(summary = "驗證", description = "")
    @PutMapping("/verify/{email}/{code}")
    public ResponseEntity<Object> verify(@PathVariable String email, @PathVariable String code) {
        Map<String, Object> res = new HashMap<>();

        if (jwtService.verifyCode(email, code)) {
            res.put("msg", "Success");
            return ResponseEntity.ok(res);
        } else {
            res.put("msg", "Verification code error.");
            return ResponseEntity.status(418).body(res);
        }
    }

    @Operation(summary = "登入", description = "error code 404 -> 此信箱為註冊過。 403 -> 密碼錯誤。")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> issueToken(@Valid @RequestBody AuthRequest request) {
        if (appUserService.hasExitUserByEmail(request.getEmail())) {
            String token = jwtService.generateToken(request);
            Map<String, Object> response = new HashMap<>(Collections.singletonMap("token", token));

            Map<String, Object> user = jwtService.parseToken(response.get("token").toString());
            String userEmail = user.get("email").toString();
            response.put("activate", appUserService.getUserByEmail(userEmail).isActivate());

            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> res = new HashMap<>();
            res.put("msg", "not found this email.");
            return ResponseEntity.status(404).body(res);
        }
    }

    @Hidden
    @Operation(summary = "前端用不到", description = "")
    @PostMapping("/parse")
    public ResponseEntity<Map<String, Object>> parseToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        Map<String, Object> response = jwtService.parseToken(token);

        return ResponseEntity.ok(response);
    }
}
