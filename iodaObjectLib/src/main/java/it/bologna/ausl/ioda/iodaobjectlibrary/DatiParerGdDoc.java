package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DatiParerGdDoc extends Document {

    protected String statoVersamentoProposto; // obbligatorio
    protected String statoVersamentoEffettivo; // obbligatorio
    protected String xmlSpecifico; // obbligatorio

    protected Boolean forzaConservazione; // opzionale. Se non passato: in inserimento viene considerato false, in aggiornamento non viene modificato
    protected Boolean forzaAccettazione; // opzionale. Se non passato: in inserimento viene considerato false, in aggiornamento non viene modificato
    protected Boolean forzaCollegamento; // opzionale. Se non passato: in inserimento viene considerato false, in aggiornamento non viene modificato
    protected Boolean idoneoVersamento; // indica se i dati pro ParER sono idonei al versamento
    protected String esitoUltimoVersamento;
    protected String codiceErroreUltimoVersamento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    protected DateTime dataUltimoVersamento;

    protected String documento_in_errore;
    protected String idGdDoc; // opzionale e da usare solamente dentro bds_tools
    
    protected String idUtenteForzatura;
    protected String motivazioneForzatura;

    public DatiParerGdDoc() {
        setNullWhereEmpty();
    }

    public DatiParerGdDoc(String statoVersamentoProposto, String statoVersamentoEffettivo, String xmlSpecifico, Boolean forzaConservazione, Boolean forzaAccettazione, Boolean forzaCollegamento, Boolean idoneoVersamento) {
        this.statoVersamentoProposto = statoVersamentoProposto;
        this.statoVersamentoEffettivo = statoVersamentoEffettivo;
        this.xmlSpecifico = xmlSpecifico;
        this.forzaConservazione = forzaConservazione;
        this.forzaAccettazione = forzaAccettazione;
        this.forzaCollegamento = forzaCollegamento;
        this.idoneoVersamento = idoneoVersamento;
    }

    public String getStatoVersamentoProposto() {
        return statoVersamentoProposto;
    }

    public void setStatoVersamentoProposto(String statoVersamentoProposto) {
        this.statoVersamentoProposto = statoVersamentoProposto;
    }

    public String getStatoVersamentoEffettivo() {
        return statoVersamentoEffettivo;
    }

    public void setStatoVersamentoEffettivo(String statoVersamentoEffettivo) {
        this.statoVersamentoEffettivo = statoVersamentoEffettivo;
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

    public Boolean getIdoneoVersamento() {
        return idoneoVersamento;
    }

    public void setIdoneoVersamento(Boolean idoneoVersamento) {
        this.idoneoVersamento = idoneoVersamento;
    }

    public String getEsitoUltimoVersamento() {
        return esitoUltimoVersamento;
    }

    public void setEsitoUltimoVersamento(String esitoUltimoVersamento) {
        this.esitoUltimoVersamento = esitoUltimoVersamento;
    }

    public String getCodiceErroreUltimoVersamento() {
        return codiceErroreUltimoVersamento;
    }

    public void setCodiceErroreUltimoVersamento(String codiceErroreUltimoVersamento) {
        this.codiceErroreUltimoVersamento = codiceErroreUltimoVersamento;
    }

    public DateTime getDataUltimoVersamento() {
        return dataUltimoVersamento;
    }

    public void setDataUltimoVersamento(DateTime dataUltimoVersamento) {
        this.dataUltimoVersamento = dataUltimoVersamento;
    }

    public String getDocumento_in_errore() {
        return documento_in_errore;
    }

    public void setDocumento_in_errore(String documento_in_errore) {
        this.documento_in_errore = documento_in_errore;
    }

    public String getIdUtenteForzatura() {
        return idUtenteForzatura;
    }

    public void setIdUtenteForzatura(String idUtenteForzatura) {
        this.idUtenteForzatura = idUtenteForzatura;
    }

    public String getMotivazioneForzatura() {
        return motivazioneForzatura;
    }

    public void setMotivazioneForzatura(String motivazioneForzatura) {
        this.motivazioneForzatura = motivazioneForzatura;
    }
    
    public static String toIsoDateFormatString(DateTime dateTime, String pattern) {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(pattern);
        return dtfOut.print(dateTime);
    }

    @JsonIgnore
    public String getIdGdDoc() {
        return idGdDoc;
    }

    @JsonIgnore
    public void setIdGdDoc(String idGdDoc) {
        this.idGdDoc = idGdDoc;
    }
}
