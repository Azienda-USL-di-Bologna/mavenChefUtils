package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.bologna.ausl.ioda.iodaobjectlibrary.exceptions.IodaDocumentException;
import it.bologna.ausl.ioda.iodaobjectlibrary.exceptions.IodaFileException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.joda.time.DateTime;

/**
 *
 * @author gdm
 */
public class GdDoc extends Document{

    public static enum IdentificationField {
        CODICE_GDDOC,
        ID_OGGETTO_ORIGINE_GDDOC
    }

    public static final String GDDOC_IDENTIFICATION_FIELD_KEY = "gd_doc_identification_field";

    public static enum GdDocCollectionNames {FASCICOLAZIONI, SOTTODOCUMENTI, PUBBLICAZIONI, SESSIONI_VERSAMENTO_PARER}
    public static enum GdDocCollectionValues {LOAD, CLEAR, LOAD_EXCLUDING_ITER}
    public static enum GdDocSortValues {ASC, DESC}
    
    @JsonIgnore
    protected String id;

    @JsonIgnore
    protected String guid;

    protected String nome; // obbligatorio, nella tabella nome_gddoc

//    protected String autoreModifica; // obbligatorio solo in caso di aggiornamento
//    protected String descrizioneModifica; // obbligatorio solo in caso di aggiornamento

    protected Boolean record; // opzionale, se non passato = true. Indica se è un record

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    protected DateTime dataUltimaModifica; //opzionale, si riferisce alla modifica nei loro sistemi
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    protected DateTime data; //opzionale, data del gdDoc, se non passata viene messa la data attuale
    
    protected Boolean visibile; // opzionale, se non passato = true. indica se il gdDoc deve essere visibile
    protected String codiceRegistro; // obbligatorio se record = true

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    protected DateTime dataRegistrazione; // obbligatorio se record = true
    protected String numeroRegistrazione; // obbligatorio se record = true

//    protected String xmlSpecificoParer; // opzionale per versamento parer (se non passato il documento non viene versato. Il versamento avviene poi nel momento in cui il parametro viene passato)
//    protected Boolean forzaConservazione; // opzionale per versamento parer, se non passato = false
//    protected Boolean forzaAccettazione; // opzionale per versamento parer, se non passato = false
//    protected Boolean forzaCollegamento; // opzionale per versamento parer, se non passato = false

    //protected List<Fascicolo> fascicoli; // opzionale: fascicoli in cui il gddoc andrà inserito
    protected List<Fascicolazione> fascicolazioni; // opzionale: fascicolazioni del GdDoc
    protected List<SottoDocumento> sottoDocumenti; // opzionale: i sottodocumenti del GdDoc
    protected List<PubblicazioneIoda> pubblicazioni; // opzionale: pubblicazioni all'albo
    protected List<SessioneVersamentoParer> sessioniVersamentoParer; // opzionale: opzionale, sessioni di versamento al parer (NB: per ora sono utilizzati solo in GetGdDoc, non in Update o Insert)

    protected DatiParerGdDoc datiParerGdDoc;
    
    protected String oggetto; // opzionale, l'oggetto del gddoc (sarebbe l'oggetto del documento associato, es. oggetto del protocollo associato) senza data, codice registro e numero

    protected String codice;
    protected Boolean numerazioneAutomatica = false;
    
    protected String nomeStrutturaFirmatario;
    protected String applicazione;
    
    protected String urlCommand;
    
    protected String idUtenteCreazione;
    
    protected String tipologiaDocumentale; // Contiene la tipologia del documento. Introdotto per differenziare PU da PE per il versamento al parer.
    
    protected Integer anno;
    
