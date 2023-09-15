import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class
Server {

    private static HashMap<String, User> connectedUsers = new HashMap<>();

    public void addUser(String SID, Socket clientSocket) {

        synchronized (connectedUsers) {
            User user = new User("", clientSocket);
            connectedUsers.put(SID, user);
        }
    }

    public void sendMessageToUser(String username, String message) {
        synchronized (connectedUsers) {
            User user = connectedUsers.get(username);
            if (user != null) {
                user.sendMessage(message);
            }
        }
    }

    private void readInput(String input) {
        String status = input.substring(0, 3);
        if (status.equals("100")) {

        }
        if (status == "200") {
        }
        if (status == "300") {
            //kald metode sendPrivateMessage
        }
        if (status == "400") {
            //kald metode quit
        }
    }

    public String createUser(String navn, Socket socket) {
        String result = "";
        for (Map.Entry<String, User> entry : connectedUsers.entrySet()) {
            User value = entry.getValue();
            if (value.getUsername().equals(navn.trim())) {
                return "";
            }
        }
        User u = new User(navn, socket);
        for (int i = 1000; i <= 9999; i++)
        {
            if (connectedUsers.containsKey(i+"") == false) {
                result = i + "";
                break;
            }
        }
        connectedUsers.put(result,u);

            return result;
    }
    public static String findFreeSID()
    {
        String SID = "";
        for (int i = 1000; i <= 9999; i++)
        {
            if (connectedUsers.containsKey(i+"") == false) {
                SID = i + "";
                break;
            }
        }return SID;
 }
    public static void main(String[] args) {
        Server theSever = new Server();

        int portNumber = 1992;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server is listening on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());
                String SID = findFreeSID();
                // Create a new thread to handle the client
                ClientHandler clientHandler = new ClientHandler(clientSocket,SID);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    }



