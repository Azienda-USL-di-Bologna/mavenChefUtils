package it.bologna.ausl.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

public class JWTGenerator {

    public enum AMBIENTE {
        TEST_BABEL,
        PROD_BABEL,
        TEST_GIPI,
        PROD_GIPI
    }

    private static final String CERTIFICATE_EXTENSION = "pkcs12";
    private final String ALIAS_NAME_BABEL_TEST = "BABEL TEST";
    private final String ALIAS_NAME_BABEL_PROD = "BABEL PROD";
    private final String ALIAS_NAME_GIPI_PROD = "GIPI PROD";
    private final String ALIAS_NAME_GIPI_TEST = "GIPI TEST";
    private static final String CLAIM_REGIONE_AZIENDA = "codiceRegioneAzienda";
    private static final String CLAIM_REAL_USER = "REAL_USER";
    private static final String CLAIM_COMPANY = "COMPANY";
    private static final String CLAIM_MODE = "mode";
    private static final String CLAIM_FROM_INTERNET = "FROM_INTERNET";

    public JWTGenerator() {
    }

    public Key generateKey(AMBIENTE ambiente, String pswd) throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {

        InputStream resourceAsStream = null;
        KeyStore p12 = KeyStore.getInstance(CERTIFICATE_EXTENSION);
        String aliasName;

        switch (ambiente) {
            case TEST_BABEL:
                resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("BABEL_TEST.p12");
                aliasName = ALIAS_NAME_BABEL_TEST;
                break;
            case PROD_BABEL:
                resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("BABEL_PROD.p12");
                aliasName = ALIAS_NAME_BABEL_PROD;
                break;
            case TEST_GIPI:
                resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("GIPI_TEST.p12");
                aliasName = ALIAS_NAME_GIPI_TEST;
                break;
            case PROD_GIPI:
                resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("GIPI_PROD.p12");
                aliasName = ALIAS_NAME_GIPI_PROD;
                break;
            default:
                throw new IllegalArgumentException("parametro ambiente non settato correttamente");
        }

        p12.load(resourceAsStream, pswd.toCharArray());
        return p12.getKey(aliasName, pswd.toCharArray());
    }

    public String createJWS(Key key, String keyId, String realUser, String subject, String codiceAzienda, String mode, String codiceRegioneAzienda, boolean fromInternetLogin) throws JoseException {
        JwtClaims claims = new JwtClaims();
//        claims.setIssuer("Issuer");  //chi crea token e firma
//        claims.setAudience("Audience"); // destinatario del token
//        claims.setExpirationTimeMinutesInTheFuture(10); // quando scadrà il token (es: 10 minuti da ora)
        claims.setGeneratedJwtId(); // identificatore univoco del token
        claims.setIssuedAtToNow();  // quando il token è stato emesso/creato (es: now)
//        claims.setNotBeforeMinutesInThePast(2); // tempo prima nel quale il token non è ancora valido (es: 2 minti fa)
//        claims.setSubject("subject"); // il subject del token
        claims.setSubject(subject);
        claims.setClaim(CLAIM_REAL_USER, realUser);
        claims.setClaim(CLAIM_COMPANY, codiceAzienda);
        claims.setClaim(CLAIM_REGIONE_AZIENDA, codiceRegioneAzienda);
        claims.setClaim(CLAIM_MODE, mode);
        claims.setClaim(CLAIM_FROM_INTERNET, fromInternetLogin);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setKey(key);
        jws.setPayload(claims.toJson());
        //facilita il reperimento della chiave nel caso si avessero molti certificati
        jws.setKeyIdHeaderValue(keyId);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        String jwt = jws.getCompactSerialization();

        return jwt;
    }

//    public String createJWSTest(String pswd, String keyId, String subject, String codiceRegioneAzienda) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, JoseException {
//
//        JWTGenerator jWTGenerator = new JWTGenerator();
//
//        Key key = jWTGenerator.generateKey(it.bologna.ausl.generator.JWTGenerator.AMBIENTE.valueOf("TEST"), pswd);
//
//        String jws = jWTGenerator.createJWS(key, keyId, subject, codiceRegioneAzienda);
//        return jws;
//    }
    public String createJWSTest(String pswd, String keyId, String realUser, String subject, String codiceAzienda, String mode, String codiceRegioneAzienda, AMBIENTE ambiente) throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException, JoseException {

        JWTGenerator jWTGenerator = new JWTGenerator();

        Key key = jWTGenerator.generateKey(ambiente, pswd);

        String jws = jWTGenerator.createJWS(key, keyId, realUser, subject, codiceAzienda, mode, codiceRegioneAzienda, false);
        return jws;
    }

}
