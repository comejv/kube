package kube.model.ai;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.Move;

public class MinMaxAI implements abstractAI, ActionListener {
    Kube k3;
    Player iaPlayer;
    Random r;
    HashMap<Kube, Move> solution, tmp;
    Method heuristic;
    int time; // in ms 
    Boolean noMoreTime;
    Timer timer;

    public MinMaxAI(int time, int seed) {
        r = new Random(seed);
        setTime(time);
    }

    public MinMaxAI() {
        r = new Random();
        setTime(3000);
    }

    public void setK3(Kube k){
        k3 = k;
    }

    public void setPlayer(Player p){
        iaPlayer = p;
    }

    @Override
    public void constructionPhase() {
        while (!iaPlayer.validateBuilding()){
            utilsAI.randomFillMountain(iaPlayer, r);
        }
    }


    @Override
    public Move nextMove() throws Exception {
        solution = new HashMap<>();
        noMoreTime = false;
        int horizon = 1;
        timer = new Timer(time, this);
        timer.start();
        while (true) {
            tmp = new HashMap<>();
            minMax(k3.clone(), horizon);
            if (noMoreTime){
                return solution.get(k3);
            } else {
                solution = tmp;
                horizon++;
            }
        }
    }

    public int minMax(Kube k, int horizon) {
        if (noMoreTime) { // Timer's end
            return -1;
        }
        ArrayList<Move> moves;
        // Leaf
        if (horizon == 0) {
            return evaluation(k);
        } else if ((moves = k.moveSet()).size() == 0) {
            return evaluation(k);
        } else {
            int value, score;
            if (k.getCurrentPlayer() == iaPlayer) {
                value = Integer.MIN_VALUE;
            } else {
                value = Integer.MAX_VALUE;
            }
            for (Move m : moves) {
                k.playMove(m);
                score = minMax(k, horizon - 1);
                if (score == -1) { // Timer's end
                    return -1;
                }
                k.unPlay();
                if (k.getCurrentPlayer() == iaPlayer) {
                    if (score > value) {
                        value = score;
                        tmp.put(k.clone(), m);
                    }
                } else {
                    if (score < value) {
                        value = score;
                    }
                }
            }
            return value;
        }
    }

    private int evaluation(Kube k) {
        try {
            return (int) heuristic.invoke(null, k); // Assuming heuristic method is static
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // or handle the exception as appropriate
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        noMoreTime = true;
        timer.stop();
    }


    public void setHeuristicMethod(Method h) {
        heuristic = h;
    }

    // Set time in ms 
    public void setTime(int t){
        time = t;
    }

    public int getTime(){
        return time;
    }
}
