package Model;

public class IsimintasKorepetitoriusKortele {

    private String vardas;
    private String kaina;
    private String dalykai;
    private int id;
    private Double ivertinimas;
    private String mokymoBudas;
    private int profilioId;

    public int getProfilioId() {
        return profilioId;
    }

    public void setProfilioId(int profilioId) {
        this.profilioId = profilioId;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public IsimintasKorepetitoriusKortele(String vardas, String kaina, String dalykai, int id, Double ivertinimas, String mokymoBudas, int profilioId) {
        this.vardas = vardas;
        this.kaina = kaina;
        this.dalykai = dalykai;
        this.id = id;
        this.ivertinimas = ivertinimas;
        this.mokymoBudas = mokymoBudas;
        this.profilioId = profilioId;
    }
}