import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Bibliotheque {

    private static final int MAX_EMPRUNTS_AUTORISES = 5;
    private static ArrayList<Livre> listeLivres;
    private static HashMap<Utilisateur, ArrayList<Livre>> empruntsUtilisateurs;


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

    public Map<String, List<Livre>> getLivresParGenre() {
        Map<String, List<Livre>> livresParGenre = new HashMap<>();

        for (Livre livre : listeLivres) {
            String genre;
            // On détermine le genre en fonction de l'instance de la classe
            if (livre instanceof Roman) {
                genre = "Roman";
            } else if (livre instanceof Essai) {
                genre = "Essai";
            } else if (livre instanceof LivreAudio) {
                genre = "LivreAudio";
            } else {
                genre = "Autre";
            }

            // Ajoute le livre à la liste correspondant à son genre
            livresParGenre.computeIfAbsent(genre, k -> new ArrayList<>()).add(livre);
        }

        return livresParGenre;
    }

    public Livre getLivre(String ISBN) {
        for (Livre livre : listeLivres) {
            if (livre.getISBN().equals(ISBN)) {
                return livre;
            }
        }
        return null;
    }

    public void enregistrerEmprunt(Utilisateur utilisateur, Livre livre) throws LibraryException {
        // Récupérer la liste des livres empruntés par l'utilisateur.
        ArrayList<Livre> livresEmpruntes = empruntsUtilisateurs.get(utilisateur);

        // Vérifier si l'utilisateur a déjà emprunté ce livre.
        if (livresEmpruntes.contains(livre)) {
            throw new LibraryException("Tentative d'emprunt d'un livre déjà emprunté: " + livre.getTitre());
        }

        // Vérifier si l'utilisateur est éligible pour l'emprunt.
        if (!utilisateur.estAJourCotisations() || livresEmpruntes.size() >= MAX_EMPRUNTS_AUTORISES) {
            throw new LibraryException("L'utilisateur n'est pas éligible pour emprunter plus de livres.");
        }

        // Enregistrer l'emprunt.
        livresEmpruntes.add(livre);
        System.out.println(utilisateur.getNom() + " a emprunté le livre: " + livre.getTitre());
    }


    public void enregistrerRetour(Livre livre) {
        empruntsUtilisateurs.forEach((utilisateur, livresEmpruntes) -> {
            if(livresEmpruntes.remove(livre)) {
                System.out.println(utilisateur.getNom() + " a retourné le livre: " + livre.getTitre());
            }
        });
    }

    public ArrayList<Livre> getEmpruntsUtilisateur(Utilisateur utilisateur) {
        return empruntsUtilisateurs.getOrDefault(utilisateur, new ArrayList<>());
    }

    public void afficherTousLesEmprunts() {
        if (empruntsUtilisateurs.isEmpty()) {
            System.out.println("Il n'y a actuellement aucun emprunt dans la bibliothèque.");
            return;
        }

        for (Map.Entry<Utilisateur, ArrayList<Livre>> entry : empruntsUtilisateurs.entrySet()) {
            Utilisateur utilisateur = entry.getKey();
            ArrayList<Livre> livresEmpruntes = entry.getValue();

            System.out.println("Utilisateur: " + utilisateur.getNom());
            for (Livre livre : livresEmpruntes) {
                System.out.println("\t- Livre emprunté: " + livre.getTitre());
            }
        }
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        if (empruntsUtilisateurs.containsKey(utilisateur)) {
            throw new RuntimeException("Un utilisateur avec ce numéro d'identification existe déjà.");
        } else {
            empruntsUtilisateurs.put(utilisateur, new ArrayList<>());
        }
    }


    public boolean peutEmprunter(Utilisateur utilisateur) {
        ArrayList<Livre> livresEmpruntes = empruntsUtilisateurs.get(utilisateur);
        return utilisateur.estAJourCotisations() && (livresEmpruntes == null || livresEmpruntes.size() < MAX_EMPRUNTS_AUTORISES);
    }

    public void afficherTousLesUtilisateurs() {
        for (Utilisateur utilisateur : empruntsUtilisateurs.keySet()) {
            System.out.println(utilisateur);
        }
    }

    public void modifierInformationsUtilisateur(int numeroIdentification, String nouveauNom, boolean aJourCotisations) {
        Utilisateur utilisateur = null;
        for (Utilisateur u : empruntsUtilisateurs.keySet()) {
            if (u.getNumeroIdentification() == numeroIdentification) {
                utilisateur = u;
                break;
            }
        }
        if (utilisateur != null) {
            utilisateur.setNom(nouveauNom);
            utilisateur.setAJourCotisations(aJourCotisations);
        } else {
            throw new RuntimeException("Utilisateur avec le numéro d'identification "
                    + numeroIdentification + " non trouvé.");
        }
    }


    public Utilisateur trouverUtilisateurParNumeroIdentification(int numeroIdentification) {
        for (Utilisateur utilisateur : empruntsUtilisateurs.keySet()) {
            if (utilisateur.getNumeroIdentification() == numeroIdentification) {
                return utilisateur;
            }
        }
        return null; // Aucun utilisateur avec ce numéro d'identification
    }

    public void supprimerUtilisateur(int numeroIdentification) {
        Utilisateur utilisateurASupprimer = null;
        for (Utilisateur u : empruntsUtilisateurs.keySet()) {
            if (u.getNumeroIdentification() == numeroIdentification) {
                utilisateurASupprimer = u;
                break;
            }
        }
        if (utilisateurASupprimer != null) {
            empruntsUtilisateurs.remove(utilisateurASupprimer);
            System.out.println("L'utilisateur avec le numéro d'identification "
                    + numeroIdentification + " a été supprimé avec succès.");
        } else {
            System.out.println("Utilisateur avec le numéro d'identification "
                    + numeroIdentification + " non trouvé.");
        }
    }


    // Méthode pour afficher les statistiques de la bibliothèque
    public static void afficherStatistiques() {
        int nombreTotalLivres = listeLivres.size();
        long nombreLivresEmpruntes = empruntsUtilisateurs.values().stream()
                .mapToInt(List::size)
                .sum();
        System.out.println("Nombre total de livres dans la bibliothèque: " + nombreTotalLivres);
        System.out.println("Nombre d'exemplaires empruntés: " + nombreLivresEmpruntes);

        // Trouver les utilisateurs les plus actifs (exemple simple)
        String utilisateursLesPlusActifs = empruntsUtilisateurs.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().size(), e1.getValue().size()))
                .limit(3) // Limite aux 3 premiers
                .map(entry -> entry.getKey().getNom() + " (" + entry.getValue().size() + " emprunts)")
                .collect(Collectors.joining(", "));

        System.out.println("Utilisateurs les plus actifs: " + utilisateursLesPlusActifs);

        // Livres les plus empruntés
        Map<Livre, Long> comptageLivres = empruntsUtilisateurs.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        String livresLesPlusEmpruntes = comptageLivres.entrySet().stream()
                .sorted(Map.Entry.<Livre, Long>comparingByValue().reversed())
                .limit(3) // Limite aux 3 livres les plus empruntés
                .map(entry -> entry.getKey().getTitre() + " (" + entry.getValue() + " fois)")
                .collect(Collectors.joining(", "));

        System.out.println("Livres les plus empruntés: " + livresLesPlusEmpruntes);

    }


}
