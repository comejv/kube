package kube.model.action;

// Import model class
import kube.model.ai.MiniMaxAI;

public class Start {

    /**********
     * ATTRIBUTES
     **********/

    private MiniMaxAI AIJ1, AIJ2;

    /**********
     * CONSTRUCTORS
     **********/

    /**
     * Constructor of the Start class
     * 
     * @param AIJ1 AI for the first player
     * @param AIJ2 AI for the second player
     */
    public Start(MiniMaxAI AIJ1, MiniMaxAI AIJ2) {
        this.AIJ1 = AIJ1;
        this.AIJ2 = AIJ2;
    }

    /**
     * Constructor of the Start class without AI for the second player
     * 
     * @param AIJ1 AI for the first player
     */
    public Start(MiniMaxAI AIJ1) {
        this(AIJ1, null);
    }    

    /**
     * Constructor of the Start class without AI
     */
    public Start() {
        this(null ,null);
    }

    /**********
     * SETTERS
     **********/

    public final void setAIJ1(MiniMaxAI aiJ1) {
        this.AIJ1 = aiJ1;
    }

    public final void setAIJ2(MiniMaxAI aiJ2) {
        this.AIJ2 = aiJ2;
    }

    /**********
     * GETTERS
     **********/

    public MiniMaxAI getAIJ1() {
        return AIJ1;
    }

    public MiniMaxAI getAIJ2() {
        return AIJ2;
    }

    /**********
     * METHOD
     **********/
    
    @Override
    public String toString() {
        return "Démarrer avec " + AIJ1 + "," + AIJ2;
    }
}
