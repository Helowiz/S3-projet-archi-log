package serveur.services.retour;

public class RetourException extends Exception {
    String document;

    public RetourException(String doc) {
        this.document = doc;
    }
    @Override
    public String toString() {
        return "Le document <<" + document + ">> ne peut pas être retourné";
    }
}