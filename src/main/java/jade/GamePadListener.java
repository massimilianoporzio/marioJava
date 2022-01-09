package jade;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class GamePadListener {
    private static GamePadListener instance; //singleton
    private static boolean isGamepadConnected = false;
    private boolean mouseButtonPressed[] = new boolean[14] ; // 14 button xbox

    //CHECK IF GAMEPAD IS PRESENT
    private int stick = Integer.MAX_VALUE;

    private GamePadListener(int stick){
        this.stick = stick;

    }
    public static GamePadListener get(){
        for(int stick = 0; stick <= GLFW_JOYSTICK_LAST; stick++) {

            if(!glfwJoystickPresent(stick)) continue;


            if(GamePadListener.instance == null){
                GamePadListener.instance = new GamePadListener(stick);
                System.out.println("JoyStick(" + stick + ")Name:" +

                        glfwGetJoystickName(stick) + " " +

                        glfwGetGamepadName(stick));
            }
            return GamePadListener.instance;


        }
        //NO GAMEPAD SE ARRIVA QUI
        return null;



    }

    public static boolean isButtonPressed(int button){
        GamePadListener instance = get();
        if(instance != null){
            ByteBuffer byteBuffer = glfwGetJoystickButtons(instance.stick);
            if(byteBuffer.get(button)!=0){
                return true;
            }
            else {
                return false;
            }
        }else
        {
            return false; //NO GAMEPAD
        }

    }

    public static float getAxisValue(int axis){
        GamePadListener instance = get();
        if(instance != null){
            FloatBuffer axisBuffer = glfwGetJoystickAxes(instance.stick);
            return axisBuffer.get(axis);
        }else{
            return Float.NaN;
        }
    }





}
