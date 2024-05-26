package serveur.abonne;

import serveur.documents.Document;
import serveur.documents.Statuts;

import java.util.ArrayList;
import java.util.Date;

public class Abonne {
    private static int cpt = 1;
    private final int numero;
    private String nom;
    private String prenom;
    private Date dateDeNaissance;
    ArrayList<Document> documents;

    public Abonne(int numero, String nom, String prenom, Date dateDeNaissance) {
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
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
