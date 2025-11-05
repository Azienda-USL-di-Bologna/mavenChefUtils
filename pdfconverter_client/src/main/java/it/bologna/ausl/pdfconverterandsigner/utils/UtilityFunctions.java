package it.bologna.ausl.pdfconverterandsigner.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import it.bologna.ausl.pdfconverterandsigner.tools.HttpMultiParamRequestCreator;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

/**
 *
 * @author Giuseppe
 */
public class UtilityFunctions {

    /** Tutti i file in questo ArrayList saranno cancellati dalla chiamata alla funzione deleteTempFiles()
     *
     */
    public static ArrayList<File> filesToDelete = new ArrayList<File>();

    /** elimina tutti i file indicati dall ArrayList statico "filesToDelete"
     *
     */
    public static void deleteTempFiles() {

        for (int i=0; i<filesToDelete.size(); i++) {
            System.out.println("elimino il file temporaneo: " + filesToDelete.get(i) + "...");
            if (filesToDelete.get(i).delete()) {
                filesToDelete.remove(i);
                i--;
            }
        }
    }

    /** Cancella tutti i file nella directory passata
     *
     * @param dir la direcotry da svuotare
     */
    public static void deleteAllFilesInDirectory(File dir) {
    File[] filesList = dir.listFiles();
        if (filesList != null) {
            for (int i=0; i<filesList.length; i++) {
                filesList[i].delete();
            }
        }
    }

    /** Termina l'applicazione/applet ritornando lo status code passato
     * prima di terminare chiama deleteTempFiles()
     *
     * @param statusCode lo status code da restituire
     */
    public static void terminateApplication(int statusCode) {
        deleteTempFiles();
//        if (progressBar != null)
//            progressBar.terminate();
//        if (true) {
//            Thread[] activeThreads = new Thread[Thread.activeCount()];
//            Thread.enumerate(activeThreads);
//            for (int i=0; i<activeThreads.length; i++) {
////                if (activeThreads[i] != Thread.currentThread())
//                    activeThreads[i] = null;
//            }
//        }
//        else
        System.exit(statusCode);
    }

    /** Controlla se è possibile creare il file passato
     *
     * @param file se "file" è una direcotry controlla se è possibile creare un file al suo interno, se "file" è un file controlla se è possibile creare un file nella directory in cui si trova
     * @return "true" se è possibile creare il file, "false" altrimenti
     */
    public static boolean canCreateFile(File file) {
        File tempFile = null;
        FileOutputStream tempStream = null;
        try {
            tempFile = new File (file.isDirectory() ? file.getAbsolutePath(): file.getParent() + "/________firmasemplice____tmp." + new Random().nextDouble());
            while (tempFile.exists()) {
                tempFile = new File (file.isDirectory() ? file.getAbsolutePath(): file.getParent() + "/________firmasemplice____tmp." + new Random().nextDouble());
            }
            tempStream = new FileOutputStream(tempFile);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
        finally {
            try {
                if (tempStream != null)
                    tempStream.close();
            }
            catch (IOException ex) {
                return false;
            }
            finally {
                if (tempStream != null)
                    tempFile.delete();
            }
        }
    }

    /** Salva un file chiedendo all'utente di selezionarne la posizione
     *
     * @param fileToSave file da salvare
     * @param filesType tipi di file possibili da mostrare in Tipo di File ("entensione" e "descrizione del tipo")
     * @param defaultFileName nome di default del file da proporre all'utente
     * @return "true" se il file è salvato correttamente, "false" altrimenti
     */
    public static boolean saveFileAs(File fileToSave, ArrayList<HashMap.SimpleEntry<String, String>> filesType, String defaultFileName) {

        JFileChooser saveFileChooser = new JFileChooser();
        for (int i=0; i<filesType.size(); i++) {
            SimpleEntry<String, String> fileType = filesType.get(i);
            saveFileChooser.setFileFilter(new FileNameExtensionFilter(fileType.getValue(), fileType.getKey()));
        }

        saveFileChooser.setSelectedFile(new File(defaultFileName));
        boolean retry = true;
        File selectedFile = null;
        while (retry) {
            if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                if (saveFileChooser.getSelectedFile().exists()) {
                    int overwriteFileMessageResult = JOptionPane.showConfirmDialog(null, "Sovrascrivere il file esistente ?", "File Esistente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (overwriteFileMessageResult == JOptionPane.YES_OPTION) {
                        selectedFile = saveFileChooser.getSelectedFile();
                        if (!canCreateFile(selectedFile))
                            retry = true;
                        else
                            retry = false;
                    }
                }
                else {
                    selectedFile = saveFileChooser.getSelectedFile();
                    if (!canCreateFile(selectedFile))
                        retry = true;
                    else
                        retry = false;
                }
            }
            else {
                return false;
            }
        }

        DataOutputStream dataos = null;
        DataInputStream datais = null;
        try {
            datais = new DataInputStream(new FileInputStream(fileToSave));
            byte[] bytes = new byte[(int)fileToSave.length()];
            datais.readFully(bytes);
            dataos = new DataOutputStream(new FileOutputStream(selectedFile));
            dataos.write(bytes);
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace(System.out);
            return false;
        }
        finally {
            try {
                datais.close();
            }
            catch (Exception ex) {
            }
            try {
                dataos.close();
            }
            catch (Exception ex) {
            }
        }
    }

