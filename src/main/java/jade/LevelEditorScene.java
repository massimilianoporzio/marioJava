package jade;

import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_GAMEPAD_BUTTON_A;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL20.*;

public class LevelEditorScene extends Scene{
    private String vertexShaderSrc ="#version 330 core\n" +
            "    layout (location=0) in vec3 aPos; //attribute position\n" +
            "    layout (location=1) in vec4 aColor; //attribute color\n" +
            "\n" +
            "    out vec4 fColor; //output color for the fragment shader\n" +
            "\n" +
            "    void main()\n" +
            "    {\n" +
            "        fColor = aColor;\n" +
            "        gl_Position = vec4(aPos, 1.0); //4 vector wity my 3 coord aPos and 1.0\n" +
            "    }";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "    in vec4 fColor; //accept the output of vertex shader as input\n" +
            "    out vec4 color; //color we output\n" +
            "\n" +
            "    void main()\n" +
            "    {\n" +
            "        color = fColor; //just pass position and color (here color for the fragment shader)\n" +
            "    }";
    private int vertexID, fragmentID, shaderProgram;
    private float[] vertexArray = {
            // position               // color
            0.5f, -0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            -0.5f,  0.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            0.5f,  0.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            -0.5f, -0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            /*
                    x        x
                    x        x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;

    public LevelEditorScene() {
//        System.out.println("Inside level editor scene");

    }

    @Override
    public void init() {
        defaultShader = new Shader("assets/shaders/default.glsl");
        //COMPILE AND LINK SHADERS
        defaultShader.compileAndLink();

        //FIrst load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader src code to the GPU
        glShaderSource(vertexID,vertexShaderSrc);
        glCompileShader(vertexID);

        //GENERATE VAO, VBO, EBObuffer objects and send to GPU
        vaoID =  glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Create a float buffer (required by GPU) for the vertex
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip(); //FLIP IS FOR GPU expecting order of VBO
        //CREATE THE VBO and upload to it the vertex floatbuffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW); // tell to draw the vertices and no more actions

        //create the indices and uplaod
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID); //NOT AN ARRAY BUFFER BUT AN ELEMENT
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        //ADD VERTEX ATTRIBUTES POINTERS - spcify we'll use three coords and 4 numbers for color and the stride is 7
        int positionsSize = 3; //x,y,z
        int colorsSize = 4;
        int floatSizeBytes = 4; //4 bytes for a float we need to specify it!
        int vertexSizeBytes = (positionsSize+colorsSize)*floatSizeBytes; //TOTAL BYTES OF A VERTEX

        glVertexAttribPointer(0,positionsSize,GL_FLOAT,false, vertexSizeBytes,0); //pointer is the offset
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorsSize,GL_FLOAT,false,vertexSizeBytes,positionsSize*floatSizeBytes);//three bytes after the x
        glEnableVertexAttribArray(1);



    }



    @Override
    public void update(float dt) {
//        System.out.println("We are running at " + (1.0f/dt) + " FPS");
        //Bind shader program
        defaultShader.use();
        //BIND the VAO
        glBindVertexArray(vaoID);

        //enable vertex array attributes
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        //DRAW!
        glDrawElements(GL_TRIANGLES,elementArray.length,GL_UNSIGNED_INT,0);

        //UNBIND EVERYTHING!
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0); //bind to "0" = no bind
        defaultShader.detach(); // don't use PrgramSahder anymore
    }
}
