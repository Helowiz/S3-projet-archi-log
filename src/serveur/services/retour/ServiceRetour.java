package serveur.services.retour;

import bserveur.Service;
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

public class ServiceRetour extends Service {

    public ServiceRetour(Socket socket) {
        super(socket);
    }

    final String fin = "##%******** Déconnexion du service de retour " + super.getNumero() + " ********";

    @Override
    public void run() {
        System.out.println("******** Lancement du service de retour " + super.getNumero() + " ********");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(super.getSocket().getInputStream()));
            PrintWriter out = new PrintWriter(super.getSocket().getOutputStream(), true);
            Mediatheque mediatheque = Mediatheque.getInstance();

            out.println("******** Connexion au service de retour " + super.getNumero() + " ********##Retour " + super.getNumero() + " <-- Saisir le numéro du document :");
            String line = in.readLine();

            try {
                Document document = mediatheque.getUnDocumentParNumero(StringToInt(line,out));
                synchronized (document){
                    document.retour();
                    GestionBD.sauvegardeBD(document,null);
                    out.println(coder("Retour " + super.getNumero() + " --> Le document <<" + line + ">> est retourné" + fin));
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
        System.out.println("******** Arrêt du service de retour " + super.getNumero() + " ********");

    }

    private int StringToInt(String line, PrintWriter out) {
        int numero = 0;
        try {
            numero = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            out.println("Retour " + super.getNumero() + " <-- " + e.getMessage() + fin);
        }
        return numero;
    }
}
