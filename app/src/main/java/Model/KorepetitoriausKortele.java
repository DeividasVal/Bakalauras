package Model;

public class KorepetitoriausKortele {

    private String vardas;
    private int kaina;
    private String dalykai;
    private int id;
    private String mokymoBudas;

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

    public int getKaina() {
        return kaina;
    }

    public void setKaina(int kaina) {
        this.kaina = kaina;
    }

    public String getDalykai() {
        return dalykai;
    }

    public void setDalykai(String dalykai) {
        this.dalykai = dalykai;
    }

    public KorepetitoriausKortele(String vardas, int kaina, String dalykai, int id, String mokymoBudas) {
        this.vardas = vardas;
        this.kaina = kaina;
        this.dalykai = dalykai;
        this.id = id;
        this.mokymoBudas = mokymoBudas;
    }
}
