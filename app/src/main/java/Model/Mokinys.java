package Model;

import java.io.Serializable;

public class Mokinys extends Vartotojas implements Serializable {
    private String name;

    public Mokinys() {}

    public Mokinys(
            int id, String login, String psw, String email, String name) {
        super(id, login, psw, email);
        this.name = name;
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
