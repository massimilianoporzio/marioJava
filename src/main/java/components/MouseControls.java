package components;

import jade.GameObject;
import jade.MouseListener;
import jade.Window;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null; //ogg che il mouse sta "tenendo"

    public void pickupObject(GameObject go){
        this.holdingObject = go;
        Window.getScene().addGameObjectToScene(go);//così diventa visibile tramite la scena

    }

    public void place() {
        this.holdingObject = null; //quando tolgo il mouse e pulsante e lo 'rilascio'
    }

    @Override
    public void update(float dt) {
        if(holdingObject != null){
            holdingObject.transform.position.x = MouseListener.getOrthoX() -16;
            holdingObject.transform.position.y = MouseListener.getOrthoY() -16;
            holdingObject.transform.position.x =
                    (int) (holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            //SERVE A MUOVERE LE COORD A STEP DI 32
            holdingObject.transform.position.y =
                    (int)(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            //SONO LE COORD WORLD..TOLGO 16 PERCHè sia "centrato" rispetto al puntatore
            {
                if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                    place(); //SE RILASCIO IL PULSANTE
                }
            }
        }
    }
}
