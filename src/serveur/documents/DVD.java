package serveur.documents;

import serveur.abonne.Abonne;
import serveur.services.emprunt.EmpruntException;
import serveur.services.reservation.ReservationException;
import serveur.services.retour.RetourException;

public class DVD implements Document {
    private final int numero;
    private final String titre;
    private final boolean adulte;
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

    public void reservation(Abonne ab) throws ReservationException {
        if(this.statut == Statuts.RESERVATION || this.statut == Statuts.EMPRUNT){
            throw new ReservationException(this.numero);
        }
        this.statut = Statuts.RESERVATION;
        this.ab = ab;
    }

    public void emprunt(Abonne ab) throws EmpruntException {
        if(!(this.statut == Statuts.DISPONIBLE || (this.statut == Statuts.RESERVATION && this.ab == ab))){
            throw new EmpruntException(this.numero);
        }
        this.statut = Statuts.EMPRUNT;
        if (this.ab == null) {
            this.ab = ab;
        }
    }

    public void retour() throws RetourException {

        if(this.statut != Statuts.EMPRUNT){
            throw new RetourException(this.numero);
        }
        this.statut = Statuts.DISPONIBLE;
        this.ab = null;
    }
}
