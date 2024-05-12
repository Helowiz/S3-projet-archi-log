package serveur.documents;

import serveur.abonne.Abonne;
import serveur.services.emprunt.EmpruntException;
import serveur.services.reservation.ReservationException;
import serveur.services.retour.RetourException;

public class DVD implements IDocument {

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
        assert(this.statut == Statuts.RESERVATION || this.statut == Statuts.EMPRUNT);
        this.statut = Statuts.RESERVATION;
        ab.ajouterDocument(this);
    }

    public void emprunt(Abonne ab) throws EmpruntException {
        assert(this.statut != Statuts.DISPONIBLE || ab.aReserve(this));
        if (!ab.aReserve(this)) {
            this.statut = Statuts.EMPRUNT;
            ab.ajouterDocument(this);
        }
    }

    public void retour() throws RetourException {
        assert(this.statut != Statuts.EMPRUNT);
        this.statut = Statuts.RETOUR;
    }
}
