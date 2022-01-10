package jade;

public abstract class Scene {
    //MAY CONTAIN PHYSICS, GAME STUFF , RENDERER,...
    protected Camera camera;
    public Scene() {
    }

    //UPDATE THE STATE OF OUR SCENE
    public abstract void update(float dt);

    public void init(){
        //TO OVERRIDE IF NECESSARY ON subclasses
    };
}
