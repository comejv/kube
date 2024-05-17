package kube.view;

import java.util.ArrayList;
import java.awt.Point;
import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.Move;
import kube.model.Mountain;
import kube.model.action.*;

public class TextualMode implements Runnable {

    Kube kube;
    Queue<Action> events;

    public TextualMode(Kube kube, Queue<Action> events) {
        this.kube = kube;
        this.events = events;
    }

    @Override
    public void run() {
        printWelcome();
        printStart();
        printHelp();
        while (true) {
            Action action = events.remove();
            switch (action.getType()) {
                case Action.PRINT_AI:
                    if (action.getData() != null) {
                        printAI();
                    } else {
                        printAI((int) action.getData());
                    }
                    break;
                case Action.PRINT_COMMAND_ERROR:
                    printCommandError();
                    printHelp();
                    break;
                case Action.PRINT_WAIT_COORDINATES:
                    printWaitCoordinates((int) action.getData());
                    break;
                case Action.PRINT_GOODBYE:
                    printGoodbye();
                    break;
                case Action.PRINT_HELP:
                    printHelp();
                    break;
                case Action.PRINT_LIST_MOVES:
                    printState();
                    printListMoves();
                    break;
                case Action.PRINT_MOVE:
                    printMove((Move) action.getData());
                    printState();
                    printHelp();
                    break;
                case Action.PRINT_MOVE_ERROR:
                    printMoveError();
                    printHelp();
                    break;
                case Action.PRINT_NEXT_PLAYER:
                    printNextPlayer();
                    break;
                case Action.PRINT_PLAYER:
                    if (action.getData() != null) {
                        printPlayer();
                    } else {
                        printPlayer((int) action.getData());
                    }
                    break;
                case Action.PRINT_RANDOM:
                    printRandom();
                    printState();
                    printHelp();
                    break;
                case Action.PRINT_REDO:
                    printRedo((Move) action.getData());
                    printState();
                    printHelp();
                    break;
                case Action.PRINT_REDO_ERROR:
                    printRedoError();
                    printState();
                    printHelp();
                    break;
                case Action.PRINT_STATE:
                    printState();
                    break;
                case Action.PRINT_SWAP:
                    printSwap();
                    printHelp();
                    break;
                case Action.PRINT_SWAP_ERROR:
                    printSwapError();
                    printHelp();
                    break;
                case Action.PRINT_SWAP_SUCCESS:
                    Swap s = (Swap) action.getData();
                    printSwapSuccess(s.getPos1(), s.getPos2());
                    printHelp();
                    break;
                case Action.PRINT_UNDO:
                    printUndo((Move) action.getData());
                    printState();
                    printHelp();
                    break;
                case Action.PRINT_UNDO_ERROR:
                    printState();
                    printUndoError();
                    printHelp();
                    break;
                case Action.PRINT_VALIDATE:
                    printValidate((boolean) action.getData());
                    printHelp();
                    break;
                case Action.PRINT_WELCOME:
                    printWelcome();
                    break;
                case Action.PRINT_WIN_MESSAGE:
                    printWinMessage((Player) action.getData());
                    break;
                case Action.PRINT_ASK_NB_PLAYERS:
                    askNbPlayers();
                    break;
                case Action.PRINT_ASK_GAME_MODE:
                    printGameMode();
                    break;
                case Action.PRINT_ASK_HOST_OR_JOIN:
                    printHostOrJoin();
                    break;
                case Action.PRINT_ASK_IP:
                    printAskIP();
                    break;
                default:
                    break;
            }
        }

    }

    public void printCommandPhase1() {
        System.out.println("Commandes disponibles : \n" +
                "-random : construit aléatoirement une tour\n" +
                "-echanger : permet d'échanger la position de 2 pièces de son choix\n" +
                "-valider : valider que sa montagne est prête\n" +
                "-afficher : affiche l'état de la base centrale et de sa montagne\n" +
                "Tour de " + kube.getCurrentPlayer().getName() + "\n" +
                "Vous devez consruire votre montagne. \n");
    }

    public void printCommandPhase2() {
        String s = "Commandes disponibles  : \n" +
                "-jouer : jouer un coup\n" +
                "-annuler : annuler le dernier coup\n" +
                "-rejouer : rejouer le dernier coup\n" +
                "-afficher : affiche l'état de la base centrale et de sa montagne\n" +
                "-aide : affiche cette liste\n" +
                "Tour de " + kube.getCurrentPlayer().getName() + "\n" +
                "Vous devez choisir une de vos pièces pour la mettre sur la montagne centrale.";
        System.out.println(s);
    }

    public void askNbPlayers() {
        System.out.println("Combien de joueurs? (0 -2)");
    }

