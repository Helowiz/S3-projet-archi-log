package serveur.services.emprunt;

import bserveur.Service;
import serveur.abonne.Abonne;
import serveur.documents.Document;
import serveur.mediatheque.GestionBD;
import serveur.mediatheque.Mediatheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServiceEmprunt extends Service {

    public ServiceEmprunt(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            Mediatheque mediatheque = Mediatheque.getInstance();
            out.println("******** Connexion au service d'emprunt " + super.getNumero() + " ********##Saisir le numéro d'abonné :");
            String line = in.readLine();
            if(!mediatheque.abonneExiste(Integer.parseInt(line))) {
                out.println("Emprunt " + super.getNumero() + " <-- Numéro d'abonné <<" + line + ">> inexistant");
            } else {
                Abonne abonne = mediatheque.getUnAbonneParNumero(Integer.parseInt(line));
                out.println("Emprunt " + super.getNumero() + " <-- Saisir le numéro du document :");
                line = in.readLine();
                if(!mediatheque.documentExiste(Integer.parseInt(line))) {
                    out.println("Emprunt " + super.getNumero() + " <-- Numéro de document <<" + line + ">> inexistant");
                } else {
                    Document document = mediatheque.getUnDocumentParNumero(Integer.parseInt(line));
                    synchronized (document){
                        try {
                            document.emprunt(abonne);
                            GestionBD.sauvegardeBD(document,abonne);
                            out.println("Emprunt " + super.getNumero() + " --> Le document <<" + line + ">> est emprunté");
                        } catch (EmpruntException e) {
                            out.println("Emprunt " + super.getNumero() + " <-- Le document <<" + line + ">> ne peut pas être emprunté");
                        }
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
