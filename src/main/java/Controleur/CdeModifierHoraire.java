package Controleur;

import Modele.PointLivraison;
import Modele.Tournee;

public class CdeModifierHoraire implements Commande {
    Tournee tournee;
    Tournee oldTournee;
    PointLivraison pointLivraison;
    double debutPlage;
    double finPlage;

    public CdeModifierHoraire(PointLivraison newPointLivraison, Tournee newTournee, double newDebutPlage, double newFinPlage) {
        tournee = newTournee;
        pointLivraison = newPointLivraison;
        debutPlage = newDebutPlage;
        finPlage = newFinPlage;
        oldTournee = new Tournee(newTournee);
    }

	public void doCde()
	{
        tournee.updateHoraire(pointLivraison,debutPlage,finPlage);
	}
	public void undoCde()
	{
		tournee = oldTournee;
	}
}