    public GdDoc() {
        super.setNullWhereEmpty();
    }

//    public GdDoc(String idOggettoOrigine, String tipoOggettoOrigine, String nome, String autoreModifica, String descrizioneModifica, Boolean record, DateTime dataUltimaModifica, Boolean visibile, String codiceRegistro, DateTime dataRegistrazione, String numeroRegistrazione, String xmlSpecificoParer, Boolean forzaConservazione, Boolean forzaAccettazione, Boolean forzaCollegamento) {
//        this();
//        super.idOggettoOrigine = idOggettoOrigine;
//        super.tipoOggettoOrigine = tipoOggettoOrigine;
//        this.nome = nome;
//        this.autoreModifica = autoreModifica;
//        this.descrizioneModifica = descrizioneModifica;
//        this.record = record;
//        this.dataUltimaModifica = dataUltimaModifica;
//        this.visibile = visibile;
//        this.codiceRegistro = codiceRegistro;
//        this.dataRegistrazione = dataRegistrazione;
//        this.numeroRegistrazione = numeroRegistrazione;
//        this.xmlSpecificoParer = xmlSpecificoParer;
//        this.forzaConservazione = forzaConservazione;
//        this.forzaAccettazione = forzaAccettazione;
//        this.forzaCollegamento = forzaCollegamento;
//    }

        /**
         * 
         * @param idOggettoOrigine
         * @param tipoOggettoOrigine
         * @param nome
         * @param record
         * @param data
         * @param dataUltimaModifica
         * @param visibile
         * @param codiceRegistro
         * @param dataRegistrazione
         * @param numeroRegistrazione 
         * @param oggetto 
         * @param codice
         * @param numerazioneAutomatica
         * @param nomeStrutturaFirmatario
         * @param applicazione
         * @param urlCommand
         * @param idUtenteCreazione
         * @param tipologiaDocumentale
         */
    public GdDoc(String idOggettoOrigine, String tipoOggettoOrigine, 
            String nome, Boolean record, DateTime data, DateTime dataUltimaModifica,
            Boolean visibile, String codiceRegistro,
            DateTime dataRegistrazione, String numeroRegistrazione,
            String oggetto, String codice, Boolean numerazioneAutomatica,
            String nomeStrutturaFirmatario, String applicazione, String urlCommand, 
            String idUtenteCreazione) {
        super.idOggettoOrigine = idOggettoOrigine;
        super.tipoOggettoOrigine = tipoOggettoOrigine;
        this.nome = nome;
        this.record = record;
        this.data = data;
        this.dataUltimaModifica = dataUltimaModifica;
        this.visibile = visibile;
        this.codiceRegistro = codiceRegistro;
        this.dataRegistrazione = dataRegistrazione;
        this.numeroRegistrazione = numeroRegistrazione;
        this.oggetto = oggetto;
        this.codice = codice;
        this.numerazioneAutomatica = numerazioneAutomatica;
        this.nomeStrutturaFirmatario = nomeStrutturaFirmatario;
        this.applicazione = applicazione;
        this.urlCommand = urlCommand;
        this.idUtenteCreazione = idUtenteCreazione;
        super.setNullWhereEmpty();
    }
    
    public GdDoc(String idOggettoOrigine, String tipoOggettoOrigine, 
            String nome, Boolean record, DateTime data, DateTime dataUltimaModifica,
            Boolean visibile, String codiceRegistro,
            DateTime dataRegistrazione, String numeroRegistrazione,
            String oggetto, String codice, Boolean numerazioneAutomatica,
            String nomeStrutturaFirmatario, String applicazione, String urlCommand, 
            String idUtenteCreazione, Integer anno) { 
        this(idOggettoOrigine, tipoOggettoOrigine, 
            nome, record, data, dataUltimaModifica,
            visibile, codiceRegistro,
            dataRegistrazione, numeroRegistrazione,
            oggetto, codice, numerazioneAutomatica,
            nomeStrutturaFirmatario, applicazione, urlCommand, 
            idUtenteCreazione);
        this.anno = anno;
    }

//    public GdDoc(String idOggettoOrigine, String tipoOggettoOrigine, String nome, Boolean record, DateTime dataUltimaModifica, Boolean visibile, String codiceRegistro, DateTime dataRegistrazione, String numeroRegistrazione) {
//        super.idOggettoOrigine = idOggettoOrigine;
//        super.tipoOggettoOrigine = tipoOggettoOrigine;
//        this.nome = nome;
//        this.record = record;
//        this.dataUltimaModifica = dataUltimaModifica;
//        this.visibile = visibile;
//        this.codiceRegistro = codiceRegistro;
//        this.dataRegistrazione = dataRegistrazione;
//        this.numeroRegistrazione = numeroRegistrazione;
//    }

