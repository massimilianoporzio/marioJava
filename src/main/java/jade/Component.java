package jade;

public abstract class Component {

    public GameObject gameObject = null; //ref to the gameObject it is related to

    public void update(float dt){

    }; //NON NECESSARIO DA OVERRIDE

    public void start() {
        //NOT NECESSARILY TO OVVERIDE THIS METHOD
    }

    //NON ASTRATTO CHI EREDITA DA COMPIONENT NON è tenuto a fare override
    public void imgui() {

    }
}
