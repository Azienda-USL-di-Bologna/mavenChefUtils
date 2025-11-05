package it.bologna.ausl.ioda.iodaobjectlibrary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.bologna.ausl.ioda.iodaobjectlibrary.exceptions.IodaDocumentException;

public class SimpleDocument extends Document{
    public SimpleDocument() {
        setNullWhereEmpty();
    }
    
    public SimpleDocument(String idOggettoOrigine, String tipoOggettoOrigine) {
        this();
        this.idOggettoOrigine = idOggettoOrigine;
        this.tipoOggettoOrigine = tipoOggettoOrigine;
        this.utente = null;
    }
    
    public SimpleDocument(String idOggettoOrigine, String tipoOggettoOrigine, String utente) {
        this();
        this.idOggettoOrigine = idOggettoOrigine;
        this.tipoOggettoOrigine = tipoOggettoOrigine;
        this.utente = utente;
    }
    
    @JsonIgnore
    public void check(DocumentOperationType operationType) throws IodaDocumentException {
        if (getIdOggettoOrigine() == null || getIdOggettoOrigine().equals("")) {
            throw new IodaDocumentException("idOggettoOrigine mancante");
        }
        else if (getTipoOggettoOrigine() == null || getTipoOggettoOrigine().equals("")) {
            throw new IodaDocumentException("tipoOggettoOrigine mancante");
        }
    
    }
}
