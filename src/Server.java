import java.util.HashMap;
import java.util.Map;

public class Server {

    private HashMap<String, User> userList = new HashMap<>();

    private void readInput(String input) {
        String status = input.substring(0, 3);
        if (status.equals("100")) {
            String sessionID = createUser(input.substring(3), "0");
            System.out.println(sessionID);
            if (sessionID.equals("")){
                System.out.println("fejl");
            } else {
                System.out.println("lykkedes");
            }
        }
        if (status == "200") {
            //kald metode sendMessage
        }
        if (status == "300") {
            //kald metode sendPrivateMessage
        }
        if (status == "400") {
            //kald metode quit
        }
    }

    private String createUser(String navn, String IP) {
        String result = "";
        for (Map.Entry<String, User> entry : userList.entrySet()) {
            System.out.println(entry.getValue().getUserName());
            User value = entry.getValue();
            if (value.getUserName().equals(navn.trim())) {
                System.out.println("kevin");
                return "kevin";
            }
        }
        User u = new User(navn, IP);
        for (int i = 1000; i <= 9999; i++) {
            if (userList.containsKey(i) == false) {

                result = i + "";
                break;
            }
        }
        userList.put(result,u);

            return result;
    }


    public static void main(String[] args) {
        User u1 = new User("emil12","1234");
        User u2 = new User("emil12","1234");
        User u3 = new User("emil1","1234");

        Server s1 = new Server();
        s1.readInput("100emil12");
        s1.readInput("100emil1");
        s1.readInput("100emil12");

        System.out.println();



    }


}
