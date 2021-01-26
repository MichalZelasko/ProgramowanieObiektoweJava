import java.util.Objects;

public class CommonBullet implements IBullet{
    protected Vector2d coordinates;
    protected MapDirection direction;
    protected int speed;
    private SquareMap map;
    private ITankElement tank;
    private boolean isActive;

    public CommonBullet(Vector2d coordinates, MapDirection direction, int speed, SquareMap map, ITankElement tank) {
        this.coordinates = coordinates;
        this.direction = direction;
        this.speed = speed;
        this.map = map;
        this.tank = tank;
        isActive = true;
    }

    public void hit(boolean tankHit) {
        map.removeElement(this);
        isActive = false;
        if(tank != null){
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
        Vector2d vector = coordinates.add(direction.toVector().multiplyByScalar(speed));
        if(!vector.precedes(map.getUpperRight()) || !vector.follows(map.getLowerLeft())) {
            hit(false);
            return;
        }
        map.handlePositionChange(coordinates, vector, this);
        coordinates = vector;
    }

    public int getForce() {
        return 1;
    }

    public boolean isNotActive() {
        return !isActive;
    }
}
