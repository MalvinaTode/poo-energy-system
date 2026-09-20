package Tema1;

public class PanouSolar extends ProducatorEnergie {

    private double putereMaxima;

    public PanouSolar(String id, double putereMaxima) {
        super(id);
        this.putereMaxima = putereMaxima;
    }

    public double calculeazaProductie(double factorExtern) {
        return putereMaxima * factorExtern;
    }

    public double getPutereMaxima() {
        return putereMaxima;
    }
}