package FeuilleDeRoute;

import Modele.Itineraire;
import Modele.PointLivraison;
import Modele.Tournee;
import Modele.Troncon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static j2html.TagCreator.*;

/**
 * @author H4401
 * Classe utilis�e pour g�n�rer une feuille de route apr�s le calcul d'une tourn�e
 */
public class FeuilleDeRoute {

    private static final String METRES = " m�tres";
    private static final String PENDANT = "pendant ";
    private static final String RUE_INCONNUE = "Rue inconnue";

    /**
     * G�n�re une feuille de route HTML � partir d'un Path choisi et d'une Tournee.
     *
     * @param filePath Path choisie par l'utilisateur depuis la bo�te de dialogue associ�e
     * @param tournee  la tourn�e calcul�e par le logiciel
     */
    public static void sortirFeuilleDeRoute(String filePath, Tournee tournee) throws IOException {
        LinkedList<Itineraire> listeOrdonneeItineraire = ordonnerItineraires(tournee);
        List<LigneFeuille> list = buildTableDeRoute(listeOrdonneeItineraire);
        String html = buildHtml(list);
        FileWriter fWriter = null;
        BufferedWriter writer = null;
        fWriter = new FileWriter(filePath + File.separator+"FeuilleDeRoute.html");
        writer = new BufferedWriter(fWriter);
        writer.write(html);
        writer.close();
    }

