package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

public class Fascicoli implements Requestable{
    protected List<Fascicolo> fascicoli;

    public Fascicoli() {
    }
    
    public void createFascicoli() {
        fascicoli = new ArrayList<>();
    }
    
    
    public List<Fascicolo> getFascicoli() {
        return fascicoli;
    }

    public void setFascicoli(List<Fascicolo> fascicoli) {
        this.fascicoli = fascicoli;
    }

    @JsonIgnore
    public void addFascicolo(Fascicolo fascicolo) {
        if (fascicoli == null)
            fascicoli = new ArrayList<>();
        fascicoli.add(fascicolo);
    }
    
    @JsonIgnore
    public Fascicolo getFascicolo(int index){
        return fascicoli.get(index);
    }

    @JsonIgnore
    public int getSize(){
        if(fascicoli == null)
            return 0;
        else
            return fascicoli.size();
    }
}
