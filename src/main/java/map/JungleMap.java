package map;

import interfaces.IPositionChangeObserver;
import interfaces.IWorldMap;
import mapElements.AbstractWorldMapElement;
import mapElements.Animal;
import mapElements.Grass;

import java.util.*;

public class JungleMap implements IWorldMap, IPositionChangeObserver {
    private final Vector2d lowerLeft = new Vector2d(0,0);
    private final Vector2d upperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final LinkedList<Animal> animals = new LinkedList<>();
    private final Map<Vector2d,LinkedList<Animal>> animalMap = new HashMap<>();
    private final Map<Vector2d, AbstractWorldMapElement> grassMap = new HashMap<>();
    private final LinkedList<Grass> grassList = new LinkedList<>();
    public final int width;
    public final int height;
    private final int startAnimalsEnergy;
    public final int jungleWidth;
    public final int jungleHeight;
    private final int dailyEnergy;
    private final int grassEnergy;
    private int sumOfAge = 0;
    private int numberOfDeadAnimals = 0;
    private final long[] geneCounter = new long[8];

    public JungleMap(int width, int height, int jungleRatio, int grassEnergy, int dailyEnergy, int startAnimalsEnergy) {
        this.startAnimalsEnergy = startAnimalsEnergy;
        this.grassEnergy = grassEnergy;
        this.dailyEnergy = dailyEnergy;
        this.width=width;
        this.height=height;
        double jungleRatio1 = (double) jungleRatio / 100;
        this.jungleWidth = (int) (jungleRatio1 * width);
        this.jungleHeight = (int) (jungleRatio1 * height);
        this.upperRight = new Vector2d(width, height);
        this.jungleLowerLeft = new Vector2d((width-jungleWidth)/2, ((height-jungleHeight)/2));
        this.jungleUpperRight = new Vector2d((width-jungleWidth)/2 + jungleWidth, ((height-jungleHeight)/2)+jungleHeight);
    }


    @Override
    public void positionChanged(Animal movedElement, Vector2d oldPosition, Vector2d newPosition) {
        LinkedList<Animal> list = animalMap.get(oldPosition);
        list.remove(movedElement);
        if(list.isEmpty()){
            animalMap.remove(oldPosition);
        }
        LinkedList<Animal> list2 = animalMap.get(newPosition);
        if(list2==null){
            LinkedList<Animal> tmp = new LinkedList<>();
            animalMap.put(newPosition, tmp);
        }
        animalMap.get(newPosition).add(movedElement);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return (position.follows(lowerLeft) && position.precedes(upperRight ));
    }

    @Override
    public boolean placeAnimal(Animal animal) {
        Vector2d freePosition = freePositionAround(animal.getPosition());
        animal.setPosition(freePosition);
        if(animalMap.get(freePosition)==null){
            LinkedList<Animal> list = new LinkedList<>();
            animalMap.put(freePosition, list);
        }
        for(int i=0; i < 8; i++){
            geneCounter[i] += animal.genes.getNumberOfGenes(i);
        }
        animal.addObserver(this);
        animals.add(animal);
        animalMap.get(freePosition).add(animal);
        return true;
    }

    public void placeStartAnimal(int n){
        Random r = new Random();
        for(int i=0; i<n; i++){
            int x = r.nextInt(jungleUpperRight.x-jungleLowerLeft.x)+jungleLowerLeft.x;
            int y = r.nextInt(jungleUpperRight.y-jungleLowerLeft.y)+jungleLowerLeft.y;
            Animal animal = new Animal(this, new Vector2d(x,y),startAnimalsEnergy);
            placeAnimal(animal);
        }
    }

    @Override
    public void removeAnimal(Animal animal, Vector2d position){
        sumOfAge += animal.getAge();
        numberOfDeadAnimals +=1;
        LinkedList<Animal> list = animalMap.get(position);
        animal.removeObserver(this);
        animals.remove(animal);
        list.remove(animal);
        if(list.isEmpty()){
            animalMap.remove(position);
        }
    }

