package ntou.notesharedevbackend.verificationModule.controller;

import io.swagger.v3.oas.annotations.Operation;
import ntou.notesharedevbackend.verificationModule.entity.AuthRequest;
import ntou.notesharedevbackend.verificationModule.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/verification", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailController {

    @Autowired
    private MailService mailService;

    @Operation(summary = "忘記密碼", description = "")
    @PostMapping("/randomPassword/{email}")
    public ResponseEntity<Object> randomPassword( @PathVariable (name = "email" ) String email) {
        Map<String,Object> res = new HashMap<>();
        mailService.randomPasswordMail(email);
        res.put("msg","Success");
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "重設密碼", description = "status code -> 200 Success, 403 Wrong password")
    @PostMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody AuthRequest request) {
        Map<String,Object> res = new HashMap<>();
        if(mailService.resetPasswordMail(request)){
            res.put("msg","Success");
            return ResponseEntity.ok(res);
        }else{
            res.put("msg","Wrong password.");
            return ResponseEntity.status(403).body(res);
        }

    }

    @Operation(summary = "重寄驗證碼", description = "")
    @PostMapping("/resendCode/{email}")
    public ResponseEntity<Object>  resendCodeMail(@PathVariable (name = "email" ) String email) {
        Map<String,Object> res = new HashMap<>();
        mailService.resendCodeMail(email);
        res.put("msg","Success");
        return ResponseEntity.ok(res);
    }

}

