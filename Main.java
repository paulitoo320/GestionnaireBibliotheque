import java.util.*;

public class Main {
    private static final Bibliotheque bibliotheque = new Bibliotheque();
    private static final HashMap<Integer, Compte> comptesUtilisateurs = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    // Stocker les comptes dans une Map pour faciliter la recherche par nom d'utilisateur
    private static final Map<String, Compte> comptes = new HashMap<>();

    public static void main(String[] args) throws LibraryException {
        // Initialiser le compte administrateur
        comptes.put("admin", new Compte("admin", "admin123", Role.ADMINISTRATEUR, new Utilisateur("admin", 1, true)));
        System.out.println("Bienvenue à la Bibliothèque");

        while (true) {
            System.out.print("Nom d'utilisateur: ");
            String nomUtilisateur = scanner.nextLine();
            System.out.print("Mot de passe: ");
            String motDePasse = scanner.nextLine();

            Compte compte = comptes.get(nomUtilisateur);
            if (compte != null && compte.verifierMotDePasse(motDePasse)) {
                switch (compte.getRole()) {
                    case ADMINISTRATEUR:
                        afficherMenuAdmin();
                        break;
                    case UTILISATEUR:
                        afficherMenuUtilisateur(compte);
                        break;
                }
            } else {
                System.out.println("Identifiants incorrects. Essayez encore ou tapez 'quitter' pour sortir.");
                if ("quitter".equalsIgnoreCase(scanner.nextLine())) {
                    System.out.println("Merci d'avoir utilisé la Bibliothèque. À bientôt !");
                    break;
                }
            }
        }
    }

