package jade;

public abstract class Component {

    public GameObject gameObject = null; //ref to the gameObject it is related to

    public abstract void update(float dt); //NECESSARY TO OVERRIDE IT

    public void start() {
        //NOT NECESSARILY TO OVVERIDE THIS METHOD
    }
}
