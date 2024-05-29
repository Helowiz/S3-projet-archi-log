package serveur.mediatheque;

import serveur.abonne.Abonne;
import serveur.abonne.AbonneException;
import serveur.documents.Document;
import serveur.documents.DocumentException;

import java.util.HashMap;
import java.util.Map;

public final class Mediatheque {
    private Map<Integer, Document> documents;
    private Map<Integer, Abonne> abonnes;

    private static final Mediatheque _instance = new Mediatheque();

    private Mediatheque() {
        documents = new HashMap<>();
        abonnes = new HashMap<>();
    }

    public static Mediatheque getInstance() {
        return _instance;
    }

    public void setDocuments(Map<Integer, Document> documents){
        this.documents = new HashMap<>(documents);
    }

    public Document getUnDocumentParNumero(int numero) throws DocumentException {
        if (documents.get(numero) == null){
            throw new DocumentException(numero);
        } else {
            return documents.get(numero);
        }
    }

    public void setAbonnes(Map<Integer, Abonne> abonnes){
        this.abonnes = new HashMap<>(abonnes);
    }

    public Abonne getUnAbonneParNumero(int numero) throws AbonneException {
        if (abonnes.get(numero) == null){
            throw new AbonneException(numero);
        } else {
            return abonnes.get(numero);
        }
    }
    public boolean documentExiste(int numero){
        return documents.get(numero) != null;
    }

    public boolean abonneExiste(int numero){
        return abonnes.get(numero) != null;
    }
}
