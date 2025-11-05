package it.bologna.ausl.ioda.iodaobjectlibrary;

/**
 *
 * @author andrea
 */
public class GdDocSessioneVersamento {

    String idSessioneVersamento, guidSessioneVersamento, idGdDoc, xmlVersato;
    String esito, codiceErrore, descrizioneErrore, rapportoVersamento, documentoInErrore;
    String azione, motivazioneAzione;

    public GdDocSessioneVersamento() {
    }

    public String getIdSessioneVersamento() {
        return idSessioneVersamento;
    }

    public void setIdSessioneVersamento(String idSessioneVersamento) {
        this.idSessioneVersamento = idSessioneVersamento;
    }

    public String getGuidSessioneVersamento() {
        return guidSessioneVersamento;
    }

    public void setGuidSessioneVersamento(String guidSessioneVersamento) {
        this.guidSessioneVersamento = guidSessioneVersamento;
    }

    public String getIdGdDoc() {
        return idGdDoc;
    }

    public void setIdGdDoc(String idGdDoc) {
        this.idGdDoc = idGdDoc;
    }

    public String getXmlVersato() {
        return xmlVersato;
    }

    public void setXmlVersato(String xmlVersato) {
        this.xmlVersato = xmlVersato;
    }

    public String getEsito() {
        return esito;
    }

    public void setEsito(String esito) {
        this.esito = esito;
    }

    public String getCodiceErrore() {
        return codiceErrore;
    }

    public void setCodiceErrore(String codiceErrore) {
        this.codiceErrore = codiceErrore;
    }

    public String getDescrizioneErrore() {
        return descrizioneErrore;
    }

    public void setDescrizioneErrore(String descrizioneErrore) {
        this.descrizioneErrore = descrizioneErrore;
    }

    public String getRapportoVersamento() {
        return rapportoVersamento;
    }

    public void setRapportoVersamento(String rapportoVersamento) {
        this.rapportoVersamento = rapportoVersamento;
    }

    public String getDocumentoInErrore() {
        return documentoInErrore;
    }

    public void setDocumentoInErrore(String documentoInErrore) {
        this.documentoInErrore = documentoInErrore;
    }

    public String getAzione() {
        return azione;
    }

    public void setAzione(String azione) {
        this.azione = azione;
    }

    public String getMotivazioneAzione() {
        return motivazioneAzione;
    }

    public void setMotivazioneAzione(String motivazioneAzione) {
        this.motivazioneAzione = motivazioneAzione;
    }
}
