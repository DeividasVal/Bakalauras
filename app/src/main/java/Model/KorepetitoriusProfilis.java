package Model;

public class KorepetitoriusProfilis {

    private String adresas;
    private String nuotrauka;
    private String miestas;
    private String tipas;
    private String val;
    private String bio;
    private String istaiga;
    private String dalykaiIstaigoje;
    private Double ivertinimas;
    private int ivertinimuKiekis;
    private String dalykaiMokymosi;
    private boolean[][] prieinamumas;

    public String getAdresas() {
        return adresas;
    }

    public void setAdresas(String adresas) {
        this.adresas = adresas;
    }

    public String getNuotrauka() {
        return nuotrauka;
    }

    public void setNuotrauka(String nuotrauka) {
        this.nuotrauka = nuotrauka;
    }

    public String getMiestas() {
        return miestas;
    }

    public void setMiestas(String miestas) {
        this.miestas = miestas;
    }

    public String getTipas() {
        return tipas;
    }

    public void setTipas(String tipas) {
        this.tipas = tipas;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getIstaiga() {
        return istaiga;
    }

    public void setIstaiga(String istaiga) {
        this.istaiga = istaiga;
    }

    public String getDalykaiIstaigoje() {
        return dalykaiIstaigoje;
    }

    public void setDalykaiIstaigoje(String dalykaiIstaigoje) {
        this.dalykaiIstaigoje = dalykaiIstaigoje;
    }

    public Double getIvertinimas() {
        return ivertinimas;
    }

    public void setIvertinimas(Double ivertinimas) {
        this.ivertinimas = ivertinimas;
    }

    public int getIvertinimuKiekis() {
        return ivertinimuKiekis;
    }

    public void setIvertinimuKiekis(int ivertinimuKiekis) {
        this.ivertinimuKiekis = ivertinimuKiekis;
    }

    public String getDalykaiMokymosi() {
        return dalykaiMokymosi;
    }

    public void setDalykaiMokymosi(String dalykaiMokymosi) {
        this.dalykaiMokymosi = dalykaiMokymosi;
    }

    public boolean[][] getPrieinamumas() {
        return prieinamumas;
    }

    public void setPrieinamumas(boolean[][] prieinamumas) {
        this.prieinamumas = prieinamumas;
    }

    public KorepetitoriusProfilis(String adresas, String nuotrauka, String miestas, String tipas, String val, String bio, String istaiga, String dalykaiIstaigoje, Double ivertinimas, int ivertinimuKiekis, String dalykaiMokymosi, boolean[][] prieinamumas) {
        this.adresas = adresas;
        this.nuotrauka = nuotrauka;
        this.miestas = miestas;
        this.tipas = tipas;
        this.val = val;
        this.bio = bio;
        this.istaiga = istaiga;
        this.dalykaiIstaigoje = dalykaiIstaigoje;
        this.ivertinimas = ivertinimas;
        this.ivertinimuKiekis = ivertinimuKiekis;
        this.dalykaiMokymosi = dalykaiMokymosi;
        this.prieinamumas = prieinamumas;
    }
}
