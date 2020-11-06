package agh.cs.lab5;

import java.util.Objects;

public class Animal implements IMapElement {

    private Vector2d position;
    private MapDirection direction;
    private IWorldMap map;

    public Animal(IWorldMap map) {
        this(map, new Vector2d(0, 0));
    }

    public Animal(IWorldMap map, Vector2d initialPosition) {
        direction = MapDirection.NORTH;
        position = initialPosition;
        if (map.place(this)) {
            this.map = map;
        }
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public String toString() {
        return direction.toString();
    }

    public void move(MoveDirection command) {
        switch (command) {
            case LEFT:
                direction = direction.previous();
                break;
            case RIGHT:
                direction = direction.next();
                break;
            case BACKWARD:
                Vector2d v1 = position.subtract(direction.toUnitVector());
                if (map.canMoveTo(v1)) {
                    position = v1;
                }
                break;
            case FORWARD:
                Vector2d v2 = position.add(direction.toUnitVector());
                if (map.canMoveTo(v2)) {
                    position = v2;
                }
                break;
        }
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Animal))
            return false;
        Animal that = (Animal) other;
        return this.position.equals(that.position) && this.direction == that.direction;
    }

    public int hashCode() {
        return Objects.hash(direction, position.x, position.y);
    }
}