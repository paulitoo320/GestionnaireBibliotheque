import java.util.ArrayList;
public class Utilisateur {
    private Compte compte;
    private String nom;
    private int numeroIdentification;
    private ArrayList<Livre> livresEmpruntes;
    private boolean aJourCotisations;


    public Utilisateur(String nom, int numeroIdentification, boolean aJourCotisations) {
        this.nom = nom;
        this.numeroIdentification = numeroIdentification;
        this.livresEmpruntes = new ArrayList<>();
        this.aJourCotisations = aJourCotisations;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public int getNumeroIdentification() {
        return numeroIdentification;
    }

    public ArrayList<Livre> getLivresEmpruntes() {
        return livresEmpruntes;
    }

    public Compte getCompte() {
        return compte;
    }

    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumeroIdentification(int numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    public boolean estAJourCotisations() {
        return aJourCotisations;
    }

    public void setAJourCotisations(boolean aJourCotisations) {
        this.aJourCotisations = aJourCotisations;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public String toString() {
        // Implémentation de la méthode pour afficher les informations de l'utilisateur
        return "Nom: " + nom + ", Numéro d'identification: " + numeroIdentification +
                ", À jour des cotisations: " + (aJourCotisations ? "Oui" : "Non");
    }

    // Méthodes pour gérer les emprunts de livres
    public void ajouterEmprunt(Livre livre) {
        if (!livresEmpruntes.contains(livre)) {
            livresEmpruntes.add(livre);
        }
    }

    public void retournerLivre(Livre livre) {
        livresEmpruntes.remove(livre);
    }
}
