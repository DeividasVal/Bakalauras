package Model;

public class KorepetitoriausUzklausaKortele
{
    private String vardasMokinio;
    private int stausas;
    private String mokinioNuotrauka;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getMokinioNuotrauka() {
        return mokinioNuotrauka;
    }

    public void setMokinioNuotrauka(String mokinioNuotrauka) {
        this.mokinioNuotrauka = mokinioNuotrauka;
    }

    public KorepetitoriausUzklausaKortele(String vardasMokinio, int stausas, String mokinioVardas, int id) {
        this.vardasMokinio = vardasMokinio;
        this.stausas = stausas;
        this.mokinioNuotrauka = mokinioVardas;
        this.id = id;
    }
}
