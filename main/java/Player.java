import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.min;

public class Player implements ITankElement {
    private Vector2d coordinates;
    private MapDirection direction;
    private int numberOfRemainingLives;
    private int numberOfPoints;
    private boolean isActive;
    private SquareMap map;
    private List<PowerUp> powerUpList = new ArrayList<>();
    private MapVisualizer visualizer;
    private int immortalityTime = -1;

    public Player(SquareMap map, MapVisualizer visualizer) {
        coordinates = new Vector2d(map.getMapSize() / 2, map.getMapSize() / 2);
        numberOfRemainingLives = 2;
        numberOfPoints = 0;
        direction = MapDirection.NORTH;
        isActive = true;
        this.map = map;
        this.visualizer = visualizer;
        this.visualizer.handleElementChange(this, MapElementAction.PLAYER_APPEARENCE, getCoordinates());
    }

    public void turnRight() {
        if (isNotActive()) {
            return;
        }
        direction = direction.next();
        visualizer.handleElementChange(this, MapElementAction.PLAYER_ROTATION, this.getCoordinates());
        visualizer.handleDayFinished();
    }

    public void turnLeft() {
        if (isNotActive()) {
            return;
        }
        direction = direction.previous();
        visualizer.handleElementChange(this, MapElementAction.PLAYER_ROTATION, this.getCoordinates());
        visualizer.handleDayFinished();
    }

    public void looseLife(int bulletPower) {
        if (immortalityTime > 0) {
            return;
        }
        numberOfRemainingLives = numberOfRemainingLives - bulletPower;
    }

    public void scorePoint() {
        numberOfPoints++;
    }

    public int getScore() {
        return numberOfPoints;
    }

    public void die() {
        isActive = false;
        visualizer.handleElementChange(this, MapElementAction.PLAYER_DESTROYED, this.getCoordinates());
    }

    public void getPowerUp(PowerUp powerUp) {
        if (isNotActive()) {
            return;
        }
        switch (powerUp.getType()) {
            case 5:
                immortalityTime = map.getPowerUpLifeTime();
                powerUp.deactivate();
                return;
            case 6:
                numberOfRemainingLives = min(numberOfRemainingLives + 1, 100000000);
                powerUp.deactivate();
                return;
            default:
                powerUpList.add(powerUp);
        }
        powerUp.deactivate();
    }

    @Override
    public void fire(int type) {
        immortalityTime--;
        if (isNotActive()) {
            return;
        }
        IBullet bullet;
        if (type == 2) {
            bullet = new CommonBullet(this.getCoordinates(), direction, 2, map, this, visualizer);
        } else if (type == 3) {
            bullet = new DoublePowerBullet(this.getCoordinates(), direction, 1, map, this, visualizer);
        } else if (type == 4) {
            bullet = new BouncingBullet(this.getCoordinates(), direction, 1, map, this, visualizer);
        } else {
            bullet = new CommonBullet(this.getCoordinates(), direction, 1, map, this, visualizer);
        }
        powerUpActualize();
        map.addBullet(bullet);
        visualizer.handleDayFinished();
    }

    @Override
    public void move() {
        immortalityTime--;
        if (isNotActive()) {
            return;
        }
        if (direction.toByte() % 2 == 0) {
            Vector2d oldCoordinates = coordinates;
            Vector2d vector = coordinates;
            vector = vector.add(direction.toVector());
            vector = vector.lowerLeft(map.getUpperRight()).upperRight(map.getLowerLeft());
            if (map.fieldIsEmpty(vector)) {
                map.handlePositionChange(coordinates, vector, this);
                coordinates = vector;
                visualizer.handleElementChange(this, MapElementAction.PLAYER_POSITION_CHANGE, oldCoordinates);
            }
        }
        powerUpActualize();
        visualizer.handleDayFinished();
    }

    public void powerUpActualize() {
        for (PowerUp power : powerUpList) {
            power.powerUpLive();
        }
    }

    public void moveBackwards() {
        if (isNotActive()) {
            return;
        }
        if (direction.toByte() % 2 == 0) {
            Vector2d oldCoordinates = coordinates;
            Vector2d vector = coordinates;
            vector = vector.add(direction.toVector().opposite());
            vector = vector.lowerLeft(map.getUpperRight()).upperRight(map.getLowerLeft());
            if (map.fieldIsEmpty(vector)) {
                map.handlePositionChange(coordinates, vector, this);
                coordinates = vector;
                visualizer.handleElementChange(this, MapElementAction.PLAYER_POSITION_CHANGE, oldCoordinates);
            }
        }
        visualizer.handleDayFinished();
    }

    public boolean checkPowerUpAvailability(int type) {
        for (PowerUp iterator : powerUpList) {
            if (iterator.getType() == type && iterator.isAvailable()) {
                iterator.usePowerUp();
                return true;
            }
        }
        return false;
    }

    @Override
    public Vector2d getCoordinates() {
        return coordinates;
    }

    public boolean isNotActive() {
        return !isActive;
    }

    public void beHit(int force) {
        if (immortalityTime > 0) {
            return;
        }
        numberOfRemainingLives = numberOfRemainingLives - force;
        if (numberOfRemainingLives <= 0) {
            die();
        }
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public List<PowerUp> getPowerUpList() {
        return powerUpList;
    }

    public int getNumberOfRemainingLives() {
        return numberOfRemainingLives;
    }

    public int getImmortalityTime() {
        return immortalityTime;
    }
}
