package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.lang.reflect.Field;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoClasse")
@JsonSubTypes({
    @Type(value = GdDoc.class, name = "documento")
    ,  
    @Type(value = SimpleDocument.class, name = "documentoBase")
    , 
    @Type(value = SottoDocumento.class, name = "sottoDocumento")
    ,
    @Type(value = Fascicolazioni.class, name = "fascicolazioni")
    ,
    @Type(value = Fascicolazione.class, name = "fascicolazione")
    ,
    @Type(value = DatiParer.class, name = "datiParer")
    ,
     @Type(value = PubblicazioneIoda.class, name = "pubblicazione")
})
public abstract class Document implements Requestable {

    public static enum DocumentOperationType {
        INSERT("insert"),
        UPDATE("update"),
        DELETE("delete");

        private final String key;

        DocumentOperationType(String key) {
            this.key = key;
        }

        @JsonCreator
        public static DocumentOperationType fromString(String key) {
            return key == null
                    ? null
                    : DocumentOperationType.valueOf(key.toUpperCase());
        }

        @JsonValue
        public String getKey() {
            return key;
        }
    }

    protected String idOggettoOrigine; // obbligatorio, id dell'oggetto nel sistema di chi sta versando
    protected String tipoOggettoOrigine; // obbligatorio, tipo dell'oggetto nel sistema di chi sta versando
    protected String utente; // facoltativo, utente passato per eventuali operazioni

    public String getIdOggettoOrigine() {
        return idOggettoOrigine;
    }

    public void setIdOggettoOrigine(String idOggettoOrigine) {
        this.idOggettoOrigine = idOggettoOrigine;
    }

    public String getTipoOggettoOrigine() {
        return tipoOggettoOrigine;
    }

    public void setTipoOggettoOrigine(String tipoOggettoOrigine) {
        this.tipoOggettoOrigine = tipoOggettoOrigine;
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
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

    /**
     * setta, nei campi in cui è necessario, il prefisso che identifica
     * l'applicazione di origine
     *
     * @param prefisso
     */
    @JsonIgnore
    public void setPrefissoApplicazioneOrigine(String prefisso) {
        setIdOggettoOrigine(prefisso + getIdOggettoOrigine());
    }

    @Override
    public String toString() {
        try {
            return getJSONString();
        } catch (JsonProcessingException ex) {
        }
        return null;
    }
}
