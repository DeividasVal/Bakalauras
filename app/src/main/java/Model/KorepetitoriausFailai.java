package Model;

public class KorepetitoriausFailai {

    private String pavadinimas;
    private String laikas;
    private String failas;
    private String vardas;
    private int failoId;
    private String mokinioNuotrauka;

    public String getMokinioNuotrauka() {
        return mokinioNuotrauka;
    }

    public void setMokinioNuotrauka(String mokinioNuotrauka) {
        this.mokinioNuotrauka = mokinioNuotrauka;
    }

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

    public int getFailoId() {
        return failoId;
    }

    public void setFailoId(int failoId) {
        this.failoId = failoId;
    }

    public KorepetitoriausFailai(String pavadinimas, String laikas, String failas, String vardas, int failoId) {
        this.pavadinimas = pavadinimas;
        this.laikas = laikas;
        this.failas = failas;
        this.vardas = vardas;
        this.failoId = failoId;
    }

    public KorepetitoriausFailai(String pavadinimas, String laikas, String failas, String vardas, int failoId, String mokinioNuotrauka) {
        this.pavadinimas = pavadinimas;
        this.laikas = laikas;
        this.failas = failas;
        this.vardas = vardas;
        this.failoId = failoId;
        this.mokinioNuotrauka = mokinioNuotrauka;
    }
}
