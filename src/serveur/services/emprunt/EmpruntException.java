package serveur.services.emprunt;

public class EmpruntException extends Exception {

    int document;

    public EmpruntException(int doc) {
        this.document = doc;
    }
    @Override
    public String toString() {
        return "Le document <<" + document + ">> ne peut pas être emprunté";
    }
}
