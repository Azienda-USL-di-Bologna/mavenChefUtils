package it.bologna.ausl.ioda.iodaobjectlibrary;

/**
 *
 * @author andrea
 */
public class BagProfiloArchivistico {
    
    String classificaFascicolo, nomeFascicolo;
    int annoFascicolo, numeroFasciolo;
    
    String nomeSottoFascicolo;
    int numeroSottoFascicolo;
    
    String nomeInserto;
    int numeroInserto;
    
    int level;

    public BagProfiloArchivistico() {
    }
    
    public String getClassificaFascicolo() {
        return classificaFascicolo;
    }

    public void setClassificaFascicolo(String classificaFascicolo) {
        this.classificaFascicolo = classificaFascicolo;
    }

    public String getNomeFascicolo() {
        return nomeFascicolo;
    }

    public void setNomeFascicolo(String nomeFascicolo) {
        this.nomeFascicolo = nomeFascicolo;
    }

    public int getAnnoFascicolo() {
        return annoFascicolo;
    }

    public void setAnnoFascicolo(int annoFascicolo) {
        this.annoFascicolo = annoFascicolo;
    }

    public int getNumeroFasciolo() {
        return numeroFasciolo;
    }

    public void setNumeroFasciolo(int numeroFasciolo) {
        this.numeroFasciolo = numeroFasciolo;
    }

    public String getNomeSottoFascicolo() {
        return nomeSottoFascicolo;
    }

    public void setNomeSottoFascicolo(String nomeSottoFascicolo) {
        this.nomeSottoFascicolo = nomeSottoFascicolo;
    }

    public int getNumeroSottoFascicolo() {
        return numeroSottoFascicolo;
    }

    public void setNumeroSottoFascicolo(int numeroSottoFascicolo) {
        this.numeroSottoFascicolo = numeroSottoFascicolo;
    }

    public String getNomeInserto() {
        return nomeInserto;
    }

    public void setNomeInserto(String nomeInserto) {
        this.nomeInserto = nomeInserto;
    }

    public int getNumeroInserto() {
        return numeroInserto;
    }

    public void setNumeroInserto(int numeroInserto) {
        this.numeroInserto = numeroInserto;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
    
}
