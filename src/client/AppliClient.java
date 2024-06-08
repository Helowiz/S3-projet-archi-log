package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static serveur.bttp2.Codage.coder;
import static serveur.bttp2.Codage.decoder;

public class AppliClient {
    private static String HOST = "localhost";

    public static void main(String[] args) throws IOException {

        String line = "";
        Socket socket = null;
        int port = Integer.parseInt(args[0]);

        while (true){

            int cmp = 0;

            try {
                socket = new Socket(HOST, port);
                BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter sout = new PrintWriter(socket.getOutputStream(),true);
                BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("******** Connexion au serveur " + socket.getInetAddress() + ":" + socket.getPort() + " ********");

                while (true) {
                    line = decoder(sin.readLine());
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
                System.err.println("Fin du service " + e);
                break;
            }
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
