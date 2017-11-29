package Modele;


/**
 * Created by flavi on 2017/11/18.
 */
public class PointLivraison extends Noeud {
    private double debutPlage;
    private double finPlage;
    private double duree;
    private double heureDepart;
    private double heureArrivee;

    public double getDebutPlage() {
        return debutPlage;
    }

    public double getFinPlage() {
        return finPlage;
    }

    public double getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(double heureDepart) {
        this.heureDepart = heureDepart;
    }

    public double getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(double heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public PointLivraison(Long id, int x, int y, double duree, double debutPlage, double finPlage) {

        super(id, x, y);
        this.duree=duree;
        this.debutPlage = debutPlage;
        this.finPlage = finPlage;
    }

    public PointLivraison(Long id, int x, int y, double duree) {
        super(id, x, y);
        this.duree = duree;
        this.debutPlage = -1;
        this.finPlage = -1;
    }

    public double getDuree() {
        return duree;
    }

    @Override
    public String toString() {
        return "PointLivraison{" +
                "id=" + id +
                "debutPlage=" + debutPlage +
                ", finPlage=" + finPlage +
                ", duree=" + duree +
                '}';
    }
}

