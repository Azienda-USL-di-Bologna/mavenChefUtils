package it.bologna.ausl.pdfconverterandsigner.utils;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

/** Memorizza le informazioni di un proxy
 *
 * @author Giuseppe
 */
public class ProxyManager {
private String proxyHost;
private String proxyPort;
private String proxyUsername;
private char[] proxyPassword;

    /** Costruisce un oggetto "ProxyManager" vuoto
     *
     */
    public ProxyManager() {
        this.proxyHost = null;
        this.proxyPort = null;
        this.proxyUsername = null;
        this.proxyPassword = null;
    }

    /** Costruisce un oggetto "ProxyManager" identificante un proxy senza autenticazione
     *
     * @param proxyHost host del proxy
     * @param proxyPort porta del proxy
     */
    public ProxyManager(String proxyHost, String proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = null;
        this.proxyPassword = null;
    }

    /** Costruisce un oggetto "ProxyManager" identificante un proxy con autenticazione
     *
     * @param proxyHost host del proxy
     * @param proxyPort porta del proxy
     * @param proxyUsername username richiesta dal proxy
     * @param proxyPassword password del proxy
     */
    public ProxyManager(String proxyHost, String proxyPort, String proxyUsername, char[] proxyPassword) {
       this.proxyHost = proxyHost;
       this.proxyPort = proxyPort;
       this.proxyUsername = proxyUsername;
       this.proxyPassword = proxyPassword;
    }

    /** Setta hostname e porta del proxy
     *
     * @param proxyHost host del proxy
     * @param proxyPort porta del proxy
     */
    public void setProxyHost(String proxyHost, String proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }

    /** Setta username e password del proxy
     *
     * @param proxyUsername username richiesta dal proxy
     * @param proxyPassword password del proxy
     */
    public void setProxyAuthentication(String proxyUsername, char[] proxyPassword) {
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }

    /** Ritorna l'host del proxy
     *
     * @return l'host del proxy
     */
    public String getProxyHost() {
        return proxyHost;
    }

    /** Ritorna la porta del proxy
     *
     * @return la porta del proxy
     */
    public String getProxyPort() {
        return proxyPort;
    }

    /** Ritorna lo username del proxy
     *
     * @return lo username del proxy
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /** Ritorna la password del proxy
     *
     * @return la password del proxy
     */
    public char[] getProxyPassword() {
        return proxyPassword;
    }

    /** Imposta il proxy a livello globale (resterà attivo per tutte le connessione della jre fino alla sua terminazione).
     * Per disattivare il proxy chiamare il metodo setProxyHost(null) e poi di nuovo updateSystemProxy()
     */
    public void updateSystemProxy() {
    Properties sysProps = System.getProperties();

        if (proxyHost != null) {
            sysProps.put( "proxySet", "true" );
            sysProps.put( "proxyHost", proxyHost);
            sysProps.put( "http.proxyHost", proxyHost );
            sysProps.put( "https.proxyHost", proxyHost );
            if (proxyPort != null) {
                sysProps.put( "proxyPort", proxyPort);
                sysProps.put( "http.proxyPort", proxyPort );
                sysProps.put( "https.proxyPort", proxyPort );
            }

            if (proxyUsername != null && proxyPassword != null) {
                final String username = proxyUsername;
                final char[] password = proxyPassword;
                Authenticator authenticator = new Authenticator() {
                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return (new PasswordAuthentication(username, password));
                    }
                };
                Authenticator.setDefault(authenticator);
            }
        }
        else {
            sysProps.put( "proxySet", "false" );
            sysProps.put( "proxyHost", "");
            sysProps.put( "http.proxyHost", "" );
            sysProps.put( "https.proxyHost", "" );
            sysProps.put( "proxyPort", "0");
            sysProps.put( "http.proxyPort", "" );
            sysProps.put( "https.proxyPort", "" );
            Authenticator.setDefault(null);
        }


    }
}