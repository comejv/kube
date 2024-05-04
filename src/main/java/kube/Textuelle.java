package kube;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

import kube.model.*;

public class Textuelle {
    public static void main(String[] args) {

        game();
    }

    public static void game() {
        Kube kube = new Kube();
        kube.fillBag();
        kube.fillBase();
        phase1(kube.getP1());
        phase1(kube.getP2());
    }

    public static void afficherCommandePahse1(){
        System.out.println("Tapez une des commandes suivantes : \n" +
                "-random : construit aléatoirement une tour\n" +
                "-echanger : permet d'échanger la position de 2 pièces de son choix\n" +
                "-valider : valider que sa montagne est prête" +
                "-afficher : affiche l'état de la base centrale et de sa montagne");
    }

    public static void randomizeMoutain(Player p){
            for (int i = 0; i < p.getMountain().getBaseSize(); i++){
                for (int j = 0; j < i + 1; j++){
                    p.removeToAvailableToBuild(i, j);
                }
            }
            for (int i = 0; i < p.getMountain().getBaseSize(); i++){
                for (int j = 0; j < i + 1; j++){
                    Color c = Color.getRandomColor();
                    while (p.getAvalaibleToBuild().get(c) == 0){
                        c = Color.getRandomColor();
                    }
                    p.addToMountain(i, j, c);
                }
            }
            
        }
 
        Collections.shuffle(c);
        for (int i = 0; i < p.getMountain().getBaseSize(); i++){
            for (int j = 0; j < i + 1; j++){
                m[i][j]
            }
        }
    }

    public static void phase1(Player p) {
        Scanner sc = new Scanner(System.in);
        String s;
        System.out.println("Première phase - Construction de la montagne du joueur " + p.getId());
        afficherCommandePahse1();
        while (sc.hasNextLine() && (s = sc.nextLine()) != "valider"){
            switch (s){
                case "random":

            }
        }
    }
}
