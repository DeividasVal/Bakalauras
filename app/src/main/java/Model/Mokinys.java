package Model;

import java.io.Serializable;

public class Mokinys extends Vartotojas implements Serializable {
    private String name;
    private String mokinioNuotrauka;

    public String getMokinioNuotrauka() {
        return mokinioNuotrauka;
    }

    public void setMokinioNuotrauka(String mokinioNuotrauka) {
        this.mokinioNuotrauka = mokinioNuotrauka;
    }

    public Mokinys() {}

    public Mokinys(
            int id, String login, String psw, String email, String name, String mokinioNuotrauka) {
        super(id, login, psw, email);
        this.name = name;
        this.mokinioNuotrauka = mokinioNuotrauka;
    }

    public Mokinys(String login, String psw, String email) {
        super(login, psw, email);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "login: " + getLogin() + ", psw: " + getPsw() + ", email: " + getEmail() + ", id: " + getId();
    }
}
