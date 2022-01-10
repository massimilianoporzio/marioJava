package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;


public class Shader {

    private int shaderProgramID;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragmentSource;
    private String filePath; //source file *.glsl

    public Shader(String filepath) {
        this.filePath = filepath;
        try {
                String source = new String(Files.readAllBytes(Paths.get(filepath)));
                //SPLIT IN THE VERTEX AND FRAGMENT PART (EVERY #type)
                String[] splitString = source.split("(#type)( )+([a-zA-Z]+)"); //such as "#type blashshBLAH
                //get the index of the FIRST  '#type' than add 6 (it requires the file has ONLY ONE SPACE after #type)
                int index = source.indexOf("#type")+6;
                int eol = source.indexOf("\r\n",index); //how many chats after index is the end of the line
                String firstPattern = source.substring(index,eol).trim();
                index = source.indexOf("#type",eol) + 6; //index from the first endline to the second #type
                eol = source.indexOf("\r\n",index); //how many chats after index is the end of the line
                String secondtPattern = source.substring(index,eol).trim();
                if(firstPattern.equals("vertex")){
                    vertexSource = splitString[1];
                }else if (firstPattern.equals("fragment")){
                    fragmentSource = splitString[1];
                }else{
                    throw new IOException("Unexpected token '"+ firstPattern+"'");
                }
                if(secondtPattern.equals("fragment")){
                    fragmentSource=splitString[2];
                }else if (secondtPattern.equals("vertex")){
                    vertexSource = splitString[2];
                }else{
                    throw new IOException("Unexpected token '"+ secondtPattern+"'");
                }
                System.out.println(vertexSource);
                System.out.println(fragmentSource);


        }catch (IOException ex){
            ex.printStackTrace();
            assert false : "Error: Could not open the file for shader: '"+ filepath +"'"; //exit program
        }
    }

    public void compileAndLink(){
        int vertexID, fragmentID;
        //FIrst load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //Pass the shader src code to the GPU
        glShaderSource(vertexID,vertexSource);
        glCompileShader(vertexID);

        //check for errors in compilation
        int success = glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH); //get info about the error

            System.out.println("ERROR: '"+filePath+"' -\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false : ""; //exit program
        }
        else{
            System.out.println("VERTEX OK");
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //Pass the shader src code to the GPU
        glShaderSource(fragmentID,fragmentSource);
        glCompileShader(fragmentID);

        //check for errors in compilation
        success = glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH); //get info about the error

            System.out.println("ERROR: '"+filePath+"'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false : ""; //exit program
        }
        //LINK SHADERS and check for errors (creating the program)
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID,vertexID);
        glAttachShader(shaderProgramID,fragmentID);
        glLinkProgram(shaderProgramID);
        //check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE){

            int len = glGetProgrami(shaderProgramID,GL_INFO_LOG_LENGTH); //get info about the error
            System.out.println("ERROR: '"+filePath +"'\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID,len));
            assert false : ""; //exit program
        }

    }

    public void use() {
        if(!beingUsed){
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }


    }

    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID,varName); //GET THE LOCATION of the shader IN THE GPU
        use(); //altrimenti l'upload non va da nessuna parte
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16); //4x4 matrix
        mat4.get(matBuffer); //[1,1,1,1,1,1,1,1,1,....] 16 linear buffer
        //BE SURE Shader.use() is called BEFORE upload
        glUniformMatrix4fv(varLocation,false,matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3){
        int varLocation = glGetUniformLocation(shaderProgramID,varName); //GET THE LOCATION of the shader IN THE GPU
        use(); //altrimenti l'upload non va da nessuna parte
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9); //3x3 matrix
        mat3.get(matBuffer); //[1,1,1,1,1,1,1,1,1,....] 9 linear buffer
        //BE SURE Shader.use() is called BEFORE upload
        glUniformMatrix3fv(varLocation,false,matBuffer);
    }

    public void uploadMat2f(String varName, Matrix2f mat2){
        int varLocation = glGetUniformLocation(shaderProgramID,varName); //GET THE LOCATION of the shader IN THE GPU
        use(); //altrimenti l'upload non va da nessuna parte
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(4); //2x2 matrix
        mat2.get(matBuffer); //[1,1,1,1] 4 linear buffer
        //BE SURE Shader.use() is called BEFORE upload
        glUniformMatrix2fv(varLocation,false,matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec){
        int varLocation = glGetUniformLocation(shaderProgramID,varName); //GET THE LOCATION of the shader IN THE GPU
        use();
        glUniform4f(varLocation,vec.x,vec.y,vec.z,vec.w); //upload 4 floats
    }

    public void uploadVec3f(String varName, Vector3f vec){
        int varLocation = glGetUniformLocation(shaderProgramID,varName); //GET THE LOCATION of the shader IN THE GPU
        use();
        glUniform3f(varLocation,vec.x,vec.y,vec.z); //upload 3 floats
    }

    public void uploadVec2f(String varName, Vector2f vec){
        int varLocation = glGetUniformLocation(shaderProgramID,varName); //GET THE LOCATION of the shader IN THE GPU
        use();
        glUniform2f(varLocation,vec.x,vec.y); //upload 2 floats
    }

    public void uploadFloat(String varName, float val){
        int varLocation = glGetUniformLocation(shaderProgramID,varName); //GET THE LOCATION of the shader IN THE GPU
        use();
        glUniform1f(varLocation,val);
    }

    public void uploadInt(String varName, int val){
        int varLocation = glGetUniformLocation(shaderProgramID,varName); //GET THE LOCATION of the shader IN THE GPU
        use();
        glUniform1i(varLocation,val);
    }
}
