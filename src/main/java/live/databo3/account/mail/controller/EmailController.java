package live.databo3.account.mail.controller;

import live.databo3.account.mail.dto.EmailAndCertificationNumberRequest;
import live.databo3.account.mail.dto.EmailCertificationRequest;
import live.databo3.account.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/account/member/email")
@RequiredArgsConstructor
public class EmailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<Map<String,String>> sendEmail(@Valid @RequestBody EmailCertificationRequest request) throws MessagingException, NoSuchAlgorithmException {

        mailService.sendMail(request.getEmail());
        Map<String, String> message = Map.of("message", request.getEmail());

        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyCertificationNumber(@Valid @RequestBody EmailAndCertificationNumberRequest request) {

        mailService.verifyCertificationNumber(request.getEmail(), request.getCertificationNumber());
        Map<String, String> message = Map.of("message", request.getEmail());

        return ResponseEntity.ok().body(message);
    }

}