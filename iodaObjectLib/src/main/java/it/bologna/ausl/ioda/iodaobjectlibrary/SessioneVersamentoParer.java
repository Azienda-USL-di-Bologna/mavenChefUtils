package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.DateTime;

/**
 *
 * @author gdm
 */
public class SessioneVersamentoParer extends Document {

public static enum EsitoVersamento {OK, ERRORE, SERVIZIO};

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private DateTime dataInizio;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private DateTime dataFine;

    private String codiceErrore;
    private String descrizioneErrore;
    private String xmlVersato;
    private EsitoVersamento esito;
    private String rapportoVersamento;

    public SessioneVersamentoParer() {
    }

    public SessioneVersamentoParer(DateTime dataInizio, DateTime dataFine, String codiceErrore, String descrizioneErrore, String xmlVersato, EsitoVersamento esito, String rapportoVersamento) {
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.codiceErrore = codiceErrore;
        this.descrizioneErrore = descrizioneErrore;
        this.xmlVersato = xmlVersato;
        this.esito = esito;
        this.rapportoVersamento = rapportoVersamento;
    }

    public DateTime getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(DateTime dataInizio) {
        this.dataInizio = dataInizio;
    }

    public DateTime getDataFine() {
        return dataFine;
    }

    public void setDataFine(DateTime dataFine) {
        this.dataFine = dataFine;
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

    public String getXmlVersato() {
        return xmlVersato;
    }

    public void setXmlVersato(String xmlVersato) {
        this.xmlVersato = xmlVersato;
    }

    public EsitoVersamento getEsito() {
        return esito;
    }

    public void setEsito(EsitoVersamento esito) {
        this.esito = esito;
    }

    public String getRapportoVersamento() {
        return rapportoVersamento;
    }

    public void setRapportoVersamento(String rappostoVersamento) {
        this.rapportoVersamento = rappostoVersamento;
    }
}
