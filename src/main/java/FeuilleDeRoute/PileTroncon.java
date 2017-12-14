package FeuilleDeRoute;

import Modele.Troncon;

/**
 * @author H4401
 *         Cette classe est utilis�e pour garder la liste des tron�ons repr�sentant la m�me rue.
 */
public class PileTroncon {

    private Troncon troncon;
    private double distanceTotale;
    private Indication indication;

    /**
     * Construit une nouvelle pile de tron�ons � partir d'un nouveau tron�on et de l'indication Indication
     *
     * @param t tron�on diff�rent du pr�c�dent
     * @param v Indication calcul�e
     */
    public PileTroncon(Troncon t, Indication v) {
        troncon = t;
        indication = v;
        distanceTotale = t.getLongueur();
    }

    /**
     * Ajouter un tro�on au nom de rue identique � la pile.
     *
     * @param t Tron�on � ajouter
     */
    public void addTroncon(Troncon t) {
        troncon = t;
        distanceTotale += t.getLongueur();
    }

    /**
     * Get
     *
     * @return troncon
     */
    public Troncon getTroncon() {
        return troncon;
    }

    /**
     * Get
     *
     * @return distance totale de la pile de tron�ons
     */
    public double getDistanceTotale() {
        return distanceTotale;
    }

    /**
     * Get
     *
     * @return Indication
     */
    public Indication getIndication() {
        return indication;
    }
}
