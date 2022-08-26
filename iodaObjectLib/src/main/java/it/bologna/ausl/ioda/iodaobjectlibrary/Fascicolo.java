package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

/**
 *
 * @author gdm
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoClasse")
public class Fascicolo implements Requestable {

    protected String codiceFascicolo; // obbligatorio: codice che identifica univocamente il fascicolo. La numerazione  gerarchica

    protected String nomeFascicolo;
    protected String nomeFascicoloInterfaccia;
    protected String descrizioneFascicolatore;
    protected int numeroFascicolo;
    protected int annoFascicolo;
    protected String idLivelloFascicolo;
    protected String idUtenteCreazione; // id utente ldap creazione
    protected DateTime dataCreazione;
    protected String idUtenteResponsabile;
    protected String descrizioneUtenteResponsabile; // nome e cognome di colui che è responsabile del fascicolo  -- campo che non esiste più sul db
    protected String statoFascicolo;
    protected String idUtenteResponsabileProposto;
    protected ClassificazioneFascicolo classificazioneFascicolo; // classificazione del fascicolo
    protected int speciale;
    protected String titolo;
    protected int accesso;
    protected String note;
    protected String idFascicoloImportato;
    protected DateTime dataChiusura;
    protected String noteImportazione;
    protected String descrizioneIter;
    protected int idTipoFascicolo;
    protected String idStruttura;
    protected String numerazioneGerarchica;
    protected DateTime dataRegistrazione;
    protected String servizioCreazione;
    protected String codiceFiscaleUtenteCreazione;
    protected String codiceFiscaleUtenteResponsabile;
    protected String codiceFiscaleUtenteResponsabileProposto;
    protected String classificazione;
    protected Integer idIter;
    protected String idPadreCatenaFascicolare;
    protected Integer visibile;
    protected Map<String,String> permessi;
    protected Integer idStrutturaInternauta;
    
    protected List<String> vicari;
    
    public static enum OperazioniFascicolo {
        TRADUCI_VICARI,
        DELETE_ITER_PRECEDENTE,
        ID_ITER_PRECEDENTE,
        UPDATE_PER_ANNULLAMENTO_ITER,
        PROVENIENZA_GIPI
    }

    public static enum StatoFascicolo {
        BOZZA("b"),
        APERTO("a"),
        PRECHIUSO("p"),
        CHIUSO("c");

        private final String name;

        StatoFascicolo(String name) {
            this.name = name;
        }

        @JsonCreator
        public static StatoFascicolo fromString(String name) {
            return name == null
                    ? null
                    : StatoFascicolo.valueOf(name.toUpperCase());
        }

        @JsonValue
        public String getName() {
            return name;
        }
    }

    public static enum Creazione {
        UTENTE("Utente"),
        DUPLICATORE_FASCICOLO_ATTIVITA("DuplicatoreFascicoliAttivita"),
        API("API");

        private final String name;

        Creazione(String name) {
            this.name = name;
        }

        @JsonCreator
        public static Creazione fromString(String name) {
            return name == null
                    ? null
                    : Creazione.valueOf(name.toUpperCase());
        }

        @JsonValue
        public String getName() {
            return name;
        }
    }

    public Fascicolo() {
        this.vicari = new ArrayList<>();
    }

    public Fascicolo(String codiceFascicolo) {
        this.codiceFascicolo = codiceFascicolo;
        this.classificazioneFascicolo = new ClassificazioneFascicolo();
        this.vicari = new ArrayList<>();
    }
    
    public Fascicolo(String codiceFascicolo, String idUtenteResponsabile) {
        this.codiceFascicolo = codiceFascicolo;
        this.idUtenteResponsabile = idUtenteResponsabile;
    }
    
    public Fascicolo(String codiceFascicolo, String idUtenteResponsabile, List<String> vicari) {
        this.codiceFascicolo = codiceFascicolo;
        this.idUtenteResponsabile = idUtenteResponsabile;
        this.vicari = vicari;
    }

    public Fascicolo(String codiceFascicolo, String nomeFascicolo,
            String idUtenteFascicolatore, String descrizioneFascicolatore,
            DateTime dataFascicolazione, int numeroFascicolo, int annoFascicolo,
            String idLivelloFascicolo, String idUtenteCreazione,
            DateTime dataCreazione, String idUtenteResponsabile,
            String descrizioneUtenteResponsabile, String statoFascicolo,
            int speciale, String titolo, int accesso) {
        this.codiceFascicolo = codiceFascicolo;
        this.nomeFascicolo = nomeFascicolo;
//        this.idUtenteFascicolatore = idUtenteFascicolatore;
        this.descrizioneFascicolatore = descrizioneFascicolatore;
//        this.dataFascicolazione = dataFascicolazione;
        this.numeroFascicolo = numeroFascicolo;
        this.annoFascicolo = annoFascicolo;
        this.idLivelloFascicolo = idLivelloFascicolo;
        this.idUtenteCreazione = idUtenteCreazione;
        this.dataCreazione = dataCreazione;
        this.idUtenteResponsabile = idUtenteResponsabile;
        this.descrizioneUtenteResponsabile = descrizioneUtenteResponsabile;
        this.statoFascicolo = statoFascicolo;
        this.speciale = speciale;
        this.titolo = titolo;
        this.accesso = accesso;
        this.classificazioneFascicolo = new ClassificazioneFascicolo();
        this.vicari = new ArrayList<>();
    }

    public Fascicolo(String codiceFascicolo, String nomeFascicolo,
            String idUtenteFascicolatore, String descrizioneFascicolatore,
            DateTime dataFascicolazione, int numeroFascicolo, int annoFascicolo,
            String idLivelloFascicolo, String idUtenteCreazione,
            DateTime dataCreazione, String idUtenteResponsabile,
            String descrizioneUtenteResponsabile, String statoFascicolo,
            int speciale, String titolo, int accesso, String descrizioneIter) {
        this(codiceFascicolo, nomeFascicolo, idUtenteFascicolatore,
                descrizioneFascicolatore, dataFascicolazione, numeroFascicolo,
                annoFascicolo, idLivelloFascicolo, idUtenteCreazione,
                dataCreazione, idUtenteResponsabile,
                descrizioneUtenteResponsabile, statoFascicolo, speciale,
                titolo, accesso);

        this.descrizioneIter = descrizioneIter;
    }

    public Fascicolo(String codiceFascicolo, String nomeFascicolo,
            String idUtenteFascicolatore, String descrizioneFascicolatore,
            DateTime dataFascicolazione, int numeroFascicolo, int annoFascicolo,
            String idLivelloFascicolo, String idUtenteCreazione,
            DateTime dataCreazione, String idUtenteResponsabile,
            String descrizioneUtenteResponsabile, String statoFascicolo,
            int speciale, String titolo, int accesso, String idStruttura,
            String descrizioneIter) {
        this(codiceFascicolo, nomeFascicolo, idUtenteFascicolatore,
                descrizioneFascicolatore, dataFascicolazione, numeroFascicolo,
                annoFascicolo, idLivelloFascicolo, idUtenteCreazione,
                dataCreazione, idUtenteResponsabile,
                descrizioneUtenteResponsabile, statoFascicolo, speciale,
                titolo, accesso);
        this.idStruttura = idStruttura;
        this.descrizioneIter = descrizioneIter;
    }
    
    public Fascicolo(String codiceFascicolo, String nomeFascicolo,
            String idUtenteFascicolatore, String descrizioneFascicolatore,
            DateTime dataFascicolazione, int numeroFascicolo, int annoFascicolo,
            String idLivelloFascicolo, String idUtenteCreazione,
            DateTime dataCreazione, String idUtenteResponsabile,
            String descrizioneUtenteResponsabile, String statoFascicolo,
            int speciale, String titolo, int accesso, String idStruttura,
            String descrizioneIter, String codiceFiscaleUtenteCreazione, 
            String codiceFiscaleUtenteResponsabile, 
            String codiceFiscaleUtenteResponsabileProposto, String classificazione, Integer idIter) {
        this(codiceFascicolo, nomeFascicolo, idUtenteFascicolatore,
                descrizioneFascicolatore, dataFascicolazione, numeroFascicolo,
                annoFascicolo, idLivelloFascicolo, idUtenteCreazione,
                dataCreazione, idUtenteResponsabile,
                descrizioneUtenteResponsabile, statoFascicolo, speciale,
                titolo, accesso);
        this.idStruttura = idStruttura;
        this.descrizioneIter = descrizioneIter;
        this.codiceFiscaleUtenteCreazione = codiceFiscaleUtenteCreazione;
        this.codiceFiscaleUtenteResponsabile = codiceFiscaleUtenteResponsabile;
        this.codiceFiscaleUtenteResponsabileProposto = codiceFiscaleUtenteResponsabileProposto;
        this.classificazione = classificazione;
        this.idIter = idIter;
    }
    
    public Fascicolo(String codiceFascicolo, String nomeFascicolo,
            String idUtenteFascicolatore, String descrizioneFascicolatore,
            DateTime dataFascicolazione, int numeroFascicolo, int annoFascicolo,
            String idLivelloFascicolo, String idUtenteCreazione,
            DateTime dataCreazione, String idUtenteResponsabile,
            String descrizioneUtenteResponsabile, String statoFascicolo,
            int speciale, String titolo, int accesso, String idStruttura,
            String descrizioneIter, String codiceFiscaleUtenteCreazione, 
            String codiceFiscaleUtenteResponsabile, 
            String codiceFiscaleUtenteResponsabileProposto, String classificazione, Integer idIter, Integer idStrutturaInternauta) {
        this(codiceFascicolo, nomeFascicolo, idUtenteFascicolatore,
                descrizioneFascicolatore, dataFascicolazione, numeroFascicolo,
                annoFascicolo, idLivelloFascicolo, idUtenteCreazione,
                dataCreazione, idUtenteResponsabile,
                descrizioneUtenteResponsabile, statoFascicolo, speciale,
                titolo, accesso, idStruttura, descrizioneIter, codiceFiscaleUtenteCreazione, 
                codiceFiscaleUtenteResponsabile, codiceFiscaleUtenteResponsabileProposto, 
                classificazione, idIter);
        this.idStrutturaInternauta = idStrutturaInternauta;
    }
    
    public String getNomeFascicoloInterfaccia() {
        return nomeFascicoloInterfaccia;
    }

    public void setNomeFascicoloInterfaccia(String nomeFascicoloInterfaccia) {
        this.nomeFascicoloInterfaccia = nomeFascicoloInterfaccia;
    }

    public String getCodiceFascicolo() {
        return codiceFascicolo;
    }

    public void setCodiceFascicolo(String codiceFascicolo) {
        this.codiceFascicolo = codiceFascicolo;
    }

    public String getNomeFascicolo() {
        return nomeFascicolo;
    }

    public void setNomeFascicolo(String nomeFascicolo) {
        this.nomeFascicolo = nomeFascicolo;
    }

    public String getDescrizioneFascicolatore() {
        return descrizioneFascicolatore;
    }

    public void setDescrizioneFascicolatore(String descrizioneFascicolatore) {
        this.descrizioneFascicolatore = descrizioneFascicolatore;
    }

    public int getNumeroFascicolo() {
        return numeroFascicolo;
    }

    public void setNumeroFascicolo(int numeroFascicolo) {
        this.numeroFascicolo = numeroFascicolo;
    }

    public int getAnnoFascicolo() {
        return annoFascicolo;
    }

    public void setAnnoFascicolo(int annoFascicolo) {
        this.annoFascicolo = annoFascicolo;
    }

    public String getIdLivelloFascicolo() {
        return idLivelloFascicolo;
    }

    public void setIdLivelloFascicolo(String idLivelloFascicolo) {
        this.idLivelloFascicolo = idLivelloFascicolo;
    }

    public String getIdUtenteCreazione() {
        return idUtenteCreazione;
    }

    public void setIdUtenteCreazione(String idUtenteCreazione) {
        this.idUtenteCreazione = idUtenteCreazione;
    }

    public DateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(DateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public String getIdUtenteResponsabile() {
        return idUtenteResponsabile;
    }

    public void setIdUtenteResponsabile(String idUtenteResponsabile) {
        this.idUtenteResponsabile = idUtenteResponsabile;
    }

    public String getDescrizioneUtenteResponsabile() {
        return descrizioneUtenteResponsabile;
    }

    public void setDescrizioneUtenteResponsabile(String descrizioneUtenteResponsabile) {
        this.descrizioneUtenteResponsabile = descrizioneUtenteResponsabile;
    }

    public String getStatoFascicolo() {
        return statoFascicolo;
    }

    public void setStatoFascicolo(String statoFascicolo) {
        this.statoFascicolo = statoFascicolo;
    }

    public String getIdUtenteResponsabileProposto() {
        return idUtenteResponsabileProposto;
    }

    public void setIdUtenteResponsabileProposto(String idUtenteResponsabileProposto) {
        this.idUtenteResponsabileProposto = idUtenteResponsabileProposto;
    }

    public int getSpeciale() {
        return speciale;
    }

    public void setSpeciale(int speciale) {
        this.speciale = speciale;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public int getAccesso() {
        return accesso;
    }

    public void setAccesso(int accesso) {
        this.accesso = accesso;
    }

    public ClassificazioneFascicolo getClassificazioneFascicolo() {
        return classificazioneFascicolo;
    }

    public void setClassificazioneFascicolo(ClassificazioneFascicolo classificazioneFascicolo) {
        this.classificazioneFascicolo = classificazioneFascicolo;
    }

    public String getIdFascicoloImportato() {
        return idFascicoloImportato;
    }

    public void setIdFascicoloImportato(String idFascicoloImportato) {
        this.idFascicoloImportato = idFascicoloImportato;
    }

    public DateTime getDataChiusura() {
        return dataChiusura;
    }

    public void setDataChiusura(DateTime dataChiusura) {
        this.dataChiusura = dataChiusura;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteImportazione() {
        return noteImportazione;
    }

    public void setNoteImportazione(String noteImportazione) {
        this.noteImportazione = noteImportazione;
    }

    public String getIdStruttura() {
        return idStruttura;
    }

    public void setIdStruttura(String idStruttura) {
        this.idStruttura = idStruttura;
    }

    @Override
    public String toString() {
        return getCodiceFascicolo();
    }

    public List<String> getVicari() {
        return vicari;
    }

    public void setVicari(List<String> vicari) {
        this.vicari = vicari;
    }

    public void addVicario(String vicario) {
        this.vicari.add(vicario);
    }

    public String getDescrizioneIter() {
        return descrizioneIter;
    }

    public void setDescrizioneIter(String descrizioneIter) {
        this.descrizioneIter = descrizioneIter;
    }

    public int getIdTipoFascicolo() {
        return idTipoFascicolo;
    }

    public void setIdTipoFascicolo(int idTipoFascicolo) {
        this.idTipoFascicolo = idTipoFascicolo;
    }

    public String getNumerazioneGerarchica() {
        return numerazioneGerarchica;
    }

    public void setNumerazioneGerarchica(String numerazioneGerarchica) {
        this.numerazioneGerarchica = numerazioneGerarchica;
    }

    public DateTime getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(DateTime dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public String getServizioCreazione() {
        return servizioCreazione;
    }

    public void setServizioCreazione(String servizioCreazione) {
        this.servizioCreazione = servizioCreazione;
    }

    public String getCodiceFiscaleUtenteCreazione() {
        return codiceFiscaleUtenteCreazione;
    }

    public void setCodiceFiscaleUtenteCreazione(String codiceFiscaleUtenteCreazione) {
        this.codiceFiscaleUtenteCreazione = codiceFiscaleUtenteCreazione;
    }

    public String getCodiceFiscaleUtenteResponsabile() {
        return codiceFiscaleUtenteResponsabile;
    }

    public void setCodiceFiscaleUtenteResponsabile(String codiceFiscaleUtenteResponsabile) {
        this.codiceFiscaleUtenteResponsabile = codiceFiscaleUtenteResponsabile;
    }

    public String getCodiceFiscaleUtenteResponsabileProposto() {
        return codiceFiscaleUtenteResponsabileProposto;
    }

    public void setCodiceFiscaleUtenteResponsabileProposto(String codiceFiscaleUtenteResponsabileProposto) {
        this.codiceFiscaleUtenteResponsabileProposto = codiceFiscaleUtenteResponsabileProposto;
    }

    public String getClassificazione() {
        return classificazione;
    }

    public void setClassificazione(String classificazione) {
        this.classificazione = classificazione;
    }

    public Integer getIdIter() {
        return idIter;
    }

    public void setIdIter(Integer idIter) {
        this.idIter = idIter;
    }

    public Integer getVisibile() {
        return visibile;
    }

    public void setVisibile(Integer visibile) {
        this.visibile = visibile;
    }

    public Map<String, String> getPermessi() {
        return permessi;
    }

    public void setPermessi(Map<String, String> permessi) {
        this.permessi = permessi;
    }

    public Integer getIdStrutturaInternauta() {
        return idStrutturaInternauta;
    }

    public void setIdStrutturaInternauta(Integer idStrutturaInternauta) {
        this.idStrutturaInternauta = idStrutturaInternauta;
    }

    public String getIdPadreCatenaFascicolare() {
        return idPadreCatenaFascicolare;
    }

    public void setIdPadreCatenaFascicolare(String idPadreCatenaFascicolare) {
        this.idPadreCatenaFascicolare = idPadreCatenaFascicolare;
    }
    
    @JsonIgnore
    protected void setNullWhereEmpty() {
        try {
            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == String.class && field.get(this) != null && field.get(this).equals("")) {
                    field.set(this, null);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }
//    @JsonIgnore
//    public String getJSONString() throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JodaModule());
//        mapper.setTimeZone(TimeZone.getDefault());
//        String writeValueAsString = mapper.writeValueAsString(this);
//        return writeValueAsString;
//    }
}
