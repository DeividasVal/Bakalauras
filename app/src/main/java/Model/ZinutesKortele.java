package Model;

public class ZinutesKortele {
    private String vardas;
    private String zinutes;
    private String laikas;
    private int siuntejoId;
    private int gavejoId;

    public int getSiuntejoId() {
        return siuntejoId;
    }

    public void setSiuntejoId(int siuntejoId) {
        this.siuntejoId = siuntejoId;
    }

    public String getVardas() {
        return vardas;
    }

    public void setVardas(String vardas) {
        this.vardas = vardas;
    }

    public String getZinutes() {
        return zinutes;
    }

    public void setZinutes(String zinutes) {
        this.zinutes = zinutes;
    }

    public String getLaikas() {
        return laikas;
    }

    public void setLaikas(String laikas) {
        this.laikas = laikas;
    }

    public int getGavejoId() {
        return gavejoId;
    }

    public void setGavejoId(int gavejoId) {
        this.gavejoId = gavejoId;
    }

    public ZinutesKortele(String vardas, String zinutes, String laikas, int siuntejoId, int gavejoId) {
        this.vardas = vardas;
        this.zinutes = zinutes;
        this.laikas = laikas;
        this.siuntejoId = siuntejoId;
        this.gavejoId = gavejoId;
    }
}
