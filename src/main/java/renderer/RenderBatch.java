package renderer;

import components.SpriteRenderer;
import jade.Window;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    //VERTEX LAYOUT
    //====
    //Pos,
    //float, float      float,float,float,float
    private final int POS_SIZE = 2; //x and y
    private final int COLOR_SIZE = 4; //r,g,b,a

    private final int POS_OFFSET = 0; //position starts first!
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE*Float.BYTES; //AFTER TWO COORDS STARTS COLOR

    private final int VERTEX_SIZE = 6; //three vertex each triangle * 2 traingle to make a quad
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    //NOW THE ARRAY OF SPRITE RENDERERS
    private SpriteRenderer[] spriteRenderers;
    private int numSprites;
    private boolean hasRoom;

    //VERTICI
    private float[] vertices;

    private int vaoID, vboID; //oggetti per la GPU
    private int maxBatchSize; //how many quads we manage

    private Shader shader;

    RenderBatch(int maxBatchSize){
        shader = new Shader("assets/shaders/default.glsl");
        shader.compileAndLink();
        this.spriteRenderers = new SpriteRenderer[maxBatchSize]; //one for each quads
        this.maxBatchSize = maxBatchSize;
        //4 vertices quads
        vertices = new float[VERTEX_SIZE*4*maxBatchSize]; //6 float ogni sprite e ogni sprite ha 4 vertici
        this.numSprites = 0; //sprites che abbiamo appena costruito il RendereBatch = 0!
        this.hasRoom = true; //c'è posto per gli sprites (non ce n'è nemmeno uno per ora)
    }

    public void start(){
        //CREATE ALL THE DATA ON THE GPU
        //GENERATE AND BIND a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        //Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertices.length*Float.BYTES,GL_DYNAMIC_DRAW); //DYNAMIC NOT STATIC: vertices can change!

        //create and uplaod the indices buffer (dove sono contenuti gli indici che definiscono i quad
        int eboID = glGenBuffers();
        //GENERO:
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,indices,GL_STATIC_DRAW); //STATIC! gli indici NON cambiano posizione

        //enable buffer attribute pointers
        glVertexAttribPointer(0,POS_SIZE,GL_FLOAT,false,VERTEX_SIZE_BYTES,POS_OFFSET); //posizione
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1,COLOR_SIZE,GL_FLOAT,false,VERTEX_SIZE_BYTES,COLOR_OFFSET); //colore
        glEnableVertexAttribArray(1);
    }

    private int[] generateIndices() {
        // 6 indices per ogni quad
        int[] elements = new int[6*maxBatchSize];
        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements,i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index; //a seconda dell'indice ogni elem inizia a 6*indice
        int offset = 4 * index; //ogni nuovo elemento è spostato di 4 rispetto al prec
        //primo tirangolo   //secondo triangolo
        //3,2,0,            0,2,1 (pgni quad ha 4 vertici che sono 0,1,2,3
        elements[offsetArrayIndex] = offset +3; //"3"
        elements[offsetArrayIndex+1] = offset +2; //"2"
        elements[offsetArrayIndex+2] = offset ; //"0"
        elements[offsetArrayIndex+3] = offset; //"0"
        elements[offsetArrayIndex+4] = offset +2; //"2"
        elements[offsetArrayIndex+5] = offset +1; //"1"

    }

    //pass data to be rendererd
    public void addSprite(SpriteRenderer renderer){
        //GEt index and renderObject
        int index = numSprites; //at the end of the current array of sprites
        this.spriteRenderers[index] = renderer; //assegno il renderer per quello sprite
        this.numSprites++; //incremento il num di sprites

        //add properties to local vertices array
        loadVertexProperties(index);
        if(numSprites >= this.maxBatchSize){
            //NON C'è più spazio per altri sprites
            this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int index) {
        //4 vertices per quad
        SpriteRenderer renderer = spriteRenderers[index]; //ottengo il renderer

        //find the offeset in the array (4 vertici per ogni quad e ogni vertice ha 6 float)
        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = renderer.getColor();
        //ADD vertices with the appropriate property
        // *3 (0,1)   *0 (1,1)
        //
        // *2 (0,0)   *1 (1,0)
        float xAdd = 1.f;
        float yAdd = 1.f;
        for (int i = 0; i < 4; i++) {
            if(i ==1){
                yAdd = 0.f;
            }else if(i==2){
                xAdd = 0.f;
//                yAdd = 0.f; //lo era dal giro prec
            }else if(i==3){
//                xAdd = 0.f; // lo era dal giro prec
                yAdd = 1.f;
            }
            //load position del quad
            vertices[offset] = renderer.gameObject.transform.position.x + (xAdd*renderer.gameObject.transform.scale.x);
            vertices[offset+1] = renderer.gameObject.transform.position.y + (yAdd*renderer.gameObject.transform.scale.y);

            //load color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w; //x,y,z,w sono i nomi std del Vector4f ma corrisp to r,g,b,a

            //SPOSTO L'OFFSET AL VERTICE SUCCESSIVO DEL QUAD
            offset += VERTEX_SIZE;
        }//fine giro del quad

    }

    public void render(){
        //if only one element changes we can re-buffer only that element
        //BUT FOR NOW we re-buffer ALL THE VERTICES BUT using SUBDATA()
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0 , vertices);

        //USE THE SHADER
        shader.use();
        shader.uploadMat4f("uProj", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0); //posiz
        glEnableVertexAttribArray(1); //colore

        glDrawElements(GL_TRIANGLES,this.numSprites*6, GL_UNSIGNED_INT,0);

        //free RAM (we passed to GPU's ram)
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }
}
