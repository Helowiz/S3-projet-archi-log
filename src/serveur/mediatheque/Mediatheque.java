package serveur.mediatheque;

import serveur.abonne.Abonne;
import serveur.documents.IDocument;

import java.util.HashMap;
import java.util.Map;

public final class Mediatheque {
    private Map<Integer, IDocument> documents;
    private Map<Integer, Abonne> abonnes;

    private static final Mediatheque _instance = new Mediatheque();

    private Mediatheque() {}

    public static Mediatheque getInstance() {
        return _instance;
    }

    public boolean documentExiste(int numero){
        return documents.get(numero) != null;
    }

    public boolean abonneExiste(int numero){
        return abonnes.get(numero) != null;
    }

    public void setDocuments(Map<Integer, IDocument> documents){
        this.documents = new HashMap<>(documents);
    }

    public void ajouterDesDocuments(Map<Integer, IDocument> documents){
        this.documents.putAll(documents);
    }

    public void ajouterUnDocument(IDocument unDocument){
        this.documents.put(unDocument.numero(), unDocument);
    }

    public IDocument getUnDocumentParNumero(int numero) {
		return documents.get(numero);
    }

    public void setAbonnes(Map<Integer, Abonne> abonnes){
        this.abonnes = new HashMap<>(abonnes);
    }

    public void ajouterDesAbonnes(Map<Integer, Abonne> abonnes){
        this.abonnes.putAll(abonnes);
    }

    public void ajouterUnAbonne(Abonne unAbonne){
        this.abonnes.put(unAbonne.numero(), unAbonne);
    }

    public Abonne getUnAbonneParNumero(int numero) {
        return abonnes.get(numero);
    }
}
