package Model;

public class KorepetitoriuiPatvirtintasMokinysKortele {

    private String vardasMokinio;
    private int mokinioId;

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
}
