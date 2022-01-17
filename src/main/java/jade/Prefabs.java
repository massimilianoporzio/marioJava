package jade;

import components.Sprite;
import components.SpriteRenderer;
import org.joml.Vector2f;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, int sizeX, int sizeY) {
        GameObject block = new GameObject("Sprite_Object_Gen"+sprite.getTexId(),
                new Transform(new Vector2f(),new Vector2f(sizeX,sizeY)),0);
        //TRANFORM PIAZZATO A 0,0 CON LE SIZE DELLO SPRITE COME SCALA
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);
        return block;
    }
}
