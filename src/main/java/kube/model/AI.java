package kube.model;

import kube.model.ai.abstractAI;

public class AI extends Player {
    abstractAI ai;

    AI(int id, abstractAI ai, Kube k3){
        super(id);
        ai.setK3(k3);
        ai.setPlayer(this);
        this.ai = ai;
    }

    @Override
    public boolean isAI(){
        return true;
    }
}
