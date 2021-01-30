import java.util.Objects;

public class Obstacle implements IMapElement {
    private final int x;
    private final int y;
    private SquareMap map;
    private int numberOfRemainingLives;
    private boolean isActive = true;
    private MapVisualizer visualizer;

    public Obstacle(SquareMap map, int x, int y, MapVisualizer visualizer) {
        this.map = map;
        this.x = x;
        this.y = y;
        this.visualizer = visualizer;
        numberOfRemainingLives = 2;
        this.visualizer.handleElementChange(this, MapElementAction.OBSTACLE_APPEARENCE, getCoordinates());
    }

    public Vector2d getCoordinates() {
        return new Vector2d(this.x, this.y);
    }

    public void beHit(int force) {
        numberOfRemainingLives = numberOfRemainingLives - force;
        if (numberOfRemainingLives <= 0) {
            deactivate();
        }
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
        visualizer.handleElementChange(this, MapElementAction.OBSTACLE_DESTROYED, getCoordinates());
        map.handleObstacleElimination(this);
    }

    public boolean isActive() {
        return isActive;
    }
}
