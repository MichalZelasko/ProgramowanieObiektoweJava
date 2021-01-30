import java.util.Objects;

public class BouncingBullet implements IBullet {
    protected Vector2d coordinates;
    protected MapDirection direction;
    protected int speed;
    private SquareMap map;
    private ITankElement tank;
    private boolean isActive;
    MapVisualizer visualizer;

    public BouncingBullet(Vector2d coordinates, MapDirection direction, int speed, SquareMap map, ITankElement tank, MapVisualizer visualizer) {
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
        BouncingBullet that = (BouncingBullet) o;
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
        Vector2d directionVector = direction.toVector();
        Vector2d vectorDif = direction.toVector().multiplyByScalar(speed);
        Vector2d vector = coordinates.add(direction.toVector().multiplyByScalar(speed));
        if (vector.getY() == map.getMapSize() || vector.getY() == -1) {
            vectorDif = new Vector2d(vectorDif.getX(), 0);
            directionVector = new Vector2d(directionVector.getX(), -directionVector.getY());
        }
        if (vector.getX() == map.getMapSize() || vector.getX() == -1) {
            vectorDif = new Vector2d(0, vectorDif.getY());
            directionVector = new Vector2d(-directionVector.getX(), directionVector.getY());
        }
        vector = coordinates.add(vectorDif.multiplyByScalar(speed));
        vector = vector.lowerLeft(map.getUpperRight()).upperRight(map.getLowerLeft());
        map.handlePositionChange(coordinates, vector, this);
        coordinates = vector;
        direction = directionVector.toMapDirection();
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
