package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import org.joda.time.DateTime;

/**
 *
 * @author gdm
 */
public class DatiProfiloCommittente implements Serializable {
    
    private Integer id;
    private String guidOggetto;
    private String tipologiaGara;
    private String cig;
    private String cigAzienda;
    private Float importo;
    private String fornitore;
    private String ragioniSceltaFornitore;
    private Boolean checkFornitoreRequisitiGenerali;
    private Boolean checkFornitoreRequisitiProfessionali;
    private String procedura;
    private String oggettoAffidamento;
    private String operatoriEconomiciInviati;
    private String operatoriEconomiciOfferenti;
    private String aggiudicatario;
    private String partitaIvaCf;
    private String importoTestuale;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ssZ")
    private DateTime dataAggiudicazione;
    private String codiceProfiloCommittente;
    private String testoProfiloCommittente;

    public DatiProfiloCommittente() {
    }

    public DatiProfiloCommittente(Integer id, String guidOggetto, String tipologiaGara, String cig, String cigAzienda, Float importo, String fornitore, String ragioniSceltaFornitore, Boolean checkFornitoreRequisitiGenerali, Boolean checkFornitoreRequisitiProfessionali, String procedura, String oggettoAffidamento, String operatoriEconomiciInviati, String operatoriEconomiciOfferenti, String aggiudicatario, String partitaIvaCf, DateTime dataAggiudicazione, String codiceProfiloCommittente, String testoProfiloCommittente, String importoTestuale) {
        this.id = id;
        this.guidOggetto = guidOggetto;
        this.tipologiaGara = tipologiaGara;
        this.cig = cig;
        this.cigAzienda = cigAzienda;
        this.importo = importo;
        this.fornitore = fornitore;
        this.ragioniSceltaFornitore = ragioniSceltaFornitore;
        this.checkFornitoreRequisitiGenerali = checkFornitoreRequisitiGenerali;
        this.checkFornitoreRequisitiProfessionali = checkFornitoreRequisitiProfessionali;
        this.procedura = procedura;
        this.oggettoAffidamento = oggettoAffidamento;
        this.operatoriEconomiciInviati = operatoriEconomiciInviati;
        this.operatoriEconomiciOfferenti = operatoriEconomiciOfferenti;
        this.aggiudicatario = aggiudicatario;
        this.partitaIvaCf = partitaIvaCf;
        this.dataAggiudicazione = dataAggiudicazione;
        this.codiceProfiloCommittente = codiceProfiloCommittente;
        this.testoProfiloCommittente = testoProfiloCommittente;
        this.importoTestuale = importoTestuale;
    }

    public String getGuidOggetto() {
        return guidOggetto;
    }

    public void setGuidOggetto(String guidOggetto) {
        this.guidOggetto = guidOggetto;
    }

    public String getTipologiaGara() {
        return tipologiaGara;
    }

    public void setTipologiaGara(String tipologiaGara) {
        this.tipologiaGara = tipologiaGara;
    }

    public String getCig() {
        return cig;
    }

    public void setCig(String cig) {
        this.cig = cig;
    }

    public String getCigAzienda() {
        return cigAzienda;
    }

    public void setCigAzienda(String cigAzienda) {
        this.cigAzienda = cigAzienda;
    }

    public Float getImporto() {
        return importo;
    }

    public void setImporto(Float importo) {
        this.importo = importo;
    }

    public String getFornitore() {
        return fornitore;
    }

    public void setFornitore(String fornitore) {
        this.fornitore = fornitore;
    }

    public String getRagioniSceltaFornitore() {
        return ragioniSceltaFornitore;
    }

    public void setRagioniSceltaFornitore(String ragioniSceltaFornitore) {
        this.ragioniSceltaFornitore = ragioniSceltaFornitore;
    }

    public Boolean getCheckFornitoreRequisitiGenerali() {
        return checkFornitoreRequisitiGenerali;
    }

    public void setCheckFornitoreRequisitiGenerali(Boolean checkFornitoreRequisitiGenerali) {
        this.checkFornitoreRequisitiGenerali = checkFornitoreRequisitiGenerali;
    }

    public Boolean getCheckFornitoreRequisitiProfessionali() {
        return checkFornitoreRequisitiProfessionali;
    }

    public void setCheckFornitoreRequisitiProfessionali(Boolean checkFornitoreRequisitiProfessionali) {
        this.checkFornitoreRequisitiProfessionali = checkFornitoreRequisitiProfessionali;
    }

    public String getProcedura() {
        return procedura;
    }

    public void setProcedura(String procedura) {
        this.procedura = procedura;
    }

    public String getOggettoAffidamento() {
        return oggettoAffidamento;
    }

    public void setOggettoAffidamento(String oggettoAffidamento) {
        this.oggettoAffidamento = oggettoAffidamento;
    }

    public String getOperatoriEconomiciInviati() {
        return operatoriEconomiciInviati;
    }

    public void setOperatoriEconomiciInviati(String operatoriEconomiciInviati) {
        this.operatoriEconomiciInviati = operatoriEconomiciInviati;
    }

    public String getOperatoriEconomiciOfferenti() {
        return operatoriEconomiciOfferenti;
    }

    public void setOperatoriEconomiciOfferenti(String operatoriEconomiciOfferenti) {
        this.operatoriEconomiciOfferenti = operatoriEconomiciOfferenti;
    }

    public String getAggiudicatario() {
        return aggiudicatario;
    }

    public void setAggiudicatario(String aggiudicatario) {
        this.aggiudicatario = aggiudicatario;
    }

    public String getPartitaIvaCf() {
        return partitaIvaCf;
    }

    public void setPartitaIvaCf(String partitaIvaCf) {
        this.partitaIvaCf = partitaIvaCf;
    }
    
    public DateTime getDataAggiudicazione() {
        return dataAggiudicazione;
    }

    public void setDataAggiudicazione(DateTime dataAggiudicazione) {
        this.dataAggiudicazione = dataAggiudicazione;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodiceProfiloCommittente() {
        return codiceProfiloCommittente;
    }

    public void setCodiceProfiloCommittente(String codiceProfiloCommittente) {
        this.codiceProfiloCommittente = codiceProfiloCommittente;
    }

    public String getTestoProfiloCommittente() {
        return testoProfiloCommittente;
    }

    public void setTestoProfiloCommittente(String testoProfiloCommittente) {
        this.testoProfiloCommittente = testoProfiloCommittente;
    }

    public String getImportoTestuale() {
        return importoTestuale;
    }

    public void setImportoTestuale(String importoTestuale) {
        this.importoTestuale = importoTestuale;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DatiProfiloCommittente)) {
            return false;
        }
        DatiProfiloCommittente other = (DatiProfiloCommittente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.bologna.ausl.ioda.iodaobjectlibrary.DatiProfiloCommittente[ id=" + id + " ]";
    }
    
}
