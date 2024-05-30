package serveur.services.emprunt;

public class EmpruntException extends Exception {
    int numDoc;

    public EmpruntException(int numDoc) {
        this.numDoc = numDoc;
    }

    @Override
    public String toString() {
        return "Le document <<" + numDoc + ">> ne peut pas être emprunté";
    }
}
