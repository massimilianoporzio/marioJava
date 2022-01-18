package components;

import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;
import util.Settings;

//Serve a disgnare la griglia
public class GridLines extends Component{
    @Override
    public void update(float dt) {
        Vector2f cameraPos = Window.getScene().camera().position;
        Vector2f projectionSize = Window.getScene().camera().getProjectionSize();

        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH)-1)* Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT)-1)* Settings.GRID_HEIGHT; //il -1 è per fare inziare la
        //griglia dall'angolo inf sinistro

        //HOW MANY LINES?
        int numVtLines = (int)(projectionSize.x / Settings.GRID_WIDTH) +2; //ALONG X
        int numHzLines = (int)(projectionSize.y / Settings.GRID_HEIGHT) +2; //ALONG Y +2 ci siamo mossi indietro prima

        int height = (int)projectionSize.y + Settings.GRID_HEIGHT*2;
        int widht = (int)projectionSize.x + Settings.GRID_HEIGHT*2;
        int maxLines = Math.max(numVtLines,numHzLines);
        Vector3f color = new Vector3f(0.2f,0.2f,0.2f);
        for (int i = 0; i < maxLines; i++) {
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);
            if( i< numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x,firstY), new Vector2f(x,firstY+height), color); //DISEGNO TIRANDO LA LINEA VERTICALE PER TUTTA LA ALTEZZA DELLA FINESTRA
            }

            if(i<numHzLines){
                DebugDraw.addLine2D(new Vector2f(firstX,y), new Vector2f(firstX+widht,y),color);
            }
            //UPDATE X AND Y
        }

    }
}
