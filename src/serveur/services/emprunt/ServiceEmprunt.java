package serveur.services.emprunt;

import bserveur.Service;
import serveur.abonne.Abonne;
import serveur.abonne.AbonneException;
import serveur.mediatheque.document.Document;
import serveur.mediatheque.document.DocumentException;
import serveur.mediatheque.GestionBD;
import serveur.mediatheque.Mediatheque;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.err;
import static serveur.bttp2.Codage.coder;

public class ServiceEmprunt extends Service {

    final String fin = "##%******** Déconnexion du service d'emprunt " + super.numero + " ********";

    public ServiceEmprunt(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        System.out.println("******** Lancement du service d'emprunt " + super.getNumero() + " ********");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            Mediatheque mediatheque = Mediatheque.getInstance();

            out.println("******** Connexion au service d'emprunt " + super.getNumero() + " ********##Saisir le numéro d'abonné : ");
            String line = in.readLine();

            try {
                Abonne abonne = mediatheque.getUnAbonneParNumero(StringToInt(line, out));
                out.println("Emprunt " + super.getNumero() + " <-- Saisir le numéro du document :");
                line = in.readLine();
                try {
                    Document document = mediatheque.getUnDocumentParNumero(StringToInt(line, out));
                    synchronized (document){
                        document.emprunt(abonne);
                        GestionBD.sauvegardeBD(document,abonne);
                        out.println(coder("Emprunt " + super.getNumero() + " --> Le document <<" + line + ">> est emprunté" + fin));
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
        System.out.println("******** Arrêt du service d'emprunt " + super.getNumero() + " ********");
    }
}
