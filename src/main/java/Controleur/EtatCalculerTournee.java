package Controleur;

import ChargeurXML.ChargeurLivraison;
import Modele.Noeud;
import Modele.Plan;
import Modele.PointLivraison;
import Modele.Tournee;
import Vue.VueAjouterPoint;

import javax.swing.*;

/**
 * @author H4401
 *         Classe Etat avec les actions possibles apr�s avoir calcul� une tourn�e
 */

public class EtatCalculerTournee extends EtatDefaut {

    /**
     * M�thode permettant l'ajout d'un point de livraison
     *
     * @param noeud
     * @param debut
     * @param fin
     * @param duree
     * @param tournee
     * @param lstDeCde
     */
    public void cdeAjouterLivraison(Noeud noeud, Double debut, Double fin, double duree, Tournee tournee, LstDeCde lstDeCde) {

        CdeAjout commandeAjout = new CdeAjout(noeud, debut, fin, duree, tournee, lstDeCde);
        try {
            commandeAjout.doCde();
            lstDeCde.ajouterCommande(commandeAjout);
        } catch (NullPointerException ne) {

        }
    }

    /**
     * M�thode permettant la suppression d'un point de livraison
     *
     * @param pointLivraison
     * @param tournee
     * @param lstDeCde
     */
    public void cdeSupprimerLivraison(PointLivraison pointLivraison, Tournee tournee, LstDeCde lstDeCde) {
        CdeSupprimer commandeSupprimer = new CdeSupprimer(pointLivraison, tournee);
        try {
            commandeSupprimer.doCde();
            lstDeCde.ajouterCommande(commandeSupprimer);
        } catch (NullPointerException ne) {
        }
    }

    /**
     * M�thode permettant de modifier la plage horaire d'un point de livraison
     *
     * @param pointLivraison Point � modifier
     * @param tournee
     * @param debutPlage
     * @param finPlage
     * @param lstDeCde
     */
    public void cdeModifierPlageHoraire(PointLivraison pointLivraison, Tournee tournee, Double debutPlage, Double finPlage, LstDeCde lstDeCde) {
        CdeModifierHoraire commandeModifier = new CdeModifierHoraire(pointLivraison, tournee, debutPlage, finPlage);
        try {
            commandeModifier.doCde();
            lstDeCde.ajouterCommande(commandeModifier);
        } catch (NullPointerException ne) {
        }
    }

    /**
     * M�thode chargeant les points de livraison depuis un fichier xml (par appel au package ChargeurXML)
     *
     * @param filePath Le chemin d'acc�s au fichier xml
     */
    public boolean chargerLivraison(String filePath, Tournee tournee) {
        if (ChargeurLivraison.getInstance().parse(tournee, filePath))
            return true;
        JOptionPane.showMessageDialog(null, "Echec du chargement, r�essayez");
        return false;
    }

    /**
     * M�thode faisant appara�tre le fen�tre d'ajout de point lors d'un clic droit sur un noeud
     *
     * @param noeud
     * @param controleur
     * @return
     */
    public boolean rightClickedOnPoint(Noeud noeud, Controleur controleur) {
        VueAjouterPoint vueAjouterPoint = new VueAjouterPoint(noeud, controleur);
        return false;
    }

    /**
     * Calculer une tourn�e n'est pas possible, un message d'erreur adapt� est donc affich�
     *
     * @param plan
     * @param tournee
     */
    public void calculerTournee(Plan plan, Tournee tournee) {
        JOptionPane.showMessageDialog(null, "La tourn�e est d�j� calcul�e");

    }

}
