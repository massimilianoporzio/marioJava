package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance; //singleton
    private boolean keyPressed[] = new boolean[350]; // number of binding to keys on keyboard

    private KeyListener(){
        //void just initialize
    }

    public static KeyListener get(){
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS){
            get().keyPressed[key] = true; //setting true the key pressed
        } else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false; //setting false the key released
        }
    }

    //GETTERS
    public static boolean isKeyPressed(int keyCode){
         return get().keyPressed.length > keyCode ? get().keyPressed[keyCode] : false;
    }
}
