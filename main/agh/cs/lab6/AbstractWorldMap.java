package agh.cs.lab5;

import java.util.LinkedHashMap;
import java.util.Map;

abstract class AbstractWorldMap implements IWorldMap {
    protected Map<Vector2d, Animal> animals = new LinkedHashMap<>();
    MapVisualizer drawMap = new MapVisualizer(this);

    public boolean canMoveTo(Vector2d position) {
        return !(this.objectAt(position) instanceof Animal);
    }

    @Override
    public boolean place(Animal animal) {
        if (!(objectAt(animal.getPosition()) instanceof Animal)) {
            animals.put(animal.getPosition(), animal);
            return true;
        }
        throw new IllegalArgumentException("There is an animal at this place.");
    }

    @Override
    public void run(MoveDirection[] directions) {
        int directionsLength = directions.length;
        Animal[] animalsArray= animals.values().toArray(new Animal[0]);
        int numberOfAnimals = animalsArray.length;
        if (numberOfAnimals <= 0) {
            return;
        }
        Vector2d v1;
        for (int j = 0; j < directionsLength; j++) {
            v1=animalsArray[j % numberOfAnimals].getPosition();
            animalsArray[j % numberOfAnimals].move(directions[j]);
            animals.remove(v1);
            animals.put(animalsArray[j % numberOfAnimals].getPosition(), animalsArray[j % numberOfAnimals]);
        }
    }

    @Override
    public Object objectAt(Vector2d position) {
        if(animals.containsKey(position)) {
            return animals.get(position);
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectAt(position) instanceof IMapElement;
    }

    protected abstract Vector2d getLowerLeftCorner();

    protected abstract Vector2d getUpperRightCorner();

    @Override
    public String toString() {
        Vector2d lowerLeft = getLowerLeftCorner();
        Vector2d upperRight = getUpperRightCorner();
        return drawMap.draw(lowerLeft, upperRight);
    }
}
