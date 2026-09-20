package Tema1;

import java.io.InputStream;
import java.util.Scanner;

public class App {

    private Scanner scanner;
    private GridController controller = new GridController();

    public App(InputStream input) {
        this.scanner = new Scanner(input);
    }

    public void run() {
        // Implementați aici cerințele din enunț
        // Pentru citirea datelor de la tastatura se folosește câmpul scanner.
        while (true) {

            if (!scanner.hasNextLine()) {
                return;
            }

            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(" ");

            if (parts.length == 0 || parts[0].isEmpty()) {
                continue;
            }

            int command = -1;

            try {
                command = Integer.parseInt(parts[0]);
            } catch (Exception e) {
                System.out.println("EROARE: Comanda necunoscuta.");
                continue;
            }

            switch (command) {

                case 0: // producator
                    if (parts.length != 4) {
                        System.out.println("EROARE: Format comanda invalid");
                    } else {
                        controller.addProducator(parts[1], parts[2], parts[3]);
                    }
                    break;

                case 1: // consumator
                    if (parts.length != 4) {
                        System.out.println("EROARE: Format comanda invalid");
                    } else {
                        controller.addConsumator(parts[1], parts[2], parts[3]);
                    }
                    break;

                case 2: // baterie
                    if (parts.length != 3) {
                        System.out.println("EROARE: Format comanda invalid");
                    } else {
                        controller.addBaterie(parts[1], parts[2]);
                    }
                    break;

                case 3: // tick
                    if (parts.length != 3) {
                        System.out.println("EROARE: Format comanda invalid");
                    } else {
                        controller.nextTick(parts[1], parts[2]);
                    }
                    break;

                case 4: // defect/op
                    if (parts.length != 3) {
                        System.out.println("EROARE: Format comanda invalid");
                    } else {
                        controller.setDefect(parts[1], parts[2]);
                    }
                    break;

                case 5:
                    controller.statusGrid();
                    break;

                case 6:
                    controller.istoricEvenimente();
                    break;

                case 7:
                    System.out.println("Simulatorul se inchide.");
                    return;

                default:
                    System.out.println("EROARE: Comanda necunoscuta.");
                    break;
            }
        }
    }


    public static void main(String[] args) {
        App app = new App(System.in);
        app.run();
    }
}