    private static void afficherMenuAdmin() throws LibraryException {
        System.out.println("Bienvenue à votre espace administrateur");
        // Afficher et gérer les options du menu administrateur
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n1. Gestion des Livres");
            System.out.println("2. Gestion des Emprunts");
            System.out.println("3. Gestion des Utilisateurs");
            System.out.println("4. Afficher les statistiques de la bibliothèque");
            System.out.println("5. Déconnexion");

            System.out.print("Choisissez une option: ");
            int choix = Integer.parseInt(scanner.nextLine());

            switch (choix) {
                case 1:
                    gererLivres();
                    break;
                case 2:
                    gererEmprunts();
                    break;
                case 3:
                    gererUtilisateurs();
                    break;
                case 4:
                    bibliotheque.afficherStatistiques();
                    break;
                case 5:
                    continuer = false;
                    System.out.println("Déconnexion réussie.");
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private static void gererLivres() {
        boolean retourMenuPrincipal = false;
        while (!retourMenuPrincipal) {
            System.out.println("\nGestion des Livres:");
            System.out.println("1. Ajouter un livre à la bibliothèque");
            System.out.println("2. Modifier un livre existant");
            System.out.println("3. Supprimer un livre de la bibliothèque");
            System.out.println("4. Rechercher un livre par titre, auteur ou ISBN");
            System.out.println("5. Afficher tous les livres de la bibliothèque par genre");
            System.out.println("6. Retour au menu principal");
            System.out.print("Choisissez une option: ");
            int choix = Integer.parseInt(scanner.nextLine());

            switch (choix) {
                case 1:
                    ajouterLivre();
                    break;
                case 2:
                    modifierLivre();
                    break;
                case 3:
                    supprimerLivre();
                    break;
                case 4:
                    rechercherLivre();
                    break;
                case 5:
                    afficherLivresParGenre();
                    break;
                case 6:
                    retourMenuPrincipal = true;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private static void ajouterLivre() {
        System.out.println("Ajouter un nouveau livre:");
        System.out.print("Genre (Roman, Essai, LivreAudio): ");
        String genre = scanner.nextLine();

        System.out.print("Titre: ");
        String titre = scanner.nextLine();
        System.out.print("Auteur: ");
        String auteur = scanner.nextLine();
        System.out.print("Année de publication: ");
        int anneePublication = Integer.parseInt(scanner.nextLine());
        System.out.print("ISBN: ");
        String ISBN = scanner.nextLine();

        // Vérifier si l'ISBN est unique avant d'ajouter le livre
        if (bibliotheque.getLivre(ISBN) != null) {
            System.out.println("Un livre avec cet ISBN existe déjà. Ajout annulé.");
            return;  // Retourne pour éviter l'ajout du livre
        }

        Livre nouveauLivre = null;
        switch (genre.toLowerCase()) {
            case "roman":
                System.out.print("Genre du roman: ");
                String genreRoman = scanner.nextLine();
                nouveauLivre = new Roman(titre, auteur, anneePublication, ISBN, genreRoman);
                break;
            case "essai":
                System.out.print("Sujet de l'essai: ");
                String sujet = scanner.nextLine();
                nouveauLivre = new Essai(titre, auteur, anneePublication, ISBN, sujet);
                break;
            case "livreaudio":
                System.out.print("Durée en minutes: ");
                int duree = Integer.parseInt(scanner.nextLine());
                System.out.print("Narrateur: ");
                String narrateur = scanner.nextLine();
                nouveauLivre = new LivreAudio(titre, auteur, anneePublication, ISBN, duree, narrateur);
                break;
            default:
                System.out.println("Genre inconnu. Le livre n'a pas été ajouté.");
                return;
        }

        if (nouveauLivre != null) {
            bibliotheque.ajouterLivre(nouveauLivre);
            System.out.println("Livre ajouté avec succès.");
        }
    }

    private static void modifierLivre() {
        System.out.println("Modifier un livre existant:");
        System.out.print("Entrez l'ISBN du livre à modifier: ");
        String ISBN = scanner.nextLine();

        // Trouver le livre par ISBN
        Livre livre = bibliotheque.getLivre(ISBN);
        if (livre == null) {
            System.out.println("Aucun livre trouvé avec l'ISBN fourni.");
            return;
        }

        System.out.println("Entrez les nouvelles informations du livre:");
        System.out.print("Nouveau titre: ");
        String titre = scanner.nextLine();
        System.out.print("Nouvel auteur: ");
        String auteur = scanner.nextLine();
        System.out.print("Nouvelle année de publication: ");
        int anneePublication = Integer.parseInt(scanner.nextLine());

        if (livre instanceof Roman) {
            // Si le livre est un roman, peut-être voudrions-nous modifier le genre du roman
            System.out.print("Nouveau genre du roman: ");
            String genreRoman = scanner.nextLine();
            livre = new Roman(titre, auteur, anneePublication, ISBN, genreRoman);
        } else if (livre instanceof Essai) {
            // Si le livre est un essai, peut-être voudrions-nous modifier le sujet de l'essai
            System.out.print("Nouveau sujet de l'essai: ");
            String sujet = scanner.nextLine();
            livre = new Essai(titre, auteur, anneePublication, ISBN, sujet);
        } else if (livre instanceof LivreAudio) {
            // Si c'est un livre audio, on pourra modifier le narrateur ou la durée
            System.out.print("Nouveau narrateur: ");
            String narrateur = scanner.nextLine();
            System.out.print("Nouvelle durée en minutes: ");
            int duree = Integer.parseInt(scanner.nextLine());
            livre = new LivreAudio(titre, auteur, anneePublication, ISBN, duree, narrateur);
        } else {
            // Si le livre n'est d'aucun type spécial, on crée simplement un nouveau Livre
            livre = new Livre(titre, auteur, anneePublication, ISBN);
        }

        try {
            bibliotheque.modifierLivre(ISBN, livre);
            System.out.println("Le livre a été modifié avec succès.");
        } catch (LibraryException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void supprimerLivre() {
        System.out.println("Supprimer un livre de la bibliothèque:");
        System.out.print("Entrez l'ISBN du livre à supprimer: ");
        String ISBN = scanner.nextLine();
        try {
            bibliotheque.supprimerLivre(ISBN);
            System.out.println("Le livre a été supprimé avec succès.");
        } catch (LibraryException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void rechercherLivre() {
        System.out.println("Rechercher un livre par titre, auteur ou ISBN:");
        System.out.print("Entrez votre critère de recherche: ");
        String critere = scanner.nextLine();
        List<Livre> livresTrouves = bibliotheque.rechercherLivres(critere);
        if (livresTrouves.isEmpty()) {
            System.out.println("Aucun livre trouvé.");
        } else {
            livresTrouves.forEach(livre -> System.out.println(livre));
        }
    }

    private static void afficherLivresParGenre() {
        System.out.println("Livres par genre:");
        // Supposons que la méthode getLivresParGenre() dans Bibliotheque retourne une Map<String, List<Livre>> où la clé est le genre
        Map<String, List<Livre>> livresParGenre = bibliotheque.getLivresParGenre();
        for (Map.Entry<String, List<Livre>> entry : livresParGenre.entrySet()) {
            System.out.println("Genre: " + entry.getKey());
            for (Livre livre : entry.getValue()) {
                System.out.println("- " + livre);
            }
        }
    }

    private static void gererEmprunts() throws LibraryException {
        boolean retourMenuPrincipal = false;
        while (!retourMenuPrincipal) {
            System.out.println("\nGestion des Emprunts:");
            System.out.println("1. Enregistrer l'emprunt d'un livre par un utilisateur");
            System.out.println("2. Enregistrer le retour d'un livre par un utilisateur");
            System.out.println("3. Afficher les livres empruntés par un utilisateur donné");
            System.out.println("4. Afficher tous les emprunts par utilisateur");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choisissez une option: ");
            int choix = Integer.parseInt(scanner.nextLine());

            switch (choix) {
                case 1:
                    enregistrerEmprunt();
                    break;
                case 2:
                    enregistrerRetour();
                    break;
                case 3:
                    afficherEmpruntsUtilisateur();
                    break;
                case 4:
                    bibliotheque.afficherTousLesEmprunts();
                case 5:
                    retourMenuPrincipal = true;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private static void enregistrerEmprunt() {
        boolean continuer = true;
        while (continuer) {
            try {
                System.out.println("Enregistrer un emprunt:");
                System.out.print("Numéro d'identification de l'utilisateur: ");
                int idUtilisateur = Integer.parseInt(scanner.nextLine());
                System.out.print("ISBN du livre à emprunter: ");
                String ISBN = scanner.nextLine();

                Utilisateur utilisateur = bibliotheque.trouverUtilisateurParNumeroIdentification(idUtilisateur);
                Livre livre = bibliotheque.getLivre(ISBN);

                if (utilisateur != null && livre != null) {
                    bibliotheque.enregistrerEmprunt(utilisateur, livre);
                    System.out.println("Emprunt enregistré avec succès pour " + utilisateur.getNom() + ".");
                } else {
                    System.out.println("Erreur : utilisateur ou livre non trouvé.");
                }

                continuer = false;  // Si tout se passe bien, sortir de la boucle
            } catch (LibraryException e) {
                System.out.println(e.getMessage() + " Veuillez réessayer.");
                // Ici, nous capturons l'exception et demandons à l'utilisateur de réessayer.
                // La boucle continuera, permettant à l'utilisateur de réessayer.
            }
        }
    }


    private static void enregistrerRetour() {
        System.out.println("Enregistrer un retour de livre:");
        System.out.print("ISBN du livre retourné: ");
        String ISBN = scanner.nextLine();

        Livre livre = bibliotheque.getLivre(ISBN);
        if (livre != null) {
            System.out.print("Entrez le numéro d'identification de l'utilisateur qui retourne le livre: ");
            int numeroIdentification = Integer.parseInt(scanner.nextLine());

            Utilisateur utilisateur = bibliotheque.trouverUtilisateurParNumeroIdentification(numeroIdentification);
            if (utilisateur != null) {
                try {
                    bibliotheque.enregistrerRetour(utilisateur, livre);
                    System.out.println("Retour enregistré avec succès pour " + utilisateur.getNom() + ".");
                } catch (LibraryException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Aucun utilisateur trouvé avec ce numéro d'identification.");
            }
        } else {
            System.out.println("Aucun livre correspondant à cet ISBN n'a été trouvé.");
        }
    }


    private static void afficherEmpruntsUtilisateur() {
        System.out.println("Afficher les emprunts d'un utilisateur:");
        System.out.print("Entrez le numéro d'identification de l'utilisateur: ");
        int numeroIdentification = Integer.parseInt(scanner.nextLine());

        Utilisateur utilisateur = bibliotheque.trouverUtilisateurParNumeroIdentification(numeroIdentification);

        if (utilisateur != null) {
            ArrayList<Livre> livresEmpruntes = bibliotheque.getEmpruntsUtilisateur(utilisateur);
            if (livresEmpruntes.isEmpty()) {
                System.out.println("Aucun emprunt enregistré pour cet utilisateur.");
            } else {
                System.out.println("Livres empruntés par l'utilisateur avec le numéro d'identification " + numeroIdentification + ":");
                for (Livre livre : livresEmpruntes) {
                    System.out.println("- " + livre);
                }
            }
        } else {
            System.out.println("Aucun utilisateur trouvé avec ce numéro d'identification.");
        }
    }

    private static void gererUtilisateurs() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\nGestion des Utilisateurs:");
            System.out.println("1. Enregistrer de nouveaux utilisateurs");
            System.out.println("2. Vérifier l'éligibilité des utilisateurs à emprunter des livres");
            System.out.println("3. Afficher tous les utilisateurs");
            System.out.println("4. Modifier les informations d'un utilisateur");
            System.out.println("5. Rechercher un utilisateur par numéro d'identification");
            System.out.println("6. Supprimer un utilisateur");
            System.out.println("7. Retour au menu principal");
            System.out.print("Choisissez une option: ");

            int choix = Integer.parseInt(scanner.nextLine());
            switch (choix) {
                case 1:
                    enregistrerNouvelUtilisateur();
                    break;
                case 2:
                    verifierEligibiliteUtilisateurs();
                    break;
                case 3:
                    bibliotheque.afficherTousLesUtilisateurs();
                    break;
                case 4:
                    modifierInformationsUtilisateur();
                    break;
                case 5:
                    rechercherUtilisateurParNumeroIdentification();
                    break;
                case 6:
                    supprimerUtilisateur();
                    break;
                case 7:
                    continuer = false;
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private static void enregistrerNouvelUtilisateur() {
        // Demander les informations de base de l'utilisateur
        System.out.print("Entrez le nom du nouvel utilisateur: ");
        String nom = scanner.nextLine();
        System.out.print("Entrez le numéro d'identification: ");
        int numeroIdentification = Integer.parseInt(scanner.nextLine());
        System.out.print("L'utilisateur a-t-il payé ses cotisations ? (oui/non): ");
        boolean aJourCotisations = scanner.nextLine().trim().equalsIgnoreCase("oui");

        // Demander le nom d'utilisateur et le mot de passe pour le compte
        System.out.print("Choisissez un nom d'utilisateur: ");
        String nomUtilisateur = scanner.nextLine();
        System.out.print("Choisissez un mot de passe: ");
        String motDePasse = scanner.nextLine();

        // Créer les objets Utilisateur et Compte
        Utilisateur nouvelUtilisateur = new Utilisateur(nom, numeroIdentification, aJourCotisations);
        Compte nouveauCompte = new Compte(nomUtilisateur, motDePasse, Role.UTILISATEUR, nouvelUtilisateur);

        // Ajouter l'utilisateur dans la HashMap des utilisateurs de la bibliothèque
        try {
            bibliotheque.ajouterUtilisateur(nouvelUtilisateur, nouveauCompte.getNomUtilisateur(), nouveauCompte.getMotDePasse(), nouveauCompte.getRole());

            comptesUtilisateurs.put(numeroIdentification, nouveauCompte);
            comptes.put(nouveauCompte.getNomUtilisateur(), nouveauCompte);

            System.out.println("Utilisateur et compte enregistrés avec succès.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void verifierEligibiliteUtilisateurs() {
        System.out.print("Entrez le numéro d'identification de l'utilisateur à vérifier : ");
        int numeroIdentification = Integer.parseInt(scanner.nextLine());

        Utilisateur utilisateur = bibliotheque.trouverUtilisateurParNumeroIdentification(numeroIdentification);

        if (utilisateur != null) {
            boolean estEligible = bibliotheque.peutEmprunter(utilisateur); // Utilisation de la méthode peutEmprunter

            String messageEligibilite = estEligible ?
                    "L'utilisateur est éligible pour emprunter des livres." :
                    "L'utilisateur n'est pas éligible pour emprunter des livres ou a atteint la limite d'emprunts.";

            System.out.println(messageEligibilite);
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }


    private static void modifierInformationsUtilisateur() {
        System.out.print("Entrez le numéro d'identification de l'utilisateur à modifier: ");
        int numeroIdentification = Integer.parseInt(scanner.nextLine());

        // Trouver l'utilisateur par son numéro d'identification
        Utilisateur utilisateur = bibliotheque.trouverUtilisateurParNumeroIdentification(numeroIdentification);
        // Trouver le compte associé à l'utilisateur dans comptesUtilisateurs
        Compte compteAssocie = comptesUtilisateurs.get(numeroIdentification);

        if (utilisateur != null && compteAssocie != null) {
            System.out.print("Entrez le nouveau nom de l'utilisateur: ");
            String nouveauNom = scanner.nextLine();

            System.out.print("Entrez le nouveau nom d'utilisateur (laissez vide pour ne pas changer): ");
            String nouveauNomUtilisateur = scanner.nextLine();

            System.out.print("Entrez le nouveau mot de passe (laissez vide pour ne pas changer): ");
            String nouveauMotDePasse = scanner.nextLine();

            System.out.print("L'utilisateur est-il à jour avec ses cotisations? (oui/non): ");
            boolean estAJour = scanner.nextLine().trim().equalsIgnoreCase("oui");

            try {
                // Mettre à jour les informations de l'utilisateur
                utilisateur.setNom(nouveauNom);
                utilisateur.setAJourCotisations(estAJour);

                // Si le nom d'utilisateur ou le mot de passe est changé, mettre à jour le compte
                if (!nouveauNomUtilisateur.isEmpty() || !nouveauMotDePasse.isEmpty()) {
                    if (!nouveauNomUtilisateur.isEmpty()) {
                        comptes.remove(compteAssocie.getNomUtilisateur()); // Supprimer l'ancienne entrée
                        compteAssocie.setNomUtilisateur(nouveauNomUtilisateur);
                    }
                    if (!nouveauMotDePasse.isEmpty()) {
                        compteAssocie.setMotDePasse(nouveauMotDePasse);
                    }
                    comptes.put(compteAssocie.getNomUtilisateur(), compteAssocie); // Ajouter la nouvelle entrée
                    comptesUtilisateurs.put(numeroIdentification, compteAssocie); // Mettre à jour comptesUtilisateurs avec le compte modifié
                }

                System.out.println("Les informations de l'utilisateur et du compte ont été mises à jour avec succès.");
            } catch (Exception e) {
                System.out.println("Une erreur est survenue lors de la mise à jour: " + e.getMessage());
            }
        } else {
            System.out.println("Utilisateur ou compte non trouvé avec ce numéro d'identification.");
        }
    }

    private static void rechercherUtilisateurParNumeroIdentification() {
        System.out.print("Entrez le numéro d'identification de l'utilisateur à rechercher : ");
        int numeroIdentification = Integer.parseInt(scanner.nextLine());

        Utilisateur utilisateur = bibliotheque.trouverUtilisateurParNumeroIdentification(numeroIdentification);
        Compte compteAssocie = comptesUtilisateurs.get(numeroIdentification); // Récupération du compte associé

        if (utilisateur != null && compteAssocie != null) {
            System.out.println("Informations sur l'utilisateur: " + utilisateur);
            System.out.println("Nom d'utilisateur associé: " + compteAssocie.getNomUtilisateur());
            System.out.println("Mot de passe associé: " + compteAssocie.getMotDePasse());
            // Afficher les livres empruntés si nécessaire
            ArrayList<Livre> livresEmpruntes = bibliotheque.getEmpruntsUtilisateur(utilisateur);
            if (!livresEmpruntes.isEmpty()) {
                System.out.println("Livres empruntés par " + utilisateur.getNom() + " :");
                for (Livre livre : livresEmpruntes) {
                    System.out.println("\t" + livre);
                }
            }
        } else {
            System.out.println("Aucun utilisateur trouvé avec ce numéro d'identification.");
        }
    }


    private static void supprimerUtilisateur() {
        System.out.print("Entrez le numéro d'identification de l'utilisateur à supprimer: ");
        int numeroIdentification = Integer.parseInt(scanner.nextLine());
        try {
            bibliotheque.supprimerUtilisateur(numeroIdentification);
            System.out.println("Utilisateur supprimé avec succès.");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }



    private static void afficherMenuUtilisateur(Compte compte) {
        System.out.println("Bienvenue à votre espace utilisateur, " + compte.getNomUtilisateur());
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n1. Emprunter un livre.");
            System.out.println("2. Retourner un livre.");
            System.out.println("3. Afficher les livres empruntés.");
            System.out.println("4. Déconnexion.");

            System.out.print("Choisissez une option: ");
            int choix = Integer.parseInt(scanner.nextLine());

            switch (choix) {
                case 1:
                    emprunterLivreUtilisateur(compte);
                    break;
                case 2:
                    retournerLivreUtilisateur(compte);
                    break;
                case 3:
                    afficherLivresEmpruntesUtilisateur(compte);
                    break;
                case 4:
                    continuer = false;
                    System.out.println("Déconnexion réussie.");
                    break;
                default:
                    System.out.println("Option invalide, veuillez réessayer.");
                    break;
            }

        }
    }

    private static void emprunterLivreUtilisateur(Compte compte) {
        System.out.println("Veuillez entrer l'ISBN du livre que vous souhaitez emprunter :");
        String isbn = scanner.nextLine();

        Livre livre = bibliotheque.getLivre(isbn);
        if (livre != null) {
            Utilisateur utilisateur = compte.getUtilisateur();
            try {
                bibliotheque.enregistrerEmprunt(utilisateur, livre);
                utilisateur.ajouterEmprunt(livre); // Garde une trace des livres empruntés par l'utilisateur
                System.out.println("Le livre a été emprunté avec succès.");
            } catch (LibraryException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Le livre n'a pas été trouvé.");
        }
    }

    private static void retournerLivreUtilisateur(Compte compte) {
        System.out.print("Veuillez entrer l'ISBN du livre que vous souhaitez retourner : ");
        String isbn = scanner.nextLine();

        Livre livre = bibliotheque.getLivre(isbn);
        if (livre != null) {
            Utilisateur utilisateur = compte.getUtilisateur(); // Récupérer l'utilisateur à partir du compte

            try {
                bibliotheque.enregistrerRetour(utilisateur, livre); // Enregistrer le retour dans la bibliothèque
                utilisateur.retournerLivre(livre); // Mettre à jour la liste des livres empruntés de l'utilisateur
                System.out.println("Le livre a été retourné avec succès.");
            } catch (LibraryException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Le livre n'a pas été trouvé.");
        }
    }

    private static void afficherLivresEmpruntesUtilisateur(Compte compte) {
        // On suppose que chaque compte a un numéro d'identification d'utilisateur associé.
        // Cette association devra être établie lors de la création de l'utilisateur et de son compte.
        Utilisateur utilisateur = bibliotheque.trouverUtilisateurParNumeroIdentification(compte.getNumeroUtilisateur());

        if (utilisateur != null) {
            ArrayList<Livre> livresEmpruntes = bibliotheque.getEmpruntsUtilisateur(utilisateur);

            System.out.println("Affichage des livres empruntés pour l'utilisateur: " + utilisateur.getNom());
            if (livresEmpruntes.isEmpty()) {
                System.out.println("Aucun livre emprunté pour le moment.");
            } else {
                for (Livre livre : livresEmpruntes) {
                    System.out.println("- " + livre);
                }
            }
        } else {
            System.out.println("Aucun utilisateur associé à ce compte ou utilisateur non trouvé.");
        }
    }


}
