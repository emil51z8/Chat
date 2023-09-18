import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static String sessionID = "";

    static Scanner input = new Scanner(System.in);

    private static String tjekBrugernavn()
    {
        String brugernavn = input.nextLine();
        for(int i = 0; i < brugernavn.length(); i ++){
            if(brugernavn.indexOf("@" && brugernavn.indexOf(" "){}
        }

    }
    private static void clientMenu(){
        if(sessionID.equals("999"))
        {

        }
    }

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 1992;

        try (
                Socket socket = new Socket(serverAddress, serverPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            System.out.println("Connected to server: " + serverAddress + ":" + serverPort);

            Thread receiveThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        if (sessionID.isEmpty() && serverMessage.substring(0, 3).equals("999")) {
                            sessionID = serverMessage.substring(3, 7);
                            System.out.println("Received sessionID: " + sessionID);
                        } else if (sessionID.isEmpty() && serverMessage.substring(0, 3).equals("000"))
                        {
                            // Vælg nyt brugernavn
                        }
                        else{
                            // Imp+ement chat here

                            System.out.println("Server: " + serverMessage);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            receiveThread.start();

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {

                out.println(userInput);
                if (userInput.equalsIgnoreCase("exit")) {
                    // send quit besked til server
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}