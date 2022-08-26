package it.bologna.ausl.pdfconverterandsigner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import it.bologna.ausl.pdfconverterandsigner.tools.HttpMultiParamRequestCreator;
import it.bologna.ausl.pdfconverterandsigner.utils.MyProgressBar;
import it.bologna.ausl.pdfconverterandsigner.utils.UtilityFunctions;
import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpResponseException;
import it.bologna.ausl.mimetypeutilities.Detector;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeType;

/** Converte in pdf/a un file richiamando la servlet di conversione
 *
 * @author Giuseppe
 */
public class PdfConvertClient {

private final int SC_OK = 200;
private final int SC_NO_CONTENT = 204;
private final int SC_PARTIAL_CONTENT = 206;
private final int SC_BAD_REQUEST = 400;
private final int SC_NOT_FOUND = 404;
//private final int SC_METHOD_NOT_ALLOWED = 405;
private final int SC_NOT_ACCEPTABLE = 406;
private final int SC_UNSUPPORTED_MEDIA_TYPE = 415;
private final int SC_INTERNAL_SERVER_ERROR = 500;
private final int SC_SERVICE_UNAVAILABLE = 503;
    
private MyProgressBar progressBar;
private String pdfConvertServiceUrl;

// mappa di parametri multipart da passare alla servlet
private Map<String, String> requestMultipartParams;

private File tempDir;
private ArrayList<File> filesToDelete;
private String proxySettingsFile;

    /** Crea l'oggetto PdfConvertClient senza barra del progresso
     *
     * @param pdfConvertServiceUrl indirizzo della servlet di conversione
     * @param tempDir directory che l'oggetto utilizzerà per i file temporanei
     * @param proxySettingsFile file contenente i dati del proxy (se il file non esiste e viene rilevato un proxy verrà creato)
     */
    public PdfConvertClient(String pdfConvertServiceUrl, File tempDir, String proxySettingsFile) {
       this.pdfConvertServiceUrl = pdfConvertServiceUrl;
       this.tempDir = tempDir;
       this.progressBar = null;
       this.filesToDelete = new ArrayList<File>();
       this.proxySettingsFile = proxySettingsFile;
    }

     /** Crea l'oggetto PdfConvertClient con la barra del progresso
     *
     * @param pdfConvertServiceUrl indirizzo della servlet di conversione
     * @param tempDir directory che l'oggetto utilizzerà per i file temporanei
     * @param proxySettingsFile file contenente i dati del proxy (se il file non esiste e viene rilevato un proxy verrà creato)
     * @param progressBar Istanza di un oggetto "MyProgressBar" che sarà usata come barra del progresso
     */
    public PdfConvertClient(String pdfConvertServiceUrl, File tempDir, String proxySettingsFile, MyProgressBar progressBar) {
       this.pdfConvertServiceUrl = pdfConvertServiceUrl;
       this.tempDir = tempDir;
       this.progressBar = progressBar;
       this.filesToDelete = new ArrayList<File>();
       this.proxySettingsFile = proxySettingsFile;
    }

