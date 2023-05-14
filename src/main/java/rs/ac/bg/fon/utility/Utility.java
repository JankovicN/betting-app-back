package rs.ac.bg.fon.utility;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.FileInputStream;
import java.util.Properties;

@Slf4j
@ConfigurationProperties(prefix = "application")
public class Utility {

    private static final String PROPERTIES_FILE = "application.properties";
    private static final Properties propertiesFile;

    static {
        propertiesFile = new Properties();
        try {
            propertiesFile.load(new FileInputStream(PROPERTIES_FILE));
            System.out.println("Loading application properties");
        } catch (Throwable e) {
            log.error("Error loading application properties");
        }
    }

    public static String getProperty(String name) {
        try {

            System.out.println(name +"  "+ propertiesFile.getProperty(name));
            return propertiesFile.getProperty(name);
        } catch (Throwable ex) {
            log.error("Error getting Property!");
            return null;
        }
    }

    public static Algorithm getAlgorithm(){
        return Algorithm.HMAC256("bdabsidubasuidasuidfiasubfius".getBytes());
    }
}
