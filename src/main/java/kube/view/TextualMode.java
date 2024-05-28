package kube.view;

import java.awt.Point;
import java.util.ArrayList;

import kube.model.Kube;
import kube.model.Mountain;
import kube.model.Player;
import kube.model.action.Action;
import kube.model.action.ActionType;
import kube.model.action.Queue;
import kube.model.action.Swap;
import kube.model.action.move.Move;

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
        boolean waitOtherTurn = false;
        while (true) {
            Action action = events.remove();
            if (waitOtherTurn && action.getType() != ActionType.ITS_YOUR_TURN
                    && action.getType() != ActionType.PRINT_STATE) {
                printOtherPlayerTurn();
                continue;
            }
            switch (action.getType()) {
                case PRINT_AI:
                    if (action.getData() != null) {
                        printAI();
                    } else {
                        printAI((int) action.getData());
                    }
                    break;
                case PRINT_COMMAND_ERROR:
                    printCommandError();
                    printHelp();
                    break;
                case PRINT_WAIT_COORDINATES:
                    printWaitCoordinates((int) action.getData());
                    break;
                case PRINT_GOODBYE:
                    printGoodbye();
                    break;
                case PRINT_HELP:
                    printHelp();
                    break;
                case PRINT_LIST_MOVES:
                    printState();
                    printListMoves();
                    break;
                case MOVE:
                    if (action.getData() == null) {
                        printMoveError();
                        printHelp();
                    } else {
                        printMove((Move) action.getData());
                        printState();
                        printHelp();
                    }
                    break;
                case PRINT_NEXT_PLAYER:
                    printNextPlayer();
                    break;
                case PRINT_PLAYER:
                    if (action.getData() != null) {
                        printPlayer();
                    } else {
                        printPlayer((int) action.getData());
                    }
                    break;
                case SHUFFLE:
                    printRandom();
                    printState();
                    printHelp();
                    break;
                case REDO:
                    if (action.getData() == null) {
                        printRedoError();
                        printState();
                        printHelp();
                    } else {
                        printRedo((Move) action.getData());
                        printState();
                        printHelp();
                    }
                    break;
                case PRINT_STATE:
                    printState();
                    break;
                case SWAP:
                    if (action.getData() == null) {
                        printSwapError();
                        printHelp();
                    } else {
                        Swap s = (Swap) action.getData();
                        printSwapSuccess(s.getPos1(), s.getPos2());
                        printHelp();
                    }
                    break;
                case UNDO:
                    if (action.getData() == null) {
                        printState();
                        printUndoError();
                        printHelp();
                    } else {
                        printUndo((Move) action.getData());
                        printState();
                        printHelp();
                    }
                    break;
                case VALIDATE:
                    printValidate((boolean) action.getData());
                    printHelp();
                    break;
                case PRINT_WELCOME:
                    printWelcome();
                    break;
                case PRINT_WIN_MESSAGE:
                    printWinMessage((Player) action.getData());
                    break;
                case PRINT_ASK_NB_PLAYERS:
                    askNbPlayers();
                    break;
                case PRINT_ASK_GAME_MODE:
                    printGameMode();
                    break;
                case PRINT_ASK_HOST_OR_JOIN:
                    printHostOrJoin();
                    break;
                case PRINT_ASK_IP:
                    printAskIP();
                    break;
                case PRINT_CONNECTION_ETABLISHED:
                    printConnectionEtablished();
                    break;
                case PRINT_WAITING_FOR_CONNECTION:
                    printWaitingForConnection((int) action.getData());
                    break;
                case PRINT_NOT_YOUR_TURN:
                    printOtherPlayerTurn();
                    waitOtherTurn = true;
                    break;
                case ITS_YOUR_TURN:
                    waitOtherTurn = false;
                    break;
                case PRINT_WAITING_RESPONSE:
                    printWaitingOtherPlayer();
                    break;
                case SAVE_KUBE:
                    printSaveAskFileName();
                    break;
                case SAVED_KUBE:
                    printSaved();
                    break;
                case LOAD_KUBE:
                    printLoadAskFileName();
                    break;
                case LOADED_KUBE:
                    printLoaded();
                    break;
                default:
                    break;
            }
        }
    }

    private void printCommandPhase1() {
        System.out.println("Commandes disponibles : \n" +
                "-random : construit aléatoirement une tour\n" +
                "-echanger : permet d'échanger la position de 2 pièces de son choix\n" +
                "-valider : valider que sa montagne est prête\n" +
                "-afficher : affiche l'état de la base centrale et de sa montagne\n" +
                "-sauvegarder : sauvegarde la partie\n" +
                "-aide : affiche cette liste\n" +
                "Tour de " + kube.getCurrentPlayer().getName() + "\n" +
                "Vous devez consruire votre montagne. \n");
    }

    private void printCommandPhase2() {
        String s = "Commandes disponibles  : \n" +
                "-jouer : jouer un coup\n" +
                "-annuler : annuler le dernier coup\n" +
                "-rejouer : rejouer le dernier coup\n" +
                "-afficher : affiche l'état de la base centrale et de sa montagne\n" +
                "-sauvegarder : sauvegarde la partie\n" +
                "-aide : affiche cette liste\n" +
                "Tour de " + kube.getCurrentPlayer().getName() + "\n" +
                "Vous devez choisir une de vos pièces pour la mettre sur la montagne centrale.";
        System.out.println(s);
    }

    private void askNbPlayers() {
        System.out.println("Combien de joueurs? (0-2)");
    }

    private void printAI() {
        System.out.println("Difficultée de l'IA? (1-3)");
    }

    private void printAI(int n) {
        System.out.println("Difficultée de l'IA" + n + "? (1-3)");
    }

    private void printCommandError() {
        System.out.println("Commande invalide. Tapez -aide pour afficher la liste des commandes.");
    }

    private void printWaitCoordinates(int i) {
        if (i == 1) {
            System.out.println("Entrez les coordonnées de la première pièce à déplacer (ex : 0 2)");
        } else {
            System.out.println("Entrez les coordonnées de la deuxième pièce à déplacer (ex : 0 2)");
        }
    }

    private void printGoodbye() {
        System.out.println("Merci d'avoir joué à Kube !");
    }

    private void printHelp() {
        if (kube.getPhase() == Kube.PREPARATION_PHASE) {
            printCommandPhase1();
        } else {
            printCommandPhase2();
        }
    }

    private ArrayList<Move> printListMoves() {
        try {
            ArrayList<Move> moves = kube.moveSet();
            System.out.println("Voici les coups possibles :");
            for (int i = 0; i < moves.size(); i++) {
                System.out.println(i + " : " + moves.get(i));
            }
            System.out.println("Entrez le numéro du coup que vous voulez jouer:");
            return moves;
        } catch (UnsupportedOperationException e) {
            return null;
        }
    }

    private void printMove(Move move) {
        System.out.println(move.getPlayer().getName() + " a joué : " + move);
    }

    private void printMoveError() {
        System.out.println("Numéro de coup invalide");
    }

    private void printNextPlayer() {
        if (kube.getPhase() == Kube.PREPARATION_PHASE) {
            System.out.println("C'est au tour de " + kube.getCurrentPlayer().getName());
        } else {
            System.out.println("C'est au tour de " + kube.getCurrentPlayer().getName());
        }
        printHelp();
        printState();
    }

    private void printPenality() {
        System.out.println("Votre adversaire a une pénalitée, choisissez une pièce à récuperer");
    }

    private void printPlayer() {
        System.out.println("Nom du joueur?");
    }

    private void printPlayer(int n) {
        System.out.println("Nom du joueur " + n + "?");
    }

    private void printRandom() {
        System.out.println("Votre montagne a été mélangée");
    }

    private void printRedo(Move move) {
        System.out.println("Coup " + move.toString() + " rejoué");
    }

    private void printRedoError() {
        System.out.println("Impossible de rejouer le coup");
    }

    private void printStart() {
        System.out.println("Combien de joueur?");
    }

    private void printState() {
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

    private void printSwap() {
        System.out.println("Entrez les coordonnées des deux pièces à échanger");
    }

    private void printSwapError() {
        System.out.println("Les coordonnées entrées ne sont pas valides");
    }

    private void printSwapSuccess(Point p1, Point p2) {
        printSwapSuccess(p1.x, p1.y, p2.x, p2.y);
    }

    private void printSwapSuccess(int x1, int y1, int x2, int y2) {
        Mountain m = kube.getCurrentPlayer().getMountain();
        System.out.println("Les pièces " + m.getCase(x1, y1).forDisplay() + " et " + m.getCase(x2, y2).forDisplay()
                + " ont été échangées");
    }

    private void printUndo(Move move) {
        System.out.println("Coup " + move.toString() + " annulé");
    }

    private void printUndoError() {
        System.out.println("Impossible d'annuler le coup");
    }

    private void printValidate(boolean b) {
        if (b) {
            System.out.println("Votre montagne a été validée");
        } else {
            System.out.println("Votre montagne n'est pas valide");
        }
    }

    private void printWelcome() {
        System.out.println("Bienvenue dans le jeu Kube !");
        System.out.println("Pour commencer, entrez 'start' ou 'exit' pour quitter.");
    }

    private void printWinMessage(Player winner) {
        System.out.println("Victoire de " + winner.getName() + ". Félicitations !");
        System.out.println("Plateau final : \n" + kube.getK3() + "\n");
        System.out.println(kube.getP1());
        System.out.println(kube.getP2());
    }

    private void printGameMode() {
        System.out.println("Mode de jeu : 1 pour local, 2 pour en ligne");
    }

    private void printAskIP() {
        System.out.println("Entrez l'adresse IP du serveur");
    }

    private void printHostOrJoin() {
        System.out.println("Entrez 1 pour héberger, 2 pour rejoindre");
    }

    private void printWaitingForConnection(int port) {
        System.out.println("En attente de connexion sur le port " + port);
    }

    private void printConnectionEtablished() {
        System.out.println("Connexion établie");
    }

    private void printOtherPlayerTurn() {
        System.out.println("C'est au tour de l'autre joueur");
    }

    private void printWaitingOtherPlayer() {
        System.out.println("En attente de l'adversaire");
    }

    private void printSaveAskFileName() {
        System.out.println("Veuillez entrer le nom du fichier de sauvegarde");
    }

    private void printSaved() {
        System.out.println("Partie sauvegardée");
    }

    private void printLoadAskFileName() {
        System.out.println("Veuillez entrer le nom du fichier de sauvegarde");
    }

    private void printLoaded() {
        System.out.println("Partie chargée");
    }
}
