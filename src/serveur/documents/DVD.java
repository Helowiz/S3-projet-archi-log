package serveur.documents;

import serveur.abonne.Abonne;
import serveur.services.emprunt.EmpruntException;
import serveur.services.reservation.ReservationException;
import serveur.services.retour.RetourException;

public class DVD implements Document {
    private int numero;
    private String titre;
    private boolean adulte;
    private Abonne ab;
    private Statuts statut;

    public DVD(int numero, String titre, boolean adulte, Abonne ab) {
        this.numero = numero;
        this.titre = titre;
        this.adulte = adulte;
        this.ab = ab;
        this.statut = Statuts.DISPONIBLE;
    }

    public int numero() {
        return numero;
    }
    public String titre() {
        return titre;
    }

    public Statuts statut() {
        return statut;
    }

    public void reservation(Abonne ab) throws ReservationException {
        if(this.statut == Statuts.RESERVATION || this.statut == Statuts.EMPRUNT){
            throw new ReservationException();
        }
        this.statut = Statuts.RESERVATION;
        ab.ajouterDocument(this);
    }

    public void emprunt(Abonne ab) throws EmpruntException {
        if(this.statut != Statuts.DISPONIBLE || ab.aReserve(this)){
            throw new EmpruntException();
        }
        if (!ab.aReserve(this)) {
            this.statut = Statuts.EMPRUNT;
            ab.ajouterDocument(this);
        }
    }

    public void retour() throws RetourException {
        if(this.statut != Statuts.EMPRUNT){
            throw new RetourException();
        }
        this.statut = Statuts.DISPONIBLE;
    }
}
