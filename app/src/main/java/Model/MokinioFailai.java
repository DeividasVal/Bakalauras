package Model;

import java.sql.Blob;

public class MokinioFailai {

    private String pavadinimas;
    private String laikas;
    private String failas;
    private String vardas;

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    public String getLaikas() {
        return laikas;
    }

    public void setLaikas(String laikas) {
        this.laikas = laikas;
    }

    public String getFailas() {
        return failas;
    }

    public void setFailas(String failas) {
        this.failas = failas;
    }

    public String getVardas() {
        return vardas;
    }

    public void setVardas(String vardas) {
        this.vardas = vardas;
    }

    public MokinioFailai(String pavadinimas, String laikas, String failas, String vardas) {
        this.pavadinimas = pavadinimas;
        this.laikas = laikas;
        this.failas = failas;
        this.vardas = vardas;
    }
}
