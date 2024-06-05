package serveur.abonne;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.*;
import static java.util.Calendar.DATE;

public class Abonne {
    private final int numero;
    private final String nom;
    private final String prenom;
    private final Date dateDeNaissance;

    public Abonne(int numero, String nom, String prenom, Date dateDeNaissance) {
        this.numero = numero;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
    }

    public int numero(){
        return numero;
    }

    public boolean estAdult(){
        Calendar a = getCalendar(this.dateDeNaissance);
        Calendar b = getCalendar(new Date());
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff >= 18;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        cal.setTime(date);
        return cal;
    }
}
