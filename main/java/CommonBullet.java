import java.util.Objects;

public class CommonBullet implements IBullet {
    protected Vector2d coordinates;
    protected MapDirection direction;
    protected int speed;
    private SquareMap map;
    private ITankElement tank;
    private boolean isActive;
    private MapVisualizer visualizer;

    public CommonBullet(Vector2d coordinates, MapDirection direction, int speed, SquareMap map, ITankElement tank, MapVisualizer visualizer) {
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
        map.removeElement(this);
        isActive = false;
        visualizer.handleElementChange(this, MapElementAction.BULLET_DESTROYED, this.getCoordinates());
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
        CommonBullet that = (CommonBullet) o;
        return speed == that.speed &&
                coordinates.equals(that.coordinates) &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates, direction, speed);
    }

    public void move() {
        Vector2d oldCoordinates = getCoordinates();
        Vector2d vector = coordinates.add(direction.toVector().multiplyByScalar(speed));
        if (vector.precedes(map.getUpperRight()) || vector.follows(map.getLowerLeft())) {
            hit(false);
            return;
        }
        map.handlePositionChange(coordinates, vector, this);
        coordinates = vector;
        visualizer.handleElementChange(this, MapElementAction.BULLET_POSITION_CHANGE, oldCoordinates);
    }

    public int getForce() {
        return 1;
    }

    public boolean isNotActive() {
        return !isActive;
    }

    public MapDirection getDirection() {
        return direction;
    }
}
