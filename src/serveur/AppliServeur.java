package serveur;

import java.io.IOException;

import bserveur.Serveur;
import serveur.mediatheque.GestionBD;
import serveur.services.emprunt.ServiceEmprunt;
import serveur.services.reservation.ServiceReservation;
import serveur.services.retour.ServiceRetour;

public class AppliServeur {
    private final static int PORT_RESERVATION = 3000;
    private final static int PORT_EMPRUNT = 4000;
    private final static int PORT_RETOUR = 5000;

    public static void main(String[] args) {
        GestionBD.connexionBD();
        try {
            new Thread(new Serveur(ServiceReservation.class, PORT_RESERVATION)).start();
            new Thread(new Serveur(ServiceEmprunt.class, PORT_EMPRUNT)).start();
            new Thread(new Serveur(ServiceRetour.class, PORT_RETOUR)).start();
        } catch (IOException e) {
            System.err.println("Problème lors de la création du serveur : " +  e);
        }
    }
}
