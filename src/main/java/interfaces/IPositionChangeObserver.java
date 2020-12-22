package interfaces;

import map.Vector2d;
import mapElements.Animal;

public interface IPositionChangeObserver {
    void positionChanged(Animal movedElement, Vector2d oldPosition, Vector2d newPosition);

}