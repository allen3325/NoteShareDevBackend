package ntou.notesharedevbackend.verificationModule.controller;

import ntou.notesharedevbackend.verificationModule.entity.AuthRequest;
import ntou.notesharedevbackend.verificationModule.entity.service.MailService;
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

    @PostMapping("/randomPassword/{email}")
    public ResponseEntity<Object> randomPassword( @PathVariable (name = "email" ) String email) {
        Map<String,Object> res = new HashMap<>();
        mailService.randomPasswordMail(email);
        res.put("msg","Success");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody AuthRequest request) {
        Map<String,Object> res = new HashMap<>();
        mailService.resetPasswordMail(request);
        res.put("msg","Success");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/resendCode/{email}")
    public ResponseEntity<Object>  resendCodeMail(@PathVariable (name = "email" ) String email) {
        Map<String,Object> res = new HashMap<>();
        mailService.resendCodeMail(email);
        res.put("msg","Success");
        return ResponseEntity.ok(res);
    }

}

