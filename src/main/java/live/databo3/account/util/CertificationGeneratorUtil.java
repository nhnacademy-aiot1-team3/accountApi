package live.databo3.account.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CertificationGeneratorUtil {
    public String createCertificationNumber() throws NoSuchAlgorithmException {
        String result;

        do {
            int num = SecureRandom.getInstanceStrong().nextInt(999999);
            result = String.valueOf(num);
        } while (result.length() != 6);

        return result;
    }
}
