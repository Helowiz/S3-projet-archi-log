package serveur.mediatheque.dvd;

import serveur.abonne.Abonne;
import serveur.mediatheque.document.Document;
import serveur.mediatheque.document.Statuts;
import serveur.services.emprunt.EmpruntException;
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
    private long tempsAttente =  109800000; // 1H30

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
        if (this.attente != null){
            this.attente.cancel();
        }
        if (this.adulte && !ab.estAdult()) {throw new EmpruntException(this.numero);}
        if((this.statut == Statuts.RESERVATION && this.ab == ab) || this.statut == Statuts.DISPONIBLE){
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
