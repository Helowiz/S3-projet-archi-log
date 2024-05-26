package serveur.bttp2;

public class Codage {
    public static String coder(String chaine) {
        StringBuilder builder = new StringBuilder();
        builder.append(chaine.replace("\n", "##"));
        return builder.toString();
    }

    public static String decoder(String chaine) {
        StringBuilder builder = new StringBuilder();
        builder.append(chaine.replace("##", "\n"));
        return builder.toString();
    }
}
