package jade;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance; //singleton
    private double scrolLX, scrollY;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3] ; // three mouse button
    private boolean isDragging;

    private MouseListener(){
        this.scrolLX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.lastX = 0.0;
        this.yPos = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get() {
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos) {
        get().lastX = get().xPos; //asign xPos to the last Xpos
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void  mouseButtonCallback(long window, int button, int action, int mods) {
        if(action==GLFW_PRESS){
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        }else if (action==GLFW_RELEASE){
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false; //because the button is released
            }
        }
    }

    public static void mouseScrollCallBack(long window,double xOffset, double yOffset){
        get().scrolLX = xOffset;
        get().scrollY = yOffset;

    }

    public static void endFrame() {
        //reset the delta
        get().scrolLX = 0.;
        get().scrollY = 0.;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    //GETTERS FOR POSITION
    public static float  getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float  getDX() {
        return (float)(get().lastX - get().xPos);
    }

    public static float  getDY() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)get().scrolLX;
    }
    public  static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        return get().mouseButtonPressed.length > button ? get().mouseButtonPressed[button] : false;
    }
}
