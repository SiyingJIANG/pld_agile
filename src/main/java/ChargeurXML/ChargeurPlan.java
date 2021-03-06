package ChargeurXML;

import Modele.Noeud;
import Modele.Plan;
import Modele.Troncon;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author H4401
 *         Classe permettant le chargement du fichier xml contenant le plan
 */
public class ChargeurPlan {
    private Plan plan;
    private static ChargeurPlan instance;

    /**
     * Méthode Get pour l'instance
     *
     * @return instance
     */
    public static ChargeurPlan getInstance() {
        if (instance == null) {
            instance = new ChargeurPlan();
        }
        return instance;
    }

    /**
     * Constructeur par défaut vide
     */
    public ChargeurPlan() {

    }

    /**
     * Méthode parsant le fichier xml
     *
     * @param plan     le plan auquel on ajoute les noeuds et tronçons
     * @param filePath Le chemin d'accès au fichier xml
     */
    public boolean parse(Plan plan, String filePath) {
        this.plan = plan;
        File xmlPlan = new File(filePath);
        HashMap<Long, Noeud> nodeMap = new HashMap<>();

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            Document documentPlan = builder.parse(xmlPlan);
            Element rootElement = documentPlan.getDocumentElement();

            NodeList nodes = rootElement.getElementsByTagName("noeud");

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);

                if (node instanceof Element) {
                    Element child = (Element) node;
                    Long idAtt = Long.parseLong(child.getAttribute("id"));
                    Integer xAtt = Integer.parseInt(child.getAttribute("x"));
                    Integer yAtt = Integer.parseInt(child.getAttribute("y"));

                    Noeud noeud = new Noeud(idAtt, xAtt, yAtt);
                    plan.addNoeud(noeud);
                    nodeMap.put(idAtt, noeud);
                }
            }

            NodeList vertexes = rootElement.getElementsByTagName("troncon");

            for (int i = 0; i < vertexes.getLength(); i++) {
                Node node = vertexes.item(i);

                if (node instanceof Element) {
                    Element child = (Element) node;
                    Long destinationAtt = Long.parseLong(child.getAttribute("destination"));
                    String longueurAtt = child.getAttribute("longueur");
                    String rueAtt = child.getAttribute("nomRue");
                    Long origineAtt = Long.parseLong(child.getAttribute("origine"));

                    Noeud noeudOrigine = nodeMap.get(origineAtt);
                    Noeud noeudDestination = nodeMap.get(destinationAtt);
                    /*Set noeuds = plan.getListeNoeuds();

                    Iterator iterator = noeuds.iterator();
                    while (iterator.hasNext()) {
                        Noeud n = (Noeud) iterator.next();
                        if (n.getId() == Long.parseLong(origineAtt)) {
                            noeudOrigine = n;
                        }
                        if (n.getId() == Long.parseLong(destinationAtt)) {
                            noeudDestination = n;
                        }
                        if (noeudOrigine != null && noeudDestination != null) break;
                    }*/

                    Troncon troncon = new Troncon(noeudOrigine, noeudDestination, Double.parseDouble(longueurAtt), rueAtt);
                    noeudOrigine.addNeighbor(troncon);
                    plan.addTroncon(troncon);
                }
            }
            plan.setCharge(true);

        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Méthode parsant le fichier xml
     *
     * @param plan     le plan auquel on ajoute les noeuds et tronçons
     * @param filePath Le chemin d'accès au fichier xml
     */
    public void parseAutre(Plan plan, String filePath) {
        this.plan = plan;
        File xmlPlan = new File(filePath);

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;

        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            Document documentPlan = builder.parse(xmlPlan);
            Element rootElement = documentPlan.getDocumentElement();

            NodeList nodes = rootElement.getElementsByTagName("noeud");
            NodeList vertexes = rootElement.getElementsByTagName("troncon");

            for (int i = 0; i < vertexes.getLength(); i++) {
                Node node = vertexes.item(i);

                if (node instanceof Element) {
                    Element child = (Element) node;
                    Long destinationAtt = Long.parseLong(child.getAttribute("destination"));
                    Double longueurAtt = Double.parseDouble(child.getAttribute("longueur"));
                    String rueAtt = child.getAttribute("nomRue");
                    Long origineAtt = Long.parseLong(child.getAttribute("origine"));

                    Noeud noeudOrigine = null, noeudDestination = null;

                    for (int j = 0; j < nodes.getLength(); j++) {
                        Node node2 = nodes.item(j);

                        if (node2 instanceof Element) {
                            Element child2 = (Element) node2;
                            String idAtt = child2.getAttribute("id");
                            String xAtt = child2.getAttribute("x");
                            String yAtt = child2.getAttribute("y");

                            Noeud newNode = new Noeud(Long.parseLong(idAtt), Integer.parseInt(xAtt), Integer.parseInt(yAtt));

                            if ((long) newNode.getId() == (long) origineAtt) {
                                Iterator iterator = plan.getListeNoeuds().iterator();
                                boolean found = false;
                                while (iterator.hasNext()) {
                                    Noeud n = (Noeud) iterator.next();
                                    if ((long) n.getId() == newNode.getId()) found = true;
                                }
                                if (found == false) plan.addNoeud(newNode);

                                noeudOrigine = newNode;
                            }
                            if ((long) newNode.getId() == (long) destinationAtt) {
                                Iterator iterator = plan.getListeNoeuds().iterator();
                                boolean found = false;
                                while (iterator.hasNext()) {
                                    Noeud n = (Noeud) iterator.next();
                                    if ((long) n.getId() == newNode.getId()) found = true;
                                }
                                if (found == false) plan.addNoeud(newNode);

                                noeudDestination = newNode;
                            }
                            if (noeudOrigine != null && noeudDestination != null) break;
                        }
                    }

                    Troncon troncon = new Troncon(noeudOrigine, noeudDestination, longueurAtt, rueAtt);
                    noeudOrigine.addNeighbor(troncon);
                    plan.addTroncon(troncon);
                }
            }
            plan.setCharge(true);

        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get plan
     *
     * @return plan
     */
    public Plan getPlan() {
        return plan;
    }
}