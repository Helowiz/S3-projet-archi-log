package serveur.services.reservation;

public class ReservationException extends Exception {

    int document;

    public ReservationException(int doc) {
        this.document = doc;
    }
    @Override
    public String toString() {
        return "Le document <<" + document + ">> ne peut pas être réservé";
    }
}
