package it.bologna.ausl.pdfconverterandsigner.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JTextField;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import it.bologna.ausl.pdfconverterandsigner.utils.DesEncrypter;
import it.bologna.ausl.pdfconverterandsigner.utils.MyPasswordPane;
import it.bologna.ausl.pdfconverterandsigner.utils.ProxyManager;
import java.net.URL;
import org.apache.http.client.CookieStore;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * Classe che facilita la creazione di una richiesta http post multipart
 * (HttpClient). La classe cerca prima di connettersi direttamente al server, se
 * si accorge che la connessione fallisce a causa di un proxy tenta la
 * connessione attraverso il proxy
 *
 * @author Giuseppe
 */
public class HttpMultiParamRequestCreator {

    private static ArrayList<String> proxyedURLs = new ArrayList<String>();

    private boolean alwaysProxyConnection;
    private boolean proxyEnabled;
    private DefaultHttpClient httpClient;
    private HttpPost httpPost;
    HttpContext localContext;
    private MultipartEntity reqMultipartEntity;
    private HttpEntity reqHttpEntity;
    private ProxyManager proxyInfo;
    private File proxySettingsFile;

    private final String DES_STRING = "____DeS_CrYpTeR_ClAsS_____";
    private String url;
    private int requestTimeout;
    private boolean proxySetted;
    private File tempFileToDelete;

    /**
     * Costruisce un oggetto "HttpMultiParamRequestCreator"
     *
     * @param url indirizzo verso il quale effetuuare la richiesta
     * @param requestTimeout timeoute della richiesta
     */
    public HttpMultiParamRequestCreator(String url, int requestTimeout) {

        this.proxyEnabled = false;
        this.proxySetted = false;
        this.proxyInfo = null;
        this.alwaysProxyConnection = false;
        this.proxySettingsFile = null;
        this.tempFileToDelete = null;

        this.url = url;
        this.requestTimeout = requestTimeout;

        reqMultipartEntity = null;
        reqHttpEntity = null;
        setHttpParameter(url, requestTimeout);
    }

    // setta i parametri della connessione
    private void setHttpParameter(String url, int requestTimeout) {

//        int timeoutIntegerValue = 0;
//        if (requestTimeout > Integer.MAX_VALUE)
//            timeoutIntegerValue = Integer.MAX_VALUE;
//        else
//            timeoutIntegerValue = (int) requestTimeout;
        httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, requestTimeout);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Integer.MAX_VALUE);
        httpClient.setRedirectHandler(new DefaultRedirectHandler());
//        httpClient.getParams().setParameter(ConnManagerPNames.TIMEOUT, requestTimeout);

        httpPost = new HttpPost(url);
        httpPost.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        httpPost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, requestTimeout);
        httpPost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Integer.MAX_VALUE);

        localContext = new BasicHttpContext();