    /** Scrive una stringa in un file
     *
     * @param filename il file da creare
     * @param stringToWrite la stringa da scrivere nel file
     * @throws IOException
     */
    public static void writeStringToFile(String filename, String stringToWrite) throws IOException {
        BufferedWriter out = null;
        try {
        out = new BufferedWriter(new FileWriter(filename));
        out.write(stringToWrite);
        out.close();
        }
        finally {
            try {
                out.close();
            }
            catch (Exception ex) {
            }
        }
    }

    /** Restituisce un InputStream della stringa passata
     *
     * @param str la stringa della quale si vule ottenere lo stream
     * @return L'InputStream della stringa passata
     */
    public static InputStream stringToInputStream(String str) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(str.getBytes("UTF-8"));
            return is;
        }
        catch (Exception ex) {
            ex.printStackTrace(System.out);
            return null;
        }
        finally {
            try {
                is.close();
            }
            catch (Exception ex) {
            }
        }
    }
    
    /** Converte un InputStream in una stringa
     * 
     * @param is l'InputStream da convertire
     * @return L'inputStream convertito in stringa
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public static String inputStreamToString(InputStream is) throws UnsupportedEncodingException, IOException {
    Writer stringWriter = new StringWriter();
    char[] buffer = new char[1024];
        try {
             Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             int n;
             while ((n = reader.read(buffer)) != -1) {
                 stringWriter.write(buffer, 0, n);
             }
         }
         finally {
         }
         return stringWriter.toString();
    }
    
    /** Legge un InputStream e ne ritorna i bytes
     * 
     * @param is l'InputStream da leggere
     * @return i bytes letti
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    public static byte[] inputStreamToBytes(InputStream is) throws UnsupportedEncodingException, IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            byte[] readData = new byte[1024];
            int bytesRead = is.read(readData);
            while (bytesRead > 0) {
                baos.write(readData, 0, bytesRead);
                bytesRead = is.read(readData);
            }
            return baos.toByteArray();
        }
        finally {
            is.close();
            baos.close();
        }
    }

    /** Scrive un InputStream in un file
     * 
     * @param inputStream l'InpurStream da scrivere
     * @param fileToCreate il file da creare
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void inputStreamToFile(InputStream inputStream, File fileToCreate) throws FileNotFoundException, IOException {
        OutputStream os = new FileOutputStream(fileToCreate);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
    }
    
    /**
     *  Ritrona i bytes del file passato
     * @param file il file
     * @return i bytes del file passato
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static byte[] getBytes(File file) throws FileNotFoundException, IOException {
        DataInputStream datais = new DataInputStream(new FileInputStream(file));
        byte[] bytes = new byte[(int)file.length()];
        datais.readFully(bytes);
        return bytes;
    }

    /** Invia il file in input alla servlet identificata dall'indirizzo passato
     * 
     * @param file il file da inviare
     * @param serviceURL l'indirizzo della servlet che riceverà il file
     * @param params eventuali parametri multipart da aggiungere alla richiesta
     * @param proxySettingsFile file contenente i dati del proxy (se il file non esiste e viene rilevato un proxy verrà creato)
     * @param progressBar barra del progresso che verrà aumentata durante le varie fasi. Passare "null" se non si vuole utilizzare
     * @return un stringa contenente la risposta della servlet
     */
    public static String sendToServer(File file, String serviceURL, String cookies, Map<String, String> params, String proxySettingsFile, MyProgressBar progressBar) {

        // aumento la barra del progresso
        if (progressBar != null) {
            progressBar.setTitle("Invio al server. Attendi...");
//            progressBar.setAlwaysOnTop(true);
            progressBar.setValue(10);
        }

        HttpMultiParamRequestCreator httpRequest = new HttpMultiParamRequestCreator(serviceURL, 20000);
        httpRequest.enableAutomaticProxyConnection(proxySettingsFile);

        if (cookies != null) {
            httpRequest.setCookies(cookies);
        }

        // inserisco i parametri passati alla richiesta da effettuare al server
        if (params != null) {
            Set<String> keySet = params.keySet();
            Iterator<String> keySetIterator = keySet.iterator();
            while (keySetIterator.hasNext()) {
                String keyName = keySetIterator.next();
                try {
                    httpRequest.addStringParam(keyName, params.get(keyName));
                }
                catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace(System.out);
                }
            }
        }

        httpRequest.addFileParam("upload-file", file);

        // aumento la barra del progresso
        if (progressBar != null)
            progressBar.setValue(30);
        
        HttpResponse response = httpRequest.executeRequest();
        if (response == null) {
            return null;
        }

        // aumento la barra del progresso
        if (progressBar != null)
            progressBar.setValue(70);

        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());

        if (response.getStatusLine().getStatusCode() != 200) {
            // aumento la barra del progresso
            if (progressBar != null)
                progressBar.setValue(100);
            return null;
        }
        else {

            BufferedReader entityBufferedReader = null;
            try {
                entityBufferedReader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
                String line = entityBufferedReader.readLine();
                String serverResponse = "";
                while (line != null) {
                    serverResponse += line;
                    line = entityBufferedReader.readLine();
                    if (line != null)
                        serverResponse += "\n";
                }
                return serverResponse;
            }
            catch (IllegalStateException ex) {
                ex.printStackTrace(System.out);
                return null;
            }
            catch (IOException ex) {
                ex.printStackTrace(System.out);
                return null;
            }
            finally {
                try {   
                    // aumento la barra del progresso
                    if (progressBar != null)
                        progressBar.setValue(100);

                    entityBufferedReader.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace(System.out);
                    return null;
                }
                httpRequest.shutDownConnection();
            }
        }
    }

    /** Esegue l'upload verso un gestore documentale che implementa CMIS
     *
     * @param file il file da inviare
     * @param cmisUrl l'indirizzo del gestore documentale
     * @param cmisDirecotryId l'id della directory in cui eseguire l'upload
     * @param cmisFileName il nome del file da creare
     * @param cmisUser lo username per l'accesso (ignorato se si passa il token)
     * @param cmisPassword la password per l'accesso (ignorato se si passa il token)
     * @param cmisToken un token per l'accesso (ignora username e password)
     * @param progressBar barra del progresso che verrà aumentata durante le varie fasi. Passare "null" se non si vuole utilizzare
     * @return l'id del file sul gestore documentale
     * @throws FileNotFoundException
     */
  
    public static void trustAllHostNameAndCertificate(final String hostNameToTrust) throws NoSuchAlgorithmException, KeyManagementException {

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                if (hostNameToTrust != null) {
                    if (hostname.equals("localhost") || hostname.equals(hostNameToTrust)) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else {
                    if (hostname.equals("localhost")) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        });

        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, java.lang.String str) {
                    }
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, java.lang.String str) {
                    }
            }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null,trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        HttpClient client = new DefaultHttpClient();
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 8443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        HttpClient http = new DefaultHttpClient(mgr, client.getParams());

//        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }
}
