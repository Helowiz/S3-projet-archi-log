package serveur.services.retour;

import bserveur.Service;
import serveur.documents.Document;
import serveur.mediatheque.GestionBD;
import serveur.mediatheque.Mediatheque;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import static serveur.bttp2.Codage.coder;

public class ServiceRetour extends Service {

    public ServiceRetour(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        System.out.println("******** Service de retour " + super.getNumero() + " demarre ********");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            String fin = "##******** Déconnexion du service de retour " + super.getNumero() + " ********";
            Mediatheque mediatheque = Mediatheque.getInstance();
            out.println("******** Connexion au service de retour " + super.getNumero() + " ********##Saisir le numéro du document :");
            String line = in.readLine();
            if(!mediatheque.documentExiste(Integer.parseInt(line))) {
                out.println(coder("Retour " + super.getNumero() + " <-- Numéro de document <<" + line + ">> inexistant" + fin));
                super.getSocket().close();
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
            out.println("******** Déconnexion du service de retour " + super.getNumero() + " ********");
            super.getSocket().close();
        } catch (IOException e) {
            System.err.println("Problème de connexion au service de retour : " + e.getMessage());
        }
    }
    @Override
    protected void finalize() {
        try {
            super.getSocket().close();
            System.out.println("******** Service de retour " + super.getNumero() + " eteinction ********");
        } catch (IOException e) {}
    }
}
