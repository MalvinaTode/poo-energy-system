package Tema1;

public  class Baterie extends ComponentaRetea {
    double capacitateMaxima;
    double energieStocata;

    public Baterie(String id, double capacitateMaxima) {
        super(id);
        this.capacitateMaxima = capacitateMaxima;
        this.energieStocata = 0;
    }

    public double incarca(double energieDisponibila) {
        double loc = capacitateMaxima - energieStocata;

        if (energieDisponibila <= loc) {
            energieStocata += energieDisponibila;
            return 0;
        } else {
            energieStocata = capacitateMaxima;
            return energieDisponibila - loc;
        }
    }

    public double descarca(double energieCeruta) {
        double furnizat;
        if (energieStocata >= energieCeruta) {
            energieStocata -= energieCeruta;
            return energieCeruta;
        } else {
            furnizat = energieStocata;
            energieStocata = 0;
            return furnizat;
        }
    }
    public double getCapacitateMaxima() {
        return capacitateMaxima;
    }

    public double getEnergieStocata() {
        return energieStocata;
    }

}
