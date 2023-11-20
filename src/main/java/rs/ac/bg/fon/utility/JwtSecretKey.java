package rs.ac.bg.fon.utility;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecretKey {

    private final JwtUtility jwtUtility;

    @Autowired
    public JwtSecretKey(JwtUtility jwtConfig) {
        this.jwtUtility = jwtConfig;
    }

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256("bnckjbasduifbsiudbfiusabciuzsdbcfuifviucxzb".getBytes());
    }
}
