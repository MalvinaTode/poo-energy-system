package Tema1;

public class ReactorNuclear extends ProducatorEnergie{

    private double putereConstanta;

    public ReactorNuclear(String id, double putereConstanta) {
        super(id);
        this.putereConstanta = putereConstanta;
    }

    public double calculeazaProductie(double factorExtern) {
        return putereConstanta;
    }

    public double getPutereConstanta() {
        return putereConstanta;
    }
}