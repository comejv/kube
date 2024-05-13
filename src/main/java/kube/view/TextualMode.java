package kube.view;

import java.util.Scanner;

import kube.controller.*;
import kube.model.Kube;
import kube.model.Player;


public class TextualMode {

    CommandListener listener;
    Scanner sc;
    Thread t;
    Kube k3;
    public static void main(String[] args) {
        System.out.println("Bienvenue dans K3 !");
        TextualMode tm = new TextualMode();
    }

    
    TextualMode(){        
        k3 = new Kube(); 
        k3.fillBag();
        k3.fillBase();
        k3.distributeCubesToPlayers();   
        listener = new CommandListener(k3, this);
        t = new Thread(listener);
        t.start();
    }

    public void update(){
        if (k3.getPhase() == Kube.preparationPhase){
            afficherCommandePhase1();
        } else if (!k3.canCurrentPlayerPlay()){
            if (k3.getCurrentPlayer() == k3.getP1()){
                winMessage(k3.getP2());
            } else {
                winMessage(k3.getP1());
            }
        } else {
            afficherCommandePhase2();
        }
    }

    public void afficherCommandePhase1() {
        System.out.println("Tapez une des commandes suivantes : \n" +
                "-random : construit aléatoirement une tour\n" +
                "-echanger : permet d'échanger la position de 2 pièces de son choix\n" +
                "-valider : valider que sa montagne est prête\n" +
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

    public void winMessage(Player winner) {
        System.out.println("Victoire de " + winner.getName() + ". Félicitations !");
        System.out.println("Plateau final : \n" + k3 + "\n");
        System.out.println(k3.getP1());
        System.out.println(k3.getP2());
    }
    
}
