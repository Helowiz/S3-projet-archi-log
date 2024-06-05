package serveur.services.reservation;

import bserveur.Service;
import serveur.abonne.Abonne;
import serveur.abonne.AbonneException;
import serveur.mediatheque.document.DocumentException;
import serveur.mediatheque.Mediatheque;
import serveur.mediatheque.document.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.err;
import static java.lang.System.out;
import static serveur.bttp2.Codage.coder;

public class ServiceReservation extends Service {

    final String fin = "##%******** Déconnexion du service de réservation " + super.getNumero() + " ********";

    public ServiceReservation(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        out.println("******** Lancement du service de réservation " + super.getNumero() + " ********");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            Mediatheque mediatheque = Mediatheque.getInstance();
            out.println(coder("******** Connexion au service de réservation " + super.getNumero() + " ********##Saisir le numéro d'abonné : "));
            String line = in.readLine();

            try {
                Abonne abonne = mediatheque.getUnAbonneParNumero(StringToInt(line, out));
                out.println("Réservation " + super.getNumero() + " <-- Saisir le numéro du document :");
                line = in.readLine();
                try {
                    Document document = mediatheque.getUnDocumentParNumero(StringToInt(line, out));
                    synchronized (document){
                        try {
                            document.reservation(abonne);
                            out.println(coder("Réservation " + super.getNumero() + " --> Le document <<" + line + ">> est réservé" + fin));
                        } catch (ReservationException e) {
                            out.println(coder("Réservation " + super.getNumero() + " <-- " + e.toString() + fin));
                        }
                    }
                } catch (DocumentException e) {
                    out.println("Réservation " + super.getNumero() + " <-- " + e.toString() + fin);
                } catch (NumberFormatException e){
                    out.println(e.getMessage() + fin);
                }

            } catch (AbonneException e){
                out.println("Réservation " + super.getNumero() + " <-- " + e.toString() + fin);
            }
            finalize();
        } catch (Throwable e) {
            err.println("Problème de connexion au service de réservation : " + e.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        out.println("******** Arrêt du service de réservation " + super.getNumero() + " ********");
    }
    private int StringToInt(String line, PrintWriter out) {
        int numero = 0;
        try {
            numero = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            out.println("Réservation " + super.getNumero() + " <-- " + e.getMessage() + fin);
        }
        return numero;
    }
}
