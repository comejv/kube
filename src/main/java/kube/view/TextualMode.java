package kube.view;

import java.util.ArrayList;
import java.awt.Point;
import java.net.InetAddress;

import kube.model.Game;
import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.Move;
import kube.model.Mountain;



public class TextualMode{

    Game game;

    public TextualMode(Game g) {
        game = g;
    }

    public void printAI() {
        System.out.println("Difficultée de l'IA? (1-3)");
    }

    public void printAI(int n) {
        System.out.println("Difficultée de l'IA" + n + "? (1-3)");
    }

    public void printCommandPhase1() {
        System.out.println("Tapez une des commandes suivantes : \n" +
                "-random : construit aléatoirement une tour\n" +
                "-echanger : permet d'échanger la position de 2 pièces de son choix\n" +
                "-valider : valider que sa montagne est prête\n" +
                "-afficher : affiche l'état de la base centrale et de sa montagne");
    }

    public void printCommandPhase2() {
        System.out.println("Tapez une des commandes suivantes : \n" +
                "-jouer : jouer un coup\n" +
                "-annuler : annuler le dernier coup\n" +
                "-rejouer : rejouer le dernier coup\n" +
                "-afficher : affiche l'état de la base centrale et de sa montagne\n" +
                "-aide : affiche cette liste\n");
    }

    public void printError() {
        System.out.println("Commande invalide. Tapez -aide pour afficher la liste des commandes.");
    }

    public void printWaitCoordinates(int i){
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
        if (game.getKube().getPhase() == Kube.PREPARATION_PHASE) {
            printCommandPhase1();
        } else {
            printCommandPhase2();
        }
    }

    public ArrayList<Move> printListMoves(){
        ArrayList<Move> moves = game.getKube().moveSet();
        System.out.println("Voici les coups possibles :");
        for (int i = 0; i < moves.size(); i++) {
            System.out.println(i+ " : " + moves.get(i));
        }
        System.out.println("Entrez le numéro du coup que vous voulez jouer:");
        return moves;
    }

    public void printGameType(){
        System.out.println("Voulez vous jouer en local (1) ou en ligne (2)?");
    }

    public void printGameOnline(){
        System.out.println("Voulez vous créer (1) ou rejoindre (2) une partie?");
    }

    public void printGameOnlineInfo(int port){
        try {
            System.out.println("Votre adresse locale est : " + InetAddress.getLocalHost() + ":" + port);
        } catch (Exception e) {
            System.out.println("Impossible de récupérer l'adresse locale");
        }
    }

    public void printGameOnlinePort(){
        System.out.println("Entrez le port sur lequel vous voulez écouter");
    }

    public void printGameOnlineConnect(){
        System.out.println("Entrez l'adresse IP et le port de la partie à rejoindre (Au format xx.xx.xx.xx:xxxx)");
    }

    public void printGameOnlineError(){
        System.out.println("Erreur lors de la connexion au serveur");
    }

    public void printMove(Move move){
        System.out.println("Vous avez joué : " + move);
    }

    public void printMoveError(){
        System.out.println("Numéro de coup invalide");
    }

    public void printNextPlayer(){
        System.out.println("C'est au tour de " + game.getKube().getCurrentPlayer().getName());
        printHelp();
        printState();
    }

    public void printPlayer() {
        System.out.println("Nom du joueur?");
    }

    public void printPlayer(int n) {
        System.out.println("Nom du joueur " + n + "?");
    }

    public void printRandom(){
        System.out.println("Votre montagne a été mélangée");
    }

    public void printRedo(){
        System.out.println("Coup rejoué ");
    }

    public void printRedoError(){
        System.out.println("Impossible de rejouer le coup");
    }       

    public void printStart() {
        System.out.println("Combien de joueur?");
    }


    public void printState() {
        if (game.getKube().getPhase() == Kube.PREPARATION_PHASE) {
            System.out.println(game.getKube().getK3());
            System.out.println(game.getCurrentPlayerToBuild());
        } else {
            System.out.println(game.getKube().getK3());
            System.out.println(game.getKube().getP1());
            System.out.println(game.getKube().getP2());
        }
    }

    public void printSwap(){
        System.out.println("Entrez les coordonnées des deux pièces à échanger");
    }

    public void printSwapError(){
        System.out.println("Les coordonnées entrées ne sont pas valides");
    }

    public void printSwapSuccess(Point p1, Point p2){
        printSwapSuccess(p1.x, p1.y, p2.x, p2.y);
    }

    public void printSwapSuccess(int x1, int y1,int x2, int y2){
        Mountain m = game.getKube().getCurrentPlayer().getMountain();
        System.out.println("Les pièces " + m.getCase(x1,y1) + " et "+ m.getCase(x2,y2) +"ont été échangées");
    }

    public void printUndo(Move move){
        System.out.println("Coup annulé");
    }
    public void printUndoError(){
        System.out.println("Impossible d'annuler le coup");
    }

    public void printValidate(){
        System.out.println("Votre montagne a été validée");
    }

    public void printWelcome() {
        System.out.println("Bienvenue dans le jeu Kube !");
        System.out.println("Pour commencer, entrez 'start' ou 'exit' pour quitter.");
    }

    public void printWinMessage(Player winner) {
        System.out.println("Victoire de " + winner.getName() + ". Félicitations !");
        System.out.println("Plateau final : \n" + game.getKube() + "\n");
        System.out.println(game.getKube().getP1());
        System.out.println(game.getKube().getP2());
    }

    public void update() {
        if (game.getKube().getPhase() == Kube.PREPARATION_PHASE) {
            printCommandPhase1();
        } else if (!game.getKube().canCurrentPlayerPlay()) {
            if (game.getKube().getCurrentPlayer() == game.getKube().getP1()) {
                printWinMessage(game.getKube().getP2());
            } else {
                printWinMessage(game.getKube().getP1());
            }
        } else {
            printCommandPhase2();
        }
    }

}
