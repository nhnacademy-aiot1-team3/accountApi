package live.databo3.account.mail.service;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;

public interface MailService {

    public void sendMail(String email) throws MessagingException, NoSuchAlgorithmException;

    public void verifyCertificationNumber(String email, String certificationNumber);

}
