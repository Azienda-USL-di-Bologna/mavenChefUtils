package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.bologna.ausl.ioda.iodaobjectlibrary.exceptions.IodaDocumentException;
import org.joda.time.DateTime;

/**
 *
 * @author gdm
 */
public class Fascicolazione extends Document implements Comparable<Fascicolazione>{

    protected DocumentOperationType tipoOperazione; // obbligatorio: operazione da effettuare (insert/update/delete)
    
    protected String codiceFascicolo; // sarebbe la numerazione gerarchica (chiave per il fascicolo)
    protected String nomeFascicolo; // il nome del fascicolo (solo per visualizzazione, non per la modifica)
    protected String idUtenteFascicolatore; // id utente ldap del fascicolatore
    protected String cfUtenteFascicolatore; // cf utente fascicolatore. serve ad es in gipi dove non abbiamo l'idutente di procton.utenti
    protected String descrizioneFascicolatore; // nome e cognome del fascicolatore
    protected int numero; // numero del fascicolo
    protected int anno; // anno del fascicolo
    protected int idIter; // L'id dell'iter a cui è associato il fascicolo
    
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    protected DateTime dataFascicolazione; // la data della fascicolazione
    
    protected boolean eliminato = false; // indica se la fascicolazione è stata logicamente eliminata
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    protected DateTime dataEliminazione; // la data di eliminazione logica della fascicolazione
    
    protected String idUtenteEliminatore; // id utente ldap di colui che ha eliminato la fascicolazione
    protected String descrizioneEliminatore; // nome e cognome di colui che ha eliminato la fascicolazione
    protected ClassificazioneFascicolo classificazione; // classificazione del fascicolo
    protected String nomeFascicoloInterfaccia; // nome fascicolo padre da mostrare in interfaccia
    protected String nomeFascicoloInterfacciaOmissis; // nome fascicolo padre da mostrare in interfaccia con omissioni se non si hanno permessi
    protected boolean permessoUtente = false; // indica se l'utente che ha fatto la richiesta ha il permessoUtente di visualizzare il nomeFascicoloInterfaccia
    protected boolean visibile;
    
    protected String idFascicoloImportato;
    
    public Fascicolazione() {
        setNullWhereEmpty();
    }

    /**
     * 
     * @param codiceFascicolo sarebbe la numerazione gerarchica
     * @param nomeFascicolo il nome del fascicolo
     * @param idUtenteFascicolatore id utente ldap del fascicolatore
     * @param descrizioneFascicolatore nome e cognome del fascicolatore
     * @param dataFascicolazione la data della fascicolazione
     * @param eliminato indica se la fascicolazione è stata logicamente eliminata
     * @param dataEliminazione la data di eliminazione logica della fascicolazione
     * @param idUtenteEliminatore id utente ldap di colui che ha eliminato la fascicolazione
     * @param descrizioneEliminatore nome e cognome di colui che ha eliminato la fascicolazione
     * @param classificazione classificazione del fascicolo
     * @param nomeFascicoloInterfaccia nome fascicolo padre da mostrare in interfaccia
     */
    public Fascicolazione(String codiceFascicolo, String nomeFascicolo, String idUtenteFascicolatore, 
            String descrizioneFascicolatore, DateTime dataFascicolazione, boolean eliminato, 
            DateTime dataEliminazione, String idUtenteEliminatore, String descrizioneEliminatore, 
            ClassificazioneFascicolo classificazione, String nomeFascicoloInterfaccia) {
        this.codiceFascicolo = codiceFascicolo;
        this.nomeFascicolo = nomeFascicolo;
        this.idUtenteFascicolatore = idUtenteFascicolatore;
        this.descrizioneFascicolatore = descrizioneFascicolatore;
        this.dataFascicolazione = dataFascicolazione;
        this.eliminato = eliminato;
        this.dataEliminazione = dataEliminazione;
        this.idUtenteEliminatore = idUtenteEliminatore;
        this.descrizioneEliminatore = descrizioneEliminatore;
        this.classificazione = classificazione;
        this.nomeFascicoloInterfaccia = nomeFascicoloInterfaccia;
        setNullWhereEmpty();
    }
    
