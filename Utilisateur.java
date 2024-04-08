import java.util.ArrayList;

public class Utilisateur {
    private String nom;
    private int numeroIdentification;
    private ArrayList<Livre> livresEmpruntes;

    public Utilisateur(String nom, int numeroIdentification) {
        this.nom = nom;
        this.numeroIdentification = numeroIdentification;
        this.livresEmpruntes = new ArrayList<>();
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

    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNumeroIdentification(int numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    // Méthodes pour gérer les emprunts de livres
    public void emprunterLivre(Livre livre) {
        if (!livresEmpruntes.contains(livre)) {
            livresEmpruntes.add(livre);
            System.out.println(nom + " a emprunté le livre: " + livre.getTitre());
        } else {
            System.out.println("Le livre est déjà emprunté par " + nom);
        }
    }

    public void retournerLivre(Livre livre) {
        if (livresEmpruntes.remove(livre)) {
            System.out.println(nom + " a retourné le livre: " + livre.getTitre());
        } else {
            System.out.println("Le livre n'a pas été trouvé parmi ceux empruntés par " + nom);
        }
    }

    public String afficherLivresEmpruntes() {
        if (livresEmpruntes.isEmpty()) {
            return nom + " n'a emprunté aucun livre.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(nom).append(" a emprunté les livres suivants:\n");
        for (Livre livre : livresEmpruntes) {
            sb.append("- ").append(livre.getTitre()).append(" par ").append(livre.getAuteur())
                    .append(" (").append(livre.getAnneePublication()).append(")\n");
        }

        return sb.toString();
    }
}
