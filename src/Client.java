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
    private static String brugerNavn = " ";
    static Scanner input = new Scanner(System.in);
    private static String tjekBrugernavn()
    {

        System.out.println("Opret brugernavn");
        System.out.println("Brugernavn må ikke indholde mellemrum eller @");
        String brugernavn = input.nextLine();
        


        boolean containsWhiteSpace = brugernavn.contains(" ");
        boolean containsElefantNoseA = brugernavn.contains("@");

        if(containsWhiteSpace || containsElefantNoseA){
            System.out.println("Der må ikke være mellemrum eller @ i dit brugernavn.");
            System.out.println("Prøv igen");
            brugernavn = "";
            tjekBrugernavn();
        }
        System.out.println("Du har fået brugernavn " + brugernavn );
        return brugernavn;
    }
    private static void clientMenu(){
        if(sessionID.equals("999"))
        {
            String valgteBrugernavn = tjekBrugernavn();
            System.out.println(valgteBrugernavn);
        }
    }
    private static boolean sendUsernameToServer(PrintWriter out, String username) {
        // Check if the PrintWriter is not null and the username is not empty
        if (out != null && username != null && !username.isEmpty()) {
            out.println(username);
            return true;
        } else {
            System.out.println("prøv igen");
            return false;

        }
    }

    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";
        int serverPort = 1992;

        try (
                Socket socket = new Socket(serverAddress, serverPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
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

            System.out.println("Skriv brugernavn");
            String username = input.nextLine();
            System.out.println("Sending username to server: " + username);
            if(sendUsernameToServer(out, username) == true)
            {
                //continue
            }
            else {
                //prøv igen
            }
            }
        }
        }
