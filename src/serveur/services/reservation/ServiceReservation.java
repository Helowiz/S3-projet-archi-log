package serveur.services.reservation;

import serveur.abonne.Abonne;
import serveur.mediatheque.Mediatheque;
import serveur.documents.IDocument;
import bserveur.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServiceReservation extends Service {
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            Mediatheque mediatheque = Mediatheque.getInstance();
            out.println("******** Connexion au service de reservation " + super.getNumero() + " ********##Saisir le numéro d'abonné :");
            String line = in.readLine();
            Abonne abonne = mediatheque.getUnAbonneParNumero(Integer.parseInt(line));
            if(mediatheque.abonneExiste(abonne)) {
                out.println("Réservation " + super.getNumero() + " <-- Numéro d'abonné <<" + line + ">> inexistant");
            } else {
                out.println("Réservation " + super.getNumero() + " <-- Saisir le numéro du document :");
                line = in.readLine();
                IDocument document = mediatheque.getUnDocumentParNumero(Integer.parseInt(line));
                if(mediatheque.documentExiste(document)) {
                    out.println("Réservation " + super.getNumero() + " <-- Numéro de document <<" + line + ">> inexistant");
                } else {
                    try {
                        document.reservation(abonne);
                        out.println("Réservation " + super.getNumero() + " --> Le document <<" + line + ">> est réservé");
                    } catch (ReservationException | InterruptedException e) {
                        out.println("Réservation " + super.getNumero() + " <-- Le document <<" + line + ">> ne peut pas être réservé");
                    }
                }
            }
            out.println("******** Déconnexion du service de reservation " + super.getNumero() + " ********");
            super.getSocket().close();
        } catch (IOException e) {
            System.err.println("Problème de connexion au service de réservation : " + e.getMessage());
        }
    }
}
