package serveur.document;

import serveur.abonne.Abonne;
import serveur.services.emprunt.EmpruntException;
import serveur.services.reservation.ReservationException;
import serveur.services.retour.RetourException;

public interface IDocument {
    int numero();

    /* pre ni réservé ni emprunté */
    void reservation(Abonne ab) throws ReservationException;

    /* libre ou réservé par l’abonné qui vient emprunter */
    void emprunt(Abonne ab) throws EmpruntException;

    /* retour d’un document ou annulation d‘une réservation */
    void retour()throws RetourException;
}