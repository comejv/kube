package kube.model.ai;

import kube.model.Kube;
import kube.model.Player;
import kube.model.action.move.Move;

public interface abstractAI {
    public void PREPARATION_PHASE();

    public Move nextMove() throws Exception;

    public void setK3(Kube k);

    public void setPlayer(Player p);
}