    public void printAI() {
        System.out.println("Difficultée de l'IA? (1-3)");
    }

    public void printAI(int n) {
        System.out.println("Difficultée de l'IA" + n + "? (1-3)");
    }

    public void printCommandError() {
        System.out.println("Commande invalide. Tapez -aide pour afficher la liste des commandes.");
    }

    public void printWaitCoordinates(int i) {
        if (i == 1) {
            System.out.println("Entrez les coordonnées de la première pièce à déplacer");
        } else {
            System.out.println("Entrez les coordonnées de la deuxième pièce à déplacer");
        }
    }

    public void printGoodbye() {
        System.out.println("Merci d'avoir joué à Kube !");
    }

    public void printHelp() {
        if (kube.getPhase() == Kube.PREPARATION_PHASE) {
            printCommandPhase1();
        } else {
            printCommandPhase2();
        }
    }

    public ArrayList<Move> printListMoves() {
        try {
            ArrayList<Move> moves = kube.moveSet();
            System.out.println("Voici les coups possibles :");
            for (int i = 0; i < moves.size(); i++) {
                System.out.println(i + " : " + moves.get(i));
            }
            System.out.println("Entrez le numéro du coup que vous voulez jouer:");
            return moves;
        } catch (Exception e) {
            return null;
        }
    }

    public void printMove(Move move) {
        System.out.println("Vous avez joué : " + move);
    }

    public void printMoveError() {
        System.out.println("Numéro de coup invalide");
    }

    public void printNextPlayer() {
        if (kube.getPhase() == Kube.PREPARATION_PHASE) {
            System.out.println("C'est au tour de " + kube.getCurrentPlayer().getName());
        } else {
            System.out.println("C'est au tour de " + kube.getCurrentPlayer().getName());
        }
        printHelp();
        printState();
    }

    public void printPenality() {
        System.out.println("Votre adversaire a une pénalitée, choisissez une pièce à récuperer");
    }

    public void printPlayer() {
        System.out.println("Nom du joueur?");
    }

    public void printPlayer(int n) {
        System.out.println("Nom du joueur " + n + "?");
    }

    public void printRandom() {
        System.out.println("Votre montagne a été mélangée");
    }

    public void printRedo(Move move) {
        System.out.println("Coup " + move.toString() + " rejoué");
    }

    public void printRedoError() {
        System.out.println("Impossible de rejouer le coup");
    }

    public void printStart() {
        System.out.println("Combien de joueur?");
    }

    public void printState() {
        if (kube.getPhase() == Kube.PREPARATION_PHASE) {
            System.out.println(kube.getK3());
            System.out.println(kube.getCurrentPlayer());
        } else {
            System.out.println(kube.getK3());
            System.out.println(kube.getP1());
            System.out.println(kube.getP2());
            if (kube.getPenality()) {
                printPenality();
            }
        }
    }

    public void printSwap() {
        System.out.println("Entrez les coordonnées des deux pièces à échanger");
    }

    public void printSwapError() {
        System.out.println("Les coordonnées entrées ne sont pas valides");
    }

    public void printSwapSuccess(Point p1, Point p2) {
        printSwapSuccess(p1.x, p1.y, p2.x, p2.y);
    }

    public void printSwapSuccess(int x1, int y1, int x2, int y2) {
        Mountain m = kube.getCurrentPlayer().getMountain();
        System.out.println("Les pièces " + m.getCase(x1, y1) + " et " + m.getCase(x2, y2) + "ont été échangées");
    }

    public void printUndo(Move move) {
        System.out.println("Coup " + move.toString() + " annulé");
    }

    public void printUndoError() {
        System.out.println("Impossible d'annuler le coup");
    }

    public void printValidate(boolean b) {
        if (b) {
            System.out.println("Votre montagne a été validée");
        } else {
            System.out.println("Votre montagne n'est pas valide");
        }
    }

    public void printWelcome() {
        System.out.println("Bienvenue dans le jeu Kube !");
        System.out.println("Pour commencer, entrez 'start' ou 'exit' pour quitter.");
    }

    public void printWinMessage(Player winner) {
        System.out.println("Victoire de " + winner.getName() + ". Félicitations !");
        System.out.println("Plateau final : \n" + kube.getK3() + "\n");
        System.out.println(kube.getP1());
        System.out.println(kube.getP2());
    }

    public void printGameMode() {
        System.out.println("Mode de jeu : 1 pour local, 2 pour en ligne");
    }

    private void printAskIP() {
        System.out.println("Entrez l'adresse IP du serveur");
    }

    private void printHostOrJoin() {
        System.out.println("Entrez 1 pour héberger, 2 pour rejoindre");
    }

}
