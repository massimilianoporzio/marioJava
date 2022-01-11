package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static util.Constants.noXAxesMoved;
import static util.Constants.noYAxesMoved;

public class Window {
    int width, height;
    String title;
    // private constructor nobody directly instantiate it and a singleton
    private static  Window window = null;

    private static Scene currentScene = null;

    public static void changeToScene(int newScene) {
        switch (newScene){
            case 0:
                currentScene =  new LevelEditorScene();
                currentScene.init();
                currentScene.start(); //AFTER INIT START
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start(); //AFTER INIT START
                break;
            default:
                assert false : "Unknown scene '" + newScene +"'";
                break;
        }
    }

    private long glfwWindow; //memory address of the window
    public float r, g, b, a;
    private boolean fadeToBlack = false;



    private Window(){
        this.width = 1366;
        this.height = 768;
        this.title = "Mario";
        this.r = 1;
        this.b = 1;
        this.g = 1 ;
        this.a = 1;

    }

    public static Window get(){
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window; // return the singleton
    }

    public void run() {
        System.out.println("Hello LWJGL "+ Version.getVersion()+"!");
        init();
        loop();

        //end! LEt'se free the memory(was C binding that created the window)
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //terminate the GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        //setup an error callback
        GLFWErrorCallback.createPrint(System.err).set(); //SET log for errors in console

        //INIT GLFW
        if ( !glfwInit() ) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        //configure GLFW
        glfwDefaultWindowHints(); //resizible, visible etc.
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); //not visible until we actually create the Window.
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); //user can resize it
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); //maxed as default

        //Create the window
         glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
         if (glfwWindow == NULL) {
             //window is not created
             throw new IllegalStateException("Failed to create the GFLW window.");
         }
         //REGISTER MOUSE LISTENER
         glfwSetCursorPosCallback(glfwWindow,MouseListener::mousePosCallback);
         glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
         glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallBack);


         //REGISTER KEY LISTENER
        glfwSetKeyCallback(glfwWindow,KeyListener::KeyCallback);

         //MAKE the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
         //enable v-sync
        glfwSwapInterval(1); //swap every frame . Monitor goes as fast as possible
        //make the window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities(); //use binding for OpenGL

        //INIT SCENE
        Window.changeToScene(0);
    }

    public void loop() {
        //TIME OF START OF THE FRAME
        float beginTime = Time.getTime();
        float endTime; //HERE THE FRAME ENDS
        float dt = -1.0f;


        while (!glfwWindowShouldClose(glfwWindow)) {
            //Poll events (key mouse etc)
            glfwPollEvents();

            glClearColor(r,g,b,a); //A WHITE FULL ALPHA
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt>=0.0){
                currentScene.update(dt);
            }

            //PRINT AXIS VALUE
            float xAxisValue = GamePadListener.getAxisValue(GLFW_GAMEPAD_AXIS_LEFT_X);
            if(!Float.isNaN(xAxisValue) && xAxisValue!=noXAxesMoved) {
                System.out.println("x Axis value is: " + xAxisValue);
            }
            float yAxisValue = GamePadListener.getAxisValue(GLFW_GAMEPAD_AXIS_LEFT_Y);
            if(!Float.isNaN(yAxisValue) && yAxisValue!=noYAxesMoved ) {
                System.out.println("y Axis value is: " + yAxisValue);
            }
            //swap automatically our buffers
            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime; //DELTA OF THE FRAME
            beginTime = endTime; //resetting time for the next frame
        }//MAIN LOOP
    }
}
