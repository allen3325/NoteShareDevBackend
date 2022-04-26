package ntou.notesharedevbackend.verificationModule.controller;

import ntou.notesharedevbackend.verificationModule.entity.AuthRequest;
import ntou.notesharedevbackend.verificationModule.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/verification", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/randomPassword")
    public ResponseEntity<String> randomPassword( @RequestParam (name = "id" , required = false) String userID) {
        mailService.randomPasswordMail(userID);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody AuthRequest request) {
        mailService.resetPasswordMail(request);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/resendCode")
    public ResponseEntity<String>  resendCodeMail(@RequestParam (name = "id" , required = false) String userID) {
        mailService.resendCodeMail(userID);
        return ResponseEntity.ok("Success");
    }

}

