package Model;

public class KorepetitoriuiPatvirtintasMokinysKortele {

    private String vardasMokinio;
    private int mokinioId;
    private String mokinioNuotrauka;

    public String getMokinioNuotrauka() {
        return mokinioNuotrauka;
    }

    public void setMokinioNuotrauka(String mokinioNuotrauka) {
        this.mokinioNuotrauka = mokinioNuotrauka;
    }

    public int getMokinioId() {
        return mokinioId;
    }

    public void setMokinioId(int mokinioId) {
        this.mokinioId = mokinioId;
    }

    public String getVardasMokinio() {
        return vardasMokinio;
    }

    public void setVardasMokinio(String vardasMokinio) {
        this.vardasMokinio = vardasMokinio;
    }

    public KorepetitoriuiPatvirtintasMokinysKortele(String vardasMokinio) {
        this.vardasMokinio = vardasMokinio;
    }

    public KorepetitoriuiPatvirtintasMokinysKortele(String vardasMokinio, int mokinioId) {
        this.vardasMokinio = vardasMokinio;
        this.mokinioId = mokinioId;
    }

    public KorepetitoriuiPatvirtintasMokinysKortele(String vardasMokinio, int mokinioId, String mokinioNuotrauka) {
        this.vardasMokinio = vardasMokinio;
        this.mokinioId = mokinioId;
        this.mokinioNuotrauka = mokinioNuotrauka;
    }
}
