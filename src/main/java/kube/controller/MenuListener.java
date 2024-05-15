package kube.controller;

import java.util.Scanner;

import kube.model.action.Action;
import kube.model.action.Queue;


public class MenuListener implements Runnable{

    Queue<Action> controlerToView;
    Scanner scanner;
    
    public MenuListener(Queue<Action> controlerToView, Scanner scanner){
        this.controlerToView = controlerToView;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        String s = "";
        while(scanner.hasNextLine()){
            
        }
        
    }

    public int askNbPlayer() {
        String s = "";
        System.out.println("Combien de joueurs ? (0-2)");
        s = scanner.nextLine();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre entre 0 et 2");
            return askNbPlayer();
        }
    }

    public int askGameMode() {
        String s = "";
        System.out.println("Mode de jeu : 1 pour local, 2 pour en ligne");
        s = scanner.nextLine();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre entre 1 et 2");
            return askGameMode();
        }
    }
}