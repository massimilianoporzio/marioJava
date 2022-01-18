package jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;
    public Vector2f position;
    private Vector2f projectionSize = new Vector2f(32.0f*40.0f,32.0f*21.0f); // dimensioni della visuale in world coords


    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        //CREATE THE PROJ MATRIX USING THE ORTHO PROJECTION (stesse dimensioni a spostarci lungo z (2d game))
        //32*40 e 32*21 sono le dimensioni nelle coord del MONDO non dip da quanto è larga o alta la finestra
        //SONO MULTIPLI DI 32 PERCHé VOGLIO DISEGNARE UNA GRIGLIA DI PASSO 32 NELL'EDITOR DEI LIVELLI
        projectionMatrix.ortho(0.0f, projectionSize.x, 0.0f, projectionSize.y,0.f, 100.f);
        //E CREO ANCHE LA INVERSA
        projectionMatrix.invert(inverseProjection);
    }

    public Vector2f getProjectionSize() {
        return projectionSize;
    }

    public Matrix4f getViewMatrix(){
        //where the camera is
        Vector3f cameraFront = new Vector3f(0.f, 0.f,-1.0f); //-z in the eye ma poi in NDC è positiva
        Vector3f cameraUp = new Vector3f(0.f,1.0f,0.0f); // viste della telecamera (versori)
        this.viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y ,20.f),
                cameraFront.add(position.x,position.y,0.0f),
                cameraUp);
        this.viewMatrix.invert(inverseView);//ogni volta cha aggiusto la matrice View faccio anche l'inversa
        return this.viewMatrix;
    }

    public Matrix4f getInverseProjection() {
        return inverseProjection;
    }

    public Matrix4f getInverseView() {
        return inverseView;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }
}
