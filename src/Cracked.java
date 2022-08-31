import java.util.ArrayList;

public class Cracked {
    String password;
    String hash;
    String salt;
    ArrayList<String> usernames;

    public Cracked() {

    }

    public Cracked(String password, String hash, ArrayList<String> usernames) {
        this.password = password;
        this.hash = hash;
        this.usernames = usernames;
    }

    public Cracked(String password, String hash, String salt, ArrayList<String> usernames) {
        this.password = password;
        this.hash = hash;
        this.salt = salt;
        this.usernames = usernames;
    }

    public String getPassword() {
        return password;
    }

    public String getHash() {
        return hash;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public String getSalt() {
        return salt;
    }
}
