package kube.model.ai;

import java.util.ArrayList;
import java.util.Random;

import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.*;

public class RandomAI implements abstractAI {

    /**********
     * ATTRIBUTES
     **********/
    Kube k3;
    Player iaPlayer;
    Random r;
    
    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the RandomAI class
     * @param k the game
     * @param p the player
     * @param seed the seed of the random
     */
    public RandomAI(int seed) {
        setR(new Random(seed));
    }

    public RandomAI() {
        setR(new Random());
    }

    /**********
     * SETTERS
     **********/
    public void setK3(Kube k) {
        k3 = k;
    }

    public void setPlayer(Player p) {
        iaPlayer = p;
    }

    public void setR(Random r) {
        this.r = r;
    }

    /**********
     * GETTERS
     **********/
    public Kube getK3() {
        return k3;
    }

    public Player getPlayer() {
        return iaPlayer;
    }

    public Random getR() {
        return r;
    }

    /**********
     * METHODS
     **********/

    /**
     * Fill the mountain with random mountains until the mountain is valid
     * 
     * @return void
     */
    public void constructionPhase() {
        while (!getPlayer().validateBuilding()) {
            utilsAI.randomFillMountain(getPlayer(), getR());
        }
    }

    /**
     * Give the next move of the AI
     * 
     * @return the next move
     * @throws Exception 
     */
    public Move nextMove() throws Exception {
        ArrayList<Move> moves;

        moves = k3.moveSet();
        
        if (moves.size() == 0){
            throw new Exception("Aucun coup jouable");
        } else {
            return moves.get(r.nextInt(moves.size()));
        }
    }
}
