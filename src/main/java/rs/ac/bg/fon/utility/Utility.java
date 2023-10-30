package rs.ac.bg.fon.utility;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import rs.ac.bg.fon.constants.Constants;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Slf4j
@ConfigurationProperties(prefix = "application")
public class Utility {

    private static final String PROPERTIES_FILE = "application.properties";
    private static final Properties propertiesFile;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

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

    public static String formatDateTime(LocalDateTime localDateTime){
        return localDateTime.format(dateTimeFormatter);
    }
    public static String formatDate(LocalDate localDate){
        return localDate.format(dateFormatter);
    }
    public static String formatDate(LocalDateTime localDate){
        return localDate.format(dateFormatter);
    }
    public static LocalDateTime parseDateTime(String date){
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    public static Algorithm getAlgorithm(){
        return Algorithm.HMAC256("bdabsidubasuidasuidfiasubfius".getBytes());
    }
}
