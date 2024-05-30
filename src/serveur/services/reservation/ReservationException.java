package serveur.services.reservation;

public class ReservationException extends Exception {
    int numDoc;

    public ReservationException(int numDoc) {
        this.numDoc = numDoc;
    }

    @Override
    public String toString() {
        return "Le document <<" + numDoc + ">> ne peut pas être réservé";
    }
}
