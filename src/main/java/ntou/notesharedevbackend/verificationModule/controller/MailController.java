package ntou.notesharedevbackend.verificationModule.controller;

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
    public ResponseEntity<String> resetPassword( @RequestParam (name = "id" , required = false) String userID,
                                              @RequestParam (name = "oldPassword", required = false) String oldPassword,
                                              @RequestParam (name = "newPassword", required = false) String newPassword) {
        mailService.resetPasswordMail(userID,oldPassword,newPassword);
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/resendCode")
    public ResponseEntity<String>  resendCodeMail(@RequestParam (name = "id" , required = false) String userID) {
        mailService.resendCodeMail(userID);
        return ResponseEntity.ok("Success");
    }

}

