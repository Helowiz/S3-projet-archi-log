package serveur.mediatheque.dvd;

import serveur.mediatheque.document.Document;
import serveur.services.retour.RetourException;

import java.util.TimerTask;

public class AttenteReservation extends TimerTask {
    private Document doc;

    public AttenteReservation(Document doc) {
        this.doc = doc;
    }

    @Override
    public void run() {
        try {
            doc.retour();
        } catch (RetourException e) {
            throw new RuntimeException(e);
        }
    }
}
