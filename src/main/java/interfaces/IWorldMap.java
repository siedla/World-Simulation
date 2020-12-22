package interfaces;





import map.Vector2d;
import mapElements.AbstractWorldMapElement;
import mapElements.Animal;
import mapElements.Grass;

import java.awt.*;
import java.util.Optional;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 *
 */
public interface IWorldMap {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    /**
     * Place a animal on the map.
     *
     * @param animal
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    boolean placeAnimal(Animal animal);

    /**
     * Move the animal on the map according to the provided move directions. Every
     * n-th direction should be sent to the n-th animal on the map.
     *
     * @param position
     *            List of move directions.
     */

    boolean isOccupied(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position
     *            The position of the object.
     * @return Object or empty Optional if the position is not occupied.
     */
    Optional<AbstractWorldMapElement> objectAt(Vector2d position);

    boolean placeGrass(Grass grass);

    boolean removeGrass(Vector2d position);

    void consumeGrassByAnimals();

    void moveAnimals();

    void reproduce();

    void clearDeadAnimals();

    void reduceEnergyForAll();

    void addNewGrass();

    int getWidth();

    int getHeight();

    Vector2d freePositionAround(Vector2d position);

    void removeAnimal(Animal animal, Vector2d position);




}
