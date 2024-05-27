package serveur.mediatheque;

import serveur.abonne.Abonne;
import serveur.documents.DVD;
import serveur.documents.Document;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public final class GestionBD {

    public static void connexionBD(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        try {
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/mediatheque", "root", "mariadb");

            Statement reqAb = connect.createStatement();
            ResultSet resAb = reqAb.executeQuery("SELECT * FROM Abonne");
            Abonne ab;
            Map<Integer, Abonne> abonnes = new HashMap<>();
            while(resAb.next()){
                ab = new Abonne(
                        resAb.getInt(1),
                        resAb.getString(2),
                        resAb.getString(3),
                        Date.valueOf(resAb.getString(4))
                );
                abonnes.put(ab.numero(),ab);
            }
            Mediatheque.getInstance().setAbonnes(abonnes);

            Statement reqDVD = connect.createStatement();
            ResultSet resDVD = reqDVD.executeQuery("SELECT * FROM DVD");
            Document doc;
            Map<Integer, Document> documents = new HashMap<>();
            while(resDVD.next()){
                doc = new DVD(
                        resDVD.getInt(1),
                        resDVD.getString(2),
                        resDVD.getBoolean(3),
                        Mediatheque.getInstance().getUnAbonneParNumero(resDVD.getInt(4))
                );
                documents.put(doc.numero(),doc);
            }
            Mediatheque.getInstance().setDocuments(documents);

            connect.close();
            reqAb.close();
            reqDVD.close();
            resAb.close();
            resDVD.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sauvegardeBD(Document doc, Abonne ab){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        try {
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/mediatheque", "root", "mariadb");

            String sql = "UPDATE DVD SET Emprunteur = ? WHERE NumDVD = ?;";
            PreparedStatement reqSauv = connect.prepareStatement(sql);
            if(ab == null){
                reqSauv.setString(1, "NULL");
            } else {
                reqSauv.setInt(1, ab.numero());
            }
            reqSauv.setInt(2, doc.numero());
            reqSauv.executeUpdate();

            connect.close();
            reqSauv.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
