package kube.view;

import java.util.Random;
import java.util.Scanner;

import kube.configuration.Config;
import kube.controller.*;
import kube.model.Player;
import kube.model.ai.AI;
import kube.model.move.Move;

public class TextualMode {

    Game game;
    Scanner sc;

    public static void main(String[] args) {
        System.out.println("Bienvenue dans K3 !");
        TextualMode tm = new TextualMode();
        tm.sc = new Scanner(System.in);
        String s;
        boolean end = false;
        while (!end) {
            System.out.println("Tapez 'start' pour commencer une partie ou 'exit' pour quitter");
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
        String s;

        if (game.getNbPlayers() == 0) {
            System.out.println("Voulez-vous voir l'IA jouer ? (O/N)");
            s = sc.nextLine();
            Boolean show = (s.equals("O") || s.equals("o"));
            Random r = new Random();
            if (r.nextInt(2) == 1) {
                game.setCurrentPlayer(game.getKube().getP1());
            } else {
                game.setCurrentPlayer(game.getKube().getP2());
            }
            winMessage(AIVsAI(show));
        } 
        else {
            System.out.println("Qui commence ? (1/2), entrée vide pour aléatoire");
            s = sc.nextLine();
            for (int i = 0; i < game.getNbPlayers(); i++) {
                phase1();
            }
            if (s.equals("1")) {
                game.setCurrentPlayer(game.getKube().getP1());
            } else if (s.equals("2")) {
                game.setCurrentPlayer(game.getKube().getP2());
            } else {
                Random r = new Random();
                if (r.nextInt(2) == 1) {
                    game.setCurrentPlayer(game.getKube().getP1());
                } else {
                    game.setCurrentPlayer(game.getKube().getP2());
                }
            }
            winMessage(phase2());
        }
    }
    


    public int askNbPlayers() {
        int nbPlayers = 2;
        System.out.println("Combien de joueurs ?");
        String s = sc.nextLine();
        try {
            nbPlayers=Integer.parseInt(s);
        } catch (Exception e) {
            System.out.println("Nombre invalide");
            return askNbPlayers();
        }
        while (nbPlayers < 0 || nbPlayers > 2) {
            System.out.println("Le nombre de joueurs doit être compris entre 0 et 2");
            try {
                nbPlayers = Integer.parseInt(s);
            } catch (Exception e) { 
                System.out.println("Nombre invalide");
                return askNbPlayers();
            }
        }
        return nbPlayers;
    }

    public void afficherCommandePahse1() {
        System.out.println("Tapez une des commandes suivantes : \n" +
                "-random : construit aléatoirement une tour\n" +
                "-echanger : permet d'échanger la position de 2 pièces de son choix\n" +
                "-valider : valider que sa montagne est prête" +
                "-afficher : affiche l'état de la base centrale et de sa montagne");
    }

    public void afficherCommandePhase2() {
        System.out.println("Tapez une des commandes suivantes : \n" +
                "-jouer : jouer un coup\n" +
                "-annuler : annuler le dernier coup\n" +
                "-rejouer : rejouer le dernier coup\n" +
                "-afficher : affiche l'état de la base centrale et de sa montagne\n" +
                "-aide : affiche cette liste\n");
    }

    public void phase1() {
        game.setPhase(1);
        if (game.getCurrentPlayer() == game.getPlayer(2) && game.getNbPlayers() == 1) {
            game.nextPlayer();
            return;
        }
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
                    System.out.print(game.getKube().getCurrentPlayer()); // Print the mountain & the additionals
                    break;
                case "valider":
                    end = true;
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
            if (game.getNbPlayers() == 1 && game.getCurrentPlayer() == game.getPlayer(2)) {
                if (game.isPenality()) {
                    playAI(true);
                }
                playAI(true);
                if (game.isOver()) {
                    continue;
                }
            }
            System.out.println("Tour du joueur " + game.getKube().getCurrentPlayer().getId());
            System.out.println("Voici la base centrale :");
            System.out.println(game.printK3());
            System.out.println("Voici votre montagne :");
            System.out.println(game.getKube().getCurrentPlayer());
            afficherCommandePhase2();
            while (!end) {
                if (game.isPenality()) {
                    playPenality();
                }

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
        if(!game.getHistory().getDone().isEmpty()){ // If the history is empty, it's the second player who wins
            game.nextPlayer();    
        }
        return game.getKube().getCurrentPlayer();

    }

    public Player AIVsAI(boolean show) {
        game.setPhase(2);
        if (show) {
            System.out.println("IA" + game.getKube().getCurrentPlayer().getId() + " commence.");
        }
        while (!game.isOver()) {
            if (show) {
                System.out.println("Tour de l'IA " + game.getKube().getCurrentPlayer().getId());
                System.out.println(game.printK3());
                System.out.print(game.getKube().getCurrentPlayer());
            }
            playAI(show);
        }

        if (!game.getHistory().getDone().isEmpty()) { // If the history is empty, it's the second player who wins
            game.nextPlayer();
        }
        return game.getKube().getCurrentPlayer();

    }
    
    public boolean playAI(boolean show) {
        AI currentAI = game.getCurrentAI();
        boolean ret = false;
        try {
            Move m = currentAI.nextMove();
            ret = game.playMove(m);
            if (show) {
                System.out.println("L'IA"+game.getCurrentPlayer().getId() +" a joué : " + m.toString());
            }
        } catch (Exception e) {
            System.err.println("Erreur de l'IA");
        }
        return ret;
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

    public boolean playPenality() {
        System.out.println("Votre adversaire a une penalité, choisissez une pièce à recupérer:\n Sa Montagne :");
        if (game.getCurrentPlayer() == game.getPlayer(1)) {
            System.out.println(game.getPlayer(2));
        } else {
            System.out.println(game.getPlayer(1));
        }
        System.out.println("Votre Montagne :\n" + game.getCurrentPlayer());
        return playMove();
    }
    

    public void winMessage(Player winner) {
        System.out.println("Victoire de " + winner.getName() + ". Félicitations !");
        System.out.println("Plateau final : \n" + game.getKube().getK3() + "\n");
        System.out.println(game.getKube().getP1());
        System.out.println(game.getKube().getP2());
    }
}
