package serveur.services.reservation;

public class ReservationException extends Exception {

    String document;

    public ReservationException(String doc) {
        this.document = doc;
    }
    @Override
    public String toString() {
        return "Le document <<" + document + ">> ne peut pas être réservé";
    }
}