    @JsonIgnore
    public static GdDoc getGdDoc(String gdDocJson, DocumentOperationType operationType) throws IodaDocumentException, IOException, IodaFileException{
        if (gdDocJson == null || gdDocJson.equals("")) {
            throw new IodaDocumentException("json descrittivo del documento da inserire mancante");
        }

        GdDoc gdDoc = Requestable.parse(gdDocJson, GdDoc.class);
        gdDoc.check(operationType);

        return gdDoc;
    }

    @JsonIgnore
    public static GdDoc getGdDoc(InputStream gdDocIs, DocumentOperationType operationType) throws IodaDocumentException, IOException {
        if (gdDocIs == null) {
            throw new IodaDocumentException("json descrittivo del documento da inserire mancante");
        }

        GdDoc gdDoc = Requestable.parse(gdDocIs, GdDoc.class);
        gdDoc.check(operationType);

        return gdDoc;
    }

    @JsonIgnore
    public void check(DocumentOperationType operationType) throws IodaDocumentException {
        boolean isRecord = isRecord() != null && isRecord();
        boolean updateConTerna = false;
        
        // Il GdDoc passa un primo controllo di validità in due casi: 
        // 1- Entrambi i dati (idOggettoOrigine, tipoOggettoOrigine) sono presenti.
        // 2- Oppure, i dati (idOggettoOrigine, tipoOggettoOrigine) sono entrambi null mentre la terna (registro, numeroRegistro, anno) sono presenti e l'operationType è UPDATE.
        if ((getIdOggettoOrigine() == null || getIdOggettoOrigine().equals("")) && (getTipoOggettoOrigine() == null || getTipoOggettoOrigine().equals(""))) {
            if(!(getNumeroRegistrazione()!= null && !getNumeroRegistrazione().equals("")
                && getCodiceRegistro()!= null && !getCodiceRegistro().equals("")
                && getAnno()!= null && !getAnno().equals("") && (operationType == DocumentOperationType.UPDATE))) {
                throw new IodaDocumentException("Dati mancanti");
            } else {
                updateConTerna = true;
            }
        }
        
        if ((getIdOggettoOrigine() == null || getIdOggettoOrigine().equals("")) && !updateConTerna) {
            throw new IodaDocumentException("idOggettoOrigine mancante");
        }
        else if ((getTipoOggettoOrigine() == null || getTipoOggettoOrigine().equals("")) && !updateConTerna) {
            throw new IodaDocumentException("tipoOggettoOrigine mancante");
        }
        else if (operationType == DocumentOperationType.INSERT && (getNome() == null || getNome().equals(""))) {
            throw new IodaDocumentException("nome mancante");
        }
//        else if (operationType == DocumentOperationType.UPDATE && (getAutoreModifica() == null || getAutoreModifica().equals(""))) {
//            throw new IodaDocumentException("autoreModifica mancante");
//        }
//        else if (operationType == DocumentOperationType.UPDATE && (getDescrizioneModifica() == null || getDescrizioneModifica().equals(""))) {
//            throw new IodaDocumentException("descrizioneModifica mancante");
//        }
//        else if (operationType == DocumentOperationType.INSERT && (fascicoli == null || fascicoli.isEmpty())) {
//            throw new IodaDocumentException("fascicoli mancanti");
//        }
        else if (operationType != DocumentOperationType.DELETE && isRecord) {
            if (getCodiceRegistro() == null || getCodiceRegistro().equals(""))
                throw new IodaDocumentException("codiceRegistro mancante. Questo campo è obbligatorio se il documento è un record");
            if (getNumeroRegistrazione() == null || getNumeroRegistrazione().equals(""))
                throw new IodaDocumentException("numeroRegistrazione mancante. Questo campo è obbligatorio se il documento è un record");
        }

        // fascicoli
        if (fascicolazioni != null && !fascicolazioni.isEmpty()) {
            for (Fascicolazione f : fascicolazioni) {
                f.check(operationType);
            }
        }

        // sottodocumenti
        if (sottoDocumenti != null && sottoDocumenti.size() > 0) {
            // controllo sulla validità dei sottodocumenti
            for (SottoDocumento sd : sottoDocumenti) {
                sd.check(operationType);
            }
        }

        // pubblicazioni
        if (pubblicazioni != null && pubblicazioni.size() > 0) {
            // controllo sulla validità dei sottodocumenti
            for (PubblicazioneIoda p : pubblicazioni) {
                p.check(operationType);
            }
        }
    }

