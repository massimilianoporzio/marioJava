package scenes;


import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderer.DebugDraw;
import scenes.Scene;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private Spritesheet sprites;
    SpriteRenderer obj1Sprite;

    GameObject levelEditorStuff = new GameObject("LevelEditor");



    public LevelEditorScene() {

    }

    @Override
    public void init() {
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        //FIRST LOAD SPRITESHEETS
        sprites = AssetPool.getSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png");

        if (levelLoaded) {
            this.activeGameObject = gameObjects.get(0);

            return;
        }



        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)), 2);
        obj1Sprite = new SpriteRenderer();
        obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
        obj1.addComponent(obj1Sprite);
        obj1.addComponent(new Rigidbody());
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);


    }

    private void loadResources() {

        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"),
                        16,16, 81,0));
        AssetPool.getTexture("assets/images/blendImage2.png");
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void imgui() {
        ImGui.begin("Test Window");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos); //GET POS AND PUT IN THE ImVec2
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize); //GET SIZE AND PUT IN THE ImVec2
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowsX2 = windowPos.x + windowSize.x; //coord x della fine della finestra
        for (int i = 0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 2; // x2 per renderli più grandi nella finestra rispetto agli attuali pixel
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();
            //l'id del bottone è la textID che è la stessa perché è uno stilesheet
            //creo una id "momentanea" da assegnare al bottone
            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight,
                    texCoords[2].x,texCoords[0].y,
                    texCoords[0].x,texCoords[2].y)){
//                System.out.println("Button " +i+ " clicked"); //SE CLICCO IN QUELLE COORDINATE (e imageButton crea anche
                //il bottone con l'immagine dello sprite
                GameObject object = Prefabs.generateSpriteObject(sprite,spriteWidth, spriteHeight);
                //ATTACH to the mouse cursor
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);

            }
            ImGui.popID();
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos); //ultima pos cliccata
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i + 1 < sprites.size() && nextButtonX2 < windowsX2){
                //STA SULLE STESSA LINEA
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }


    @Override
    public void update(float dt) {

        levelEditorStuff.update(dt);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
