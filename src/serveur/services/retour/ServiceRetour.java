package serveur.services.retour;

import bserveur.Service;
import serveur.abonne.Abonne;
import serveur.documents.IDocument;
import serveur.mediatheque.Mediatheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServiceRetour extends Service {
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            Mediatheque mediatheque = Mediatheque.getInstance();
            out.println("******** Connexion au service de retour " + super.getNumero() + " ********##Saisir le numéro du document :");
            String line = in.readLine();
            if(!mediatheque.documentExiste(Integer.parseInt(line))) {
                out.println("Retour " + super.getNumero() + " <-- Numéro de document <<" + line + ">> inexistant");
            } else {
                IDocument document = mediatheque.getUnDocumentParNumero(Integer.parseInt(line));
                synchronized (document){
                    try {
                        document.retour();
                        out.println("Retour " + super.getNumero() + " --> Le document <<" + line + ">> est retourné");
                    } catch (RetourException e) {
                        out.println("Retour " + super.getNumero() + " <-- Le document <<" + line + ">> ne peut pas être retourné");
                    }
                }
            }
            out.println("******** Déconnexion du service de retour " + super.getNumero() + " ********");
            super.getSocket().close();
        } catch (IOException e) {
            System.err.println("Problème de connexion au service de retour : " + e.getMessage());
        }
    }
}
