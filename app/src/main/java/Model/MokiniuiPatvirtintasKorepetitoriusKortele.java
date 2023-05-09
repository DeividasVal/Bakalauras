package Model;

public class MokiniuiPatvirtintasKorepetitoriusKortele {

    private int kaina;
    private String vardasKorepetitoriaus;
    private String dalykai;
    private String adresas;
    private int korepetitoriausId;
    private int profilioId;
    private String korepetitoriausNuotrauka;
    private int patvirtintasId;

    public int getPatvirtintasId() {
        return patvirtintasId;
    }

    public void setPatvirtintasId(int patvirtintasId) {
        this.patvirtintasId = patvirtintasId;
    }

    public int getProfilioId() {
        return profilioId;
    }

    public void setProfilioId(int profilioId) {
        this.profilioId = profilioId;
    }

    public int getProfiliausId() {
        return profilioId;
    }

    public void setProfiliausId(int profiliausId) {
        this.profilioId = profiliausId;
    }

    public int getKorepetitoriausId() {
        return korepetitoriausId;
    }

    public void setKorepetitoriausId(int korepetitoriausId) {
        this.korepetitoriausId = korepetitoriausId;
    }

    public int getKaina() {
        return kaina;
    }

    public void setKaina(int kaina) {
        this.kaina = kaina;
    }

    public String getVardasKorepetitoriaus() {
        return vardasKorepetitoriaus;
    }

    public void setVardasKorepetitoriaus(String vardasKorepetitoriaus) {
        this.vardasKorepetitoriaus = vardasKorepetitoriaus;
    }

    public String getDalykai() {
        return dalykai;
    }

    public void setDalykai(String dalykai) {
        this.dalykai = dalykai;
    }

    public String getAdresas() {
        return adresas;
    }

    public void setAdresas(String adresas) {
        this.adresas = adresas;
    }

    public String getKorepetitoriausNuotrauka() {
        return korepetitoriausNuotrauka;
    }

    public void setKorepetitoriausNuotrauka(String korepetitoriausNuotrauka) {
        this.korepetitoriausNuotrauka = korepetitoriausNuotrauka;
    }

    public MokiniuiPatvirtintasKorepetitoriusKortele(int kaina, String vardasKorepetitoriaus, String dalykai, String adresas, int korepetitoriausId, int profilioId) {
        this.kaina = kaina;
        this.vardasKorepetitoriaus = vardasKorepetitoriaus;
        this.dalykai = dalykai;
        this.adresas = adresas;
        this.korepetitoriausId = korepetitoriausId;
        this.profilioId = profilioId;
    }

    public MokiniuiPatvirtintasKorepetitoriusKortele(int kaina, String vardasKorepetitoriaus, String dalykai, String adresas, int korepetitoriausId, int profilioId, String korepetitoriausNuotrauka, int patvirtintasId) {
        this.kaina = kaina;
        this.vardasKorepetitoriaus = vardasKorepetitoriaus;
        this.dalykai = dalykai;
        this.adresas = adresas;
        this.korepetitoriausId = korepetitoriausId;
        this.profilioId = profilioId;
        this.korepetitoriausNuotrauka = korepetitoriausNuotrauka;
        this.patvirtintasId = patvirtintasId;
    }
}
