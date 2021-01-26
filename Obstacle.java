import java.util.Objects;

public class Obstacle implements IMapElement{
    private final int x;
    private final int y;
    private SquareMap map;
    private int numberOfRemainingLives;
    private boolean isActive = true;

    public Obstacle(SquareMap map, int x, int y) {
        this.map = map;
        this.x = x;
        this.y = y;
        numberOfRemainingLives = 2;
    }

    public Vector2d getCoordinates() {
        return new Vector2d(this.x, this.y);
    }

    public void beHit(int force) {
        numberOfRemainingLives--;
        if(numberOfRemainingLives <= 2) {
            deactivate();
        }
    }

    public void remove() {
        map.handleObstacleElimination(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obstacle that = (Obstacle) o;
        return x == that.getCoordinates().getX() && y == that.getCoordinates().getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, map);
    }

    public void deactivate() {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }
}
