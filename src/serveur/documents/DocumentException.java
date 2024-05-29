package serveur.documents;

public class DocumentException extends Exception{

    int document;

    public DocumentException(int doc) {
        this.document = doc;
    }
    @Override
    public String toString() {
        return "%Le document <<" + document + ">> est inexistant";
    }
}
