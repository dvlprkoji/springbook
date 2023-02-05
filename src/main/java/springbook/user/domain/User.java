package springbook.user.domain;

public class User {

    String id;
    String name;
    String password;

    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() { return name; }

    public String getPassword() {
        return password;
    }
}
