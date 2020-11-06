package agh.cs.lab5;

public class RectangularMap extends AbstractWorldMap {
    private final Vector2d lowerLeftCorner;
    private final Vector2d upperRightCorner;

    public RectangularMap(int width, int height) {
        lowerLeftCorner=new Vector2d(0, 0);
        upperRightCorner = new Vector2d(width-1, height-1);
    }

    @Override
    public boolean place(Animal animal) {
        if (super.place(animal) && animal.getPosition().follows(lowerLeftCorner) && animal.getPosition().precedes(upperRightCorner)) {
            return true;
        }
        throw new IllegalArgumentException("Animal out of the map.");
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.follows(lowerLeftCorner) && position.precedes(upperRightCorner) && super.canMoveTo(position);
    }

    protected Vector2d getLowerLeftCorner() {
        return lowerLeftCorner;
    }

    protected Vector2d getUpperRightCorner() {
        return upperRightCorner;
    }
}
