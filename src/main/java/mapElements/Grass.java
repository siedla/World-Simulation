package mapElements;

import map.Vector2d;

public class Grass extends AbstractWorldMapElement{


    public Grass(Vector2d initialPostion){
        position = initialPostion;
    }

    public String toString() {
        return "*";
    }

    public boolean isPassable() {
        return true;
    }

}