package serveur.abonne;

public class AbonneException extends Exception{
    int numAb;

    public AbonneException(int numAb) {
        this.numAb = numAb;
    }

    @Override
    public String toString() {
        return "%L'abonné(e) <<" + numAb + ">> n'existe pas";
    }
}

