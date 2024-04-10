public class Essai extends Livre {
    private String sujet; // Le sujet principal de l'essai

    public Essai(String titre, String auteur, int anneePublication, String ISBN, String sujet) {
        super(titre, auteur, anneePublication, ISBN);
        this.sujet = sujet;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }
}
