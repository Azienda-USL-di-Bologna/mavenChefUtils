package it.bologna.ausl.ioda.iodaobjectlibrary;

public class FascicoliSpecialiResearcher extends Search {

    private int anno;

    public FascicoliSpecialiResearcher() {
    }

    public FascicoliSpecialiResearcher(int anno) {
        this();
        this.anno = anno;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }
}