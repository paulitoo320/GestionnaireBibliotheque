public class Compte {
    private String nomUtilisateur;
    private String motDePasse;
    private Role role;

    public Compte(String nomUtilisateur, String motDePasse, Role role) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.role = role;
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
}
