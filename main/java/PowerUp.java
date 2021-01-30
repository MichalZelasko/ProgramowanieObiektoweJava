import java.util.Objects;

public class PowerUp implements IMapElement {
    private final Vector2d position;
    private PowerUpType type;
    private boolean isActive;
    private boolean isAvailable;
    private int key;
    private MapVisualizer visualizer;
    private int lifeTime;

    public PowerUp(int type, int x, int y, int key, MapVisualizer visualizer) {
        position = new Vector2d(x, y);
        this.type = PowerUpType.TwoMovePerRound;
        this.type = this.type.fromInt(type);
        isActive = true;
        isAvailable = true;
        this.key = key;
        this.visualizer = visualizer;
        this.visualizer.handleElementChange(this, MapElementAction.POWER_UP_APPEARENCE, getCoordinates());
        lifeTime = this.visualizer.getMap().getPowerUpLifeTime();
    }

    public void usePowerUp() {
        isAvailable = false;
    }

    public int getType() {
        return type.toInt();
    }

    public PowerUpType getPowerUpType() {
        return type;
    }

    @Override
    public Vector2d getCoordinates() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerUp powerUp = (PowerUp) o;
        return isActive == powerUp.isActive &&
                position.equals(powerUp.position) &&
                type == powerUp.type &&
                key == powerUp.key;
    }

    public void deactivate() {
        visualizer.handleElementChange(this, MapElementAction.POWER_UP_CONSUMPTION, this.getCoordinates());
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, type, isActive, key);
    }

    public void powerUpLive() {
        if (lifeTime <= 0) {
            isAvailable = false;
        }
        if (!isActive && isAvailable) {
            lifeTime--;
        }
    }
}
