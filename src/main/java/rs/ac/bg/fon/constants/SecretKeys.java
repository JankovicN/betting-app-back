package rs.ac.bg.fon.constants;

import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class SecretKeys {

    private static final Logger logger = LoggerFactory.getLogger(SecretKeys.class);
    private static final String SECRET_FILE = "secret.properties";
    private static final Properties secretFile;
    private static final String api_key_field = "api_key";
    private static final String jwt_secret_key_field = "jwt_secret_key";

    static {
        secretFile = new Properties();
        try {
            secretFile.load(new FileInputStream(Constants.RESOURCE_FOLDER + SECRET_FILE));
            logger.info("Successfully loaded secret properties");
        } catch (Exception e) {
            logger.error("Exception: Error loading secret properties");
        }
    }

    public static String getApi_key() {
        return secretFile.getProperty(api_key_field);
    }

    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secretFile.getProperty(jwt_secret_key_field).getBytes());
    }
}
