package Model;

public class KorepetitoriusKortelePagrindinis {

    private String vardas;
    private String kaina;
    private String dalykai;
    private int id;
    private Double ivertinimas;
    private String mokymoBudas;
    private String korepetitoriausNuotrauka;

    public String getKorepetitoriausNuotrauka() {
        return korepetitoriausNuotrauka;
    }

    public void setKorepetitoriausNuotrauka(String korepetitoriausNuotrauka) {
        this.korepetitoriausNuotrauka = korepetitoriausNuotrauka;
    }

    public String getVardas() {
        return vardas;
    }

    public void setVardas(String vardas) {
        this.vardas = vardas;
    }

    public String getKaina() {
        return kaina;
    }

    public void setKaina(String kaina) {
        this.kaina = kaina;
    }

    public String getDalykai() {
        return dalykai;
    }

    public void setDalykai(String dalykai) {
        this.dalykai = dalykai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getIvertinimas() {
        return ivertinimas;
    }

    public void setIvertinimas(Double ivertinimas) {
        this.ivertinimas = ivertinimas;
    }

    public String getMokymoBudas() {
        return mokymoBudas;
    }

    public void setMokymoBudas(String mokymoBudas) {
        this.mokymoBudas = mokymoBudas;
    }

    public KorepetitoriusKortelePagrindinis(String vardas, String kaina, String dalykai, int id, Double ivertinimas, String mokymoBudas) {
        this.vardas = vardas;
        this.kaina = kaina;
        this.dalykai = dalykai;
        this.id = id;
        this.ivertinimas = ivertinimas;
        this.mokymoBudas = mokymoBudas;
    }

    public KorepetitoriusKortelePagrindinis(String vardas, String kaina, String dalykai, int id, Double ivertinimas, String mokymoBudas, String korepetitoriausNuotrauka) {
        this.vardas = vardas;
        this.kaina = kaina;
        this.dalykai = dalykai;
        this.id = id;
        this.ivertinimas = ivertinimas;
        this.mokymoBudas = mokymoBudas;
        this.korepetitoriausNuotrauka = korepetitoriausNuotrauka;
    }
}
