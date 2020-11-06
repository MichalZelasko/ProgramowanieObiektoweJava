package agh.cs.lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrassField extends AbstractWorldMap {

    private final List<Grass> grass = new ArrayList<>();

    public GrassField(int numberOfGrass) {
        Random generator = new Random(42);
        int range = (int) Math.sqrt(10 * numberOfGrass);
        for (int i = 0; i < numberOfGrass; i++) {
            Vector2d vector;
            do {
                vector = new Vector2d(generator.nextInt(range), generator.nextInt(range));
            } while (this.objectAt(vector) instanceof Grass);
            Grass g1 = new Grass(vector);
            grass.add(g1);
        }
    }

    @Override
    public Object objectAt(Vector2d position) {
        Object object = super.objectAt(position);
        if (object != null) {
            return object;
        }
        for (Grass grassIterator : grass) {
            if (grassIterator.getPosition().equals(position)) {
                return grassIterator;
            }
        }
        return null;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return super.isOccupied(position) || this.objectAt(position) instanceof Grass;
    }

    protected Vector2d getLowerLeftCorner() {
        Vector2d lowerLeft = new Vector2d(0, 0);
        if (!grass.isEmpty()) {
            lowerLeft = grass.get(0).getPosition();
        }
        Animal[] animalsArray= animals.values().toArray(new Animal[0]);
        if(animalsArray.length!=0){
            lowerLeft=animalsArray[0].getPosition();
        }
        for(Animal animalIterator : animalsArray){
            lowerLeft=lowerLeft.lowerLeft(animalIterator.getPosition());
        }
        for (Grass grassIt : grass) {
            lowerLeft = lowerLeft.lowerLeft(grassIt.getPosition());
        }
        return lowerLeft;
    }

    protected Vector2d getUpperRightCorner() {
        Vector2d upperRight = new Vector2d(0, 0);
        Animal[] animalsArray= animals.values().toArray(new Animal[0]);
        if (!grass.isEmpty()) {
            upperRight = grass.get(0).getPosition();
        }
        if (animalsArray.length!=0) {
            upperRight = animalsArray[0].getPosition();
        }
        for(Animal animalIterator : animalsArray){
            upperRight=upperRight.upperRight(animalIterator.getPosition());
        }
        for (Grass grass : grass) {
            upperRight = upperRight.upperRight(grass.getPosition());
        }
        return upperRight;
    }

}