//        httpPost.getParams().setParameter(ConnManagerPNames.TIMEOUT, requestTimeout);
    }

    /**
     * Aggiunge un header alla richiesta
     *
     * @param headerName il nome dell'header da aggiungere
     * @param HeaderValue il valore dell'header da aggiungere
     */
    public void addHeader(String headerName, String HeaderValue) {

        if (headerName.equalsIgnoreCase("Content-Length")) {
            httpPost.setHeader(headerName, HeaderValue);
        } else {
            if (httpPost.getLastHeader(headerName) != null) {
                httpPost.setHeader(headerName, HeaderValue);
            } else {
                httpPost.addHeader(headerName, HeaderValue);
            }
        }
    }

    public void setCookies(String cookies) {
        try {
            CookieStore cookieStore = new BasicCookieStore();
            String[] cookiesSplitted = cookies.split(";");
            for (String cookieString : cookiesSplitted) {
                try {
                    String[] cookieStringSplitted = cookieString.split("=");
                    String cookieName = cookieStringSplitted[0];
                    String cookieValue = cookieStringSplitted[1];
                    BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
                    cookie.setDomain(new URL(url).getHost());
                    cookieStore.addCookie(cookie);
                } catch (Exception ex) {
                    System.out.println("errore nel settaggio del cookie: " + cookieString + " lo salto");
                    ex.printStackTrace(System.out);
                }
            }

            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            localContext.setAttribute(ClientContext.COOKIE_SPEC, CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.setCookieStore(cookieStore);
        } catch (Exception ex) {
            System.out.println("Errore nel settaggio dei cookies");
            ex.printStackTrace(System.out);
        }

    }

    /**
     * Imposta la richiesta all'utente dei dati del proxy in caso la connessione
     * fallisce a causa di un errore lagato al proxy. I dati del proxy saranno
     * memorizzati su disco e nelle successive richieste non saranno più chiesti
     * all'utente
     *
     * @param proxySettingsPath file dal quale leggere/nel quale memorizzare gli
     * eventuali dati del proxy, se null l'opzione non viene abilitata
     */
    public void enableAutomaticProxyConnection(String proxySettingsPath) {
        if (proxySettingsPath != null) {
            this.proxyEnabled = true;
            this.proxySettingsFile = new File(proxySettingsPath);
        }
    }

    /**
     * Disabilita la richiesta all'utente dei dati del proxy in caso la
     * connessione fallisce a causa di un errore lagato al proxy.
     *
     */
    public void disableAutomaticProxyConnection() {
        this.proxyEnabled = false;
        this.proxySettingsFile = null;
    }

    /**
     * Fa in modo che la connessione avvenga attraverso il proxy anche al primo
     * tentativo.
     *
     * @param value "true" per impostare la connessione attraverso il proxy
     * anche al primo tentativo, "false" altrimenti
     */
    public void setAlwaysProxyConnection(boolean value) {
        this.alwaysProxyConnection = value;
    }

    /**
     * Aggiunge un parametro stringa alla richiesta
     *
     * @param paramName nome del parametro
     * @param paramValue valore del parametro
     * @throws UnsupportedEncodingException
     */
    public void addStringParam(String paramName, String paramValue) throws UnsupportedEncodingException {
        reqHttpEntity = null;
        if (reqMultipartEntity == null) {
            reqMultipartEntity = new MultipartEntity();
        }
        reqMultipartEntity.addPart(paramName, new StringBody(paramValue));
    }

    /**
     * Aggiunge un parametro (part) binario alla richiesta Il parametro binario
     * è identificato da un oggetto "File"
     *
     * @param paramName nome del parametro
     * @param paramValue valore del parametro
     */
    public void addFileParam(String paramName, File paramValue) {
        reqHttpEntity = null;
        if (reqMultipartEntity == null) {
            reqMultipartEntity = new MultipartEntity();
        }
        reqMultipartEntity.addPart(paramName, new FileBody(paramValue, "binary/octet-stream"));
    }

    /**
     * Aggiunge un parametro (part) binario alla richiesta Il parametro binario
     * è identificato da un "InputStream"
     *
     * @param paramName nome del parametro
     * @param paramValue valore del parametro
     * @param fileName nome del file che sarà creato sul server una volta
     * analizzata la richiesta multipart
     * @throws FileNotFoundException se il proxy è attivo e non riesce a creare
     * il file temporaneo a partire dall'input stream passato
     * @throws IOException se il proxy è attivo e non riesce a creare il file
     * temporaneo a partire dall'input stream passato
     */
    public void addFileParam(String paramName, InputStream paramValue, String fileName) throws FileNotFoundException, IOException {
        reqHttpEntity = null;
        if (reqMultipartEntity == null) {
            reqMultipartEntity = new MultipartEntity();
        }

        // la libreria non supporta un parametro di tipo inputstrem se si setta il proxy.
        // In questo caso trasformo l'input stream in un file temporaneo che memorizzerò nella stessa cartella che contiene il proxySettingsFile
        if (proxyEnabled) {
            FileOutputStream fos = null;
            try {
                tempFileToDelete = new File(proxySettingsFile.getParent() + "/" + fileName);
                fos = new FileOutputStream(tempFileToDelete);
                byte[] readData = new byte[1024];
                int i = paramValue.read(readData);
                while (i != -1) {
                    fos.write(readData, 0, i);
                    i = paramValue.read(readData);
                }
            } finally {
                try {
                    fos.close();
                } catch (Exception ex) {
                }
            }
            reqMultipartEntity.addPart(paramName, new FileBody(tempFileToDelete, "binary/octet-stream"));
        } else {
            reqMultipartEntity.addPart(paramName, new InputStreamBody(paramValue, "binary/octet-stream", fileName));
        }
    }

    /**
     * setta i bytes del body della richiesta
     *
     * @param bytes body della richiesta
     */
    public void setBytesToSend(byte[] bytes) {

        reqMultipartEntity = null;
        reqHttpEntity = new ByteArrayEntity(bytes);
    }

    /**
     * Esegue la richiesta e ritorna la risposta
     *
     * @return risposta del server, "null" se la richiesta non va a buon fine
     */
    public HttpResponse executeRequest() {
        boolean proxyTry = false;

        if (reqMultipartEntity != null) {
            httpPost.setEntity(reqMultipartEntity);
        } else {
            httpPost.setEntity(reqHttpEntity);
        }

        if (alwaysProxyConnection) {
            proxyTry = true;
            if (!setProxy()) {
                return null;
            }
        } else if (proxyEnabled && proxySettingsFile != null && proxySettingsFile.exists()) {

            try {
                // Read from disk using FileInputStream
                FileInputStream f_in = new FileInputStream(proxySettingsFile.getParent() + "/proxyedURLs.data");

                // Read object using ObjectInputStream
                ObjectInputStream obj_in = new ObjectInputStream(f_in);

                // Read an object
                Object obj = obj_in.readObject();

                proxyedURLs = (ArrayList<String>) obj;

                if (proxyedURLs.contains(url)) {
                    System.out.println("Url della richiesta trovata in cache proxy, eseguo la richiesta direttamente attraverso il proxy...");
                    proxyTry = true;
                    if (!setProxy()) {
                        return null;
                    }
                }
            } catch (Exception ex) {
            }
        }

        if (proxyTry) {
            System.out.println("Eseguo la richiesta attraverso il proxy: " + httpPost.getRequestLine() + "...");
        } else {
            System.out.println("Eseguo la richiesta: " + httpPost.getRequestLine() + "...");
        }

        HttpResponse response = null;

        boolean retry = true;
        while (retry) {
            try {
                try {
                    httpClient.getConnectionManager().getSchemeRegistry().register(
                            new Scheme("https", SSLSocketFactory.getSocketFactory(), 443)
                    );
                    response = httpClient.execute(httpPost, localContext);
                } catch (Exception ex) {
                    if (proxyEnabled) {
                        throw ex;
                    } else {
                        System.out.println("Connessione fallita");
                        ex.printStackTrace(System.out);
                        return null;
                    }
                }

                // se ho usato il proxy e la connessione è andata a buon fine memorizzo i gli eventuali dati aggiornati del proxy sul disco
                if (response.getStatusLine().getStatusCode() == 200 && proxyTry) {

                    if (!proxyedURLs.contains(url)) {
                        proxyedURLs.add(url);

                        // scrivo l'oggetto su disco in modo da sapere che questo url ha bisogno del proxy le volte successive
                        FileOutputStream f_out = null;
                        try {
                            // Write to disk with FileOutputStream
                            f_out = new FileOutputStream(proxySettingsFile.getParent() + "/proxyedURLs.data");

                            // Write object with ObjectOutputStream
                            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

                            // Write object out to disk
                            obj_out.writeObject(proxyedURLs);
                        } finally {
                            try {
                                f_out.close();
                            } catch (Exception ex) {
                            }
                        }
                    }
                    if (!proxySettingsFile.exists()) {
                        createProxySettingsFile(proxyInfo.getProxyHost(), Integer.parseInt(proxyInfo.getProxyPort()), proxyInfo.getProxyUsername(), new String(proxyInfo.getProxyPassword()));
                    }
                }
                // proxy autentication required
                if (response.getStatusLine().getStatusCode() == 407) {
                    httpClient.getConnectionManager().shutdown();
                    setHttpParameter(url, requestTimeout);
                    httpPost.setEntity(reqMultipartEntity);
                    throw new SocketException("Proxy Authentication Required");
                } else {
                    retry = false;
                }
            } //            catch (SocketException ex) {
            //                ex.printStackTrace(System.out);
            //                if (proxyEnabled && (ex.getMessage().contains("proxy") || ex.getMessage().contains("Proxy"))) {
            //                    System.out.println("Connessione non riuscita, provo a impostare il proxy...");
            //                    proxyTry = true;
            //                    // se ho già settato il proxy e ancora ho avuto lo stesso errore cancello il file con i dati del proxy e lo richiedo all'utente
            //                    if (proxySetted)
            //                        proxySettingsFile.delete();
            //                        if (!setProxy())
            //                            return null;
            //                }
            //                else {
            //                    ex.printStackTrace(System.out);
            //                    response = null;
            //                    retry = false;
            //                }
            //            }
            //            catch (ConnectTimeoutException ex) {
            //                ex.printStackTrace(System.out);
            //                System.out.println("Connessione non riuscita, provo a impostare il proxy...");
            //                proxyTry = true;
            //                // se ho già settato il proxy e ancora ho avuto lo stesso errore cancello il file con i dati del proxy e lo richiedo all'utente
            //                if (proxySetted)
            //                    proxySettingsFile.delete();
            //                if (!setProxy())
            //                    return null;
            //            }
            catch (UnknownHostException ex) {
                if (proxySetted) {
                    System.out.println("Connessione attraverso il proxy non riuscita, disabilito il proxy e riprovo...");
                    unSetProxy();
                    proxySettingsFile.delete();
                    proxyTry = false;
                } else {
                    ex.printStackTrace(System.out);
                    response = null;
                    retry = false;
                }
            } catch (SocketException ex) {
                if (ex.getMessage().toLowerCase().contains("connection reset")) {
                    if (proxySetted) {
                        System.out.println("Connessione attraverso il proxy non riuscita, disabilito il proxy e riprovo...");
                        unSetProxy();
                        proxySettingsFile.delete();
                        proxyTry = false;
                    } else {
                        ex.printStackTrace(System.out);
                        response = null;
                        retry = false;
                    }
                } else {
                    ex.printStackTrace(System.out);
                    System.out.println("Connessione non riuscita, provo a impostare il proxy...");
                    proxyTry = true;
                    // se ho già settato il proxy e ancora ho avuto lo stesso errore cancello il file con i dati del proxy e lo richiedo all'utente
                    if (proxySetted) {
                        proxySettingsFile.delete();
                    }
                    if (!setProxy()) {
                        return null;
                    }
                }
            } catch (Exception ex) {
//                ex.printStackTrace(System.out);
//                response = null;
//                retry = false;

                ex.printStackTrace(System.out);
                System.out.println("Connessione non riuscita, provo a impostare il proxy...");
                proxyTry = true;
                // se ho già settato il proxy e ancora ho avuto lo stesso errore cancello il file con i dati del proxy e lo richiedo all'utente
                if (proxySetted) {
                    proxySettingsFile.delete();
                }
                if (!setProxy()) {
                    return null;
                }
            }
        }
        if (tempFileToDelete != null && tempFileToDelete.exists()) {
            tempFileToDelete.delete();
        }

        return response;
    }

    /**
     * Chiude la connessione
     *
     */
    public void shutDownConnection() {

        httpClient.getConnectionManager().shutdown();
        proxySetted = false;
        httpClient = null;
        httpPost = null;
        reqMultipartEntity = null;
    }

    // imposta il proxy
    private boolean setProxy() {

        if (proxyEnabled) {
            proxyInfo = readProxyInfoFromDisk();

            if (proxyInfo == null) {

                boolean retry = true;
                JTextField hostTextField = new JTextField();
                JTextField portTextField = new JTextField();
                JTextField usernameTextField = new JTextField();
                MyPasswordPane passwordPane = null;

                String proxyHost = null;
                int proxyPort = 0;
                String proxyUsername = null;
                char[] proxyPassword = null;

                while (retry) {

                    if (proxyHost != null) {
                        hostTextField.setText(proxyHost);
                    }

                    if (proxyPort != 0) {
                        portTextField.setText(String.valueOf(proxyPort));
                    }

                    if (proxyUsername != null) {
                        usernameTextField.setText(proxyUsername);
                    }

                    //myp.setText("");
                    Object[] message = new Object[9];
                    message[0] = "Errore nella connessione.\nSe la rete è protetta da un proxy provare ad inserire i dati del proxy:";
                    message[1] = "Indirizzo del proxy:";
                    message[2] = hostTextField;
                    message[3] = "Porta del proxy:";
                    message[4] = portTextField;
                    message[5] = "Nome Utente:";
                    message[6] = usernameTextField;
                    message[7] = "Password:";

                    Object[] options = new Object[2];
                    options[0] = "OK";
                    options[1] = "Annulla";

                    passwordPane = new MyPasswordPane("Proxy", message, options);

                    if (passwordPane.getSelection() == 0) {

                        proxyHost = hostTextField.getText();
                        if (proxyHost.equals("")) {
                            proxyHost = null;
                        }

                        try {
                            if (portTextField == null || portTextField.getText().equals("")) {
                                proxyPort = 80;
                            } else {
                                proxyPort = Integer.parseInt(portTextField.getText());
                            }
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace(System.out);
                            proxyPort = 0;
                        }

                        proxyUsername = usernameTextField.getText();
                        proxyPassword = passwordPane.getPassword();

                        if (proxyHost != null && proxyPort != 0) {
                            proxyInfo = new ProxyManager(proxyHost, Integer.toString(proxyPort), proxyUsername, proxyPassword);
                            retry = false;
                        }
                    } else {
                        return false;
                    }
//                        retry = false;
                }
                //passwordField.setText("");
                passwordPane.dispose();
                proxyPassword = null;
            }

            System.out.println("Setto il proxy...");
            final HttpHost hcProxyHost = new HttpHost(proxyInfo.getProxyHost(), Integer.parseInt(proxyInfo.getProxyPort()), "http");

            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, hcProxyHost);
            if (proxyInfo.getProxyUsername() != null && proxyInfo.getProxyPassword() != null) {
                httpClient.getCredentialsProvider().setCredentials(
                        new AuthScope(proxyInfo.getProxyHost(), Integer.parseInt(proxyInfo.getProxyPort())),
                        new UsernamePasswordCredentials(proxyInfo.getProxyUsername(),
                                new String(proxyInfo.getProxyPassword()))
                );
            }
//            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, hcProxyHost);
        }
        proxySetted = true;
        return true;
    }

    // disabilita il proxy
    private void unSetProxy() {
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, null);
        httpClient.getCredentialsProvider().clear();
        proxySetted = false;
    }

    // legge le informazioni del proxy dal disco
    private ProxyManager readProxyInfoFromDisk() {
        FileInputStream fis = null;
        try {
            Properties proxyProperties = new Properties();
            fis = new FileInputStream(proxySettingsFile);
            proxyProperties.load(fis);

            String host = proxyProperties.getProperty("host");
            int port = Integer.parseInt(proxyProperties.getProperty("port"));
            String username = proxyProperties.getProperty("username");
            String encryptedPassword = proxyProperties.getProperty("encryptedPassword");

            DesEncrypter des = new DesEncrypter(DES_STRING);
            String decryptedPassword = des.decrypt(encryptedPassword);

            proxyInfo = new ProxyManager(host, Integer.toString(port), username, decryptedPassword.toCharArray());
            try {
                fis.close();
            } catch (Exception subEx) {
            }
            return proxyInfo;
        } catch (Exception ex) {
            try {
                fis.close();
            } catch (Exception subEx) {
            }
            proxySettingsFile.delete();
            return null;
        }
    }

    // crea il file contenente i dati del proxy che sarà usato per le altre richieste
    private void createProxySettingsFile(String proxyHost, int proxyPort, String proxyUsername, String proxyPassword) {
        Properties proxySettingsProperties = new Properties();
        FileOutputStream proxySettingsOutputStream = null;
        FileInputStream proxySettingsInputStream = null;
        try {
            proxySettingsOutputStream = new FileOutputStream(proxySettingsFile.getAbsolutePath());
            proxySettingsInputStream = new FileInputStream(proxySettingsFile.getAbsolutePath());
            proxySettingsProperties.load(proxySettingsInputStream);

            DesEncrypter encrypter = new DesEncrypter(DES_STRING);

            proxySettingsProperties.setProperty("host", proxyHost);
            proxySettingsProperties.setProperty("port", String.valueOf(proxyPort));
            proxySettingsProperties.setProperty("username", proxyUsername);
            proxySettingsProperties.setProperty("encryptedPassword", encrypter.encrypt(proxyPassword));

            proxySettingsProperties.store(proxySettingsOutputStream, "dati per l'accesso al proxy");
            try {
                proxySettingsInputStream.close();
            } catch (Exception subEx) {
            }
            try {
                proxySettingsOutputStream.close();
            } catch (Exception subEx) {
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            try {
                proxySettingsInputStream.close();
            } catch (Exception subEx) {
            }
            try {
                proxySettingsOutputStream.close();
            } catch (Exception subEx) {
            }
            proxySettingsFile.delete();
        }
    }
}
