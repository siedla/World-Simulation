package mapElements;

import EnumClasses.MapDirection;
import interfaces.IPositionChangeObserver;
import interfaces.IPositionChangePublisher;
import interfaces.IWorldMap;
import map.Vector2d;

import java.util.LinkedList;
import java.util.Random;

public class Animal extends AbstractWorldMapElement implements IPositionChangePublisher {
    private MapDirection orientation = MapDirection.NORTH;
    private final IWorldMap map;
    private final LinkedList<IPositionChangeObserver> observerList = new LinkedList<>();
    private final LinkedList<Animal> childrenList = new LinkedList<>();
    private int energy;
    private int age = 0;
    private boolean isAlive = true;


    public AnimalGenes genes;

    public Animal(IWorldMap map, Vector2d initialPosition, int energy){
        position = initialPosition;
        this.map = map;
        genes = new AnimalGenes();
        this.energy=energy;
    }

    public Animal(IWorldMap map, Animal father, Animal mother, Vector2d initialPosition, int energy){
        position = initialPosition;
        this.map = map;
        genes = new AnimalGenes(father.genes, mother.genes);
        this.energy=energy;
    }

    public Animal copulation(Animal parent){
        int childEnergy = (int) (0.25 * parent.energy) + (int) (this.energy * 0.25);
        parent.changeAnimalEnergy((int) (-0.25 * parent.energy));
        this.changeAnimalEnergy((int) (-this.energy * 0.25));
        return new Animal(map, this, parent, this.getPosition(),childEnergy);
    }
    public void move(){

        Vector2d old = this.getPosition();
        position = position.add(orientation.toUnitVector());
        if(position.x > map.getWidth() - 1){
            position = new Vector2d(0, position.y);
        }
        if(position.y > map.getHeight() - 1){
            position = new Vector2d(position.x, 0);
        }
        if(position.x < 0){
            position = new Vector2d(map.getWidth(), position.y);
        }
        if(position.y < 0){
            position = new Vector2d(position.x, map.getHeight()-1);
        }
        age++;
        notifyChangePosition(old, position);


    }

    public void changeDirection(){
        Random r = new Random();
        int index = r.nextInt(32);

        int rotation = genes.getGene(index);
        for(int i=0; i<rotation; i++){
            orientation = orientation.next();

        }
    }

    public void setPosition(Vector2d newPosition){
        this.position = newPosition;
    }

    public boolean isPassable() {
        return false;
    }

    @Override
    public void addObserver(IPositionChangeObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(IPositionChangeObserver observer) {
        observerList.remove(observer);
    }

    public void notifyChangePosition(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver o: observerList){
            o.positionChanged(this, oldPosition, newPosition);
        }
    }

    public void changeAnimalEnergy(int energy){
        this.energy += energy;
    }

    public int getEnergy(){
        return energy;
    }

    public int getAge(){
        return age;
    }

    public int getNumberOfChildren(){
        return childrenList.size();
    }

    public void addChild(Animal child){
        childrenList.add(child);
    }

    public void reduceEnergyAfterCopulation(){
        energy = (int) (energy*0.75);
    }

    public void died(){
        isAlive = false;
    }

    public boolean isAlive(){
        return isAlive;
    }
}
