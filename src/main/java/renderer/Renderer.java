package renderer;

import components.SpriteRenderer;
import jade.GameObject;

import java.util.ArrayList;
import java.util.List;

//RENDER ALL THE OBJECTS
public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer(){
        this.batches = new ArrayList<>();
    }

    public void add(GameObject go){
        //guardo se il game object ha uno sprite renderer
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
        if(spr!=null){
            add(spr);
        }
    }

    private void add(SpriteRenderer spr) {
        boolean added = false; //serve per vedere se sono riuscito ad agg (spazio esaurisce!)
        for (RenderBatch batch: batches) {
            if(batch.hasRoom()){
                Texture tex = spr.getTexture();
                if(tex == null || batch.hasTexture(tex) || batch.hasTextureRoom()){
                    //se c'è spazio per una nuova tex
                    batch.addSprite(spr);
                    added = true;
                    break;
                }

            }
        }//giro i batches per vedere se ho spazio
        if(!added){
            //non c'era spazio
            //ALLORA CREO UN NUOVO BATCH
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(spr);
        }
    }

    public void render(){
        for (RenderBatch batch :
                batches) {
            batch.render();
        }
    }
}