    /**
     * 
     * @param codiceFascicolo sarebbe la numerazione gerarchica
     * @param nomeFascicolo il nome del fascicolo
     * @param idUtenteFascicolatore id utente ldap del fascicolatore
     * @param descrizioneFascicolatore nome e cognome del fascicolatore
     * @param dataFascicolazione la data della fascicolazione
     * @param eliminato indica se la fascicolazione è stata logicamente eliminata
     * @param dataEliminazione la data di eliminazione logica della fascicolazione
     * @param idUtenteEliminatore id utente ldap di colui che ha eliminato la fascicolazione
     * @param descrizioneEliminatore nome e cognome di colui che ha eliminato la fascicolazione
     * @param classificazione classificazione del fascicolo
     * @param nomeFascicoloInterfaccia nome fascicolo padre da mostrare in interfaccia
     * @param nomeFascicoloInterfacciaOmissis nome fascicolo padre da mostrare in interfaccia con omissioni se non si hanno permessi
     * @param permesso indica se l'utente che ha fatto la richiesta ha il permessoUtente di visualizzare il nomeFascicoloInterfaccia
     */
    public Fascicolazione(String codiceFascicolo, String nomeFascicolo, String idUtenteFascicolatore, 
            String descrizioneFascicolatore, DateTime dataFascicolazione, boolean eliminato, 
            DateTime dataEliminazione, String idUtenteEliminatore, String descrizioneEliminatore, 
            ClassificazioneFascicolo classificazione, String nomeFascicoloInterfaccia,
            String nomeFascicoloInterfacciaOmissis, boolean permesso) {
        this.codiceFascicolo = codiceFascicolo;
        this.nomeFascicolo = nomeFascicolo;
        this.idUtenteFascicolatore = idUtenteFascicolatore;
        this.descrizioneFascicolatore = descrizioneFascicolatore;
        this.dataFascicolazione = dataFascicolazione;
        this.eliminato = eliminato;
        this.dataEliminazione = dataEliminazione;
        this.idUtenteEliminatore = idUtenteEliminatore;
        this.descrizioneEliminatore = descrizioneEliminatore;
        this.classificazione = classificazione;
        this.nomeFascicoloInterfaccia = nomeFascicoloInterfaccia;
        this.nomeFascicoloInterfacciaOmissis = nomeFascicoloInterfacciaOmissis;
        this.permessoUtente = permesso;
        setNullWhereEmpty();
    }

    public Fascicolazione(String codiceFascicolo, String nomeFascicolo, String idUtenteFascicolatore, 
            String descrizioneFascicolatore, DateTime dataFascicolazione, boolean eliminato, 
            DateTime dataEliminazione, String idUtenteEliminatore, String descrizioneEliminatore, 
            ClassificazioneFascicolo classificazione, String nomeFascicoloInterfaccia, DocumentOperationType tipoOperazione) {
        this.codiceFascicolo = codiceFascicolo;
        this.nomeFascicolo = nomeFascicolo;
        this.idUtenteFascicolatore = idUtenteFascicolatore;
        this.descrizioneFascicolatore = descrizioneFascicolatore;
        this.dataFascicolazione = dataFascicolazione;
        this.eliminato = eliminato;
        this.dataEliminazione = dataEliminazione;
        this.idUtenteEliminatore = idUtenteEliminatore;
        this.descrizioneEliminatore = descrizioneEliminatore;
        this.classificazione = classificazione;
        this.nomeFascicoloInterfaccia = nomeFascicoloInterfaccia;
        this.tipoOperazione = tipoOperazione;
        setNullWhereEmpty();
    }
    
