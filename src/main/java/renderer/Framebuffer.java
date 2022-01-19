package renderer;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {
    private int fboID = 0; //indirizzo in gpu
    private Texture texture = null;
    public Framebuffer(int width, int height){
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER,fboID);

        //create the texture to render the data to and attach it to our framebuffer
        this.texture = new Texture(width,height);
        //ATTACH A 2d texture ...posso avere 4 livelli di attachment qui uso solo il primo
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D,
                this.texture.getTextID(),0);
        //create the renderbuffer store the depth info (how many BIT per pixel)
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER,rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT24,width,height); //24 bit per pixel
        //NOW ACTUAL ATTACH THE RENDERBUFFER TO THE FRAMEBUFFER
        glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_RENDERBUFFER,rboID);

        if(glCheckFramebufferStatus(GL_FRAMEBUFFER)!=GL_FRAMEBUFFER_COMPLETE){
            assert false : "Error: dramebuffer is not complete";
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0); //UNBIND AND PUT IN THE WINDOW NOT IN THE FRAMEBUFFER
    }

    public int getFboID() {
        return fboID;
    }

    public int getTextureId() {
        return texture.getTextID();
    }

    public void bind() {
        glBindBuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbind(){
        glBindBuffer(GL_FRAMEBUFFER, 0);
    }


}
