package kube.controller;

import java.util.Scanner;

public class CommandListener implements Runnable {

    String command;

    @Override
    public void run() {
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()){
            setCommand(sc.nextLine());
        }
    }
    
    synchronized public void setCommand(String s){
        command = s;
    }

    synchronized public String getCommand(){
        return command;
    }
}
