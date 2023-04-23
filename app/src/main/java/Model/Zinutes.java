package Model;

public class Zinutes {
    private int gavejo_id;
    private int siuntejo_id;
    private String laikas_zinutes;
    private String zinutes_aprasymas;

    public Zinutes(String zinutes_aprasymas, int siuntejo_id) {
        this.siuntejo_id = siuntejo_id;
        this.zinutes_aprasymas = zinutes_aprasymas;

    }

    public int getGavejo_id() {
        return gavejo_id;
    }

    public void setGavejo_id(int gavejo_id) {
        this.gavejo_id = gavejo_id;
    }

    public int getSiuntejo_id() {
        return siuntejo_id;
    }

    public void setSiuntejo_id(int siuntejo_id) {
        this.siuntejo_id = siuntejo_id;
    }

    public String getLaikas_zinutes() {
        return laikas_zinutes;
    }

    public void setLaikas_zinutes(String laikas_zinutes) {
        this.laikas_zinutes = laikas_zinutes;
    }

    public String getZinutes_aprasymas() {
        return zinutes_aprasymas;
    }

    public void setZinutes_aprasymas(String zinutes_aprasymas) {
        this.zinutes_aprasymas = zinutes_aprasymas;
    }

    public Zinutes(int gavejo_id, int siuntejo_id, String laikas_zinutes, String zinutes_aprasymas) {
        this.gavejo_id = gavejo_id;
        this.siuntejo_id = siuntejo_id;
        this.laikas_zinutes = laikas_zinutes;
        this.zinutes_aprasymas = zinutes_aprasymas;
    }
}
