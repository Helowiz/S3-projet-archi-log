package serveur.documents;

import serveur.abonne.Abonne;
import serveur.services.emprunt.EmpruntException;
import serveur.services.reservation.AttenteReservation;
import serveur.services.reservation.ReservationException;
import serveur.services.retour.RetourException;

import java.util.Timer;

public class DVD implements Document {
    private final int numero;
    private final String titre;
    private final boolean adulte;
    private Abonne ab;
    private Statuts statut;
    private Timer attente;
    private long tempsAttente = 10000; // 109800000 1H30

    public DVD(int numero, String titre, boolean adulte, Abonne ab, Statuts statut) {
        this.numero = numero;
        this.titre = titre;
        this.adulte = adulte;
        this.ab = ab;
        this.statut = statut;
    }

    public void reservation(Abonne ab) throws ReservationException {
        if((this.adulte && !ab.estAdult()) || this.statut != Statuts.DISPONIBLE){
            throw new ReservationException(this.numero);
        }
        synchronized (this) {
            this.statut = Statuts.RESERVATION;
            this.ab = ab;
        }
        this.attente = new Timer();
        this.attente.schedule(new AttenteReservation(this), this.tempsAttente);
    }

    public void emprunt(Abonne ab) throws EmpruntException {
        this.attente.cancel();
        if((this.statut == Statuts.RESERVATION && this.ab == ab || this.statut == Statuts.DISPONIBLE) || (this.adulte && !ab.estAdult())){
            synchronized (this){
                this.statut = Statuts.EMPRUNT;
                if (this.ab == null) {
                    this.ab = ab;
                }
                this.notifyAll();
            }
        } else {
            throw new EmpruntException(this.numero);
        }
    }

    public void retour() throws RetourException {
        if(this.statut == Statuts.DISPONIBLE){
            throw new RetourException(this.numero);
        }
        synchronized (this){
            this.statut = Statuts.DISPONIBLE;
            this.ab = null;
            this.notifyAll();
        }
    }

    @Override
    public int numero() {
        return numero;
    }
}
