public class LivreAudio extends Livre {
    private int duree; // Durée du livre audio en minutes
    private String narrateur; // La personne qui lit le livre

    public LivreAudio(String titre, String auteur, int anneePublication, String ISBN, int duree, String narrateur) {
        super(titre, auteur, anneePublication, ISBN);
        this.duree = duree;
        this.narrateur = narrateur;
    }

    public int getDuree() {
        return duree;
    }

    public String getNarrateur() {
        return narrateur;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setNarrateur(String narrateur) {
        this.narrateur = narrateur;
    }
}
