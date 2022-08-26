package it.bologna.ausl.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import org.jose4j.lang.JoseException;

public class test {

    public static void main(String[] args) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        JWTGenerator jWTGenerator = new JWTGenerator();
        Key generateKey = jWTGenerator.generateKey(JWTGenerator.AMBIENTE.TEST_BABEL, "siamofreschi");
        System.out.println(generateKey.toString());
    }
    
    public static void main1(String[] args) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, JoseException {

        JWTGenerator jWTGenerator = new JWTGenerator();

        // String pathP12 = "C:\\Users\\Amministratore\\Documents\\ProgettiJava\\jwt\\babeljwt.p12";
        String pswd = "siamofreschi";
        String keyIdCert = "BDS CA";

        String jws = jWTGenerator.createJWSTest(pswd, keyIdCert, "DMRGPP83E29D851C", "TSCMNN78H66E435S", "105", "test", "080105", JWTGenerator.AMBIENTE.TEST_BABEL);
        System.out.println("JWS_per_gipi: " + jws);
//        String token = Jwts.builder()
//                .setSubject("294718")
//                .claim("SSO_LOGIN", true)
//                .claim("COMPANY", "080105")
//                .setIssuedAt(new Date())
//                .signWith(SignatureAlgorithm.HS256, "B3G85C766382F4CA669609C8215876C65G09D181T7DFDT257BFDEDE51B43DCF7")
//                .compact();

//        System.out.println("JWS_di_login: " + token);
    }
}