    @Override
    public Vector2d freePositionAround(Vector2d position){
        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                Vector2d checkPosition = new Vector2d(position.x + i, position.y + j);
                if(animalMap.get(checkPosition)==null && checkPosition.follows(lowerLeft) && checkPosition.precedes(upperRight)){
                    return checkPosition;
                }
            }
        }
        return position;
    }

    @Override
    public boolean placeGrass(Grass grass){
        if(grassMap.get(grass.getPosition())==null){
            grassMap.put(grass.getPosition(), grass);
            grassList.add(grass);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeGrass(Vector2d position){
        if(grassMap.get(position) != null){
            grassList.remove(grassMap.get(position));
            grassMap.remove(position);
        }
        return true;
    }

    @Override
    public void consumeGrassByAnimals(){
        LinkedList <AbstractWorldMapElement> grassToRemove = new LinkedList<>();
        for(AbstractWorldMapElement toEat : grassList){
            LinkedList<Animal> list = animalMap.get(toEat.getPosition());
            if(list != null && !list.isEmpty()){
                int maxEnergy = 0;
                int counter;
                counter = 1;
                for (AbstractWorldMapElement abstractWorldMapElement : list) {
                    Animal toCompare = (Animal) abstractWorldMapElement;
                    if (toCompare.getEnergy() > maxEnergy) {
                        maxEnergy = toCompare.getEnergy();
                        counter = 1;
                    }
                    else if (toCompare.getEnergy() == maxEnergy){
                        counter++;
                    }
                }
                for (AbstractWorldMapElement abstractWorldMapElement : list) {
                    Animal toChange = (Animal) abstractWorldMapElement;
                    if(toChange.getEnergy() == maxEnergy){
                        toChange.changeAnimalEnergy(grassEnergy/counter);
                    }
                }
                grassToRemove.push(toEat);

            }

        }
        for(AbstractWorldMapElement toRemove: grassToRemove){
            removeGrass(toRemove.getPosition());
        }
    }

    @Override
    public void moveAnimals(){

        for(Animal animal : animals){
            animal.changeDirection();
            animal.move();
        }

    }

    @Override
    public void reproduce(){
        LinkedList<Animal> animalToAdd = new LinkedList<>();
        for (LinkedList<Animal> animalList : animalMap.values()) {
                if (animalList.size() >= 2) {
                    animalList.sort(Comparator.comparingInt(Animal::getEnergy));
                    if(animalList.get(0).getEnergy() > 0.5 * startAnimalsEnergy && animalList.get(1).getEnergy() > 0.5 * startAnimalsEnergy){
                        Animal newAnimal = animalList.get(0).copulation(animalList.get(1));
                        animalToAdd.push(newAnimal);
                        animalList.get(0).addChild(newAnimal);
                        animalList.get(1).addChild(newAnimal);
                        animalList.get(0).reduceEnergyAfterCopulation();
                        animalList.get(1).reduceEnergyAfterCopulation();
                    }
                }
        }
        for(Animal animal: animalToAdd){
            placeAnimal(animal);
        }
    }

    @Override
    public void clearDeadAnimals(){
        LinkedList<Animal> animalsToRemove = new LinkedList<>();
        for(Animal animal : animals){
            if(animal.getEnergy() <= 0){
                animalsToRemove.push(animal);

            }
        }
        for(Animal animal : animalsToRemove){
            animal.died();
            removeAnimal(animal, animal.getPosition());
        }
    }

    @Override
    public void reduceEnergyForAll(){
        for(Animal animal: animals){
            animal.changeAnimalEnergy((-1)*dailyEnergy);
        }
    }

    @Override
    public void addNewGrass(){
        LinkedList<Vector2d> freePlacesSteppe = new LinkedList<>();
        LinkedList<Vector2d> freePlacesJungle = new LinkedList<>();
        for(int i=0; i < upperRight.x; i++){
            for(int j=0; j < upperRight.y; j++){
                Vector2d current = new Vector2d(i,j);
                if(objectAt(current).isEmpty()){
                    if(current.follows(jungleLowerLeft) && current.precedes(jungleUpperRight)){
                        freePlacesJungle.add(current);
                    }
                    else if(canMoveTo(current)){
                        freePlacesSteppe.add(current);
                    }
                }
            }
        }
        if(!freePlacesJungle.isEmpty()){
            Random r = new Random();
            int index = r.nextInt(freePlacesJungle.size());
            placeGrass(new Grass(freePlacesJungle.get(index)));
        }
        if(!freePlacesSteppe.isEmpty()){
            Random r = new Random();
            int index = r.nextInt(freePlacesSteppe.size());
            placeGrass(new Grass(freePlacesSteppe.get(index)));
        }
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position).isPresent();
    }

    @Override
    public Optional<AbstractWorldMapElement> objectAt(Vector2d position) {
        AbstractWorldMapElement animalAt;
        if(animalMap.get(position)!=null){
            animalAt = animalMap.get(position).getFirst();
            if(animalAt != null)
                return Optional.of(animalAt);
        }

        AbstractWorldMapElement grassAt = grassMap.get(position);
        if(grassAt!=null){
            return Optional.of(grassAt);
        }
        return Optional.empty();
    }

    @Override
    public int getWidth(){
        return width;
    }

    @Override
    public int getHeight(){
        return height;
    }


    public LinkedList<Grass> getGrass(){
        return grassList;
    }

    public LinkedList<Animal> getAnimals(){
        return animals;
    }

    public Vector2d getJungleLowerLeft(){
        return jungleLowerLeft;
    }

    public int averageAge(){
        if(numberOfDeadAnimals > 0){
            return sumOfAge / numberOfDeadAnimals;
        }
        else return 0;
    }

    public int averageEnergy() {
        int numberOfanimals = animals.size();
        int sum = 0;
        for(Animal animal: animals){
            sum += animal.getEnergy();
        }
        if(numberOfanimals > 0){
            return sum/numberOfanimals;
        }
        else return 0;
    }

    public double averageChildrenNumber(){
        int numberOfanimals = animals.size();
        int sum = 0;
        for(Animal animal: animals){
            sum += animal.getNumberOfChildren();
        }
        if(numberOfanimals > 0){
            double average = (double)sum/numberOfanimals;
            average *= 100;
            average = Math.round(average);
            average/=100;
            return average;
        }
        else return 0;
    }

    public int getStartAnimalsEnergy(){
        return startAnimalsEnergy;
    }

    public int getMostPopularGene(){
        long max = 0;
        int geneNumber = 0;
        for(int i=0; i < 8; i++){
            if(geneCounter[i] > max){
                max = geneCounter[i];
                geneNumber = i;
            }
        }
        return geneNumber;
    }

    public Animal getAnimalAtPosition(Vector2d position){
        if(animalMap.get(position) != null){
            return animalMap.get(position).getFirst();
        }
        return null;
    }
}
