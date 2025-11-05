package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import it.bologna.ausl.ioda.iodaobjectlibrary.exceptions.IodaDocumentException;
import it.bologna.ausl.ioda.iodaobjectlibrary.exceptions.IodaFileException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TimeZone;

/**
 *
 * @author gdm
 */
public class IodaRequestDescriptor {

    private String idApplicazione;
    private String tokenApplicazione;
    private Requestable object;
    private Map<String, Object> additionalData;

    public IodaRequestDescriptor() {
    }

    public IodaRequestDescriptor(String idApplicazione, String tokenApplicazione, Requestable object) {
        this.idApplicazione = idApplicazione;
        this.tokenApplicazione = tokenApplicazione;
        this.object = object;
        this.additionalData = null;
    }

    public IodaRequestDescriptor(String idApplicazione, String tokenApplicazione, Requestable object, Map<String, Object> additionalData) {
        this.idApplicazione = idApplicazione;
        this.tokenApplicazione = tokenApplicazione;
        this.object = object;
        this.additionalData = additionalData;
    }

    public String getIdApplicazione() {
        return idApplicazione;
    }

    public void setIdApplicazione(String idApplicazione) {
        this.idApplicazione = idApplicazione;
    }

    public String getTokenApplicazione() {
        return tokenApplicazione;
    }

    public void setTokenApplicazione(String tokenApplicazione) {
        this.tokenApplicazione = tokenApplicazione;
    }

    public Requestable getObject() {
        return object;
    }

    public void setObject(Requestable object) {
        this.object = object;
    }

    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }

    @JsonIgnore
    public static IodaRequestDescriptor parse(String value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setTimeZone(TimeZone.getDefault());
        return mapper.readValue(value, IodaRequestDescriptor.class);
    }

    @JsonIgnore
    public static IodaRequestDescriptor parse(InputStream value) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setTimeZone(TimeZone.getDefault());
        return mapper.readValue(value, IodaRequestDescriptor.class);
    }

    @JsonIgnore
    public String getJSONString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setTimeZone(TimeZone.getDefault());
        String writeValueAsString = mapper.writeValueAsString(this);
        return writeValueAsString;
    }

    @JsonIgnore
    public GdDoc getGdDoc(Document.DocumentOperationType operationType) throws IodaDocumentException, IOException {
        GdDoc gdDoc = Requestable.parse(((Document) getObject()).getJSONString(), GdDoc.class);
        if (gdDoc == null) {
            throw new IodaDocumentException("json descrittivo del documento da inserire mancante");
        }
        gdDoc.check(operationType);

        return gdDoc;
    }

    @JsonIgnore
    public DatiParer getDatiParer(Document.DocumentOperationType operationType) throws IodaDocumentException, IOException, IodaFileException {
        DatiParer datiParer = Requestable.parse(((DatiParer) getObject()).getJSONString(), DatiParer.class);
        if (datiParer == null) {
            throw new IodaDocumentException("json descrittivo della descrizione dell'aggiornamento/versamento parer da mancante");
        }
        datiParer.check(operationType);

        return datiParer;
    }

    @JsonIgnore
    public Fascicolo getFascicolo() throws IodaDocumentException, IOException {
        Fascicolo fascicolo = Requestable.parse((getObject()).getJSONString(), Fascicolo.class);
        if (fascicolo == null) {
            throw new IodaDocumentException("json descrittivo del documento da inserire mancante");
        }

        return fascicolo;
    }
}