    public void trustAllServerCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        UtilityFunctions.trustAllHostNameAndCertificate(proxySettingsFile);
    }

    public Map<String, String> getRequestMultipartParams() {
        return requestMultipartParams;
    }

    public void setRequestMultipartParams(Map<String, String> requestMultipartParams) {
        this.requestMultipartParams = requestMultipartParams;
    }

    public ArrayList<File> getFileToDeleteList() {
        return filesToDelete;
    }

    public void convert(String htmlString, File destFile, boolean silentMode, ArrayList<HashMap.SimpleEntry<File, String>> errorMessages) throws IOException {
        InputStream inputStream = UtilityFunctions.stringToInputStream(htmlString);
        internalConvert(null, inputStream, destFile, null, silentMode, errorMessages);
    }

    /** Crea il file passato nel parametro "destFile" come conversione in pdf del file passato nel parametro "inputFile"
     *
     * @param inputFile il file da convertire
     * @param destFile il file pdf da creare
     * @param silentMode se "true" riduce al minimo il prompt di messaggi/richieste all'utente
     * @param errorMessages viene riempito con gli errori/richieste non mostrate all'utente (solo se silentmode uguale a "true")
     * @return il file creato, che in questo caso coincide con destFile
     * @throws IOException
     */
    public File convert(File inputFile, File destFile, boolean silentMode, ArrayList<HashMap.SimpleEntry<File, String>> errorMessages) throws IOException {
        return internalConvert(inputFile, null, destFile, null, silentMode, errorMessages);
    }
    
    /** converte l'InputStream passato in pdf e ne ritorna i bytes
     *
     * @param is InputStream del file da convertire
     * @param silentMode se "true" riduce al minimo il prompt di messaggi/richieste all'utente
     * @param errorMessages viene riempito con gli errori/richieste non mostrate all'utente (solo se silentmode uguale a "true")
     * @return i bytes del file creato
     * @throws IOException
     */
    public byte[] convert(InputStream is, boolean silentMode, ArrayList<HashMap.SimpleEntry<File, String>> errorMessages) throws IOException {

        HttpMultiParamRequestCreator httpRequest = new HttpMultiParamRequestCreator(pdfConvertServiceUrl, 20000);
        InputStream convertedFileInputStream = doHttpRequest(httpRequest, is, null, silentMode, errorMessages);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            byte[] readData = new byte[1024];
            int bytesRead = convertedFileInputStream.read(readData);

            while (bytesRead > 0) {
                baos.write(readData, 0, bytesRead);
                bytesRead = convertedFileInputStream.read(readData);
            }
            return baos.toByteArray();
        }
        finally {
            IOUtils.closeQuietly(convertedFileInputStream);
            httpRequest.shutDownConnection();
            IOUtils.closeQuietly(baos);
        }
    }

    /** Crea un file pdf convertendo in pdf il file passato nel prametro "inputFile".
     * Il file pdf sarà creato nella stessa directory di "inputFile" e il suo nome sara lo stesso di "inputFile" con l'aggiunta della stringa [1]
     * @param inputFile il file da convertire
     * @param silentMode se "true" riduce al minimo il prompt di messaggi/richieste all'utente
     * @param errorMessages viene riempito con gli errori/richieste non mostrate all'utente (solo se silentmode uguale a "true")
     * @return il file creato
     * @throws IOException
     */
    public File convert(File inputFile, boolean silentMode, ArrayList<HashMap.SimpleEntry<File, String>> errorMessages) throws IOException {
        return internalConvert(inputFile, null, null, null, silentMode, errorMessages);
    }

    /** Crea un file pdf convertendo in pdf il file presente all'indirizzo indicato nel parametro "inputFileURL".
     * La posizione nella quale salvare il file sarà chiesta all'utente
     * @param inputFileURL il file da scaricare e convertire
     * @param silentMode se "true" riduce al minimo il prompt di messaggi/richieste all'utente
     * @param errorMessages viene riempito con gli errori/richieste non mostrate all'utente (solo se silentmode uguale a "true")
     * @return il file creato
     * @throws IOException
     */
    public File convert(URL inputFileURL, boolean silentMode, ArrayList<HashMap.SimpleEntry<File, String>> errorMessages) throws IOException {
        return convert(inputFileURL, null, silentMode, errorMessages);
    }

    /** Crea il file passato nel parametro "destFile" come conversione in pdf del file passato nel parametro "inputFileURL"
     *
     * @param inputFileURL il file da scaricare e convertire
     * @param destFile il file pdf da creare
     * @param silentMode se "true" riduce al minimo il prompt di messaggi/richieste all'utente
     * @param errorMessages viene riempito con gli errori/richieste non mostrate all'utente (solo se silentmode uguale a "true")
     * @return un oggeto "File" del quale è utile solo il nome. Il nome contiene il nome originale del file creato (quello assegnato dal server che è ottenuto come nomeFileScaricato_converted)
     * @throws IOException
     */
    public File convert(URL inputFileURL, File destFile, boolean silentMode, ArrayList<HashMap.SimpleEntry<File, String>> errorMessages) throws IOException {
        // aumento la barra del progresso
        if (progressBar != null)
            progressBar.setValue(10);

        System.out.println("Scaricamento del file dopo che è stato processsato dal server...");
        File inputFile = new File(inputFileURL.toString());

        // converto il file in pdf passando alla servlet l'url del file da convertire
        HttpMultiParamRequestCreator httpRequest = new HttpMultiParamRequestCreator(pdfConvertServiceUrl, 20000);
        httpRequest.addHeader("User-agent", "ConverterClient");
        httpRequest.enableAutomaticProxyConnection(proxySettingsFile);

        try {
             httpRequest.addStringParam("FileUrl", inputFileURL.toString());
             httpRequest.addStringParam("ForceSignedFileConvert", "true");

            if (requestMultipartParams != null) {
                Iterator<Entry<String,String>> paramsSetIterator = requestMultipartParams.entrySet().iterator();

                while (paramsSetIterator.hasNext()) {
                   Map.Entry<String, String> requestParam = paramsSetIterator.next();
                   httpRequest.addStringParam(requestParam.getKey(), requestParam.getValue());
                }
            }
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace(System.out);
            if (silentMode) {
                if (errorMessages != null)
                    errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;Errore nella creazione della richiesta da inviare al server (proprietà campo firma da creare)"));
            }
            else
                JOptionPane.showMessageDialog(null, "Errore nella creazione della richiesta da inviare al server (nome del file del modello da allegare)", "Errore richiesta", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // aumento la barra del progresso
        if (progressBar != null)
            progressBar.setValue(30);

        HttpResponse response = httpRequest.executeRequest();
        if (response == null) {
            if (silentMode) {
                if (errorMessages != null)
                    errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;Impossibile connettersi al server"));
            }
            else
                JOptionPane.showMessageDialog(null, "Impossibile connettersi al server", "Errore connessione", JOptionPane.ERROR_MESSAGE);
            throw new IOException("Impossibile connettersi al server");
//            return null;
        }

        HttpEntity resEntity = response.getEntity();
        System.out.println(response.getStatusLine());

        if (response.getStatusLine().getStatusCode() != SC_OK) {
        String errorMessage = "Errore sconosciuto";
            switch (response.getStatusLine().getStatusCode()) {
//                case SC_METHOD_NOT_ALLOWED:
//                    errorMessage = "Impossibile convertire il file, il file sul server è già un pdf";
//                    break;
                case SC_BAD_REQUEST:
                    errorMessage = "Errore nell'inserimento del campo firma";
                    break;
                case SC_NOT_ACCEPTABLE:
                    errorMessage = "Manca l'estensione del file, impossibile convertirlo in pdf";
                    break;
                case SC_SERVICE_UNAVAILABLE:
                    errorMessage = "Errore nella connessione a OpenOffice";
                    break;
                case SC_UNSUPPORTED_MEDIA_TYPE:
                    errorMessage = "Errore nella conversione in pdf, formato file non supportato";
                    break;
                case SC_PARTIAL_CONTENT:
                    errorMessage = "Errore nell'unione del modello al documento";
                    break;
                case SC_NOT_FOUND:
                    errorMessage = "L'indirizzo del servizio di conversione è errato o inesistente.\n\"" + pdfConvertServiceUrl + "\"";
                    break;
                case SC_NO_CONTENT:
                    errorMessage = "Non esiste nessun file all'indirizzo: \"" + inputFileURL + "\"";
                    break;
                case SC_INTERNAL_SERVER_ERROR:
                    errorMessage = "Errore interno del server";
                    break;
            }
            if (silentMode) {
                if (errorMessages != null)
                    errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;" + errorMessage));
            }
            else
                JOptionPane.showMessageDialog(null, errorMessage, "Errore del server", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // aumento la barra del progresso
        if (progressBar != null)
            progressBar.setValue(40);

        InputStream recivedis = null;
//        File recivedFile = null;
        try {
            try {
//                    resEntity.getContent();
                recivedis = resEntity.getContent();
            }
            catch (IllegalStateException ex) {
                ex.printStackTrace(System.out);
                if (silentMode) {
                    if (errorMessages != null)
                        errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;Errore nella decodifica del pdf ricevuto dal server"));
                }
                else
                    JOptionPane.showMessageDialog(null, "Errore nella decodifica del pdf ricevuto dal server", "Errore risposta", JOptionPane.ERROR_MESSAGE);
                return null;
            }
//                recivedPdfFilePath = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf('.')) + "_recived_.temp";

//            File tempDir = new File(converterAndSignerMain.appPath + "/temp_recived/");
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }
            File tempFile = new File(tempDir.getAbsolutePath() + "/___tempFile___" + new Random().nextDouble());
            filesToDelete.add(tempFile);

            FileOutputStream tempos = null;
            if (destFile != null)
                tempos = new FileOutputStream(destFile);
            else
                tempos = new FileOutputStream(tempFile);
            byte[] readData = new byte[1024];
            int bytesRead = recivedis.read(readData);

            while (bytesRead > 0) {
                tempos.write(readData, 0, bytesRead);
                bytesRead = recivedis.read(readData);
            }
            recivedis.close();
            tempos.close();
            httpRequest.shutDownConnection();

            // aumento la barra del progresso
            if (progressBar != null)
                progressBar.setValue(60);

            inputFile = new File(response.getHeaders("Content-Disposition")[0].getValue().split("=")[1]);
            if (destFile != null) {
//                tempFile.renameTo(destFile);

                // aumento la barra del progresso
                if (progressBar != null)
                    progressBar.setValue(100);
                return inputFile;
//                return destFile;
            }
            else
                return internalConvert(inputFile, null, null, tempFile, silentMode, errorMessages);
        }
        catch (IOException ex) {
            ex.printStackTrace(System.out);
            if (silentMode) {
                if (errorMessages != null)
                    errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;Errore nella decodifica del pdf ricevuto dal server"));
            }
            else
                JOptionPane.showMessageDialog(null, "Errore nella decodifica del pdf ricevuto dal server", "Errore risposta", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    
    private InputStream doHttpRequest(HttpMultiParamRequestCreator httpRequest, InputStream inputStream, File inputFile, boolean silentMode, ArrayList<HashMap.SimpleEntry<File, String>> errorMessages) throws FileNotFoundException, IOException {
    HttpEntity resEntity = null;
    File srcTempFile = null;
        try {
            // aumento la barra del progresso
            if (progressBar != null)
                progressBar.setValue(10);

            // converto il file in pdf passando alla servlet il file da convertire
            System.out.println("Converto il file...");

            System.out.println("Collegamento al server...");
            httpRequest.addHeader("User-agent", "ConverterClient");
            httpRequest.enableAutomaticProxyConnection(proxySettingsFile);

            // aumento la barra del progresso
            if (progressBar != null)
                progressBar.setValue(30);


            try {
                if (inputStream != null) {
    //                byte[] inputStreamBytes = UtilityFunctions.inputStreamToBytes(inputStream);
                    String fileType;
                    srcTempFile = File.createTempFile(getClass().getSimpleName(), null);
                    UtilityFunctions.inputStreamToFile(inputStream, srcTempFile);
                    try {
                        Detector d = new Detector();
                        String mediaType = d.getMimeType(srcTempFile.getAbsolutePath());
                        TikaConfig config = TikaConfig.getDefaultConfig();
                        MimeType mimeType = config.getMimeRepository().forName(mediaType);
    //                    if (mediaType == MediaType.TEXT_PLAIN)
    //                        fileType = "txt";
    //                    else
                        fileType = mimeType.getExtension().substring(1);
                    }
                    catch (Exception ex) {
                        fileType = "txt";
                    }

    //                ByteArrayInputStream bis = new ByteArrayInputStream(inputStreamBytes);
                    httpRequest.addFileParam("upload-file", new FileInputStream(srcTempFile), srcTempFile.getName() + "." + fileType);
                }
                else
                    httpRequest.addFileParam("upload-file", inputFile);

                httpRequest.addStringParam("ForceSignedFileConvert", "true");
                if (requestMultipartParams != null) {
                    Iterator<Entry<String, String>> paramsSetIterator = requestMultipartParams.entrySet().iterator();

                    while (paramsSetIterator.hasNext()) {
                        Map.Entry<String, String> requestParam = paramsSetIterator.next();
                        httpRequest.addStringParam(requestParam.getKey(), requestParam.getValue());
                    }
                }
            }
            catch (UnsupportedEncodingException ex) {
                ex.printStackTrace(System.out);
                if (silentMode) {
                    if (errorMessages != null)
                        errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;Impossibile connettersi al server"));
                }
                else
                    JOptionPane.showMessageDialog(null, "Impossibile connettersi al server", "Errore connessione", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // aumento la barra del progresso
            if (progressBar != null)
                progressBar.setValue(50);

            HttpResponse response = httpRequest.executeRequest();
            if (response == null) {
                if (silentMode) {
                    if (errorMessages != null)
                        errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;Impossibile connettersi al server"));
                }
                else
                    JOptionPane.showMessageDialog(null, "Impossibile connettersi al server", "Errore connessione", JOptionPane.ERROR_MESSAGE);
                throw new IOException("Impossibile connettersi al server");
            }

            resEntity = response.getEntity();
            System.out.println(response.getStatusLine());

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != SC_OK) {
            String errorMessage = "Errore sconosciuto";
                switch (response.getStatusLine().getStatusCode()) {
                    case SC_BAD_REQUEST:
                        errorMessage = "Errore nell'inserimento del campo firma";
                        break;
                    case SC_NOT_ACCEPTABLE:
                        errorMessage = "Manca l'estensione del file, impossibile convertirlo in pdf";
                        break;
                    case SC_SERVICE_UNAVAILABLE:
                        errorMessage = "Errore nella connessione a OpenOffice";
                        break;
                    case SC_UNSUPPORTED_MEDIA_TYPE:
                        errorMessage = "Errore nella conversione in pdf, formato file non supportato";
                        break;
                    case SC_PARTIAL_CONTENT:
                        errorMessage = "Errore nell'unione del modello al documento";
                        break;
                    case SC_NOT_FOUND:
                        errorMessage = "L'indirizzo del servizio di conversione è errato o inesistente.\n\"" + pdfConvertServiceUrl + "\"";
                        break;
                    case SC_INTERNAL_SERVER_ERROR:
                        errorMessage = "Errore interno del server";
                        break;
                }
                throw new HttpResponseException(statusCode, errorMessage);
    //            if (silentMode) {
    //                if (errorMessages != null)
    //                    errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;" + errorMessage));
    //            }
    //            else
    //                JOptionPane.showMessageDialog(null, errorMessage, "Errore del server", JOptionPane.ERROR_MESSAGE);
    //            return null;
            }
            return resEntity.getContent();
        }
        finally {
            if (srcTempFile != null)
               srcTempFile.delete();
        }
    }
    
    // converte in pdf chiamando la servlet (nel caso di input locale) e gestisce la creazione del file in locale
    private File internalConvert(File inputFile, InputStream inputStream, File destFile, File tempFile, boolean silentMode, ArrayList<HashMap.SimpleEntry<File, String>> errorMessages) throws IOException {
    String recivedPdfFilePath = null;
    if (inputFile != null) {
        recivedPdfFilePath = inputFile.getAbsolutePath();
    }

        // aumento la barra del progresso
        if (progressBar != null)
            progressBar.setValue(10);

        // se tempFile == null vuol dire che il file è stato già convertito e lo devo solo salvare
        boolean remoteInput = false;
        if (tempFile != null)
            remoteInput = true;

        // converto il file in pdf passando alla servlet il file da convertire
        if (!remoteInput) {
            HttpMultiParamRequestCreator httpRequest = new HttpMultiParamRequestCreator(pdfConvertServiceUrl, 20000);
            InputStream tempis = doHttpRequest(httpRequest, inputStream, inputFile, silentMode, errorMessages);
            if (inputFile != null)
                recivedPdfFilePath = inputFile.getAbsolutePath().substring(0, inputFile.getAbsolutePath().lastIndexOf('.')) + "[1].pdf";

            // aumento la barra del progresso
            if (progressBar != null)
                progressBar.setValue(70);

            // salvo il file in una directory temporanea in modo da chiudere subito la connessione con il server ed evitare che cada
            try {
//                File tempDir = new File(converterAndSignerMain.appPath + "/temp_recived/");
                if (!tempDir.exists()) {
                    tempDir.mkdir();
                }
                tempFile = new File(tempDir.getAbsolutePath() + "/___tempFile___" + new Random().nextDouble());
                filesToDelete.add(tempFile);
                FileOutputStream tempos = null;
                if (destFile != null)
                    tempos = new FileOutputStream(destFile);
                else
                    tempos = new FileOutputStream(tempFile);
                byte[] readData = new byte[1024];
                int bytesRead = tempis.read(readData);

                while (bytesRead > 0) {
                    tempos.write(readData, 0, bytesRead);
                    bytesRead = tempis.read(readData);
                }
                tempis.close();
                tempos.close();

                httpRequest.shutDownConnection();
                if (destFile != null) {
//                    tempFile.renameTo(destFile);
//                    tempFile = destFile;

                    // aumento la barra del progresso
                    if (progressBar != null)
                        progressBar.setValue(100);

                    return destFile;
                }
            }
            catch (IOException ex) {
                ex.printStackTrace(System.out);
                if (silentMode) {
                    if (errorMessages != null)
                        errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;Errore nella decodifica del pdf ricevuto dal server"));
                }
                else
                    JOptionPane.showMessageDialog(null, "Errore nella decodifica del pdf ricevuto dal server", "Errore risposta", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            catch (UnsupportedOperationException ex) {
                ex.printStackTrace(System.out);
                if (silentMode) {
                    if (errorMessages != null)
                        errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "error;Errore nella decodifica del pdf ricevuto dal server"));
                }
                else
                    JOptionPane.showMessageDialog(null, "Errore nella decodifica del pdf ricevuto dal server", "Errore risposta", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        // aumento la barra del progresso
        if (progressBar != null)
            progressBar.setValue(50);

        File recivedFile = new File(recivedPdfFilePath);
        JFileChooser saveFileChooser = new JFileChooser(recivedFile);
        saveFileChooser.setSelectedFile(new File (saveFileChooser.getCurrentDirectory() + "/" + recivedFile.getName()));
        saveFileChooser.setFileFilter(new FileNameExtensionFilter("PDF File", "pdf"));

        boolean retry = false;
        if (remoteInput || !UtilityFunctions.canCreateFile(inputFile))
            retry = true;
        while (retry) {
            if (progressBar != null)
                progressBar.setAlwaysOnTop(false);

            if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                recivedFile = saveFileChooser.getSelectedFile();
                if (!UtilityFunctions.canCreateFile(recivedFile))
                    retry = true;
                else
                    retry = false;
            }
            else {
                if (silentMode) {
                    if (errorMessages != null)
                        errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "warning;Conversione del file annullata dall'utente"));
                }
                return null;
            }
        }
        if (progressBar != null)
            progressBar.setAlwaysOnTop(true);

        // aumento la barra del progresso
        if (progressBar != null)
            progressBar.setValue(80);

        saveFileChooser.setSelectedFile(recivedFile);

        boolean showedExistingFileWarnMessage = false;
        Object[] options = new Object[3];
        options[0] = "     Si     ";
        options[1] = "Salva con nome";
        if (silentMode)
            options[2] = "    Salta   ";
        else
            options[2] = "   Annulla  ";
        retry = false;
        while (recivedFile.exists() || retry) {
            retry = false;
            if (!showedExistingFileWarnMessage) {
                showedExistingFileWarnMessage = true;
                int existingFileWarnMessageResult = JOptionPane.showOptionDialog(null, "Esiste già un file con il nome del file convertito: \"" + recivedFile.getName() + "\" sovrascriverlo ?", recivedFile.getName() + " esiste", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

                if (existingFileWarnMessageResult == JOptionPane.YES_OPTION)
                    break;
                else if(existingFileWarnMessageResult == JOptionPane.CANCEL_OPTION || existingFileWarnMessageResult == JOptionPane.CLOSED_OPTION) {
                    if (silentMode) {
                    if (errorMessages != null)
                        errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "waring;Conversione del file annullata dall'utente"));
                    }
                    return null;
                }
            }
            if (progressBar != null)
                progressBar.setAlwaysOnTop(false);

            // a volte da eccezione incomprensibilmente, in caso succede riprovo
            try {
                saveFileChooser.setSelectedFile(saveFileChooser.getSelectedFile() == null ? recivedFile : saveFileChooser.getSelectedFile());
            }
            catch (Exception ex) {
                ex.printStackTrace(System.out);
                continue;
            }

            if (saveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                if (saveFileChooser.getSelectedFile().exists()) {
                    int overwriteFileMessageResult = JOptionPane.showConfirmDialog(null, "Sovrascrivere il file esistente ?", "File Esistente", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (overwriteFileMessageResult == JOptionPane.YES_OPTION) {
                        recivedFile = saveFileChooser.getSelectedFile();
                        if (!UtilityFunctions.canCreateFile(recivedFile)) {
                            retry = true;
                            continue;
                        }
                        else {
                            break;
                        }
                    }
                    else if (overwriteFileMessageResult == JOptionPane.CLOSED_OPTION) {
                        if (silentMode) {
                        if (errorMessages != null)
                            errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "waring;Conversione del file annullata dall'utente"));
                        }
                        return null;
                    }
                }
                else {
                    recivedFile = saveFileChooser.getSelectedFile();
                    if (!UtilityFunctions.canCreateFile(recivedFile)) {
                        retry = true;
                        continue;
                    }
                }
            }
            else {
                if (silentMode) {
                    if (errorMessages != null)
                        errorMessages.add(new HashMap.SimpleEntry<File, String>(inputFile, "waring;Conversione del file annullata dall'utente"));
                }
                return null;
            }
        }

        if (progressBar != null) {
            progressBar.setValue(90);
            progressBar.setAlwaysOnTop(true);
        }

        recivedFile.delete();
        tempFile.renameTo(recivedFile);

        // aumento la barra del progresso
        if (progressBar != null)
            progressBar.setValue(100);
        return recivedFile;
    }
}
