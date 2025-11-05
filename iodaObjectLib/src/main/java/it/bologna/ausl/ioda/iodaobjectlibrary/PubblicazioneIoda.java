package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.bologna.ausl.ioda.iodaobjectlibrary.exceptions.IodaDocumentException;
import java.util.Objects;
import org.joda.time.DateTime;


/**
 *
 * @author gdm
 */
public class PubblicazioneIoda extends Document implements Comparable<PubblicazioneIoda> {

    public static enum Tipologia {ALBO, POLITICO, DIRIGENTE, COMMITTENTE}
    
    private Long id; // id della pubblicazione
    private Long numeroPubblicazione; // numero pubblicazione fatta
    private Integer annoPubblicazione; // anno pubblicazione fatta
    private String pubblicatore; // descrizione del pubblicatore
    private String esecutivita; // esecutività della pubblicazione, da passare a Balbo (es. esecutiva, non_esecutiva)
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private DateTime dataDal;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private DateTime dataAl;

    protected DocumentOperationType tipoOperazione; // obbligatorio: insert/update o delete. In caso di inserimento del GdDoc tutti le pubblicazioni devono essere insert

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    private DateTime dataDefissione;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    private DateTime dataEsecutivita;
    
    private Boolean esecutiva;
    
    private Tipologia tipologia;
    
    private Boolean pubblicaSoloSePubblicatoAlbo;
    
    protected String tipoProvvedimentoTrasparenza; // campo in sola lettura, non verrà usato in fase di insert o update. Contiene la descrizione del tipo provvedimento delle trasparenza, serve per la pubblicazione trasparenza
    
    protected String tipoProfiloCommittente; // campo in sola lettura, non verrà usato in fase di insert o update. Contiene l'id del tipo profilo committente
    
    protected String descrizionePaginaPubblicazione; // campo in sola lettura, non verrà usato in fase di insert o update. Contiene la descrizione della pagine in cui avviene la pubblicazione
    
    private String uuidRelata;
    
    public PubblicazioneIoda() {
    }
 
    public PubblicazioneIoda(DateTime dataDal, DateTime dataAl, DocumentOperationType tipoOperazione, String pubblicatore, String esecutivita, DateTime dataEsecutivita, Boolean esecutiva, Tipologia tipologia, Boolean pubblicaSoloSePubblicatoSuAlbo) {
        this.dataDal = dataDal;
        this.dataAl = dataAl;
        this.tipoOperazione = tipoOperazione;
        this.pubblicatore = pubblicatore;
        this.esecutivita = esecutivita;
        this.dataEsecutivita = dataEsecutivita;
        this.esecutiva = esecutiva;
        this.tipologia = tipologia;
        this.pubblicaSoloSePubblicatoAlbo = pubblicaSoloSePubblicatoSuAlbo;
    }

    public PubblicazioneIoda(Long numeroPubblicazione, Integer annoPubblicazione, DateTime dataDal, DateTime dataAl, DocumentOperationType tipoOperazione, String pubblicatore, String esecutivita, DateTime dataEsecutivita, Boolean esecutiva) {
        this.numeroPubblicazione = numeroPubblicazione;
        this.annoPubblicazione = annoPubblicazione;
        this.dataDal = dataDal;
        this.dataAl = dataAl;
        this.tipoOperazione = tipoOperazione;
        this.pubblicatore = pubblicatore;
        this.esecutivita = esecutivita;
        this.dataEsecutivita = dataEsecutivita;
        this.esecutiva = esecutiva;
        this.pubblicaSoloSePubblicatoAlbo = false;
    }
    
    public PubblicazioneIoda(DocumentOperationType tipoOperazione, String pubblicatore, Tipologia tipologia, Boolean pubblicaSoloSePubblicatoSuAlbo) {
        this.tipoOperazione = tipoOperazione;
        this.pubblicatore = pubblicatore;
        this.tipologia = tipologia;
        this.esecutiva = true;
        this.pubblicaSoloSePubblicatoAlbo = pubblicaSoloSePubblicatoSuAlbo;
    }

