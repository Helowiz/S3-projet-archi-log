package serveur.abonne;

import java.util.Date;

public class Abonne {
    private final int numero;
    private final String nom;
    private final String prenom;
    private final Date dateDeNaissance;

    public Abonne(int numero, String nom, String prenom, Date dateDeNaissance) {
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
    }

    public int numero(){
        return numero;
    }
}
