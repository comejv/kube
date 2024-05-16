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
        int nb = askNbPlayer();
        int mode = askGameMode();        
    }

    public int askNbPlayer() {
        String s = "";
        controlerToView.add(new Action(Action.PRINT_ASK_NB_PLAYERS));
        s = scanner.nextLine();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return askNbPlayer();
        }
    }

    public int askGameMode() {
        String s = "";
        s = scanner.nextLine();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return askGameMode();
        }
    }
}