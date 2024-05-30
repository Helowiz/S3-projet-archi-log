package serveur.documents;

public class DocumentException extends Exception{
    int numDoc;

    public DocumentException(int numDoc) {
        this.numDoc = numDoc;
    }

    @Override
    public String toString() {
        return "%Le document <<" + numDoc + ">> n'existe pas";
    }
}