    /**
     * Constuit le texte HTML sous la forme d'un string pour une tourn�e donn�e.
     *
     * @param list Liste de listes contenant les champs � �crire
     * @return html le contenu html
     */
    private static String buildHtml(List<LigneFeuille> list) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String html = html(
                head(
                        meta().withCharset("ISO-8859-1"),
                        link().withRel("stylesheet").withHref("https://unpkg.com/spectre.css/dist/spectre.min.css"),
                        link().withRel("stylesheet").withHref("https://unpkg.com/spectre.css/dist/spectre-exp.min.css"),
                        link().withRel("stylesheet").withHref("https://unpkg.com/spectre.css/dist/spectre-icons.min.css"),
                        title("Feuille de route")
                ),
                body(
                        main(attrs("#main.content"),
                                h1("Feuille de route Optimod'IF"),
                                h2("Tourn�e du " + dateFormat.format(today.getTime()))
                        ),
                        table(attrs(".table table-striped table-hover"),
                                thead(
                                        tr(
                                                th("Heure d'arriv�e"),
                                                th("Heure de d�part"),
                                                th(),
                                                th("Directive"),
                                                th("Indication"),
                                                th("Longueur du trajet (m)")
                                        )
                                ),
                                tbody(
                                        each(list, row -> tr(
                                                td(row.gethArrivee()),
                                                td(row.gethDepart()),
                                                td(button(
                                                        attrs(".btn btn-primary btn-action"),
                                                        i(attrs(".icon "+row.getIconString()))
                                                        ).withStyle("pointer-events:none")
                                                ),
                                                td(row.getIndication()),
                                                td(row.getRue()),
                                                td(row.getLongueur().equals("-") ? row.getLongueur() :PENDANT+row.getLongueur()+METRES)
                                                )
                                        )
                                )
                        ).withStyle("width:60%")
                )
        ).render();
        return html;
    }

    /**
     * Cr��e une table afin d'extraire uniquement les donn�es � afficher sur la feuille de route.
     *
     * @param listeItineraire itin�raires de la tourn�e rang�es dans l'ordre
     * @return la liste des lignes de la feuille de route � faire traiter par le module html
     */
    private static List<LigneFeuille> buildTableDeRoute(LinkedList<Itineraire> listeItineraire) {
        List<LigneFeuille> mainList = new ArrayList<LigneFeuille>();
        int livraisonId = 1;
        Boolean lastIti = false;
        //boucle itin�raire
        for (Itineraire i : listeItineraire) {
            if (listeItineraire.getLast() == i) lastIti = true;
            PileTroncon lastPile = null;
            //add depart
            LigneFeuille ligneDepart = new LigneFeuille(
                    0,
                    ((PointLivraison) i.getNoeudOrigine()).getHeureDepart(),
                    Indication.DebutItineraire.getTexte(),
                    Integer.toString(livraisonId),
                    0);
            mainList.add(ligneDepart);
            //foreach tron�on
            for (Troncon t : i.getListeTroncons()) {
                if ((lastPile == null) || !(t.getNomRue().equals(lastPile.getTroncon().getNomRue()))) {
                    //premier troncon
                    if (i.getListeTroncons().getFirst() == t) {
                        lastPile = new PileTroncon(t, Indication.Demarrage);
                    } else {
                        //calcul indication
                        Vecteur u = new Vecteur(lastPile.getTroncon().getOrigine(), lastPile.getTroncon().getDestination());
                        Vecteur v = new Vecteur(t.getOrigine(), t.getDestination());
                        Indication indic = calculerIndication(u, v);
                        //nouvelle ligne feuille de la derniere pile
                        LigneFeuille ligne = new LigneFeuille(
                                0,
                                0,
                                lastPile.getIndication().getTexte(),
                                lastPile.getTroncon().getNomRue().equals("")?RUE_INCONNUE : lastPile.getTroncon().getNomRue(),
                                lastPile.getDistanceTotale());
                        //ajout ligne feuille
                        mainList.add(ligne);
                        //reset de la pile
                        lastPile = new PileTroncon(t, indic);
                    }
                } else {
                    lastPile.addTroncon(t);
                }
            }
            //add last pile
            LigneFeuille ligneDernierePile = new LigneFeuille(
                    0,
                    0,
                    lastPile.getIndication().getTexte(),
                    lastPile.getTroncon().getNomRue(),
                    lastPile.getDistanceTotale());
            mainList.add(ligneDernierePile);
            //add arrivee itineraire
            if (lastIti) {
                LigneFeuille ligneArrivee = new LigneFeuille(
                        ((PointLivraison) i.getNoeudDestination()).getHeureArrivee(),
                        0,
                        Indication.FinTournee.getTexte(),
                        "-",
                        0);
                mainList.add(ligneArrivee);
            } else {
                LigneFeuille ligneArrivee = new LigneFeuille(
                        ((PointLivraison) i.getNoeudDestination()).getHeureArrivee(),
                        0,
                        Indication.FinItineraire.getTexte(),
                        Integer.toString(livraisonId++),
                        0);
                mainList.add(ligneArrivee);
            }
        }

        return mainList;
    }

    /**
     * Calcule l'indication � prendre depuis une �num�ration Indication
     *
     * @param u Vecteur repr�sentant le tron�on de d�part
     * @param v Vecteur repr�sentant le tron�on � prendre
     * @return Item de l'�num�ration Indication
     */
    private static Indication calculerIndication(Vecteur u, Vecteur v) {
        double angle = Math.toDegrees(Math.atan2((u.x * v.y) - (u.y * v.x), (u.x * v.x) + (u.y * v.y)));
        if (angle >= -10 && angle <= 10) return Indication.ToutDroit;
        else if (angle > 0) return Indication.ADroite;
        else return Indication.AGauche;
    }

    /**
     * R�ordonne les itin�raires d'une tourn�e par recherche dans la liste des points de livraison
     * @param tournee la tourn�e calcul�e
     * @return la liste ordonn�e des itin�raires
     */
    private static LinkedList<Itineraire> ordonnerItineraires(Tournee tournee) {
        List<PointLivraison> listePoints = tournee.getListePointLivraisons();
        LinkedList<Itineraire> listeOrdonnee = new LinkedList<>();
        for (int i = 1; i < listePoints.size(); i++) {
            Map.Entry<PointLivraison, PointLivraison> key = new AbstractMap.SimpleEntry<>(listePoints.get(i - 1), listePoints.get(i));
            listeOrdonnee.add(tournee.getItinerairesMap().get(key));
        }
        return listeOrdonnee;
    }
}
