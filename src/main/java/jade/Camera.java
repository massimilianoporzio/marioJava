package jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        //CREATE THE PROJ MATRIX USING THE ORTHO PROJECTION (stesse dimensioni a spostarci lungo z (2d game))
        projectionMatrix.ortho(0.0f, 32.0f*40.0f, 0.0f, 32.0f*21.0f,0.f, 100.f);
    }

    public Matrix4f getViewMatrix(){
        //where the camera is
        Vector3f cameraFront = new Vector3f(0.f, 0.f,-1.0f); //-z in the eye ma poi in NDC è positiva
        Vector3f cameraUp = new Vector3f(0.f,1.0f,0.0f); // viste della telecamera (versori)
        this.viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y ,20.f),
                cameraFront.add(position.x,position.y,0.0f),
                cameraUp);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }
}
