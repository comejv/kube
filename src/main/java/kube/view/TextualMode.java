package kube.view;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import kube.configuration.Config;
import kube.controller.*;

public class TextualMode {

    Game game;
    Scanner sc;
        

    public static void main(String[] args) {
        System.out.println("Bienvenue dans K3 !");
        TextualMode tm = new TextualMode();
        tm.sc = new Scanner(System.in);
        String s;
        // while(tm.sc.hasNextLine() && (s = tm.sc.nextLine()) != "exit"){
        //     switch (s){
        //         case "start":
        //             tm.game = new Game(tm.askNbPlayers());
        //             break;
        //         case "exit":
        //             System.out.println("Merci d'avoir joué !");
        //             break;
        //         default:
        //             System.out.println("Commande inconnue");
        //     }
        // }
        tm.game = new Game(tm.askNbPlayers());
        

        for (int i = 0; i < tm.game.getNbPlayers(); i++) {
            Config.debug("Player " + tm.game.getKube().getCurrentPlayer().getId());
            tm.phase1();
        }

     }

    public int askNbPlayers() {
        System.out.println("Combien de joueurs ?");
        int nbPlayers;
        String s= sc.next();
        nbPlayers = Integer.parseInt(s);
        while (nbPlayers<0 || nbPlayers>4) {
            System.out.println("Le nombre de joueurs doit être compris entre 0 et 4");
            nbPlayers = sc.nextInt();
        }
        Config.debug("nbPlayers: " + s);

        return nbPlayers;
    }

    public static void afficherCommandePahse1(){
        System.out.println("Tapez une des commandes suivantes : \n" +
                "-random : construit aléatoirement une tour\n" +
                "-echanger : permet d'échanger la position de 2 pièces de son choix\n" +
                "-valider : valider que sa montagne est prête" +
                "-afficher : affiche l'état de la base centrale et de sa montagne");
    }



    public void phase1() {
        String s;
        boolean end = false;
        System.out.println("Première phase - Construction de la montagne du joueur " + game.getKube().getCurrentPlayer().getId() + " :");
        afficherCommandePahse1();
        System.out.println("Votre montagne tirée de manière aléatoire :\n" + game.getKube().getCurrentPlayer().getMountain().toString());
        while (sc.hasNextLine() && !end) {
            s = sc.nextLine();
            switch (s){
                case "random":
                    game.randomizeMoutain();
                case "afficher":
                    System.out.print(game.getKube().getCurrentPlayer().getMountain().toString());
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
                default:
                    System.out.println("Commande inconnue");
                    break;
            }
        }
        game.nextPlayer();
    }
}
