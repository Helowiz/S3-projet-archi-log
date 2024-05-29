package serveur.abonne;

public class AbonneException extends Exception{

    int abonne;

    public AbonneException(int ab) {
        this.abonne = ab;
    }

    @Override
    public String toString() {
        return "%L'abonné(e) <<" + abonne + ">> n'existe pas";
    }
}

