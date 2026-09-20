package Tema1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridController {
    //liste pt componenetele din retea
    private List<ProducatorEnergie> producatori;
    private List<ConsumatorEnergie> consumatori;
    private List<Baterie> baterii;
    private List<ComponentaRetea> toateComponentele;
    private List<String> istoricEvenimente; //pt istoric

    //stare retea
    private boolean esteInBlackout;
    private int tickCurent;

    public GridController() {
        this.producatori = new ArrayList<>();
        this.consumatori = new ArrayList<>();
        this.baterii = new ArrayList<>();
        this.toateComponentele = new ArrayList<>();
        this.istoricEvenimente = new ArrayList<>();
        this.esteInBlackout = false;
        this.tickCurent = 0;
    }

    //functie pentru parsare,luata din labul8
    private Double parseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    //comanda0->aduaga producator
    public void addProducator(String tip, String id, String putereStr) {
        //verific daca exista id ul deja
        for (ComponentaRetea comp : toateComponentele) {
            if (comp.getId().equals(id)) {
                System.out.println("EROARE: Exista deja o componenta cu id-ul " + id);
                return;
            }
        }

        // verific puterea
        Double putere = parseDouble(putereStr);
        if (putere <= 0) {
            System.out.println("EROARE: Putere invalida");
            return;
        }

        ProducatorEnergie producator = null;

        if (tip.equals("solar")) {
            producator = new PanouSolar(id, putere);
        } else if (tip.equals("turbina")) {
            producator = new TurbinaEoliana(id, putere);
        } else if (tip.equals("reactor")) {
            producator = new ReactorNuclear(id, putere);
        } else {
            System.out.println("EROARE: Tip producator invalid");
            return;
        }

        producatori.add(producator);
        toateComponentele.add(producator);
        System.out.println("S-a adaugat producatorul " + id + " de tip " + tip);
    }

    // comanda1->adaug consumator
    public void addConsumator(String tip, String id, String cerereStr) {
        // verific daca id ul este deja
        for (ComponentaRetea comp : toateComponentele) {
            if (comp.getId().equals(id)) {
                System.out.println("EROARE: Exista deja o componenta cu id-ul " + id);
                return;
            }
        }

        // cerific cererea
        Double cerere = parseDouble(cerereStr);
        if (cerere == null || cerere <= 0) {
            System.out.println("EROARE: Cerere putere invalida");
            return;
        }

        //creez consumator in functie de tip
        ConsumatorEnergie consumator = null;

        if (tip.equals("suport_viata")) {
            consumator = new SistemSuportViata(id, cerere);
        } else if (tip.equals("laborator")) {
            consumator = new LaboratorStiintific(id, cerere);
        } else if (tip.equals("iluminat")) {
            consumator = new SistemIluminat(id, cerere);
        } else {
            System.out.println("EROARE: Tip consumator invalid");
            return;
        }
        //il adaug
        consumatori.add(consumator);
        toateComponentele.add(consumator);
        System.out.println("S-a adaugat consumatorul " + id + " de tip " + tip);
    }

    // cmd 2->adaug baterie
    public void addBaterie(String id, String capacitateStr) {
        // verific daca id ul exista deja
        for (ComponentaRetea comp : toateComponentele) {
            if (comp.getId().equals(id)) {
                System.out.println("EROARE: Exista deja o componenta cu id-ul " + id);
                return;
            }
        }

        // verific capacitatea
        Double capacitate = parseDouble(capacitateStr);
        if  (capacitate <= 0) {
            System.out.println("EROARE: Capacitate invalida");
            return;
        }

        //adaug bateria
        Baterie baterie = new Baterie(id, capacitate);
        baterii.add(baterie);
        toateComponentele.add(baterie);
        System.out.printf("S-a adaugat bateria %s cu capacitatea %.2f%n", id, capacitate);
    }



    // cmd 3->simuleazatick
    public void nextTick(String factorSoareStr, String factorVantStr) {
        // daca sunt deja in blackout
        if (esteInBlackout) {
            System.out.println("EROARE: Reteaua este in BLACKOUT. Simulare oprita.");
            return;
        }

        //verific factorii
        Double factorSoare = parseDouble(factorSoareStr);
        Double factorVant = parseDouble(factorVantStr);

        if (factorSoare == null || factorVant == null) {
            System.out.println("EROARE: Factori invalizi");
            return;
        }

        tickCurent = tickCurent + 1;

        // resetez consumatorii cuplati
        for (ConsumatorEnergie cons : consumatori) {
            cons.cupleazaLaRetea();
        }

        // calculez Productia totala
        double productieTotala = 0;
        for (ProducatorEnergie prod : producatori) {
            if (prod.isStatusOperational()) {
                if (prod instanceof PanouSolar) {
                    productieTotala += prod.calculeazaProductie(factorSoare);
                } else if (prod instanceof TurbinaEoliana) {
                    productieTotala += prod.calculeazaProductie(factorVant);
                } else if (prod instanceof ReactorNuclear) {
                    productieTotala += prod.calculeazaProductie(0);
                }
            }
        }

        //calculez cererea totala
        double cerereTotala = 0;
        for (ConsumatorEnergie cons : consumatori) {
            cerereTotala = cerereTotala + cons.getCerereCurenta();
        }

        // calculez balanta
        double delta = productieTotala - cerereTotala;
        List<String> decuplati = new ArrayList<>();

        // scenariul1 delta>0
        if (delta > 0) {
            for (Baterie bat : baterii) {
                if (bat.isStatusOperational()) {
                    delta = bat.incarca(delta);
                }
            }
        }
        // scenariu2 delta<0
        else if (delta < 0) {
            double deficit = -delta;

            // descarc baterii
            for (Baterie bat : baterii) {
                if (bat.isStatusOperational() && deficit > 0) {
                    double furnizat = bat.descarca(deficit);
                    deficit = deficit - furnizat;
                }
            }

            // scenariu3
            if (deficit > 0) {
                // Sortare exact ca în lab5 creez lista si apelez Collections.sort()
                List<ConsumatorEnergie> consumatoriSortati = new ArrayList<>(consumatori);
                Collections.sort(consumatoriSortati);

                for (ConsumatorEnergie cons : consumatoriSortati) {
                    if (deficit > 0 && cons.getPrioritate() > 1 && cons.isAlimentat() && cons.isStatusOperational()) {

                        // Decuplare
                        cons.decupleazaDeLaRetea();
                        deficit -= cons.getCerereEnergie(); // deficit = deficit - cerere

                        // Istoric
                        decuplati.add(cons.getId());
                        String eveniment = "Tick " + tickCurent + ": Deficit - Decuplat " + cons.getId();
                        istoricEvenimente.add(eveniment);
                    }
                }

                //scenariu4
                if (deficit > 0) {
                    esteInBlackout = true;
                    String eveniment = "Tick " + tickCurent + ": BLACKOUT! SIMULARE OPRITA.";
                    istoricEvenimente.add(eveniment);
                    System.out.println("BLACKOUT! SIMULARE OPRITA.");
                    return;
                }
            }
        }

        // energia totala din baterii
        double energieTotalaBaterii = 0;
        for (Baterie bat : baterii) {
            energieTotalaBaterii = energieTotalaBaterii + bat.getEnergieStocata();
        }
        //afisez
        String listaDecuplati = decuplati.toString();

        System.out.printf("TICK: Productie %.2f, Cerere %.2f. Baterii: %.2f MW. Decuplati: %s%n",
                productieTotala, cerereTotala, energieTotalaBaterii, listaDecuplati);
    }

    // comanda4
    public void setDefect(String id, String statusStr) {
        ComponentaRetea componenta = null;
        for (ComponentaRetea comp : toateComponentele) {
            if (comp.getId().equals(id)) {
                componenta = comp;
                break;
            }
        }

        if (componenta == null) {
            System.out.println("EROARE: Nu exista componenta cu id-ul " + id);
            return;
        }

        //gasesc status
        boolean status;
        if (statusStr.equals("true")) {
            status = true;
        } else if (statusStr.equals("false")) {
            status = false;
        } else {
            System.out.println("EROARE: Status invalid");
            return;
        }

        componenta.setStatusOperational(status);

        if (status) {
            System.out.println("Componenta " + id + " este acum operationala.");
        } else {
            System.out.println("Componenta " + id + " este acum defecta.");
        }
    }



    // cmd 5
    public void statusGrid() {
        if (toateComponentele.isEmpty()) {
            System.out.println("Reteaua este goala.");
            return;
        }

        if (esteInBlackout) {
            System.out.println("Stare Retea: BLACKOUT");
            System.out.println("EROARE: Reteaua este in BLACKOUT. Simulare oprita.");
        } else {
            System.out.println("Stare Retea: STABILA");
        }

        for (ProducatorEnergie prod : producatori) {
            String status;
            if (prod.isStatusOperational()) {
                status = "Operational";
            } else {
                status = "Defect";
            }

            String tip = " ";
            double valoarePutere = 0;

            if (prod instanceof PanouSolar) {
                valoarePutere = ((PanouSolar) prod).getPutereMaxima();
                tip = "PanouSolar";
            } else if (prod instanceof TurbinaEoliana) {
                valoarePutere = ((TurbinaEoliana) prod).getPutereBaza();
                tip = "TurbinaEoliana";
            } else if (prod instanceof ReactorNuclear) {
                valoarePutere = ((ReactorNuclear) prod).getPutereConstanta();
                tip = "ReactorNuclear";
            }

            System.out.printf("Producator %s (%s) - PutereBaza: %.2f - Status: %s%n",
                    prod.getId(), tip, valoarePutere, status);
        }


        for (ConsumatorEnergie cons : consumatori) {
            String statusFinal;
            if (cons.isAlimentat()) {
                statusFinal = "Alimentat";
            } else {
                statusFinal = "Decuplat";
            }

            String tip = " ";

            if (cons instanceof SistemSuportViata) {
                tip = "SistemSuportViata";
            } else if (cons instanceof LaboratorStiintific) {
                tip = "LaboratorStiintific";
            } else if (cons instanceof SistemIluminat) {
                tip = "SistemIluminat";
            }

            System.out.printf("Consumator %s (%s) - Cerere: %.2f - Prioritate: %d - Status: %s%n",
                    cons.getId(), tip, cons.getCerereEnergie(),
                    cons.getPrioritate(), statusFinal);
        }


        for (Baterie bat : baterii) {
            String status;
            if (bat.isStatusOperational()) {
                status = "Operational";
            } else {
                status = "Defect";
            }

            System.out.printf("Baterie %s - Stocare: %.2f/%.2f - Status: %s%n",
                    bat.getId(), bat.getEnergieStocata(),
                    bat.getCapacitateMaxima(), status);
        }
    }

    //cmd6
    public void istoricEvenimente() {
        if (istoricEvenimente.isEmpty()) {
            System.out.println("Istoric evenimente gol.");
            return;
        }

        for (String eveniment : istoricEvenimente) {
            System.out.println(eveniment);
        }
    }
}