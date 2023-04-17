package Model;

public class Atsiliepimas {

    private String vardas;
    private int skelbejoID;
    private int profilioID;
    private int korepetitoriausID;
    private String tekstas;
    private Double ivertinimas;
    private String laikas;

    public String getVardas() {
        return vardas;
    }

    public void setVardas(String vardas) {
        this.vardas = vardas;
    }

    public int getSkelbejoID() {
        return skelbejoID;
    }

    public void setSkelbejoID(int skelbejoID) {
        this.skelbejoID = skelbejoID;
    }

    public int getProfilioID() {
        return profilioID;
    }

    public void setProfilioID(int profilioID) {
        this.profilioID = profilioID;
    }

    public int getKorepetitoriausID() {
        return korepetitoriausID;
    }

    public void setKorepetitoriausID(int korepetitoriausID) {
        this.korepetitoriausID = korepetitoriausID;
    }

    public String getTekstas() {
        return tekstas;
    }

    public void setTekstas(String tekstas) {
        this.tekstas = tekstas;
    }

    public Double getIvertinimas() {
        return ivertinimas;
    }

    public void setIvertinimas(Double ivertinimas) {
        this.ivertinimas = ivertinimas;
    }

    public String getLaikas() {
        return laikas;
    }

    public Atsiliepimas(String vardas, int skelbejoID, int profilioID, int korepetitoriausID, String tekstas, Double ivertinimas, String laikas) {
        this.vardas = vardas;
        this.skelbejoID = skelbejoID;
        this.profilioID = profilioID;
        this.korepetitoriausID = korepetitoriausID;
        this.tekstas = tekstas;
        this.ivertinimas = ivertinimas;
        this.laikas = laikas;
    }

    public void setLaikas(String laikas) {
        this.laikas = laikas;
    }
}
