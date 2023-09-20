import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Client {
    private static String sessionID = "";
    private static String brugerNavn = " ";
    static Scanner input = new Scanner(System.in);

    private static String tjekBrugernavn() {

        System.out.println("Opret brugernavn");
        System.out.println("Brugernavn må ikke indholde mellemrum eller @");
        String brugernavn = input.nextLine();


        boolean containsWhiteSpace = brugernavn.contains(" ");
        boolean containsElefantNoseA = brugernavn.contains("@");

        if (containsWhiteSpace || containsElefantNoseA) {
            System.out.println("Der må ikke være mellemrum eller @ i dit brugernavn.");
            System.out.println("Prøv igen");
            brugernavn = "";
            tjekBrugernavn();
        }
        System.out.println("Du har fået brugernavn " + brugernavn);
        return brugernavn;
    }

    private static void clientMenu() {
        if (sessionID.equals("999")) {
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

    private static void chooseUserName(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Skriv dit ønskede brugernavn");
        String navn = input.nextLine();
        out.println("100" + sessionID + navn);
        System.out.println("100" + sessionID + navn);
        //System.out.println(in.readLine());

        String serverMessage = in.readLine(); //flyttet readLine ud af while()
        while (serverMessage != null) {
            if (serverMessage.substring(0, 3).equals("999")) {
                brugerNavn = navn;
                System.out.println(brugerNavn);
                break;
            }
            if (serverMessage.substring(0, 3).equals("000")) {
                System.out.println("Brugernavn er taget prøv igen");
                chooseUserName(out, in);
            }
        }

        }
        public static void main (String[]args) throws IOException {
            String serverAddress = "localhost";
            int serverPort = 1992;
            try {
                Socket socket = new Socket(serverAddress, serverPort);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Connected to server: " + serverAddress + ":" + serverPort);

                String testMessage;
                while ((testMessage = in.readLine()) != null) {
                    if (sessionID.isEmpty() && testMessage.substring(0, 3).equals("999")) {
                        sessionID = testMessage.substring(3, 7);
                        System.out.println("Received sessionID: " + sessionID);
                        break;
                    }
                }
                chooseUserName(out,in);


                Thread receiveThread = new Thread(() -> {
                    try {
                        String inputMessage;
                        while ((inputMessage = in.readLine()) != null) {
                         //skriv til gui
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                receiveThread.start();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

