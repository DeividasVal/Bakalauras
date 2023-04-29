package Model;

import java.io.Serializable;

public class Korepetitorius extends Vartotojas implements Serializable {
    private String name;
    private String korepetitoriausNuotrauka;

    public String getKorepetitoriausNuotrauka() {
        return korepetitoriausNuotrauka;
    }

    public void setKorepetitoriausNuotrauka(String korepetitoriausNuotrauka) {
        this.korepetitoriausNuotrauka = korepetitoriausNuotrauka;
    }

    public Korepetitorius() {}

    public Korepetitorius(
            int id, String login, String psw, String email, String name, String korepetitoriausNuotrauka) {
        super(id, login, psw, email);
        this.name = name;
        this.korepetitoriausNuotrauka = korepetitoriausNuotrauka;
    }

    public Korepetitorius(String login, String psw, String email) {
        super(login, psw, email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
