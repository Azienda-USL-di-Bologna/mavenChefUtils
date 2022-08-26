package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.lang.reflect.Field;

@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "tipoClasse")  
@JsonSubTypes({  
    @JsonSubTypes.Type(value = Researcher.class, name = "researcher"),
    @JsonSubTypes.Type(value = FascicoliSpecialiResearcher.class, name = "fascicoliSpecialiResearcher")
})  
public abstract class Search implements Requestable {
        
    protected String searchString; // stringa da ricercare
    protected String idUtente; // utente che effettua la ricerca
    protected int limiteDiRicerca; // limite di risultati; se è 0 allora non si impostano limiti

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(String idUtente) {
        this.idUtente = idUtente;
    }

    public int getLimiteDiRicerca() {
        return limiteDiRicerca;
    }

    public void setLimiteDiRicerca(int limiteDiRicerca) {
        this.limiteDiRicerca = limiteDiRicerca;
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
}