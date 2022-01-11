package components;

import jade.Component;

public class SpriteRenderer extends Component {

    private boolean firstTime = true;

    @Override
    public void update(float dt) {
        System.out.println("I AM UPDATING");
    }

    @Override
    public void start() {
        if(firstTime){
            System.out.println("I AM STARTING");
            firstTime = false;
        }

    }
}
