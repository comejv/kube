package kube.model.ia;

import kube.model.move.Move;

public interface AI {    
    public void preparationPhase();
    public void gamePhase();
    public Move nextMove();
    
}
