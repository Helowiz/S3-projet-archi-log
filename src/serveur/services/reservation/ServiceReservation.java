package serveur.services.reservation;

import bserveur.Service;
import serveur.abonne.Abonne;
import serveur.mediatheque.GestionBD;
import serveur.mediatheque.Mediatheque;
import serveur.documents.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static serveur.bttp2.Codage.coder;

public class ServiceReservation extends Service {

    public ServiceReservation(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        System.out.println("******** Service de reservation " + super.getNumero() + " demarre ********");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            String fin = "##******** Déconnexion du service de reservation " + super.getNumero() + " ********";
            Mediatheque mediatheque = Mediatheque.getInstance();
            out.println(coder("******** Connexion au service de reservation " + super.getNumero() + " ********##Saisir le numéro d'abonné : "));
            String line = in.readLine();
            if(!mediatheque.abonneExiste(Integer.parseInt(line))) {
                out.println("Réservation " + super.getNumero() + " <-- Numéro d'abonné <<" + line + ">> inexistant" + fin);
                super.getSocket().close();
            } else {
                Abonne abonne = mediatheque.getUnAbonneParNumero(Integer.parseInt(line));
                out.println("Réservation " + super.getNumero() + " <-- Saisir le numéro du document :");
                line = in.readLine();
                if(!mediatheque.documentExiste(Integer.parseInt(line))) {
                    out.println("Réservation " + super.getNumero() + " <-- Numéro de document <<" + line + ">> inexistant" + fin);
                } else {
                    Document document = mediatheque.getUnDocumentParNumero(Integer.parseInt(line));
                    synchronized (document){
                        try {
                            document.reservation(abonne);
                            GestionBD.sauvegardeBD(document,abonne);
                            out.println("Réservation " + super.getNumero() + " --> Le document <<" + line + ">> est réservé" + fin);
                        } catch (ReservationException | InterruptedException e) {
                            out.println("Réservation " + super.getNumero() + " <-- Le document <<" + line + ">> ne peut pas être réservé" + fin);
                        }
                    }
                }
            }
            super.getSocket().close();
        } catch (IOException e) {
            System.err.println("Problème de connexion au service de réservation : " + e.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.socket.close();
    }
}
