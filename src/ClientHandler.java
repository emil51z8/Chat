import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private String SID;

    public ClientHandler(Socket clientSocket, String SID) {
        this.clientSocket = clientSocket;
        this.SID = SID;
    }
    public void sendMessage(String message) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
             readInput(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Closed connection from " + clientSocket.getInetAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}