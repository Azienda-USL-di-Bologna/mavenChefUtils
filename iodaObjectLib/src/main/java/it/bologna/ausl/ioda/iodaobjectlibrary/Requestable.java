package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gdm
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoClasse")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Document.class, name = "document")
    ,
    @JsonSubTypes.Type(value = Fascicolo.class, name = "fascicolo")
    ,
    @JsonSubTypes.Type(value = Search.class, name = "search")
})
public interface Requestable {

    @JsonIgnore
    public static <T extends Requestable> T parse(String value, Class<T> requestableClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setTimeZone(TimeZone.getDefault());
        return mapper.readValue(value, requestableClass);
    }

    @JsonIgnore
    public static <T extends Requestable> T parse(InputStream value, Class<T> requestableClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setTimeZone(TimeZone.getDefault());
        return mapper.readValue(value, requestableClass);
    }

    @JsonIgnore
    public default String getJSONString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.setTimeZone(TimeZone.getDefault());
        String writeValueAsString = mapper.writeValueAsString(this);
        return writeValueAsString;
    }
}
