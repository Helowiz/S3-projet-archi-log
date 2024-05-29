package serveur.services.retour;

public class RetourException extends Exception {
    int document;

    public RetourException(int doc) {
        this.document = doc;
    }
    @Override
    public String toString() {
        return "Le document <<" + document + ">> ne peut pas être retourné";
    }
}