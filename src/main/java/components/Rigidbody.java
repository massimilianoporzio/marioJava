package components;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class Rigidbody extends Component {
    private int colliderType = 0;
    private float friction = 0.8f; //ATTRITO

    public Vector3f velocity = new Vector3f(0,0.5f,0); //ALONG y
    public transient Vector4f tmp = new Vector4f(0,0,0,0);

}