    @JsonIgnore
    public Integer getAnnoRegistrazione() {
        if (getDataRegistrazione() != null)
            return getDataRegistrazione().getYear();
        else 
            return null;
    }

    public void addSottoDocumento(SottoDocumento sd){
        if(this.sottoDocumenti == null){
            this.sottoDocumenti = new ArrayList<>();
        }
        this.sottoDocumenti.add(sd);
    }

    @JsonIgnore
    public String getId() {
        return id;
    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public String getGuid() {
        return guid;
    }

    @JsonIgnore
    public void setGuid(String guid) {
        this.guid = guid;
    }
    
    @Override
    public void setPrefissoApplicazioneOrigine(String prefisso) {
        super.setPrefissoApplicazioneOrigine(prefisso);
        setCodice(prefisso + codice);
        //setIdOggettoOrigine(prefisso + getIdOggettoOrigine());
    }    

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

//    public String getAutoreModifica() {
//        return autoreModifica;
//    }
//
//    public void setAutoreModifica(String autoreModifica) {
//        this.autoreModifica = autoreModifica;
//    }
//
//    public String getDescrizioneModifica() {
//        return descrizioneModifica;
//    }
//
//    public void setDescrizioneModifica(String descrizioneModifica) {
//        this.descrizioneModifica = descrizioneModifica;
//    }

    public Boolean isRecord() {
        return record;
    }

    public void setRecord(Boolean record) {
        this.record = record;
    }

    public DateTime getData() {
        return data;
    }

    public void setData(DateTime data) {
        this.data = data;
    }

    public DateTime getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public void setDataUltimaModifica(DateTime dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    public Boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(Boolean visibile) {
        this.visibile = visibile;
    }

    public String getCodiceRegistro() {
        return codiceRegistro;
    }

    public void setCodiceRegistro(String codiceRegistro) {
        this.codiceRegistro = codiceRegistro;
    }

    public DateTime getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(DateTime dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
    
    public String getNumeroRegistrazione() {
        return numeroRegistrazione;
    }

    public void setNumeroRegistrazione(String numeroRegistrazione) {
        this.numeroRegistrazione = numeroRegistrazione;
    }

//    public String getXmlSpecificoParer() {
//        return xmlSpecificoParer;
//    }
//
//    public void setXmlSpecificoParer(String xmlSpecificoParer) {
//        this.xmlSpecificoParer = xmlSpecificoParer;
//    }
//
//    public Boolean isForzaConservazione() {
//        return forzaConservazione;
//    }
//
//    public void setForzaConservazione(Boolean forzaConservazione) {
//        this.forzaConservazione = forzaConservazione;
//    }
//
//    public Boolean isForzaAccettazione() {
//        return forzaAccettazione;
//    }
//
//    public void setForzaAccettazione(Boolean forzaAccettazione) {
//        this.forzaAccettazione = forzaAccettazione;
//    }
//
//    public Boolean isForzaCollegamento() {
//        return forzaCollegamento;
//    }
//
//    public void setForzaCollegamento(Boolean forzaCollegamento) {
//        this.forzaCollegamento = forzaCollegamento;
//    }

    public List<Fascicolazione> getFascicolazioni() {
        return fascicolazioni;
    }
    
    public void setFascicolazioni(List<Fascicolazione> fascicolazioni) {
        this.fascicolazioni = fascicolazioni;
    }
    
    public List<PubblicazioneIoda> getPubblicazioni() {
        return pubblicazioni;
    }

    public void setPubblicazioni(List<PubblicazioneIoda> pubblicazioni) {
        this.pubblicazioni = pubblicazioni;
    }

    public List<SottoDocumento> getSottoDocumenti() {
        return sottoDocumenti;
    }

    public void setSottoDocumenti(List<SottoDocumento> sottoDocumenti) {
        this.sottoDocumenti = sottoDocumenti;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public Boolean getNumerazioneAutomatica() {
        return numerazioneAutomatica;
    }

    public void setNumerazioneAutomatica(Boolean numerazioneAutomatica) {
        this.numerazioneAutomatica = numerazioneAutomatica;
    }

    public String getNomeStrutturaFirmatario() {
        return nomeStrutturaFirmatario;
    }

    public void setNomeStrutturaFirmatario(String nomeStrutturaFirmatario) {
        this.nomeStrutturaFirmatario = nomeStrutturaFirmatario;
    }

    public String getApplicazione() {
        return applicazione;
    }

    public void setApplicazione(String applicazione) {
        this.applicazione = applicazione;
    }

    public String getUrlCommand() {
        return urlCommand;
    }

    public void setUrlCommand(String urlCommand) {
        this.urlCommand = urlCommand;
    }

    public String getIdUtenteCreazione() {
        return idUtenteCreazione;
    }

    public void setIdUtenteCreazione(String idUtenteCreazione) {
        this.idUtenteCreazione = idUtenteCreazione;
    }

    public List<SessioneVersamentoParer> getSessioniVersamentoParer() {
        return sessioniVersamentoParer;
    }

    public void setSessioniVersamentoParer(List<SessioneVersamentoParer> sessioniVersamentoParer) {
        this.sessioniVersamentoParer = sessioniVersamentoParer;
    }

    public DatiParerGdDoc getDatiParerGdDoc() {
        return datiParerGdDoc;
    }

    public void setDatiParerGdDoc(DatiParerGdDoc datiParerGdDoc) {
        this.datiParerGdDoc = datiParerGdDoc;
    }

    public String getTipologiaDocumentale() {
        return tipologiaDocumentale;
    }

    public void setTipologiaDocumentale(String tipologiaDocumentale) {
        this.tipologiaDocumentale = tipologiaDocumentale;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }
    
    public List<Fascicolazione> ordinaFascicolazioni(List<Fascicolazione> fascicolazioni, GdDocSortValues value){
        
        List<Fascicolazione> res = null;
        
        if (fascicolazioni != null){
            if (fascicolazioni.size() > 0){
                // ordino in ordine crescente
                try{
                    if(value == GdDocSortValues.ASC){
                        Collections.sort(fascicolazioni);
                    }
                   
                    if(value == GdDocSortValues.DESC){
                        Collections.reverse(fascicolazioni);
                    }
                    
                    res = fascicolazioni;
                }
                catch(Exception ex){
                    res = null;
                }
            }
        }
        return res;
    }
    
    public List<PubblicazioneIoda> ordinaPubblicazioni(List<PubblicazioneIoda> pubblicazioni, GdDocSortValues value){
        
        List<PubblicazioneIoda> res = null;
        
        if (pubblicazioni != null){
            if (pubblicazioni.size() > 0){
                // ordino in ordine crescente
                try{
                    if(value == GdDocSortValues.ASC){
                        Collections.sort(pubblicazioni);
                    }
                   
                    if(value == GdDocSortValues.DESC){
                        Collections.reverse(pubblicazioni);
                    }
                    
                    res = pubblicazioni;
                }
                catch(Exception ex){
                    res = null;
                }
            }
        }
        return res;
    }
    	    
    @Override
    public boolean equals(Object obj) {
        //Questa equals mi serve per implementare una sorta di findObject per il solo id
        GdDoc gddoc = (GdDoc) obj;
        return gddoc.id.equals(this.getId());
                
    }
}
