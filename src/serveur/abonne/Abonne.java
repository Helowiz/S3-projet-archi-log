package serveur.abonne;

import serveur.documents.Document;
import serveur.documents.Statuts;

import java.util.ArrayList;

public class Abonne {
    private static int cpt = 1;
    private final int numero;
    ArrayList<Document> documents;

    public Abonne(int numero) {
        this.numero = numero;
    }

    public Abonne() {
        this.numero = cpt++;
    }

    public int numero(){
        return numero;
    }

    public void ajouterDocument(Document document){
        documents.add(document);
    }

    public boolean aReserve(Document document) {
        return documents.contains(document) && document.statut() == Statuts.RESERVATION;
    }
}
