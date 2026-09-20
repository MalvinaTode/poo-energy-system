package Tema1;

public class TurbinaEoliana extends ProducatorEnergie {

    private double putereBaza;

    public TurbinaEoliana(String id, double putereBaza) {
        super(id);
        this.putereBaza = putereBaza;
    }

    public double calculeazaProductie(double factorExtern) {
        return putereBaza * factorExtern;
    }

    public double getPutereBaza() {
        return putereBaza;
    }
}