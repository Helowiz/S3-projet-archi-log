package serveur;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import bserveur.Serveur;
import serveur.abonne.Abonne;
import serveur.documents.DVD;
import serveur.documents.Document;
import serveur.mediatheque.GestionBD;
import serveur.mediatheque.Mediatheque;
import serveur.services.emprunt.ServiceEmprunt;
import serveur.services.reservation.ServiceReservation;
import serveur.services.retour.ServiceRetour;

public class AppliServeur {
    private final static int PORT_RESERVATION = 3000;
    private final static int PORT_EMPRUNT = 4000;
    private final static int PORT_RETOUR = 5000;

    public static void main(String[] args) {
        initMediatheque();
        try {
            new Thread(new Serveur(ServiceEmprunt.class, PORT_EMPRUNT)).start();
            new Thread(new Serveur(ServiceReservation.class, PORT_RESERVATION)).start();
            new Thread(new Serveur(ServiceRetour.class, PORT_RETOUR)).start();
        } catch (IOException | NoSuchMethodException e) {
            System.err.println("Problème lors de la création du serveur : " +  e);
        }
    }

    private static void initMediatheque() {
        GestionBD.connexionBD();
        /*
        Document dvd1 = new DVD(1, "La Cite de la peur");
        Document dvd2 = new DVD(2, "Titanic");
        Document dvd3 = new DVD(3, "Blanche-Neige et les septs nains");

        Abonne ab1 = new Abonne(1);
        Abonne ab2 = new Abonne(2);
        Abonne ab3 = new Abonne(3);

        Map<Integer, Document> documents = new HashMap<>();
        documents.put(1, dvd1);
        documents.put(2, dvd2);
        documents.put(3, dvd3);
        Mediatheque.getInstance().ajouterDesDocuments(documents);

        Map<Integer, Abonne> abonnes = new HashMap<>();
        abonnes.put(1, ab1);
        abonnes.put(2, ab2);
        abonnes.put(3, ab3);
        Mediatheque.getInstance().ajouterDesAbonnes(abonnes);
        */
    }
}
