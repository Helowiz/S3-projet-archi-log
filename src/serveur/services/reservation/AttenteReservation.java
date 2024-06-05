package serveur.services.reservation;

import serveur.documents.Document;
import serveur.services.retour.RetourException;

import java.util.TimerTask;

public class AttenteReservation extends TimerTask {
    private Document doc;

    public AttenteReservation(Document doc) {
        this.doc = doc;
    }

    @Override
    public void run() {
        System.out.println("tâche");
        try {
            doc.retour();
        } catch (RetourException e) {
            throw new RuntimeException(e);
        }
    }
}
