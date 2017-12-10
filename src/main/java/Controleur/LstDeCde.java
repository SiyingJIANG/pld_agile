package Controleur;

import java.util.LinkedList;

/**
 * @author H4401
 * Classe Liste de Commande permettant d'utiliser les "Undo/Redo"
 */
public class LstDeCde {
	private LinkedList<Commande> liste;
	private int i;

	/**
	 * Constructeur par d�faut
	 */
	public LstDeCde()
	{
		i = 0;
		liste = new LinkedList<Commande>();
	}

	/**
	 * M�thode Get
	 * @return i
	 */
	public int getI() {
		return i;
	}

	/**
	 * M�thode Set i
	 * @param i
	 */
	public void setI(int i) {
		this.i = i;
	}

	/**
	 * M�thode permettant d'ajouter une commande � la liste
	 * @param newCde
	 */
	public void ajouterCommande(Commande newCde) {
		i++;
		liste.add(newCde);
		if(this.liste.size()==i+1)
		{
			liste.remove(i-1);
		}
	}

	/**
	 * M�thode permettant l'utilisation de "Undo"
	 */
	public void undo() {
		if(i>0) {
			liste.get(i - 1).undoCde();
			i--;
		}
	}

	/**
	 * M�thode permettant l'utilisation de "Redo"
	 */
	public void redo()
	{
		if(i<liste.size()) {
			liste.get(i).doCde();
			i++;
		}
	}
}
