package client;

import serveur.abonne.AbonneException;
import serveur.documents.DocumentException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
        
        while (!line.trim().equalsIgnoreCase("exit")){
            int port = 0;
            int cmp = 0;
            while (port == 0) {
                Scanner sc = new Scanner(System.in);
                int numeroPort = 0;
                System.out.println("Saisir le numéro correspondant au service");
                System.out.print("Les différents services disponibles :\n" +
                        "Réservation : 1\n" +
                        "Emprunt : 2\n" +
                        "Retour : 3\n");
                System.out.print("-> ");
                try{
                    numeroPort = sc.nextInt();
                }
                catch(Exception e){
                    System.out.println("La valeur rentrée n'est pas du type voulu");
                    continue;
                }
                switch (numeroPort) {
                    case 1:
                        port = PORT_SERVICE_RESERVATION;
                        break;
                    case 2:
                        port = PORT_SERVICE_EMPRUNT;
                        break;
                    case 3:
                        port = PORT_SERVICE_RETOUR;
                        cmp = 1;
                        break;
                    default:
                        System.err.println("Le numéro ne correspond à aucun service");
                }
            }

            try {
                socket = new Socket(HOST, port);
                BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter sout = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("******** Connexion au serveur " + socket.getInetAddress() + ":" + socket.getPort() + " ********");

                while (cmp < 3 && !(line = decoder(sin.readLine())).trim().equalsIgnoreCase("exit")) {

                    if (line.contains("%")) {
                        line = line.replace("%", "");
                        System.out.println(line);
                        break;
                    }

                    System.out.println(line);
                    System.out.print("-> ");
                    line = coder(clavier.readLine());
                    sout.println(line);
                    ++cmp;
                }
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
