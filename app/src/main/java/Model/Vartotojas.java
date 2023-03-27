package Model;

import java.io.Serializable;

public class Vartotojas implements Serializable {

    private int id;
    private String login;
    private String psw;
    private String email;

    public Vartotojas() {}

    public Vartotojas(String login, String psw, String email) {
        this.login = login;
        this.psw = psw;
        this.email = email;
    }

    public Vartotojas(int id, String login, String psw, String email) {
        this.id = id;
        this.login = login;
        this.psw = psw;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
