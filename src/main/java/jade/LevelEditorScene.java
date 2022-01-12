package jade;


import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

public class LevelEditorScene extends Scene{

    public LevelEditorScene() {
//        System.out.println("Inside level editor scene");

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-250,0)); //camera at 0,0
        int xOffset = 10;
        int yOffset = 10;
        int padding = 0;

        float totalWidth = (float)(600-xOffset*2);
        float totalHeight = (float)(300-yOffset*2);
        float sizeX = totalWidth/100.f;
        float sizeY = totalHeight/100.f;
        
        //CREATE 10000 quads (userò 10 batches quindi)
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x*sizeX) + (x*padding);
                float yPos = yOffset + (y*sizeY) + (y*padding);

                GameObject go = new GameObject("Obj_"+x+"_"+y,new Transform(new Vector2f(xPos,yPos),new Vector2f(sizeX,sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos/totalWidth, yPos/totalHeight,1,1))); //il colore che uso red e green in funz della posiz
                this.addGameObjectToScene(go);
            }
        }

    }



    @Override
    public void update(float dt) {
//        System.out.println("FPS: "+(1.f/dt));
        if(KeyListener.isKeyPressed(GLFW_KEY_RIGHT)){
            camera.position.x -= 100f*dt;
        }else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)){
            camera.position.x += 100f*dt;
        }

        for (GameObject go : this.gameObjects) {
            go.uodate(dt); //AD OGNI FRAME GIRO I GAME OBJ E LI FACCIO AGGIORNARE ALLO STEP DT
        }
        this.renderer.render();
    }

}
