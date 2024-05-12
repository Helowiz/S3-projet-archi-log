package serveur.abonne;

import java.net.Socket;

public class Abonne {
    private static int cpt = 1;
    private final int numero;

    public Abonne(int numero) {
        this.numero = numero;
    }

    public Abonne() {
        this.numero = cpt++;
    }

    public int numero(){
        return numero;
    }
}
