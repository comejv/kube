package kube.model.action;

import kube.model.ai.MiniMaxAI;

public class Start {

    private MiniMaxAI aiJ1;
    private MiniMaxAI aiJ2;

    public Start(MiniMaxAI aiJ1, MiniMaxAI aiJ2) {
        this.aiJ1 = aiJ1;
        this.aiJ2 = aiJ2;
    }
    public Start(MiniMaxAI aiJ1) {
        this(aiJ1, null);
    }    
    public Start() {
        this(null ,null);
    }
    public MiniMaxAI getAiJ1() {
        return aiJ1;
    }

    public MiniMaxAI getAiJ2() {
        return aiJ2;
    }

    public void setAiJ1(MiniMaxAI aiJ1) {
        this.aiJ1 = aiJ1;
    }

    public void setAiJ2(MiniMaxAI aiJ2) {
        this.aiJ2 = aiJ2;
    }

    @Override
    public String toString() {
        return "Démarrer avec " + aiJ1 + "," + aiJ2;
    }
}
