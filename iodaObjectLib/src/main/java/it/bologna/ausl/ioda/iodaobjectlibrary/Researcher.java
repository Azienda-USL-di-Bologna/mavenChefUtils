package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class Researcher extends Search{

    public Researcher() {
    }
    
    public Researcher(String idUtente, String searchString, int limiteDiRicerca) {
        this();
        this.searchString = searchString;
        this.idUtente = idUtente;
        this.limiteDiRicerca = limiteDiRicerca;
    }
    
}
