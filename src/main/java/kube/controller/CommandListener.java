package kube.controller;

import kube.model.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import kube.model.Kube;
import kube.model.Player;
import kube.model.ai.utilsAI;
import kube.model.move.Move;
import kube.view.TextualMode;

public class CommandListener implements Runnable {

    String command;
    Kube k3;
    TextualMode tm;

    public CommandListener(Kube k3, TextualMode tm) {
        this.k3 = k3;
        this.tm = tm;
    }

    @Override
    public void run() {
        Player currentPlayerToBuild = k3.getP1();
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            switch (sc.nextLine()) {
                case "random":
                    utilsAI.randomFillMountain(currentPlayerToBuild, new Random());
                    System.out.print(currentPlayerToBuild); // Print the mountain & the additionals
                    break;
                case "echanger":
                    if (swap(currentPlayerToBuild, sc)) {
                        System.out.println(currentPlayerToBuild); // Print the mountain & the additionals
                    } else {
                        System.out.println("Erreur lors de la lecture des coordonnées");
                    }
                    break;
                case "afficher":
                    if (k3.getPhase() == Kube.preparationPhase) {
                        System.out.println(k3.getK3());
                        System.out.println(currentPlayerToBuild);
                    } else {
                        System.out.println(k3.getK3());
                        System.out.println(k3.getP1());
                        System.out.println(k3.getP2());
                    }
                    break;
                case "valider":
                    if (!currentPlayerToBuild.getMountain().isFull()) {
                        System.out.println("Vous n'avez pas rempli votre montagne !");
                        break;
                    }
                    if (currentPlayerToBuild == k3.getP1()) {
                        currentPlayerToBuild = k3.getP2();
                    } else {
                        k3.setPhase(Kube.gamePhase);
                    }
                    break;
                case "jouer":
                    playMove(sc);
                    break;
                case "annuler":
                    k3.unPlay();
                    break;
                case "rejouer":
                    k3.rePlay();
                    break;
                case "":
                    break;
                default:
                    System.out.println("Commande inconnue");
                    break;
            }
            System.out.println("\n");
            tm.update();
        }
    }

    public boolean swap(Player p, Scanner sc) {
        try {
            System.out.println("Entrez les coordonnées de la première pièce :");
            String s = sc.nextLine();
            String[] coords = s.split(" ");
            int x1 = Integer.parseInt(coords[0]);
            int y1 = Integer.parseInt(coords[1]);
            System.out.println("Entrez les coordonnées de la deuxième pièce :");
            s = sc.nextLine();
            coords = s.split(" ");
            int x2 = Integer.parseInt(coords[0]);
            int y2 = Integer.parseInt(coords[1]);
            Color c = p.unbuildFromMoutain(x1, y1);
            Color c2 = p.unbuildFromMoutain(x2, y2);
            if (c == Color.EMPTY || c2 == Color.EMPTY) {
                return false;
            }
            p.buildToMoutain(x1, y1, c2);
            p.buildToMoutain(x2, y2, c);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean playMove(Scanner sc) {
        ArrayList<Move> moves = k3.moveSet();
        System.out.println("Voici les coups possibles :");
        System.out.println(moves);
        System.out.println("Entrez le numéro du coup que vous voulez jouer:");
        String s = sc.nextLine();
        try {
            int n = Integer.parseInt(s);
            return k3.playMove(moves.get(n));
        } catch (Exception e) {
            System.out.println("Numéro invalide");
            return false;
        }
    }
}
