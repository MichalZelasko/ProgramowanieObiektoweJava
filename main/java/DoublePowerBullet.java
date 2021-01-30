import java.util.Objects;

public class DoublePowerBullet implements IBullet {
    protected Vector2d coordinates;
    protected MapDirection direction;
    protected int speed;
    private SquareMap map;
    private ITankElement tank;
    private boolean isActive;
    private MapVisualizer visualizer;

    public DoublePowerBullet(Vector2d coordinates, MapDirection direction, int speed, SquareMap map, ITankElement tank, MapVisualizer visualizer) {
        this.coordinates = coordinates;
        this.direction = direction;
        this.speed = speed;
        this.map = map;
        this.tank = tank;
        this.visualizer = visualizer;
        isActive = true;
        this.visualizer.handleElementChange(this, MapElementAction.BULLET_APPEARENCE, getCoordinates());
    }

    public void hit(boolean tankHit) {
        if (tank != null && tankHit) {
            tank.scorePoint();
        }
    }

    public Vector2d getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoublePowerBullet that = (DoublePowerBullet) o;
        return speed == that.speed &&
                coordinates.equals(that.coordinates) &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, direction, speed);
    }

    public void move() {
        Vector2d vector = coordinates.add(direction.toVector().multiplyByScalar(speed));
        if (vector.precedes(map.getUpperRight()) || vector.follows(map.getLowerLeft())) {
            isActive = false;
            map.removeElement(this);
            visualizer.handleElementChange(this, MapElementAction.BULLET_DESTROYED, getCoordinates());
            return;
        }
        map.handlePositionChange(coordinates, vector, this);
        coordinates = vector;
    }

    public int getForce() {
        return 2;
    }

    public boolean isNotActive() {
        return !isActive;
    }

    public MapDirection getDirection() {
        return direction;
    }
}
