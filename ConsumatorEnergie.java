package Tema1;

public abstract class ConsumatorEnergie extends ComponentaRetea implements Comparable<ConsumatorEnergie> {
    private double cerereEnergie;
    protected int prioritate;
    private boolean esteAlimentat;

    public ConsumatorEnergie(String id, double cerereEnergie) {
        super(id);
        this.cerereEnergie = cerereEnergie;
        this.esteAlimentat = true;
    }

    public double getCerereCurenta(){
        if (this.esteAlimentat && this.isStatusOperational()) {
            return this.cerereEnergie;
        } else {
            return 0;
        }
    }

    public void cupleazaLaRetea() {
        this.esteAlimentat = true;
    }

    public void decupleazaDeLaRetea() {
        this.esteAlimentat = false;
    }

    public double getCerereEnergie() {
        return cerereEnergie;
    }

    public int getPrioritate() {
        return prioritate;
    }

    public boolean isAlimentat() {
        return esteAlimentat;
    }

//metoda compareto luata din lab5
    @Override
    public int compareTo(ConsumatorEnergie other) {
        return Integer.compare(other.prioritate, this.prioritate);
    }
}