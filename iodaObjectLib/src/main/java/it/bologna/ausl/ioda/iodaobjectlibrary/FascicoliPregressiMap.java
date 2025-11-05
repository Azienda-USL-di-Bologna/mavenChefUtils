package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author user
 */
@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "tipoClasse")  
@JsonSubTypes({  
    @JsonSubTypes.Type(value = FascicoliPregressiMap.class, name = "fascicoliPregressiMap")
})  
public class FascicoliPregressiMap implements Requestable{
    
    private Map<String, Fascicolo> map;

    public FascicoliPregressiMap() {
        map = new HashMap<>();
    }
    
    @JsonIgnore
    public void addFascicolo (String idFascicolopregresso, Fascicolo fascicolo){
        map.put(idFascicolopregresso, fascicolo);
    }
    
    @JsonIgnore
    public Fascicolo getFascicolo (String idFascicolopregresso){
        return map.get(idFascicolopregresso);
    }

    public Map<String, Fascicolo> getMap() {
        return map;
    }

    public void setMap(Map<String, Fascicolo> map) {
        this.map = map;
    }
    
}
