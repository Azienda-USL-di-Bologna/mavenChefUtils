package it.bologna.ausl.ioda.iodaobjectlibrary;

/**
 *
 * @author gdm
 */
public class ClassificazioneFascicolo {

    private String codiceCategoria;
    private String nomeCategoria;
    private String codiceClasse;
    private String nomeClasse;
    private String codiceSottoclasse;
    private String nomeSottoclasse;

    public ClassificazioneFascicolo() {
    }

    /**
     * 
     * @param codiceCategoria
     * @param nomeCategoria
     * @param codiceClasse
     * @param nomeClasse
     * @param codiceSottoclasse
     * @param nomeSottoclasse 
     */
    public ClassificazioneFascicolo(String codiceCategoria, String nomeCategoria, String codiceClasse, String nomeClasse, String codiceSottoclasse, String nomeSottoclasse) {
        this.codiceCategoria = codiceCategoria;
        this.nomeCategoria = nomeCategoria;
        this.codiceClasse = codiceClasse;
        this.nomeClasse = nomeClasse;
        this.codiceSottoclasse = codiceSottoclasse;
        this.nomeSottoclasse = nomeSottoclasse;
    }

    public String getCodiceCategoria() {
        return codiceCategoria;
    }

    public void setCodiceCategoria(String codiceCategoria) {
        this.codiceCategoria = codiceCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getCodiceClasse() {
        return codiceClasse;
    }

    public void setCodiceClasse(String codiceClasse) {
        this.codiceClasse = codiceClasse;
    }

    public String getNomeClasse() {
        return nomeClasse;
    }

    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    public String getCodiceSottoclasse() {
        return codiceSottoclasse;
    }

    public void setCodiceSottoclasse(String codiceSottoclasse) {
        this.codiceSottoclasse = codiceSottoclasse;
    }

    public String getNomeSottoclasse() {
        return nomeSottoclasse;
    }

    public void setNomeSottoclasse(String nomeSottoclasse) {
        this.nomeSottoclasse = nomeSottoclasse;
    }

}
