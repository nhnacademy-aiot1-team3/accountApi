package live.databo3.account.mail.service.impl;

import live.databo3.account.error.ErrorCode;
import live.databo3.account.exception.CustomException;
import live.databo3.account.mail.service.MailService;
import live.databo3.account.util.CertificationGeneratorUtil;
import live.databo3.account.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final CertificationGeneratorUtil generatorUtil;
    private final RedisUtil redisUtil;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.sender}")
    private String senderEmail;

    @Value("${spring.mail.subject}")
    private String subject;

    // mail.html에 code 부분에 난수 입력 하고 해당 html -> string 만들기, redis에 mail : certificationNumber 저장
    private String writeEmailContent(String email) throws NoSuchAlgorithmException {

        String certificationNumber = generatorUtil.createCertificationNumber();

        Context context = new Context();
        context.setVariable("code", certificationNumber);

        redisUtil.saveCertificationNumber(email, certificationNumber);

        return templateEngine.process("mail", context);
    }

    private MimeMessage makeMailForm(String email) throws MessagingException, NoSuchAlgorithmException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        messageHelper.setFrom(senderEmail);
        messageHelper.setTo(email);
        messageHelper.setSubject(subject);
        messageHelper.setText(writeEmailContent(email), true);

        return mimeMessage;
    }

    @Override
    public void sendMail(String email) throws MessagingException, NoSuchAlgorithmException {
        if (redisUtil.hasCertificationNumber(email)) {
            redisUtil.removeCertificationNumber(email);
        }
        MimeMessage emailFormMessage = makeMailForm(email);
        mailSender.send(emailFormMessage);
    }

    @Override
    public void verifyCertificationNumber(String email, String certificationNumber) {
        if (!redisUtil.hasCertificationNumber(email)) {
            log.info("verifyCertificationNumber : 이미 만료된 키");
            throw new CustomException(ErrorCode.EMAIL_KEY_EXPIRE);
        }
        String savedCertificationNumber = redisUtil.getCertificationNumber(email);

        if (!certificationNumber.equals(savedCertificationNumber)) {
            log.info("verifyCertificationNumber : 틀린 코드 입력");
            throw new CustomException(ErrorCode.CERTIFICATION_NUMBER_DOES_NOT_MATCH);
        }

        log.info("verifyCertificationNumber : 이메일 인증 성공");
        redisUtil.removeCertificationNumber(email);
        log.info("verifyCertificationNumber : 인증 성공 후, 레디스 정보 삭제");
    }
}
