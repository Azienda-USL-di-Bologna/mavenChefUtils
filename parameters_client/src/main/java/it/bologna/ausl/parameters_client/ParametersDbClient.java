package it.bologna.ausl.parameters_client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 *
 * @author gdm
 */
public class ParametersDbClient implements ParametersClient {

    private static final Object retrySqlStateLock = new Object();
    private static List<String> retrySqlState = null; // lista degli errori riprovabili

    static {
        if (retrySqlState == null) {
            synchronized (retrySqlStateLock) {
                if (retrySqlState == null) {
                    retrySqlState = new ArrayList<>();
                    retrySqlState.add("57P03"); // il database si sta avviando / spegnendo
                    retrySqlState.add("08001"); // il database è spento o non raggiungibile
                }
            }
        }
    }

    private static DataSource datasource = null;
    private static final Logger log = LogManager.getLogger(ParametersClient.class);
    private static final Object initLock = new Object();

    private static String azienda;
    private static String ambiente;
    private static String regione;
    private static String encryptionKey;
    private static String jwtGeneratorKeyStorePassword;
    private static String internautaJwtSecret;

    public ParametersDbClient() throws IOException {
        initDataSource();
    }

    private void initDataSource() throws IOException {

        if (datasource == null) {
            synchronized (initLock) {
                if (datasource == null) {

                    Properties config = readConfigFile();

                    PoolProperties p = new PoolProperties();
                    p.setUrl(config.getProperty("db.uri"));
                    p.setDriverClassName(config.getProperty("db.driver"));
                    p.setUsername(config.getProperty("db.username"));
                    p.setPassword(config.getProperty("db.password"));
                    p.setTestWhileIdle(false);
                    p.setTestOnBorrow(true);
                    p.setValidationQuery("SELECT 1");
                    p.setTestOnReturn(false);
                    p.setValidationInterval(1000);
                    p.setTimeBetweenEvictionRunsMillis(30000);
                    p.setMaxActive(20);
                    p.setMaxIdle(2);
                    p.setInitialSize(2);
                    p.setMaxWait(60000);
                    p.setMinIdle(0);
                    p.setInitSQL("set application_name to '" + getClass().getSimpleName() + "'");
                    datasource = new DataSource();
                    datasource.setPoolProperties(p);

                    ambiente = config.getProperty("ambiente");
                    azienda = config.getProperty("azienda");
                    regione = config.getProperty("regione");
                    encryptionKey = config.getProperty("db.encryptionkey");
                    jwtGeneratorKeyStorePassword = config.getProperty("jwtgenerator.keystore.password");
                    internautaJwtSecret = config.getProperty("internauta.jwt.secret");
                }
            }
        }
    }

    private Connection getConnection() throws SQLException {
        Connection ds = null;
        boolean exit = false;
        while (!exit) {
            try {
                ds = datasource.getConnection();
                exit = true;
            } catch (SQLException ex) {
                // se l'errore è uno dei riprovabili, ritento la connessione, altrimenti rilancio l'eccezione e esco
                if (!retrySqlState.contains(ex.getSQLState())) {
                    throw ex;
                }
            }
        }
        return ds;
    }

    @Override
    public List<Map<String, String>> getCompleteRawParameters() throws IOException {
        String query = "select id_parametro, nome_parametro, val_parametro, note_parametro from bds_tools.parametri_pubblici";
        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            List<Map<String, String>> rows = new ArrayList<>();

            ResultSetMetaData metaData = rs.getMetaData();
            while (rs.next()) {
                Map<String, String> row = new HashMap<>(4);
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    row.put(metaData.getColumnName(i), rs.getString(i));
                }
                rows.add(row);
            }
            return rows;
        } catch (SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    @Override
    public Map<String, String> getCompleteRawParameter(String nomeParametro) throws IOException {
        String query = "select id_parametro, nome_parametro, val_parametro, note_parametro from bds_tools.parametri_pubblici where nome_parametro = ?";
        try (
                Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nomeParametro);
            ResultSet rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            Map<String, String> row = new HashMap<>(4);
            while (rs.next()) {
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    row.put(metaData.getColumnName(i), rs.getString(i));
                }
            }
            return row;
        } catch (SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    @Override
    public Map<String, String> getRawParameters() throws IOException {
        List<Map<String, String>> parameters = getCompleteRawParameters();
        Map<String, String> lightParameters = new HashMap<>();
        parameters.stream().forEach(p -> lightParameters.put(p.get("nome_parametro"), p.get("val_parametro")));
        return lightParameters;
    }

    @Override
    public String getRawParameter(String nomeParametro) throws IOException {
        Map<String, String> parameter = getCompleteRawParameter(nomeParametro);
        if (parameter != null) {
            return parameter.get("val_parametro");
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

    public static String getRegione() {
        return regione;
    }

    @Override
    public String getEncryptionKey() {
        return encryptionKey;
    }

    public static String getJwtGeneratorKeyStorePassword() {
        return jwtGeneratorKeyStorePassword;
    }

    public static String getInternautaJwtSecret() {
        return internautaJwtSecret;
    }
}
