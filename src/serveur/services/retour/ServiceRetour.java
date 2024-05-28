package serveur.services.retour;

import bserveur.Service;
import serveur.documents.Document;
import serveur.mediatheque.GestionBD;
import serveur.mediatheque.Mediatheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import static serveur.bttp2.Codage.coder;

public class ServiceRetour extends Service {
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            String fin = "******** Déconnexion du service de retour " + super.getNumero() + " ********";
            Mediatheque mediatheque = Mediatheque.getInstance();
            out.println("******** Connexion au service de retour " + super.getNumero() + " ********##Saisir le numéro du document :");
            String line = in.readLine();
            if(!mediatheque.documentExiste(Integer.parseInt(line))) {
                out.println(coder("Retour " + super.getNumero() + " <-- Numéro de document <<" + line + ">> inexistant" + fin));
            } else {
                Document document = mediatheque.getUnDocumentParNumero(Integer.parseInt(line));
                synchronized (document){
                    try {
                        document.retour();
                        GestionBD.sauvegardeBD(document,null);
                        out.println(coder("Retour " + super.getNumero() + " --> Le document <<" + line + ">> est retourné" + fin));
                    } catch (RetourException e) {
                        out.println(coder("Retour " + super.getNumero() + " <-- " + e.toString() + fin));
                    }
                }
            }
            super.getSocket().close();
        } catch (IOException e) {
            System.err.println("Problème de connexion au service de retour : " + e.getMessage());
        }
    }
}
