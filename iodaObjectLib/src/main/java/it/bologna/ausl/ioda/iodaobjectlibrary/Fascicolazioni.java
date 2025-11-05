package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author gdm
 */
public class Fascicolazioni extends Document {
    protected List<Fascicolazione> fascicolazioni;

    public Fascicolazioni() {
        setNullWhereEmpty();
    }

    public Fascicolazioni(String idOggettoOrigine, String tipoOggettoOrigine) {
        this.idOggettoOrigine = idOggettoOrigine;
        this.tipoOggettoOrigine = tipoOggettoOrigine;
        setNullWhereEmpty();
    }

    public Fascicolazioni(String idOggettoOrigine, String tipoOggettoOrigine, List<Fascicolazione> fascicolazioni) {
        this(idOggettoOrigine, tipoOggettoOrigine);
        this.fascicolazioni = fascicolazioni;
    }

    public List<Fascicolazione> getFascicolazioni() {
        return fascicolazioni;
    }

    public void setFascicolazioni(List<Fascicolazione> fascicolazioni) {
        this.fascicolazioni = fascicolazioni;
    }

    public void addFascicolazione(Fascicolazione fascicolazione) {
        if (fascicolazioni == null)
            fascicolazioni = new ArrayList<>();
        fascicolazioni.add(fascicolazione);
    }
    
    @JsonIgnore
    public Fascicolazione getFascicolazioneByIdIter(int idIter) {
        Fascicolazione fascicolazione = fascicolazioni.stream().filter(f -> f.idIter == idIter).findFirst().get();
        return fascicolazione;
    }
    
//    @JsonIgnore
//    public static Fascicolazioni parse(String value) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JodaModule());
//        mapper.setTimeZone(TimeZone.getDefault());
//        return mapper.readValue(value, Fascicolazioni.class);
//    }
//    
//    @JsonIgnore
//    public static Fascicolazioni parse(InputStream value) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JodaModule());
//        mapper.setTimeZone(TimeZone.getDefault());
//        return mapper.readValue(value, Fascicolazioni.class);
//    }
//    
//    @JsonIgnore
//    public String getJSONString() throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JodaModule());
//        mapper.setTimeZone(TimeZone.getDefault());
//        String writeValueAsString = mapper.writeValueAsString(this);
//        return writeValueAsString;
//    }

}
