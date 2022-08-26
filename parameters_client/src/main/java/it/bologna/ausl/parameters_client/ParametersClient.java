package it.bologna.ausl.parameters_client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author gdm
 */
public interface ParametersClient {

    /**
     * Torna la lista con tutti i parametri della tabella dei parametri_pubblici
     * completi di tutti i campi della tabella, senza fare le sostituzioni dei
     * parametri con il link simbolico
     *
     * @return la lista con tutti i campi della tabella, senza fare le
     * sostituzioni dei parametri con il link simbolico
     * @throws IOException
     */
    public List<Map<String, String>> getCompleteRawParameters() throws IOException;

    /**
     * Torna tutti i campi della tabella relativi al parametro passato, senza
     * fare le sostituzioni dei parametri con il link simbolico
     *
     * @param nomeParametro
     * @return tutti i campi della tabella relativi al parametro passato, senza
     * fare le sostituzioni dei parametri con il link simbolico
     * @throws IOException
     */
    public Map<String, String> getCompleteRawParameter(String nomeParametro) throws IOException;

    /**
     * Torna una mappa contenente tutti i parametri, in cui la chiave è il nome
     * del parametro e il valore è il valore del parametro. Non effettua le
     * sostituzioni dei parametri con il link simbolico.
     *
     * @return una mappa contenente tutti i parametri, in cui la chiave è il
     * nome del parametro e il valore è il valore del parametro
     * @throws IOException
     */
    public Map<String, String> getRawParameters() throws IOException;

    /**
     * Torna il valore del parametro passatto senza effettuare la sostituzione
     * del link simbolico
     *
     * @param nomeParametro
     * @return il valore del parametro passatto senza effettuare la sostituzione
     * del link simbolico
     * @throws IOException
     */
    public String getRawParameter(String nomeParametro) throws IOException;

    /**
     * Torna la lista con tutti i parametri della tabella dei parametri_pubblici
     * completi di tutti i campi della tabella effettuando le sostituzioni dei
     * parametri con il link simbolico
     *
     * @return la lista con tutti i campi della tabella effettuando le
     * sostituzioni dei parametri con il link simbolico
     * @throws IOException
     */
    public default List<Map<String, String>> getCompleteParameters() throws IOException {
        List<Map<String, String>> completeRawParameters = getCompleteRawParameters();
        for (Map<String, String> entry : completeRawParameters) {
            if (entry.get("val_parametro") == null || entry.get("val_parametro").equals("")) {
                System.out.println("----------NULL------------");
                System.out.println(entry);
            }
            entry.put("val_parametro", replaceSymbolicParams(entry.get("val_parametro")));
        }

        return completeRawParameters;
    }

    /**
     * Torna tutti i campi della tabella relativi al parametro passato
     * effettuando le sostituzioni dei parametri con il link simbolico
     *
     * @param nomeParametro
     * @return tutti i campi della tabella relativi al parametro passato
     * effettuando le sostituzioni dei parametri con il link simbolico
     * @throws IOException
     */
    public default Map<String, String> getCompleteParameter(String nomeParametro) throws IOException {
        Map<String, String> rawParameter = getCompleteRawParameter(nomeParametro);
        rawParameter.put("val_parametro", replaceSymbolicParams(rawParameter.get("val_parametro")));
        return rawParameter;
    }

    /**
     * Torna una mappa contenente tutti i parametri, in cui la chiave è il nome
     * del parametro e il valore è il valore del parametro. Effettua le
     * sostituzioni dei parametri con il link simbolico.
     *
     * @return una mappa contenente tutti i parametri, in cui la chiave è il
     * nome del parametro e il valore è il valore del parametro
     * @throws IOException
     */
    public default Map<String, String> getParameters() throws IOException {
        Map<String, String> rawParameters = getRawParameters();
        Set<String> keys = rawParameters.keySet();
        for (String key : keys) {
            rawParameters.put(key, replaceSymbolicParams(rawParameters.get(key)));
        }
        return rawParameters;
    }

    /**
     * Torna il valore del parametro passatto effettuando la sostituzione del
     * link simbolico
     *
     * @param nomeParametro
     * @return il valore del parametro passatto effettuando la sostituzione del
     * link simbolico
     * @throws IOException
     */
    public default String getParameter(String nomeParametro) throws IOException {
        return replaceSymbolicParams(getRawParameter(nomeParametro));
    }

    /**
     * Legge il file di configurazione dell'ambiente provando * prima a leggere
     * il file indentificato dalla proprietà "aziendaconfig.path" del file di
     * configurazione di default
     * ("it/bologna/ausl/parameters_client/resources/default_azienda_config.properties"),
     * * se il file non esiste, allora legge il path dalla variabile d'ambiente
     * di sistema "parameters_client.config", * se non esite il file o la
     * variabile d'ambiente di sistema non è valorizzata, allora torna il file
     * di configurazionwe di default.
     *
     * @return il file di configurazione
     * @throws IOException
     */
    public default Properties readConfigFile() throws IOException {
        Properties config = new Properties();

        // leggo la configurazione di default, che contiente anche il path del file di configurazione della macchina
        InputStream defaultConfig = Thread.currentThread().getContextClassLoader().getResourceAsStream("it/bologna/ausl/parameters_client/default_azienda_config.properties");
        config.load(defaultConfig);

        // leggo il path del file di configurazione della macchina
        String configFileLocation = config.getProperty("aziendaconfig.path");

        // provo a leggere il file di configurazione dal disco
        File configFile = new File(configFileLocation);
        // se esiste uso le proprietà configurate in esso, altrimenti lascio quelle di default lette prima
        if (configFile.exists()) {

            config.load(new FileInputStream(configFile));
        } else//il file non c'e' guardiamo la system properties.
         if (System.getProperty("parameters_client.config") != null
                    && Files.isReadable(Paths.get(System.getProperty("parameters_client.config")))) {
                config.load(new FileInputStream(System.getProperty("parameters_client.config")));
            }

        return config;
    }

    public default String replaceSymbolicParams(String param) throws IOException {
        Map<String, String> symbolicParams = getAllMatches(param, "\\$\\{([^\\}]+)}");

        while (symbolicParams != null && !symbolicParams.isEmpty()) {
            Set<String> keys = symbolicParams.keySet();
            for (String p : keys) {
//                System.out.println(p + ": " + symbolicParams.get(p));
                param = param.replace(p, getParameter(symbolicParams.get(p)));
            }
            symbolicParams = getAllMatches(param, "\\$\\{([^\\}]+)}");
        }

        return param;
    }

    public default Map<String, String> getAllMatches(String text, String regex) {
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(text);
        Map<String, String> matches = new HashMap<>();
        while (m.find()) { // find next match
            matches.put(m.group(), m.group(1));
        }
        return matches;
    }

    /**
     *
     * @return torna il codice azienda. Es. 195, 106, 909, ecc.
     */
    public String getAzienda();

    /**
     *
     * @return torna prod o test a seconda se siamo su un ambiente di produzione
     * o di test
     */
    public String getAmbiente();

    /**
     *
     * @return torna la chiave utilizzata per criptare e de-criptare le password
     * utilizzate nella firma remota
     */
    public String getEncryptionKey();
}
