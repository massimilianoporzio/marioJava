package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private int textID;

    public Texture(String filepath) {
        this.filepath = filepath;


        //GENERATE texture ON GPU
        textID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D,textID);

        //set the texture parameters
        //REPEAT THE IMAGE IN BOTH DIRECTIONS
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        //When streching the image in the texture DON'T BLUR but PIXELATE
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_NEAREST); //minify
        //When shrinking pixelate (interpolate)
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_NEAREST); //magnify
        //LOAD THE IMAGE
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
//        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath,width,height,channels,0); //desired = 0 == non cambiare i canali
        if (image!=null){
            if(channels.get(0)== 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), //USE GL_RGBA if images have alpha!!!
                        0, GL_RGB, GL_UNSIGNED_BYTE, image); //uplaod to the GPU
            }else if(channels.get(0)==4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), //USE GL_RGBA if images have alpha!!!
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image); //uplaod to the GPU
            }else{
                assert false: "Error: (Texture) unknwon number of channels: "+ channels.get(0);
            }
        }else{
            assert false: "Error: (Texture) Could not load image '"+filepath+"'";
        }
        //NOW FREE THE RAM (NOW THE IMAGE IS LOADED TO THE GPU'S RAM)
        stbi_image_free(image);


    }


    public void bind(){
        glBindTexture(GL_TEXTURE_2D,textID);
    }

    public void unBind(){
        glBindTexture(GL_TEXTURE_2D,0);
    }

}