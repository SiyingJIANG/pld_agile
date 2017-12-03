package FeuilleDeRoute;

public enum Indication {
    ToutDroit("Continuez tout droit pour aller sur"),
    ADroite("Tournez à droite pour aller sur"),
    AGauche("Tournez à gauche pour aller sur"),
    Demarrage("Démarrez et allez sur"),
    FinItineraire("Stoppez et livrez la la livraison numéro"),
    DebutItineraire("Début de la livraison numéro");

    private String texte;

    Indication(String texte) {
        this.texte = texte;
    }

    public String getTexte() {
        return this.texte;
    }
}
