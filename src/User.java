public class User {
    private String userName;
    private String IP;

    public User(String userName, String IP) {
        this.userName = userName;
        this.IP = IP;
    }

    public User() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", IP='" + IP + '\'' +
                '}';
    }
}
