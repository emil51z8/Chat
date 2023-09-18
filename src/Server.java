import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static ConcurrentHashMap<String, User> connectedUsers = new ConcurrentHashMap<>();
    private static ExecutorService executor = Executors.newFixedThreadPool(20); // Change the pool size as needed

    public void addUser(String SID, Socket clientSocket) {
        User user = new User("", clientSocket);
        connectedUsers.put(SID, user);
    }
    public void sendMessageToUser(String SID, String message, String receiverSID) {
        User reciever = connectedUsers.get(receiverSID);
        User sender = connectedUsers.get(SID);
        if (reciever != null) {
            reciever.sendMessage("PM Fra: " +sender.getUsername()+ ": " + message);
        }
    }
    public void sendBroadcastMessage(String name, String message) {
        for (Map.Entry<String, User> entry : connectedUsers.entrySet()) {
            User user = entry.getValue();
            user.sendMessage(name+": " +message);
        }
    }
    public void updateName(String SID, String name)
    {
        connectedUsers.get(SID).setUsername(name);
    }
    public boolean isUsernameTaken(String username) {
        for (Map.Entry<String, User> entry : connectedUsers.entrySet()) {
            User user = entry.getValue();
            if (user.getUsername().equals(username)) {
                return true; // Username er taget
            }
        }
        return false; // Username er ledigt
    }

    private Boolean readInput(String input) {
        String status = input.substring(0, 3);
        String SID = input.substring(3,7);
        if (status.equals("100"))
        {
           String name = input.substring(7);
           if (isUsernameTaken(name) == false) {
               updateName(SID, name);
           }
           else return true;
        }
        if (status == "200") {
            String msg = input.substring(7);
            sendBroadcastMessage(connectedUsers.get(SID).getUsername(),msg);
        }
        if (status == "300") {
            String reciever = input.substring(7,11);
            String msg = input.substring(11);
            sendMessageToUser(SID,msg,reciever);
        }
        if (status == "400") {
            sendBroadcastMessage("Server :", connectedUsers.get(SID).getUsername() + " har forladt chatten");
           connectedUsers.remove(SID);
        }
        return false;
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
        Server theServer = new Server();

        int portNumber = 1992;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server is listening on port " + portNumber);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                //System.out.println("Accepted connection from " + clientSocket.getInetAddress());
                String SID = findFreeSID();
                theServer.addUser(SID,clientSocket);

                executor.submit(() -> {
                    try (
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                    )
                    {
                        out.println("999"+SID);
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            Boolean taget = theServer.readInput(inputLine);
                            if(taget = true)
                            {
                                out.print(000); // Brugernavn er taget Client skal vælge et andet
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                            //System.out.println("Closed connection from " + clientSocket.getInetAddress());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
