package jade;


import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.util.SortedMap;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene{
    private GameObject obj1;
    private Spritesheet sprites;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)),
                1);
//        obj2.addComponent(new SpriteRenderer(sprites.getSprite(15)));
        obj2.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/blendImage2.png")
        )));
        this.addGameObjectToScene(obj2);

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)),
                2);
//        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        obj1.addComponent(new SpriteRenderer(new Sprite(
                AssetPool.getTexture("assets/images/blendImage1.png")
        )));
        this.addGameObjectToScene(obj1);




    }

    private void loadResources() {

        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16,16, 26,0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
//        spriteFlipTimeLeft -= dt; //PASSA IL TEMPO
////        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
////            camera.position.x -= 100f * dt;
////        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
////            camera.position.x += 100f * dt;
////        }
////        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
////            camera.position.y -= 100f * dt;
////        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
////            camera.position.y += 100f * dt;
////        }
//        if (spriteFlipTimeLeft <= 0){
//            spriteFlipTimeLeft = spriteFlipTime; //RESET TGE TIME FOR THE ANIMATION
//            spriteIndex++;
//            if(spriteIndex > 4){
//                //reset inizio animazione
//                spriteIndex = 0;
//            }
//            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
//        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
