package serveur.services.emprunt;

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

public class ServiceEmprunt extends Service {

    public ServiceEmprunt(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        System.out.println("******** Service de Emprunt " + super.getNumero() + " start ********");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            String fin = "##%******** Déconnexion du service d'emprunt " + super.getNumero() + " ********";
            Mediatheque mediatheque = Mediatheque.getInstance();

            out.println("******** Connexion au service d'emprunt " + super.getNumero() + " ********##Saisir le numéro d'abonné : ");
            String line = in.readLine();

            try {
                Abonne abonne = mediatheque.getUnAbonneParNumero(Integer.parseInt(line));
                out.println("Emprunt " + super.getNumero() + " <-- Saisir le numéro du document :");
                line = in.readLine();
                try {
                    Document document = mediatheque.getUnDocumentParNumero(Integer.parseInt(line));
                    synchronized (document){
                        document.emprunt(abonne);
                        GestionBD.sauvegardeBD(document,abonne);
                        out.println(coder("Réservation " + super.getNumero() + " --> Le document <<" + line + ">> est réservé" + fin));
                    }
                } catch (DocumentException e) {
                    out.println(coder("Emprunt " + super.getNumero() + " <--" + e + fin));
                }
            } catch (AbonneException e){
                out.println(coder("Emprunt " + super.getNumero() + " <--" + e + fin));
            } catch (EmpruntException e) {
                out.println("Emprunt " + super.getNumero() + " <--" + e + fin);
            }
            finalize();
        } catch (Throwable e){
            err.println("Problème de connexion au service d'emprunt (throwable) : " + e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("******** Service de Emprunt " + super.getNumero() + " stop ********");
    }
}
