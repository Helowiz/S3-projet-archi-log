package serveur.services.emprunt;

import bserveur.Service;
import serveur.abonne.Abonne;
import serveur.documents.IDocument;
import serveur.mediatheque.Mediatheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServiceEmprunt extends Service {
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            Mediatheque mediatheque = Mediatheque.getInstance();
            out.println("******** Connexion au service d'emprunt " + super.getNumero() + " ********##Saisir le numéro d'abonné :");
            String line = in.readLine();
            Abonne abonne = mediatheque.getUnAbonneParNumero(Integer.parseInt(line));
            if(mediatheque.abonneExiste(abonne)) {
                out.println("Emprunt " + super.getNumero() + " <-- Numéro d'abonné <<" + line + ">> inexistant");
            } else {
                out.println("Emprunt " + super.getNumero() + " <-- Saisir le numéro du document :");
                line = in.readLine();
                IDocument document = mediatheque.getUnDocumentParNumero(Integer.parseInt(line));
                if(mediatheque.documentExiste(document)) {
                    out.println("Emprunt " + super.getNumero() + " <-- Numéro de document <<" + line + ">> inexistant");
                } else {
                    try {
                        document.emprunt(abonne);
                        out.println("Emprunt " + super.getNumero() + " --> Le document <<" + line + ">> est emprunté");
                    } catch (EmpruntException e) {
                        out.println("Emprunt " + super.getNumero() + " <-- Le document <<" + line + ">> ne peut pas être emprunté");
                    }
                }
            }
            out.println("******** Déconnexion du service d'emprunt " + super.getNumero() + " ********");
            super.getSocket().close();
        } catch (IOException e) {
            System.err.println("Problème de connexion au service d'emprunt : " + e.getMessage());
        }
    }
}
