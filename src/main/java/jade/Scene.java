package jade;

import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    //MAY CONTAIN PHYSICS, GAME STUFF , RENDERER,...
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {
    }

    public Camera getCamera() {
        return camera;
    }

    //UPDATE THE STATE OF OUR SCENE
    public abstract void update(float dt);

    //start the scene (equals to start each GameObject)
    public void start(){
        for (GameObject go: gameObjects
             ) {
            go.start();
            renderer.add(go); //agg l'oggetto al renderer
        }
        isRunning = true;

    }

    //add GameObjects to the scene
    public void addGameObjectToScene(GameObject go){
        if(!isRunning){
            //DO NOTHING
            gameObjects.add(go);
        }else{
            gameObjects.add(go);
            go.start(); //START IMMEDIATELY THE GAME OBJECT IF THE SCENE IS NOT RUNNING
            renderer.add(go); //agg l'ogg al renderer
        }
    }

    public void init(){
        //TO OVERRIDE IF NECESSARY ON subclasses
    };
}
