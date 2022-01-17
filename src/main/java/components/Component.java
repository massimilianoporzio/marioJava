package components;

import imgui.ImGui;
import jade.GameObject;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {

    private static int ID_COUNTER = 0; //GLOBVAL COUNTER DEI COMPONENTS

    private int uid = -1; //ID OV THE SINGLE COMPONENT

    public transient GameObject gameObject = null; //ref to the gameObject it is related to

    public void update(float dt){

    }; //NON NECESSARIO DA OVERRIDE

    public void start() {
        //NOT NECESSARILY TO OVVERIDE THIS METHOD
    }

    //NON ASTRATTO CHI EREDITA DA COMPIONENT NON è tenuto a fare override
    public void imgui() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field :fields) {
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if(isTransient){
                    continue;//PASSA AL FIELD SUCCESSIVO NON VOGLIO APPAIA NELLA IMGUI
                }
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if(isPrivate){
                    field.setAccessible(true);
                }
                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class){
                    int val = (int)value;
                    int[] imInt = {val}; //imGui is expecting an array for an int input
                    if(ImGui.dragInt(name + ": ",imInt)){ //if changes in imGui then set changes
                        field.set(this, imInt[0]);
                    }
                }else if(type == float.class){
                    float val = (float)value;
                    float[] imFloat = {val};
                    if(ImGui.dragFloat(name + ": ",imFloat)){ //if changes in imGui then set changes
                        field.set(this, imFloat[0]);
                    }

                }else if(type == boolean.class){
                    boolean val = (boolean) value;

                    if(ImGui.checkbox(name + ": ",val)){
                        val = !val; //TOGGLE
                        field.set(this, val);
                    }
                }else if (type == Vector3f.class){
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if(ImGui.dragFloat3(name + ": ",imVec)){ //if changes in imGui then set changes
                        val.set(imVec[0], imVec[1], imVec[2]);
                        //NON SERVE FIELD.SET che non sono primitive
                    }
                }else if (type == Vector4f.class){
                    Vector4f val = (Vector4f) value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if(ImGui.dragFloat4(name + ": ",imVec)){ //if changes in imGui then set changes
                        val.set(imVec[0], imVec[1], imVec[2], imVec[4]);
                        //NON SERVE FIELD.SET che non sono primitive
                    }
                }

                if(isPrivate){
                    field.setAccessible(false);
                }
            }
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }

    public void genereateId(){
        if(this.uid == -1){
            this.uid = ID_COUNTER++; //PRIMO COMPONENT: uid = 1 ID_COUNTER = 1 etc
        }
    }

    public int getUid(){
        return this.uid;
    }

    public static void init(int maxId){
        ID_COUNTER = maxId; //QUANDO CARICO DA FILE AVRò UN po' di oggetti allora inizializzo il contatore con maxId
        //E RIPARTO DA LI A CONTARE
    }

}
