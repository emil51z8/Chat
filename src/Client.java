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



    private static String tjekBrugernavn()
    {
        Scanner input = new Scanner(System.in);
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

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 1992;
        tjekBrugernavn();

        //tjekBrugernavn();
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

                            //tjekBrugernavn();

                            System.out.println("Received sessionID: " + sessionID);
                        } else if (sessionID.isEmpty() && serverMessage.substring(0, 3).equals("000"))
                        {
                            // Vælg nyt brugernavn
                            //tjekBrugernavn();

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