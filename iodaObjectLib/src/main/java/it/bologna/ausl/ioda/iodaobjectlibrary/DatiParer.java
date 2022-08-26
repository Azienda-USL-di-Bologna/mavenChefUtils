package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.bologna.ausl.ioda.iodaobjectlibrary.exceptions.IodaDocumentException;

/**
 *
 * @author gdm
 */
public class DatiParer extends Document {
//public static enum StatoAggiornamento {DA_NON_VERSARE, DA_VERSARE, VERSATO}

    //private StatoAggiornamento statoAggiornamento; // (da versare, versato, da non versare)    

    protected String autoreAggiornamento; // obbligatorio
    protected String descrizioneAggiornamento; // obbligatorio
    protected String xmlSpecifico; // obbligatorio

    protected Boolean forzaConservazione; // opzionale. Se non passato: in inserimento viene considerato false, in aggiornamento non viene modificato
    protected Boolean forzaAccettazione; // opzionale. Se non passato: in inserimento viene considerato false, in aggiornamento non viene modificato
    protected Boolean forzaCollegamento; // opzionale. Se non passato: in inserimento viene considerato false, in aggiornamento non viene modificato

    protected String statoVersamentoProposto;
    
    public DatiParer() {
        setNullWhereEmpty();
    }

    public DatiParer(String idOggettoOrigine, String tipoOggettoOrigine, String autoreAggiornamento, String descrizioneAggiornamento, String xmlSpecifico, Boolean forzaConservazione, Boolean forzaAccettazione, Boolean forzaCollegamento, String statoVersamentoProposto) {
        setIdOggettoOrigine(idOggettoOrigine);
        setTipoOggettoOrigine(tipoOggettoOrigine);
        this.autoreAggiornamento = autoreAggiornamento;
        this.descrizioneAggiornamento = descrizioneAggiornamento;
        this.xmlSpecifico = xmlSpecifico;
        this.forzaConservazione = forzaConservazione;
        this.forzaAccettazione = forzaAccettazione;
        this.forzaCollegamento = forzaCollegamento;
        this.statoVersamentoProposto = statoVersamentoProposto;
        setNullWhereEmpty();
    }
    
   public DatiParer(String idOggettoOrigine, String tipoOggettoOrigine, String autoreAggiornamento, String descrizioneAggiornamento, String xmlSpecifico) {
        setIdOggettoOrigine(idOggettoOrigine);
        setTipoOggettoOrigine(tipoOggettoOrigine);
        this.autoreAggiornamento = autoreAggiornamento;
        this.descrizioneAggiornamento = descrizioneAggiornamento;
        this.xmlSpecifico = xmlSpecifico;
        setNullWhereEmpty();
   }

   @JsonIgnore
   public void check(Document.DocumentOperationType operation) throws IodaDocumentException {
        if (getIdOggettoOrigine() == null || getIdOggettoOrigine().equals("")) {
            throw new IodaDocumentException("idOggettoOrigine mancante");
        }
        else if (getTipoOggettoOrigine() == null || getTipoOggettoOrigine().equals("")) {
            throw new IodaDocumentException("tipoOggettoOrigine mancante");
        }
//        else if (getXmlSpecifico() == null) {
//            throw new IodaDocumentException("xmlSpecifico mancante");
//        }
        else if (operation == DocumentOperationType.UPDATE && getDescrizioneAggiornamento( ) == null) {
            throw new IodaDocumentException("descrizioneAggiornamento mancante");
        }
        else if (operation == DocumentOperationType.UPDATE && getAutoreAggiornamento() == null) {
            throw new IodaDocumentException("autoreAggiornamento mancante");
        }
   }

    public String getAutoreAggiornamento() {
        return autoreAggiornamento;
    }

    public void setAutoreAggiornamento(String autoreAggiornamento) {
        this.autoreAggiornamento = autoreAggiornamento;
    }

    public String getDescrizioneAggiornamento() {
        return descrizioneAggiornamento;
    }

    public void setDescrizioneAggiornamento(String descrizioneAggiornamento) {
        this.descrizioneAggiornamento = descrizioneAggiornamento;
    }

    public String getXmlSpecifico() {
        return xmlSpecifico;
    }

    public void setXmlSpecifico(String xmlSpecifico) {
        this.xmlSpecifico = xmlSpecifico;
    }

    public Boolean getForzaConservazione() {
        return forzaConservazione;
    }

    public void setForzaConservazione(Boolean forzaConservazione) {
        this.forzaConservazione = forzaConservazione;
    }

    public Boolean getForzaAccettazione() {
        return forzaAccettazione;
    }

    public void setForzaAccettazione(Boolean forzaAccettazione) {
        this.forzaAccettazione = forzaAccettazione;
    }

    public Boolean getForzaCollegamento() {
        return forzaCollegamento;
    }

    public void setForzaCollegamento(Boolean forzaCollegamento) {
        this.forzaCollegamento = forzaCollegamento;
    }

    public String getStatoVersamentoProposto() {
        return statoVersamentoProposto;
    }

    public void setStatoVersamentoProposto(String statoVersamentoProposto) {
        this.statoVersamentoProposto = statoVersamentoProposto;
    }
    
}
