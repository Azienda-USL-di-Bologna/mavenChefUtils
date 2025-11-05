package it.bologna.ausl.parameters_client;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.URLName;
import javax.net.ssl.SSLContext;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author gdm
 */
public class ParametersRestClient implements ParametersClient {

    private final String baseURL;
    private final RestTemplate restTemplate;
    private static final Logger log = LogManager.getLogger(ParametersRestClient.class);

    private static String azienda;
    private static String ambiente;
    private static String encryptionKey;

    public ParametersRestClient() throws IOException {

        Properties config = readConfigFile();

        ambiente = config.getProperty("ambiente");
        azienda = config.getProperty("azienda");
        encryptionKey = config.getProperty("db.encryptionkey");

        this.baseURL = config.getProperty("parameters_service.uri");

        URLName url = new URLName(baseURL);
        restTemplate = new RestTemplate(setupHttpClient(url.getUsername(), url.getPassword(), true));

    }

    /**
     * Imposta il connection factory per il client http, per usare basic
     * authentication e/o diverse strategie SSL
     *
     * @param username
     * @param password
     * @return
     */
    private ClientHttpRequestFactory setupHttpClient(String username, String password, boolean trustAnySSL) {

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        if (trustAnySSL) {
            SSLContext sslContext = null;
            try {
                KeyStore ks = KeyStore.getInstance("JKS");
                InputStream jks = Thread.currentThread().getContextClassLoader().getResourceAsStream("it/bologna/ausl/parameters_client/resources/star.internal.ausl.bologna.it.crt.jks");
//                ks.load(new FileInputStream("/Users/andrea/NetBeansProjects/bdm_client_2/star.internal.ausl.bologna.it.crt.jks"), "siamofreschi".toCharArray());
                ks.load(jks, "siamofreschi".toCharArray());
                //sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).useTLS().build();
                sslContext = SSLContexts.custom().loadTrustMaterial(ks).useTLS().build();
            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException ex) {
                log.error(ex);
            } catch (IOException | CertificateException ex) {
                log.error(ex);
            }
            SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());

            clientBuilder.setSSLSocketFactory(sslConnectionFactory);

        }

        if (username != null && password != null) {
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
            clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        }

        HttpClient httpClient = clientBuilder.build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return requestFactory;
    }

    @Override
    public List<Map<String, String>> getCompleteRawParameters() throws IOException {
        List<Map<String, String>> parameters = restTemplate.getForObject(baseURL + "parametri/", List.class);
        return parameters;
    }

    @Override
    public Map<String, String> getCompleteRawParameter(String nomeParametro) throws IOException {
        List<Map<String, String>> parameters = restTemplate.getForObject(baseURL + "parametri/" + nomeParametro, List.class);
        if (!parameters.isEmpty()) {
            return parameters.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String> getRawParameters() throws IOException {
        List<Map<String, String>> parameters = getCompleteParameters();
        Map<String, String> lightParameters = new HashMap<>();
        parameters.stream().forEach(p -> lightParameters.put(p.get("nomeParametro"), p.get("valParametro")));
        return lightParameters;
    }

    @Override
    public String getRawParameter(String nomeParametro) throws IOException {
        Map<String, String> parameter = getCompleteParameter(nomeParametro);
        if (parameter != null) {
            return parameter.get("valParametro");
        } else {
            return null;
        }
    }

    @Override
    public String getAzienda() {
        return azienda;
    }

    @Override
    public String getAmbiente() {
        return ambiente;
    }

    @Override
    public String getEncryptionKey() {
        return encryptionKey;
    }
}
