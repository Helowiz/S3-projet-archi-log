package serveur.services.retour;

public class RetourException extends Exception {
    int numDoc;

    public RetourException(int numDoc) {
        this.numDoc = numDoc;
    }

    @Override
    public String toString() {
        return "Le document <<" + numDoc + ">> ne peut pas être retourné";
    }
}