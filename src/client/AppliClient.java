package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

import static serveur.bttp2.Codage.coder;
import static serveur.bttp2.Codage.decoder;

public class AppliClient {
    private static int PORT_SERVICE_RESERVATION = 3000;
    private static int PORT_SERVICE_EMPRUNT = 4000;
    private static int PORT_SERVICE_RETOUR = 5000;
    private static String HOST = "localhost";

    public static void main(String[] args) throws IOException {

        String line = "";
        Socket socket = null;
        while (!line.equals("exit")){
            int port = 0;
            while (port == 0) {
                Scanner sc = new Scanner(System.in);
                System.out.println("Tapez le numéro correspondant au service");
                System.out.print("Les différents services disponibles :\n" +
                        "Réservation : 1\n" +
                        "Emprunt : 2\n" +
                        "Retour : 3\n");
                System.out.print("-> ");
                switch (sc.nextInt()) {
                    case 1:
                        port = PORT_SERVICE_RESERVATION;
                        break;
                    case 2:
                        port = PORT_SERVICE_EMPRUNT;
                        break;
                    case 3:
                        port = PORT_SERVICE_RETOUR;
                        break;
                    default:
                        System.err.println("Ce n'est pas un caractère correspondant à un service");
                }
            }

            try {
                socket = new Socket(HOST, port);
                System.out.println("connecté au port :" + port);
                BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter sout = new PrintWriter(socket.getOutputStream(), true);
                // Informe l'utilisateur de la connection
                BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("******** Connexion au serveur " + socket.getInetAddress() + ":" + socket.getPort() + " ********");


                int cmp = 0;
                while (cmp < 2) { //!line.equals("exit")
                    line = decoder(sin.readLine()); //viens du serveur
                    System.out.println(line);

                    System.out.print("-> ");
                    line = coder(clavier.readLine()); //viens de l'user
                    sout.println(line);
                    ++cmp;
                }
                line = decoder(sin.readLine()); //viens du serveur
                System.out.println(line);
            } catch (IOException e) {
                System.err.println("Fin du service" + e);
            }
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
