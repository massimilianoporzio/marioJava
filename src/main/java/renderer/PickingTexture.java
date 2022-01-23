package renderer;
// serve a renderizzare nel viewport l'id di ogni oggetto di modo che poi cliccando posso sapere l'id


import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

public class PickingTexture {
    private int pickingTextureId;
    private int fbo; //ID OF FRAMEBUFFER ASSOCIATED
    private int depthTexture; //hom many bit

    public PickingTexture(int width, int heigth) {
         if(!init(width,heigth)){
             assert false: "Error initializing picking texture";
         }
    }

    private boolean init(int width, int heigth) {
        // Generate framebuffer
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        // Create the texture
        pickingTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D,pickingTextureId);
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S, GL_REPEAT); //repeat if wrap
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T, GL_REPEAT); //repeat if wrap in wide direction
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER, GL_NEAREST); //inteporlate if mag
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER, GL_NEAREST); //inteporlate if min
        //formato di storage:
        glTexImage2D(GL_TEXTURE_2D,0,GL_RGB32F,width,heigth,0,GL_RGB, GL_FLOAT,0); //empty space for pixels

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                this.pickingTextureId, 0);
        //fin qui creato "immagine" per ospitare la texture

        //create the textureobject for depth buffer
        glEnable(GL_TEXTURE_2D);
        depthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D,depthTexture);
        glTexImage2D(GL_TEXTURE_2D,0,GL_DEPTH_COMPONENT,width,heigth,0,GL_DEPTH_COMPONENT,GL_FLOAT,0);
        //ATTACH THE TEXTURE
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_TEXTURE_2D,depthTexture,0);
        //RENDERBUFFER NO..non devo mostrare nulla a video
        //Disable th reading
        glReadBuffer(GL_NONE); //DON'T READ FROM ANY BUFFER
        //DRAW
        glDrawBuffer(GL_COLOR_ATTACHMENT0);



        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            assert false : "Error: Framebuffer is not complete";
            return false;
        }
        //Unbind the texture and framebuffer
        glBindTexture(GL_TEXTURE_2D,0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        return true;
    }

    //REBIND THE FRAMEBUFFER
    public void enableWriting(){
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo);
    }

    //UNBIND THE FRAMBUFFER
    public void disableWriting(){
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }

    public int readPixel(int x, int y){
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fbo); //BIND FOR READING
        glReadBuffer(GL_COLOR_ATTACHMENT0); //READ FROM THE 0 ATTACH WHERE WE ADD OUR DATA
        float pixels[] = new float[3];
        glReadPixels(x,y,1,1,GL_RGB, GL_FLOAT, pixels); //READ 1 by 1 from x,y and put inside pixels
        return (int)(pixels[0]-1); //gli id sono memorizzati come colore RGB e prendo il primo valore (rosso) che ho
        //settato a 0 gli tolgo 1 cosi se clicco dove non c'è un oggetto mi torna -1
    }

}
