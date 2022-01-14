package components;

import jade.Component;
import jade.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

public class SpriteRenderer extends Component {
    private Vector4f color;
    private Sprite sprite;

    private Transform lastTransform; //last position / camera / scale / etc
    private boolean isDirty = false; //SE DEVO FARE UPDATE

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1, 1, 1, 1);
    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy(); //copy from the gameobject so if it changes also the sprite
    }

    @Override
    public void update(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform)){
            //THE GAME OBJECT HAS CHANGES POSITION AND/OR SCALE SO ALSO THE SPRITE HAS TO CHANGE!
            this.gameObject.transform.copy(this.lastTransform); //AGGIORNO LAST TRANSFORM CON la transform del game obj
            isDirty = true;
        }

    }

    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite; //DA FARE ANALOGO A COLOR (NON DIRTI SE è LO STESSO SPRITE
        this.isDirty = true; //perché ho appena chiamato il metodo per agg lo Sprite
    }

    public void setColor(Vector4f color) {
         if(!this.color.equals(color)){
            this.color.set(color); //lo cambio solo se sono diversi!
            this.isDirty = true; //perché hho appena cambiato il color
        }

    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setClean(){
        this.isDirty = false;
    }
}