    public Fascicolazione(String codiceFascicolo, String nomeFascicolo, String idUtenteFascicolatore, 
            String descrizioneFascicolatore, DateTime dataFascicolazione, boolean eliminato, 
            DateTime dataEliminazione, String idUtenteEliminatore, String descrizioneEliminatore, 
            ClassificazioneFascicolo classificazione, String nomeFascicoloInterfaccia, DocumentOperationType tipoOperazione,
            String nomeFascicoloInterfacciaOmissis, boolean permesso) {
        this.codiceFascicolo = codiceFascicolo;
        this.nomeFascicolo = nomeFascicolo;
        this.idUtenteFascicolatore = idUtenteFascicolatore;
        this.descrizioneFascicolatore = descrizioneFascicolatore;
        this.dataFascicolazione = dataFascicolazione;
        this.eliminato = eliminato;
        this.dataEliminazione = dataEliminazione;
        this.idUtenteEliminatore = idUtenteEliminatore;
        this.descrizioneEliminatore = descrizioneEliminatore;
        this.classificazione = classificazione;
        this.nomeFascicoloInterfaccia = nomeFascicoloInterfaccia;
        this.tipoOperazione = tipoOperazione;
        this.nomeFascicoloInterfacciaOmissis = nomeFascicoloInterfacciaOmissis;
        this.permessoUtente = permesso;
        setNullWhereEmpty();
    }
        
    public Fascicolazione(String codiceFascicolo, String nomeFascicolo, String idUtenteFascicolatore, 
            String descrizioneFascicolatore, DateTime dataFascicolazione, boolean eliminato, 
            DateTime dataEliminazione, String idUtenteEliminatore, String descrizioneEliminatore, DocumentOperationType tipoOperazione) {
        this.codiceFascicolo = codiceFascicolo;
        this.nomeFascicolo = nomeFascicolo;
        this.idUtenteFascicolatore = idUtenteFascicolatore;
        this.descrizioneFascicolatore = descrizioneFascicolatore;
        this.dataFascicolazione = dataFascicolazione;
        this.eliminato = eliminato;
        this.dataEliminazione = dataEliminazione;
        this.idUtenteEliminatore = idUtenteEliminatore;
        this.descrizioneEliminatore = descrizioneEliminatore;
        this.tipoOperazione = tipoOperazione;
        setNullWhereEmpty();
    }
    
