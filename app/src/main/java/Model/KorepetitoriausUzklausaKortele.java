package Model;

public class KorepetitoriausUzklausaKortele
{
    private String vardasMokinio;
    private int stausas;

    public int getStausas() {
        return stausas;
    }

    public KorepetitoriausUzklausaKortele(String vardasMokinio, int stausas) {
        this.vardasMokinio = vardasMokinio;
        this.stausas = stausas;
    }

    public void setStausas(int stausas) {
        this.stausas = stausas;
    }

    public String getVardasMokinio() {
        return vardasMokinio;
    }

    public void setVardasMokinio(String vardasMokinio) {
        this.vardasMokinio = vardasMokinio;
    }
}
