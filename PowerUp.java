import java.util.Objects;

public class PowerUp implements IMapElement{
    private final Vector2d position;
    private PowerUpType type;
    private boolean isActive;
    private int key;

    public PowerUp(int type, int x, int y, int key) {
        position = new Vector2d(x, y);
        this.type = PowerUpType.TwoMovePerRound;
        this.type = this.type.fromInt(type);
        isActive = true;
        this.key = key;
    }

    public void usePowerUp(){
        isActive = false;
    }

    public int getKey(){
        return this.key;
    }

    public int getType(){
        return type.toInt();
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
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, type, isActive, key);
    }
}
