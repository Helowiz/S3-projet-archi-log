package serveur.abonne;

import serveur.documents.IDocument;
import serveur.documents.Statuts;

import java.util.ArrayList;

public class Abonne {
    private static int cpt = 1;
    private final int numero;
    ArrayList<IDocument> documents;

    public Abonne(int numero) {
        this.numero = numero;
    }

    public Abonne() {
        this.numero = cpt++;
    }

    public int numero(){
        return numero;
    }

    public void ajouterDocument(IDocument document){
        documents.add(document);
    }

    public boolean aReserve(IDocument document) {
        return documents.contains(document) && document.statut() == Statuts.RESERVATION;
    }
}
