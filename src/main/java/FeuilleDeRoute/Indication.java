package FeuilleDeRoute;

public enum Indication {
    ToutDroit("Continuez tout droit pour aller sur"),
    ADroite("Tournez � droite pour aller sur"),
    AGauche("Tournez � gauche pour aller sur"),
    Demarrage("D�marrez et allez sur"),
    FinItineraire("Stoppez et terminez la livraison num�ro"),
    DebutItineraire("D�but de la livraison num�ro"),
    FinTournee("Stoppez � l'entrep�t et terminez la tourn�e");

    private String texte;

    Indication(String texte) {
        this.texte = texte;
    }

    public String getTexte() {
        return this.texte;
    }
}
