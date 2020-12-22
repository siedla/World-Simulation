package mapElements;

import map.Vector2d;

public abstract class AbstractWorldMapElement {
    protected Vector2d position;

    public Vector2d getPosition(){
        return position;
    }

    public abstract boolean isPassable();
}