    public Fascicolazione(String codiceFascicolo, String nomeFascicolo, String idUtenteFascicolatore, 
            String descrizioneFascicolatore, DateTime dataFascicolazione, DocumentOperationType tipoOperazione) {
        this.codiceFascicolo = codiceFascicolo;
        this.nomeFascicolo = nomeFascicolo;
        this.idUtenteFascicolatore = idUtenteFascicolatore;
        this.descrizioneFascicolatore = descrizioneFascicolatore;
        this.dataFascicolazione = dataFascicolazione;
        this.tipoOperazione = tipoOperazione;
        setNullWhereEmpty();
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

    public String getIdUtenteFascicolatore() {
        return idUtenteFascicolatore;
    }

    public void setIdUtenteFascicolatore(String idUtenteFascicolatore) {
        this.idUtenteFascicolatore = idUtenteFascicolatore;
    }

    public String getCfUtenteFascicolatore() {
        return cfUtenteFascicolatore;
    }

    public void setCfUtenteFascicolatore(String cfUtenteFascicolatore) {
        this.cfUtenteFascicolatore = cfUtenteFascicolatore;
    }

    public String getDescrizioneFascicolatore() {
        return descrizioneFascicolatore;
    }

    public void setDescrizioneFascicolatore(String descrizioneFascicolatore) {
        this.descrizioneFascicolatore = descrizioneFascicolatore;
    }

    public DateTime getDataFascicolazione() {
        return dataFascicolazione;
    }

    public void setDataFascicolazione(DateTime dataFascicolazione) {
        this.dataFascicolazione = dataFascicolazione;
    }

    public boolean isEliminato() {
        return eliminato;
    }

    public void setEliminato(boolean eliminato) {
        this.eliminato = eliminato;
    }

    public DateTime getDataEliminazione() {
        return dataEliminazione;
    }

    public void setDataEliminazione(DateTime dataEliminazione) {
        this.dataEliminazione = dataEliminazione;
    }

    public String getIdUtenteEliminatore() {
        return idUtenteEliminatore;
    }

    public void setIdUtenteEliminatore(String idUtenteEliminatore) {
        this.idUtenteEliminatore = idUtenteEliminatore;
    }

    public String getDescrizioneEliminatore() {
        return descrizioneEliminatore;
    }

    public void setDescrizioneEliminatore(String descrizioneEliminatore) {
        this.descrizioneEliminatore = descrizioneEliminatore;
    }

    public ClassificazioneFascicolo getClassificazione() {
        return classificazione;
    }

    public void setClassificazione(ClassificazioneFascicolo classificazione) {
        this.classificazione = classificazione;
    }

    public String getNomeFascicoloInterfaccia() {
        return nomeFascicoloInterfaccia;
    }

    public void setNomeFascicoloInterfaccia(String nomeFascicoloInterfaccia) {
        this.nomeFascicoloInterfaccia = nomeFascicoloInterfaccia;
    }

    public DocumentOperationType getTipoOperazione() {
        return tipoOperazione;
    }

    public void setTipoOperazione(DocumentOperationType tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }

    public String getNomeFascicoloInterfacciaOmissis() {
        return nomeFascicoloInterfacciaOmissis;
    }

    public void setNomeFascicoloInterfacciaOmissis(String nomeFascicoloInterfacciaOmissis) {
        this.nomeFascicoloInterfacciaOmissis = nomeFascicoloInterfacciaOmissis;
    }

    public boolean isPermessoUtente() {
        return permessoUtente;
    }

    public void setPermessoUtente(boolean permessoUtente) {
        this.permessoUtente = permessoUtente;
    }

    public int getIdIter() {
        return idIter;
    }

    public void setIdIter(int idIter) {
        this.idIter = idIter;
    }
    
    @JsonIgnore
    public int getNumero() {
        return numero;
    }

    @JsonIgnore
    public void setNumero(int numero) {
        this.numero = numero;
    }

    @JsonIgnore
    public int getAnno() {
        return anno;
    }

    @JsonIgnore
    public void setAnno(int anno) {
        this.anno = anno;
    }

    public boolean isVisibile() {
        return visibile;
    }

    public void setVisibile(boolean visibile) {
        this.visibile = visibile;
    }

    public String getIdFascicoloImportato() {
        return idFascicoloImportato;
    }

    public void setIdFascicoloImportato(String idFascicoloImportato) {
        this.idFascicoloImportato = idFascicoloImportato;
    }
    
    @JsonIgnore
    public void check(Document.DocumentOperationType tipoOperazioneGdDocContenitore) throws IodaDocumentException {
        if (getCodiceFascicolo() == null) {
            throw new IodaDocumentException("codice del fascicolo: " + toString() + " mancante");
        }
        else if (getTipoOperazione() == null) {
            throw new IodaDocumentException("tipoOperazione della fascicolazione: " + toString()+ " mancante");
        }
        else if (tipoOperazioneGdDocContenitore == Document.DocumentOperationType.INSERT && getTipoOperazione() != Document.DocumentOperationType.INSERT) {
            throw new IodaDocumentException("tipoOperazione della fascicolazione: " + toString() + " errata. Se il Documento è in fase di inserimento tutti i fascicoli devono avere tipoOperazione: insert");
        }
        else if (eliminato && (dataEliminazione == null || (idUtenteEliminatore == null || idUtenteEliminatore.isEmpty()))) {
            throw new IodaDocumentException("per eliminare logicamente un fascicolo è necessario specificare i seguenti campi: idUtenteEliminatore, dataEliminazione. Almeno uno è mancante. Fascicolazione: " + toString());
        }
    }
  
    @Override
    @JsonIgnore
    public int compareTo(Fascicolazione o) {
        DateTime data = ((Fascicolazione)o).getDataFascicolazione();
        
        return this.dataFascicolazione.compareTo(data);
    }
}
