package it.bologna.ausl.generator;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;

/**
 *
 * @author gdm
 */
public class JWTUtils {
    
    private static JwtClaims processHS256TokenClaims(String token, String secretKey) throws InvalidJwtException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
            .setVerificationKey(new HmacKey(Base64.getDecoder().decode(secretKey)))
            .setRelaxVerificationKeyValidation()
            .build();
        return jwtConsumer.processToClaims(token);
    }

    public static Map<String, String> getClaimsFromHS256Token(String token, String secretKey) throws InvalidJwtException {
        JwtClaims claims = processHS256TokenClaims(token, secretKey);
        Map<String, String> res = new HashMap();
        Map<String, List<Object>> flattenClaims = claims.flattenClaims();
        flattenClaims.keySet().stream().forEach(key -> {
           res.put(key, flattenClaims.get(key) != null ? flattenClaims.get(key).get(0).toString(): null);
        });
        return res;
    }
    
    public static boolean isExpiredHS256Token(String token, String secretKey) throws InvalidJwtException, MalformedClaimException {
        return JWTUtils.isExpiringHS256Token(token, secretKey, 0);
    }
    
    /**
     * torna se il token è scaduto o sta per scadere (sta per scadere vuol dire che la scadenza è tra offsetSeconds secondi)
     * @param token
     * @param secretKey
     * @param offsetSeconds
     * @return
     * @throws InvalidJwtException
     * @throws MalformedClaimException 
     */
    public static boolean isExpiringHS256Token(String token, String secretKey, int offsetSeconds) throws InvalidJwtException, MalformedClaimException {
        JwtClaims claims = null;
        try {
            claims = processHS256TokenClaims(token, secretKey);
        } catch (InvalidJwtException invalidJwtException) {
            if (invalidJwtException.hasExpired()) {
                return true;
            }
            throw invalidJwtException;
        }
        
        NumericDate now = NumericDate.now();
        now.addSeconds(offsetSeconds);
        NumericDate expirationTime = claims.getExpirationTime();
        return now.isOnOrAfter(expirationTime);
    }
    
    public static void main(String[] args) throws InvalidJwtException, MalformedClaimException {
        
        String passToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMzUwNzMiLCJVU0VSTkFNRSI6IlRTQ01OTjc4SDY2RTQzNVMiLCJVU0VSX1NTT19GSUVMRF9WQUxVRSI6IlRTQ01OTjc4SDY2RTQzNVMiLCJSRUFMX1VTRVIiOiIyOTQ3MTQiLCJSRUFMX1VTRVJfVVNFUk5BTUUiOiJnLmRlbWFyY28iLCJSRUFMX1VTRVJfU1NPX0ZJRUxEX1ZBTFVFIjoiRE1SR1BQODNFMjlEODUxQyIsImlhdCI6MTU3NDc3NzA4OCwiZXhwIjoxNTc0Nzc3MTQ4fQ.M0vNLZtXDo7HPdiZb-UBtTb9lggM6SQPzjoM1z7I2q0";
        String tokenInternauta = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIzMzUwNzMiLCJDT01QQU5ZIjoiMiIsIlNTT19MT0dJTiI6dHJ1ZSwiVVNFUl9FTlRJVFlfQ0xBU1MiOiJpdC5ib2xvZ25hLmF1c2wubW9kZWwuZW50aXRpZXMuYmFib3JnLlBlcnNvbmEiLCJVU0VSX0ZJRUxEIjoiY29kaWNlRmlzY2FsZSIsIlVTRVJfU1NPX0ZJRUxEX1ZBTFVFIjoiVFNDTU5ONzhINjZFNDM1UyIsIklEX1NFU1NJT05fTE9HIjoiNjk0MzE1IiwiUkVBTF9VU0VSIjoiMjk0NzE0IiwiaWF0IjoxNTc0NzY3MTYxLCJleHAiOjE1NzUxOTkxNjF9.TPUDfw-Ky-XL4Uco45o5VG5TuDRTJguezUkw53SsdcM";
        String secret = "QUdvMWJHUEJZem5CTFBQalYxNmEzb3FWRkZPM3hqbXV5Y25TMHBTZXZaQk5ScHVJUFhMNzAwQllCY2dYNnJpaw==";
//        System.out.println(getClaimsFromHS256Token(token, secret));
        System.out.println(isExpiringHS256Token(tokenInternauta, secret, 432000));
    }
}
