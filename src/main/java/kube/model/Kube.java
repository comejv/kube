package kube.model;

import org.w3c.dom.html.HTMLIsIndexElement;

public class Kube {
    History history;

    // Constructeurs
    public Kube() {
        
    }

   
    public void setHistoric(History h){
        history = h;
    }

    public History getHistoric(){
        return history;
    }
}
