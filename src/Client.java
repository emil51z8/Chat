import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

import java.util.Scanner;

public class Client {
    private static String sessionID = "";
    private static String brugerNavn = " ";
    static Scanner input = new Scanner(System.in);

    private static String tjekBrugerInput() {

        System.out.println("Opret brugernavn");
        System.out.println("Brugernavn må ikke indholde mellemrum eller @");
        String brugernavnInput = input.nextLine();
        return brugernavnInput;
    }

    private static String tjekLovligtBrugernavn(String brugernavn) {

            while (brugernavn.contains(" ") || brugernavn.contains("@")) {
                System.out.println("Der må ikke være mellemrum eller @ i dit brugernavn.");
                System.out.println("Prøv igen");
                brugernavn = tjekBrugerInput();
            }
            return brugernavn;
        }

    private static boolean sendUsernameToServer(PrintWriter out, String username) {
        if (out != null && username != null && !username.isEmpty()) {
            out.println(username);
            return true;
        } else {
            System.out.println("prøv igen");
            return false;

        }
    }

    private static void chooseUserName(PrintWriter out, BufferedReader in) throws IOException {
        //Gui beder om indtastning
        String navn = tjekLovligtBrugernavn(tjekBrugerInput());
        //sender brugernavn til server
        out.println("100" + sessionID + navn);
        System.out.println("100" + sessionID + navn);
        //venter på svar fra severen
        String serverMessage = in.readLine();
        while (serverMessage != null) {
            //hvis brugernavnet er accepteret
            if (serverMessage.substring(0, 3).equals("999")) {
                brugerNavn = navn;
                break;
            }
            //Hvis brugernavnet ikke er ledigt
            if (serverMessage.substring(0, 3).equals("000")) {
                System.out.println("Brugernavn er taget prøv igen");
                serverMessage = null;
                //metoden kaldes igen for at vælge nyt brugernavn
                chooseUserName(out, in);
            }
        }

    }
        public static void sendMessage(PrintWriter out) {
            String message = input.nextLine();
            if(message.equalsIgnoreCase("exit")){
                out.println("400"+sessionID);
            }
            else if(message.equalsIgnoreCase("!Brugere")){
                out.println(500 + sessionID);
            }
            else{
                if(message.length() == 0)
                {
                    message = null;
                    sendMessage(out);
                }
                if(message.charAt(0) == '@'){
                    out.println("300"+sessionID+message.substring(1));
                    message = null;
                    sendMessage(out);
                }
                if(message != null && !message.trim().isEmpty()){
                    out.println("200" + sessionID + message);
                    message = null;
                    sendMessage(out);
                }
            }
        }




        public static void main (String[]args) throws IOException {

            String serverAddress = "10.200.130.36";
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
                            System.out.println(inputMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                receiveThread.start();
                sendMessage(out);
                receiveThread.stop();



            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

