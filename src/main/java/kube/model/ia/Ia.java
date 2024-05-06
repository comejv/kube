package kube.model.ia;

import kube.model.move.Move;

public interface Ia {    
    public void preparationPhase();
    public void gamePhase();
    public Move nextMove();
    
}
