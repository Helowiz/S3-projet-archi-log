package serveur.services.emprunt;

public class EmpruntException extends Exception {

    String document;

    public EmpruntException(String doc) {
        this.document = doc;
    }
    @Override
    public String toString() {
        return "Le document <<" + document + ">> ne peut pas être emprunté";
    }
}
