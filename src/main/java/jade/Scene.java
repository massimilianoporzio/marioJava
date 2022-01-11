package jade;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    //MAY CONTAIN PHYSICS, GAME STUFF , RENDERER,...
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene() {
    }

    //UPDATE THE STATE OF OUR SCENE
    public abstract void update(float dt);

    //start the scene (equals to start each GameObject)
    public void start(){
        for (GameObject go: gameObjects
             ) {
            go.start();
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
        }
    }

    public void init(){
        //TO OVERRIDE IF NECESSARY ON subclasses
    };
}
