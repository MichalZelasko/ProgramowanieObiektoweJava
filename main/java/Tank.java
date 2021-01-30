import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.min;
import static java.lang.Math.abs;

public class Tank implements IMapElement {
    private int x;
    private int y;
    private List<PowerUp> powerUpList = new ArrayList<>();
    private MapDirection direction;
    private Player player;
    private SquareMap map;
    private int numberOfRemainingLives;
    private Random generator = new Random();
    private boolean isActive = true;
    private MapVisualizer visualizer;
    private int immortalityTime = -1;

    public Tank(int x, int y, Player player, SquareMap map, int number, MapVisualizer visualizer) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.map = map;
        this.visualizer = visualizer;
        this.direction = MapDirection.NORTH;
        this.visualizer.handleElementChange(this, MapElementAction.TANK_APPEARENCE, new Vector2d(x, y));
        generatePowerUps(number);
        rotateToFire();
    }

    public Vector2d getCoordinates() {
        return new Vector2d(x, y);
    }

    public void generatePowerUps(int number) {
        for (int i = 0; i < number; i++) {
            powerUpList.add(new PowerUp(abs(generator.nextInt()) % 6, 0, 0, 0, visualizer));
        }
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUpList.add(powerUp);
        powerUp.deactivate();
    }

    public void removePowerUp(PowerUp powerUp) {
        powerUpList.removeIf(e -> (e == powerUp));
    }

    public void takeTurn() {
        if (generator.nextInt() % 2 == 0) {
            int n = abs(generator.nextInt()) % 10;
            if (n <= 4 && n >= 2 && checkPowerUpAvailability(n)) {
                fire(n);
                return;
            }
            fire(1);
            return;
        }
        move();
        if (generator.nextInt() % 2 == 0 && checkPowerUpAvailability(1)) {
            if (generator.nextInt() % 2 == 0) {
                int n = abs(generator.nextInt()) % 10;
                if (n <= 4 && n >= 2 && checkPowerUpAvailability(n)) {
                    fire(n);
                    return;
                }
                fire(1);
                return;
            }
            move();
        }
    }

    public void getPowerUp(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case 5:
                immortalityTime = map.getPowerUpLifeTime();
                numberOfRemainingLives = 100000000;
                powerUp.deactivate();
                return;
            case 6:
                numberOfRemainingLives = min(numberOfRemainingLives + 1, 100000000);
                powerUp.deactivate();
                return;
            default:
                addPowerUp(powerUp);
        }
        powerUp.deactivate();
    }

    public void fire(int type) {
        rotateToFire();
        IBullet bullet;
        switch (type) {
            case 2:
                bullet = new CommonBullet(this.getCoordinates(), direction, 2, map, null, visualizer);
                break;
            case 3:
                bullet = new DoublePowerBullet(this.getCoordinates(), direction, 1, map, null, visualizer);
                break;
            case 4:
                bullet = new BouncingBullet(this.getCoordinates(), direction, 1, map, null, visualizer);
                break;
            default:
                bullet = new CommonBullet(this.getCoordinates(), direction, 1, map, null, visualizer);
        }
        map.addBullet(bullet);
    }

    public void move() {
        immortalityTime--;
        powerUpActualize();
        rotateToMove();
        moveForward();
    }

    public void beHit(int power) {
        if (immortalityTime > 0) {
            return;
        }
        numberOfRemainingLives = numberOfRemainingLives - power;
        if (numberOfRemainingLives <= 0) {
            deactivate();
            visualizer.handleElementChange(this, MapElementAction.TANK_DESTROYED, this.getCoordinates());
        }
    }

    private void moveForward() {
        if (direction.toByte() % 2 == 0) {
            Vector2d coordinates = getCoordinates();
            Vector2d vector = coordinates;
            vector = vector.add(direction.toVector());
            vector = vector.lowerLeft(map.getUpperRight()).upperRight(map.getLowerLeft());
            if (map.fieldIsEmpty(vector)) {
                map.handlePositionChange(coordinates, vector, this);
                x = vector.getX();
                y = vector.getY();
                visualizer.handleElementChange(this, MapElementAction.TANK_POSITION_CHANGE, coordinates);
            }
        }
    }

    public void rotateToFire() {
        int xDif = player.getCoordinates().getX() - x;
        int yDif = player.getCoordinates().getY() - y;
        if (xDif > 0 && yDif > 0) {
            if (yDif * 414 >= xDif * 1000) {
                direction = MapDirection.NORTH;
            } else if (yDif * 2414 >= xDif * 1000) {
                direction = MapDirection.NORTHEAST;
            } else {
                direction = MapDirection.EAST;
            }
        } else if (xDif > 0 && yDif < 0) {
            if (-yDif * 2414 <= xDif * 1000) {
                direction = MapDirection.EAST;
            } else if (-yDif * 414 <= xDif * 1000) {
                direction = MapDirection.SOUTHEAST;
            } else {
                direction = MapDirection.SOUTH;
            }
        } else if (xDif < 0 && yDif < 0) {
            if (-yDif * 414 >= -xDif * 1000) {
                direction = MapDirection.SOUTH;
            } else if (-yDif * 2414 >= -xDif * 1000) {
                direction = MapDirection.SOUTHWEST;
            } else {
                direction = MapDirection.WEST;
            }
        } else {
            if (yDif * 2414 <= -xDif * 1000) {
                direction = MapDirection.WEST;
            } else if (yDif * 414 <= -xDif * 1000) {
                direction = MapDirection.NORTHWEST;
            } else {
                direction = MapDirection.NORTH;
            }
        }
        visualizer.handleElementChange(this, MapElementAction.TANK_ROTATION, this.getCoordinates());
    }

    public void rotateToMove() {
        int xDif = player.getCoordinates().getX() - x;
        int yDif = player.getCoordinates().getY() - y;
        if (abs(xDif) >= abs(yDif)) {
            if (xDif < 0) {
                direction = MapDirection.WEST;
            } else {
                direction = MapDirection.EAST;
            }
        } else {
            if (yDif < 0) {
                direction = MapDirection.SOUTH;
            } else {
                direction = MapDirection.NORTH;
            }
        }
        visualizer.handleElementChange(this, MapElementAction.TANK_ROTATION, this.getCoordinates());
    }

    public boolean checkPowerUpAvailability(int type) {
        for (PowerUp iterator : powerUpList) {
            if (iterator.getType() == type) {
                removePowerUp(iterator);
                return true;
            }
        }
        return false;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public void deactivate() {
        isActive = false;
    }

    public boolean isNotActive() {
        return !isActive;
    }

    public void powerUpActualize() {
        for (PowerUp power : powerUpList) {
            power.powerUpLive();
        }
    }
}
