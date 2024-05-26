package serveur.documents;

import serveur.abonne.Abonne;
import serveur.services.emprunt.EmpruntException;
import serveur.services.reservation.ReservationException;
import serveur.services.retour.RetourException;

public class DVD implements Document {

    int id;
    String label;
    Statuts statut;

    public DVD(int id, String label) {
        this.id = id;
        this.label = label;
        this.statut = Statuts.DISPONIBLE;
    }

    public int numero() {
        return id;
    }
    public String titre() {
        return label;
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
