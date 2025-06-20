package entity;

public class User {
    private int userId;
    private String username;
    private String upassword;
    private String phone;

    // 无参构造
    public User() {}

    // 全参构造
    public User(int userId, String username, String upassword, String phone) {
        this.userId = userId;
        this.username = username;
        this.upassword = upassword;
        this.phone = phone;
    }

    // Getter 和 Setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", upassword='" + upassword + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
