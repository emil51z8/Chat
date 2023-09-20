import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static ConcurrentHashMap<String, User> connectedUsers = new ConcurrentHashMap<>();
    private static ExecutorService executor = Executors.newFixedThreadPool(20); // Change the pool size as needed
    private static String fejl = "";

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
           else{
               fejl = "Brugernavn er taget, vælg et nyt";
               return true;
           }
        }
        if (status.equals("200")) {
            String msg = input.substring(7);
            sendBroadcastMessage(connectedUsers.get(SID).getUsername(),msg);

        }
        if (status.equals("300")) {
            String splitInput = getFirstWordUsingSplit(input)[0];
            String msg = getFirstWordUsingSplit(input)[1];

            String recieverName = splitInput.substring(7);
            String recieverSID = findSIDforUser(recieverName);

            if(recieverSID.equals("fejl")) {
                fejl = "Bruger eksisterer ikke";
                return true;
            } else sendMessageToUser(SID,msg,recieverSID);
        }
        if (status.equals("400")) {
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

    public String findSIDforUser(String username) {
        for (Map.Entry<String, User> entry : connectedUsers.entrySet()) {
            User user = entry.getValue();
            if (user.getUsername().equals(username)) {
                return entry.getKey(); // returner SID for bruger med username
            }
        }
        return "fejl";
    }


    public String[] getFirstWordUsingSplit(String input) {
        String[] tokens = input.split(" ", 2);
        return tokens;
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
                String SID = findFreeSID();
                theServer.addUser(SID,clientSocket);

                executor.submit(() -> {
                    try (
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
                    )
                    {
                        out.println("999"+SID);
                        String inputLine = "";
                        while (true) {
                            inputLine = in.readLine();
                            System.out.println(inputLine);
                            Boolean fejlStatus = theServer.readInput(inputLine);
                            System.out.println("Brugernavn er taget:" +fejlStatus);
                            if(fejlStatus == true)
                            {
                                out.println("000" + fejl); // Brugernavn er taget Client skal vælge et andet
                                out.flush();
                            }
                            if(fejlStatus == false)
                            {
                                out.println("999");
                                out.flush();
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