    public PubblicazioneIoda(Long numeroPubblicazione, Integer annoPubblicazione, DocumentOperationType tipoOperazione, String pubblicatore) {
        this.numeroPubblicazione = numeroPubblicazione;
        this.annoPubblicazione = annoPubblicazione;
        this.tipoOperazione = tipoOperazione;
        this.pubblicatore = pubblicatore;
        this.esecutiva = true;
        this.pubblicaSoloSePubblicatoAlbo = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroPubblicazione() {
        return numeroPubblicazione;
    }

    public void setNumeroPubblicazione(Long numeroPubblicazione) {
        this.numeroPubblicazione = numeroPubblicazione;
    }

    public Integer getAnnoPubblicazione() {
        return annoPubblicazione;
    }

    public void setAnnoPubblicazione(Integer annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public DateTime getDataDal() {
        return dataDal;
    }

    public void setDataDal(DateTime dataDal) {
        this.dataDal = dataDal;
    }

    public DateTime getDataAl() {
        return dataAl;
    }

    public void setDataAl(DateTime dataAl) {
        this.dataAl = dataAl;
    }

    public DocumentOperationType getTipoOperazione() {
        return tipoOperazione;
    }

    public void setTipoOperazione(DocumentOperationType tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }

    public String getPubblicatore() {
        return pubblicatore;
    }

    public void setPubblicatore(String pubblicatore) {
        this.pubblicatore = pubblicatore;
    }

    public String getEsecutivita() {
        return esecutivita;
    }

    public void setEsecutivita(String esecutivita) {
        this.esecutivita = esecutivita;
    }

    public DateTime getDataDefissione() {
        return dataDefissione;
    }

    public void setDataDefissione(DateTime dataDefissione) {
        this.dataDefissione = dataDefissione;
    }
    
    public DateTime getDataEsecutivita(){
        return dataEsecutivita;
    }
    
    public void setDataEsecutivita(DateTime dataEsecutivita){
        this.dataEsecutivita = dataEsecutivita;
    }
    
    public Boolean isEsecutiva(){
        return esecutiva;
    }
    
    public void setEsecutiva(Boolean esecutiva){
        this.esecutiva = esecutiva;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }

    public Boolean isPubblicaSoloSePubblicatoAlbo() {
        return pubblicaSoloSePubblicatoAlbo;
    }

    public void setPubblicaSoloSePubblicatoAlbo(Boolean pubblicaSoloSePubblicatoAlbo) {
        this.pubblicaSoloSePubblicatoAlbo = pubblicaSoloSePubblicatoAlbo;
    }

    public String getTipoProvvedimentoTrasparenza() {
        return tipoProvvedimentoTrasparenza;
    }

    public void setTipoProvvedimentoTrasparenza(String tipoProvvedimentoTrasparenza) {
        this.tipoProvvedimentoTrasparenza = tipoProvvedimentoTrasparenza;
    }

    public String getTipoProfiloCommittente() {
        return tipoProfiloCommittente;
    }

    public void setTipoProfiloCommittente(String tipoProfiloCommittente) {
        this.tipoProfiloCommittente = tipoProfiloCommittente;
    }

    public String getDescrizionePaginaPubblicazione() {
        return descrizionePaginaPubblicazione;
    }

    public void setDescrizionePaginaPubblicazione(String descrizionePaginaPubblicazione) {
        this.descrizionePaginaPubblicazione = descrizionePaginaPubblicazione;
    }

    public String getUuidRelata() {
        return uuidRelata;
    }

    public void setUuidRelata(String uuidRelata) {
        this.uuidRelata = uuidRelata;
    }

    @JsonIgnore
    public void check(DocumentOperationType operationType) throws IodaDocumentException {
        if (tipologia == null) {
            throw new IodaDocumentException("tipologia mancante");
        }
        else if (tipologia == Tipologia.ALBO && dataDal == null) {
            throw new IodaDocumentException("dataDal mancante, è obbligatoria per pubblicazioni con tipologia ALBO");
        }
        else if (tipologia == Tipologia.ALBO && dataAl == null) {
            throw new IodaDocumentException("dataAl mancante, è obbligatoria per pubblicazioni con tipologia ALBO");
        }
        else if (tipologia == Tipologia.ALBO && esecutivita == null) {
            throw new IodaDocumentException("esecutivita mancante, è obbligatoria per pubblicazioni con tipologia ALBO");
        }
    }
    
    @Override
    @JsonIgnore
    public int compareTo(PubblicazioneIoda o) {
        if (o != null){
            Long idPubb = ((PubblicazioneIoda)o).getId();
            return id.compareTo(idPubb);
        }
        return 0;     
    }
    
    @Override
    @JsonIgnore
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof PubblicazioneIoda)) return false;
        PubblicazioneIoda p = (PubblicazioneIoda) obj;
        boolean res = (Objects.equals(p.id, this.id)) && 
                      (Objects.equals(p.numeroPubblicazione, this.numeroPubblicazione)) &&
                      (Objects.equals(p.annoPubblicazione, this.annoPubblicazione)) &&
                      (p.pubblicatore.equalsIgnoreCase(this.pubblicatore)) &&
                      (p.esecutivita.equalsIgnoreCase(this.esecutivita)) &&
                      (p.uuidRelata.equalsIgnoreCase(this.uuidRelata)) &&
                      (p.dataDal.equals(this.dataDal)) &&
                      (p.dataAl.equals(this.dataAl)) &&
                      (p.dataDefissione.equals(this.dataDefissione)) &&
                      (p.dataEsecutivita.equals(this.dataEsecutivita)) &&
                      (p.tipoProvvedimentoTrasparenza.equalsIgnoreCase(this.tipoProvvedimentoTrasparenza)) &&
                      (p.tipologia == this.tipologia) &&
                      ((p.esecutiva).booleanValue() == (this.esecutiva).booleanValue());
         
        return res;
    }
}