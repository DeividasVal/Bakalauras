package Model;

public class MokinioUzklausaKortele {
    private String vardasKorepetitoriaus;
    private int stausas;

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
}
