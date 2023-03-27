package Model;

import java.io.Serializable;

public class Korepetitorius extends Vartotojas implements Serializable {
    private String name;

    public Korepetitorius() {}

    public Korepetitorius(
            int id, String login, String psw, String email, String name) {
        super(id, login, psw, email);
        this.name = name;
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
