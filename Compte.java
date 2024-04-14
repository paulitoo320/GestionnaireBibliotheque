public class Compte {
    private String nomUtilisateur;
    private String motDePasse;
    private Role role;
    private Utilisateur utilisateur;


    public Compte(String nomUtilisateur, String motDePasse, Role role, Utilisateur utilisateur) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
        this.utilisateur = utilisateur;
    }

    public boolean verifierIdentifiants(String nomUtilisateur, String motDePasse) {
        return this.nomUtilisateur.equals(nomUtilisateur) && this.motDePasse.equals(motDePasse);
    }

    public Role getRole() {
        return role;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean verifierMotDePasse(String motDePasse) {
        return this.motDePasse.equals(motDePasse);
    }

    public int getNumeroUtilisateur() {
        return utilisateur.getNumeroIdentification();
    }
}
