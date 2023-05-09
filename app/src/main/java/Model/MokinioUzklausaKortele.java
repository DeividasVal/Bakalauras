package Model;

public class MokinioUzklausaKortele {
    private String vardasKorepetitoriaus;
    private int stausas;
    private String korepetitoriausNuotrauka;
    private int Id;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getKorepetitoriausNuotrauka() {
        return korepetitoriausNuotrauka;
    }

    public void setKorepetitoriausNuotrauka(String korepetitoriausNuotrauka) {
        this.korepetitoriausNuotrauka = korepetitoriausNuotrauka;
    }

    public String getVardasKorepetitoriaus() {
        return vardasKorepetitoriaus;
    }

    public void setVardasKorepetitoriaus(String vardasKorepetitoriaus) {
        this.vardasKorepetitoriaus = vardasKorepetitoriaus;
    }

    public int getStausas() {
        return stausas;
    }

    public void setStausas(int stausas) {
        this.stausas = stausas;
    }

    public MokinioUzklausaKortele(String vardasKorepetitoriaus, int stausas) {
        this.vardasKorepetitoriaus = vardasKorepetitoriaus;
        this.stausas = stausas;
    }

    public MokinioUzklausaKortele(String vardasKorepetitoriaus, int stausas, String korepetitoriausNuotrauka, int Id) {
        this.vardasKorepetitoriaus = vardasKorepetitoriaus;
        this.stausas = stausas;
        this.korepetitoriausNuotrauka = korepetitoriausNuotrauka;
        this.Id = Id;
    }
}
