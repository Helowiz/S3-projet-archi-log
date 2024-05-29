package serveur.services.retour;

import bserveur.Service;
import serveur.abonne.Abonne;
import serveur.abonne.AbonneException;
import serveur.documents.Document;
import serveur.documents.DocumentException;
import serveur.mediatheque.GestionBD;
import serveur.mediatheque.Mediatheque;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


import static java.lang.System.err;
import static serveur.bttp2.Codage.coder;

public class ServiceRetour extends Service {

    public ServiceRetour(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        System.out.println("******** Service de Retour " + super.getNumero() + " start ********");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            String fin = "##%******** Déconnexion du service de retour " + super.getNumero() + " ********";
            Mediatheque mediatheque = Mediatheque.getInstance();

            out.println("******** Connexion au service de retour " + super.getNumero() + " ********##Retour " + super.getNumero() + " <-- Saisir le numéro du document :");
            String line = in.readLine();

            try {
                Document document = mediatheque.getUnDocumentParNumero(Integer.parseInt(line));
                synchronized (document){
                    document.retour();
                    GestionBD.sauvegardeBD(document,null);
                    out.println(coder("Retour " + super.getNumero() + " --> Le document <<" + line + ">> est réservé" + fin));
                }
            } catch (DocumentException e) {
                out.println(coder("Retour " + super.getNumero() + " <--" + e + fin));
            } catch (RetourException e){
                out.println(coder("Retour " + super.getNumero() + " <--" + e + fin));
            }
            finalize();
        } catch (Throwable e) {
            err.println("Problème de connexion au service de retour : " + e.getMessage());
        }
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("******** Service de Retour " + super.getNumero() + " stop ********");

    }
}
