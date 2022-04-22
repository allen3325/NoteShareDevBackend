package ntou.notesharedevbackend.verificationModule.controller;

import ntou.notesharedevbackend.verificationModule.entity.SendMailRequest;
import ntou.notesharedevbackend.verificationModule.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/verification", produces = MediaType.APPLICATION_JSON_VALUE)
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/randomPassword")
    public ResponseEntity<SendMailRequest> randomPassword(@Valid @RequestBody SendMailRequest request) {

        mailService.randomPasswordMail("lasfkjdf",request);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody SendMailRequest request) {

        mailService.resetPasswordMail(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resendCode")
    public ResponseEntity<Void>  resendCodeMail(@Valid @RequestBody SendMailRequest request) {

        mailService.resendCodeMail(request);

        return ResponseEntity.noContent().build();
    }

}

