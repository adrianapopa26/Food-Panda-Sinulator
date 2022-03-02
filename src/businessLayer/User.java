package businessLayer;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String function;

    public User(String username, String password, String function) {
        this.username = username;
        this.password = password;
        this.function = function;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
