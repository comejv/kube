package kube.view;

import java.util.Scanner;

import kube.controller.*;
import kube.model.Player;

public class TextualMode {

    Game game;
    Scanner sc;

    public static void main(String[] args) {
        System.out.println("Bienvenue dans K3 !");
        TextualMode tm = new TextualMode();
        tm.sc = new Scanner(System.in);
        String s;
        boolean end = false;
        System.out.println("Tapez 'start' pour commencer une partie ou 'exit' pour quitter");
        while (!end) {
            s = tm.sc.nextLine();
            switch (s) {
                case "start":
                    tm.startGame();
                    break;
                case "exit":
                    end = true;
                    System.out.println("Merci d'avoir joué !");
                    break;
                case "":
                    break;
                default:
                    System.out.println("Commande inconnue");
            }
        }
    }

    public void startGame() {
        game = new Game(askNbPlayers());

        for (int i = 0; i < game.getNbPlayers(); i++) {
            phase1();
        }
        Player winner = phase2();
        System.out.println("Victoire de " + winner.getName() + ". Félicitations !");
        System.out.println(game.getKube().getK3());
        System.out.println(game.getKube().getP1());
        System.out.println(game.getKube().getP2());
    }

    public int askNbPlayers() {
        int nbPlayers = 2;
        //TODO 
        // System.out.println("Combien de joueurs ?");
        // String s = sc.next();
        // nbPlayers = Integer.parseInt(s);
        // while (nbPlayers < 0 || nbPlayers > 4) {
        //     System.out.println("Le nombre de joueurs doit être compris entre 0 et 4");
        //     nbPlayers = sc.nextInt();
        // }
        return nbPlayers;
    }

    public void afficherCommandePahse1() {
        System.out.println("Tapez une des commandes suivantes : \n" +
                "-random : construit aléatoirement une tour\n" +
                "-echanger : permet d'échanger la position de 2 pièces de son choix\n" +
                "-valider : valider que sa montagne est prête" +
                "-afficher : affiche l'état de la base centrale et de sa montagne");
    }

    public void afficherCommandePhase2(){
        System.out.println("Tapez une des commandes suivantes : \n" +
                "-jouer : jouer un coup\n" +
                "-annuler : annuler le dernier coup\n" +
                "-rejouer : rejouer le dernier coup\n" +
                "-afficher : affiche l'état de la base centrale et de sa montagne\n" +
                "-aide : affiche cette liste\n");
    }

    public void phase1() {
        game.setPhase(1);
        String s;
        boolean end = false;
        System.out.println("Première phase - Construction de la montagne du joueur "
                + game.getKube().getCurrentPlayer().getId() + " :");
        afficherCommandePahse1();
        game.randomizeMoutain();
        System.out.println("Voici la base centrale :\n" + game.printK3());
        System.out.println("Votre montagne tirée de manière aléatoire :\n"
                + game.getKube().getCurrentPlayer().getMountain().toString());
        sc.reset();
        while (!end) {
            s = sc.nextLine();
            switch (s) {
                case "random":
                    game.randomizeMoutain();
                case "afficher":
                    System.out.print(game.getKube().getCurrentPlayer()); // Print the mountain & the additionals
                    break;
                case "echanger":
                    try {
                        System.out.println("Entrez les coordonnées de la première pièce :");
                        s = sc.nextLine();
                        String[] coords = s.split(" ");
                        int x1 = Integer.parseInt(coords[0]);
                        int y1 = Integer.parseInt(coords[1]);
                        System.out.println("Entrez les coordonnées de la deuxième pièce :");
                        s = sc.nextLine();
                        coords = s.split(" ");
                        int x2 = Integer.parseInt(coords[0]);
                        int y2 = Integer.parseInt(coords[1]);
                        System.out.println(game.swap(x1, y1, x2, y2));
                    } catch (Exception e) {
                        System.out.println("Erreur de saisie");
                        break;
                    }
                    break;
                case "valider":
                    end = true;
                    System.out.println("");
                    break;
                case "":
                    break;
                default:
                    System.out.println("Commande inconnue");
                    break;
            }
        }
        game.nextPlayer();
    }

    // Return the winner
    public Player phase2() {
        game.setPhase(2);
        String s;
        boolean end = false;
        System.out.println("Deuxième phase - Jeu :");
        while (!game.isOver()) {
            System.out.println("Tour du joueur " + game.getKube().getCurrentPlayer().getId());
            System.out.println("Voici la base centrale :");
            System.out.println(game.printK3());
            System.out.println("Voici votre montagne :");
            System.out.println(game.printMountain(game.getKube().getCurrentPlayer()));
            afficherCommandePhase2();
            while (!end) {
                s = sc.nextLine();
                switch (s) {
                    case "afficher":
                        System.out.println(game.printK3());
                        System.out.print(game.getKube().getCurrentPlayer());
                        break;
                    case "jouer":
                        end = playMove();
                        break;
                    case "annuler":
                        game.undo();
                        break;
                    case "rejouer":
                        game.redo();
                        break;
                    case "aide":
                        afficherCommandePhase2();
                        break;
                    case "":
                        break;
                    default:
                        System.out.println("Commande inconnue");
                        break;
                }
            }
            end = false;
        }
        return game.getKube().getCurrentPlayer();

    }

    public boolean playMove() {
        System.out.println("Voici les coups possibles :");
        System.out.println(game.listMove());
        System.out.println("Entrez le numéro du coup que vous voulez jouer:");
        String s = sc.nextLine();
        try {
            Integer.parseInt(s);
        } catch (Exception e) {
            System.out.println("Numéro invalide");
            return false;
        }
        if (!game.playMove(Integer.parseInt(s))) {
            System.out.println("Numéro invalide invalide");
            return false;
        }
        return true;
    }
}
