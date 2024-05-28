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

import static serveur.bttp2.Codage.coder;

public class ServiceEmprunt extends Service {
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            String fin = "##******** Déconnexion du service d'emprunt " + super.getNumero() + " ********";
            Mediatheque mediatheque = Mediatheque.getInstance();
            out.println("******** Connexion au service d'emprunt " + super.getNumero() + " ********##Saisir le numéro d'abonné : ");
            String line = in.readLine();
            if(!mediatheque.abonneExiste(Integer.parseInt(line))) {
                out.println(coder("Emprunt " + super.getNumero() + " <-- Numéro d'abonné <<" + line + ">> inexistant" + fin));
                super.getSocket().close();
            } else {
                Abonne abonne = mediatheque.getUnAbonneParNumero(Integer.parseInt(line));
                out.println("Emprunt " + super.getNumero() + " <-- Saisir le numéro du document : ");
                line = in.readLine();
                if(!mediatheque.documentExiste(Integer.parseInt(line))) {
                    out.println(coder("Emprunt " + super.getNumero() + " <-- Numéro de document <<" + line + ">> inexistant" + fin));
                } else {
                    Document document = mediatheque.getUnDocumentParNumero(Integer.parseInt(line));
                    synchronized (document){
                        try {
                            document.emprunt(abonne);
                            GestionBD.sauvegardeBD(document,abonne);
                            out.println(coder("Emprunt " + super.getNumero() + " --> Le document <<" + line + ">> est emprunté" + fin));
                        } catch (EmpruntException e) {
                            out.println(coder("Emprunt " + super.getNumero() + " <-- " + e.toString() + fin));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Problème de connexion au service d'emprunt : " + e.getMessage());
        }
    }
}
