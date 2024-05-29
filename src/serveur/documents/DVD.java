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

    public DVD(int numero, String titre, boolean adulte, Abonne ab, Statuts statut) {
        this.numero = numero;
        this.titre = titre;
        this.adulte = adulte;
        this.ab = ab;
        this.statut = statut;
    }

    public int numero() {
        return numero;
    }

    public void reservation(Abonne ab) throws ReservationException {
        if(this.statut == Statuts.RESERVATION || this.statut == Statuts.EMPRUNT){
            throw new ReservationException(this.numero);
        }

        synchronized (this){

            this.statut = Statuts.RESERVATION;
            this.ab = ab;

            while(this.statut == Statuts.RESERVATION){
                try {
                    System.out.println("début de l'attente");
                    this.wait(100000); //10sec pour l'instant
                    System.out.println("fin de l'attente");
                } catch (InterruptedException _) {}
                try {
                    this.retour();
                    throw new ReservationException(this.numero);
                } catch (RetourException e) {}
            }
        }
    }

    public void emprunt(Abonne ab) throws EmpruntException {

        if(this.statut == Statuts.RESERVATION && this.ab == ab || this.statut == Statuts.DISPONIBLE){
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
        if(this.statut == Statuts.EMPRUNT){
            throw new RetourException(this.numero);
        }
        synchronized (this){
            this.statut = Statuts.DISPONIBLE;
            this.ab = null;
            this.notifyAll();
        }
    }
}
