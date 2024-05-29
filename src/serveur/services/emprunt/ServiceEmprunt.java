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

import static serveur.bttp2.Codage.coder;

public class ServiceEmprunt extends Service {

    public ServiceEmprunt(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        System.out.println("******** Service de emprunt " + super.getNumero() + " demarre ********");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            String fin = "##******** Déconnexion du service d'emprunt " + super.getNumero() + " ********";
            Mediatheque mediatheque = Mediatheque.getInstance();

            out.println("******** Connexion au service d'emprunt " + super.getNumero() + " ********##Saisir le numéro d'abonné : ");
            String line = in.readLine();
            if(!mediatheque.abonneExiste(Integer.parseInt(line))) {
                out.println(coder("Emprunt " + super.getNumero() + " <-- Numéro d'abonné <<" + line + ">> inexistant" + fin));
                finalize();
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
            out.println("******** Déconnexion du service d'emprunt " + super.getNumero() + " ********");
            finalize();
        } catch (Throwable e) {
            System.err.println("Problème de connexion au service d'emprunt : " + e.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("******** Service de emprunt " + super.getNumero() + " eteinction ********");
    }
}
