import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Bibliotheque {
    private ArrayList<Livre> listeLivres;
    private HashMap<Utilisateur, ArrayList<Livre>> empruntsUtilisateurs;

    public Bibliotheque() {
        this.listeLivres = new ArrayList<>();
        this.empruntsUtilisateurs = new HashMap<>();
    }

    public void ajouterLivre(Livre livre) {
        listeLivres.add(livre);
        System.out.println("Le livre a été ajouté avec succès: " + livre);
    }

    public List<Livre> rechercherLivres(String critere) {
        return listeLivres.stream()
                .filter(livre -> livre.getTitre().toLowerCase().contains(critere.toLowerCase()) ||
                        livre.getAuteur().toLowerCase().contains(critere.toLowerCase()) ||
                        livre.getISBN().contains(critere))
                .collect(Collectors.toList());
    }

    public void modifierLivre(String ISBN, Livre livreModifie) throws LibraryException {
        Livre livre = listeLivres.stream()
                .filter(l -> l.getISBN().equals(ISBN))
                .findFirst()
                .orElseThrow(() -> new LibraryException("Livre non trouvé avec l'ISBN: " + ISBN));

        livre.setTitre(livreModifie.getTitre());
        livre.setAuteur(livreModifie.getAuteur());
        livre.setAnneePublication(livreModifie.getAnneePublication());
        livre.setISBN(livreModifie.getISBN());
        System.out.println("Le livre a été modifié avec succès: " + livre);
    }

    public void supprimerLivre(String ISBN) throws LibraryException {
        boolean isRemoved = listeLivres.removeIf(livre -> livre.getISBN().equals(ISBN));
        if (!isRemoved) {
            throw new LibraryException("Suppression impossible, livre non trouvé avec l'ISBN: " + ISBN);
        }
        System.out.println("Le livre avec l'ISBN " + ISBN + " a été supprimé avec succès.");
    }

    public void enregistrerEmprunt(Utilisateur utilisateur, Livre livre) {
        ArrayList<Livre> livresEmpruntes = empruntsUtilisateurs.getOrDefault(utilisateur, new ArrayList<>());
        livresEmpruntes.add(livre);
        empruntsUtilisateurs.put(utilisateur, livresEmpruntes);
        System.out.println(utilisateur.getNom() + " a emprunté le livre: " + livre.getTitre());
    }

    public void enregistrerRetour(Livre livre) {
        empruntsUtilisateurs.forEach((utilisateur, livresEmpruntes) -> {
            if(livresEmpruntes.remove(livre)) {
                System.out.println(utilisateur.getNom() + " a retourné le livre: " + livre.getTitre());
            }
        });
    }
}
