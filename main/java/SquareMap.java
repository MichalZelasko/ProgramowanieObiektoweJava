import java.util.*;

import static java.lang.StrictMath.abs;

public class SquareMap {
    private int mapSize;
    private List<Tank> tankList = new ArrayList<>();
    private List<Obstacle> obstacleList = new ArrayList<>();
    private List<PowerUp> powerUpList = new ArrayList<>();
    private List<IBullet> bulletList = new ArrayList<>();
    private HashMap<Vector2d, IMapElement> occupiedFields;
    private Player player;
    private int timeToTankAdd;
    private int timeToPowerUpAdd;
    private int timeToObstacleAdd;
    private int timeFromLastTankAddition = 0;
    private int timeFromLastPowerUpAddition = 0;
    private int timeFromLastObstacleAddition = 0;
    private int numberOfPowerUps;
    private Random generator = new Random();
    private Vector2d lowerLeft = new Vector2d(0, 0);
    private Vector2d upperRight;
    private MapVisualizer visualizer;
    private int numberOfEnemies;
    private int numberOfObstacles;
    private int number;
    private int powerUpLifeTime;

    public SquareMap(int mapSize, int numberOfEnemies, int numberOfPowerUps, int numberOfObstacles, int timeToTankAdd, int timeToPowerUpAdd, int timeToObstacleAdd, int number, int powerUpLifeTime) {
        this.mapSize = mapSize;
        upperRight = new Vector2d(mapSize - 1, mapSize - 1);
        this.timeToTankAdd = timeToTankAdd;
        this.timeToPowerUpAdd = timeToPowerUpAdd;
        this.timeToObstacleAdd = timeToObstacleAdd;
        this.numberOfEnemies = numberOfEnemies;
        this.numberOfObstacles = numberOfObstacles;
        this.numberOfPowerUps = numberOfPowerUps;
        this.number = number;
        this.powerUpLifeTime = powerUpLifeTime;
        occupiedFields = new HashMap<>();
    }

    public void setVisualizer(MapVisualizer visualizer) {
        this.visualizer = visualizer;
        this.player = new Player(this, visualizer);
        addPlayer(player);
        prepareMap(numberOfEnemies, numberOfPowerUps, numberOfObstacles, number);
    }

    public void prepareMap(int numberOfEnemies, int numberOfPowerUps, int numberOfObstacles, int number) {
        int iterator = 0;
        while (iterator < numberOfEnemies) {
            if (generateTank(number)) {
                iterator++;
            }
        }
        iterator = 0;
        while (iterator < numberOfPowerUps) {
            if (generatePowerUp()) {
                iterator++;
            }
        }
        iterator = 0;
        while (iterator < numberOfObstacles) {
            if (generateObstacle()) {
                iterator++;
            }
        }
        visualizer.handleDayFinished();
    }

    public void oneRound(int number, int canMove) {
        if (player.isNotActive() || canMove > 0) {
            return;
        }
        visualizer.handleDayFinished();
        for (Tank iterator : tankList) {
            iterator.takeTurn();
        }
        visualizer.handleDayFinished();
        for (IBullet iterator : bulletList) {
            iterator.move();
        }
        visualizer.handleDayFinished();
        for (IBullet bulletIterator : bulletList) {
            if (player.getCoordinates().getX() == bulletIterator.getCoordinates().getX() && player.getCoordinates().getY() == bulletIterator.getCoordinates().getY()) {
                bulletIterator.hit(true);
                player.beHit(bulletIterator.getForce());
            }
            for (Tank tankIterator : tankList) {
                if (tankIterator.getCoordinates().equals(bulletIterator.getCoordinates())) {
                    bulletIterator.hit(true);
                    tankIterator.beHit(bulletIterator.getForce());
                }
            }
            for (Obstacle obstacleIterator : obstacleList) {
                if (obstacleIterator.getCoordinates().equals(bulletIterator.getCoordinates())) {
                    bulletIterator.hit(false);
                    obstacleIterator.beHit(bulletIterator.getForce());
                }
            }
        }
        for (Tank tankIterator : tankList) {
            if (tankIterator.isNotActive()) {
                handleTankElimination(tankIterator);
            }
        }
        for (Obstacle obstacleIterator : obstacleList) {
            if (obstacleIterator.isActive()) {
                handleObstacleElimination(obstacleIterator);
            }
        }
        tankList.removeIf(Tank::isNotActive);
        obstacleList.removeIf(e -> !e.isActive());
        bulletList.removeIf(IBullet::isNotActive);
        for (PowerUp powerUpIterator : powerUpList) {
            if (player.getCoordinates().getX() == powerUpIterator.getCoordinates().getX() && player.getCoordinates().getY() == powerUpIterator.getCoordinates().getY()) {
                player.getPowerUp(powerUpIterator);
            }
            for (Tank tankIterator : tankList) {
                if (tankIterator.getCoordinates().equals(powerUpIterator.getCoordinates())) {
                    tankIterator.getPowerUp(powerUpIterator);
                }
            }
        }
        visualizer.handleDayFinished();
        powerUpList.removeIf(e -> !e.isActive());
        timeFromLastTankAddition++;
        timeFromLastObstacleAddition++;
        timeFromLastPowerUpAddition++;
        if (abs(generator.nextInt()) % timeToTankAdd < timeFromLastTankAddition || tankList.size() == 0) {
            generateTank(number);
        }
        if (abs(generator.nextInt()) % timeToPowerUpAdd < timeFromLastPowerUpAddition || powerUpList.size() == 0) {
            generatePowerUp();
        }
        if (abs(generator.nextInt()) % timeToObstacleAdd < timeFromLastObstacleAddition || obstacleList.size() == 0) {
            generateObstacle();
        }
        visualizer.handleDayFinished();
    }

    private boolean generateTank(int number) {
        int x = abs(generator.nextInt()) % mapSize;
        int y = abs(generator.nextInt()) % mapSize;
        if (abs(x - player.getCoordinates().getX()) > 2 && abs(y - player.getCoordinates().getY()) > 2 && fieldIsEmpty(new Vector2d(x, y))) {
            System.out.print(x);
            System.out.println(y);
            Tank tank = new Tank(x, y, player, this, abs(generator.nextInt()) % number, visualizer);
            addTank(tank);
            timeFromLastTankAddition = 0;
            return true;
        }
        return false;
    }

    private boolean generateObstacle() {
        int x = abs(generator.nextInt()) % mapSize;
        int y = abs(generator.nextInt()) % mapSize;
        if (fieldIsEmpty(new Vector2d(x, y))) {
            System.out.print(x);
            System.out.println(y);
            Obstacle obstacle = new Obstacle(this, x, y, visualizer);
            addObstacle(obstacle);
            timeFromLastObstacleAddition = 0;
            return true;
        }
        return false;
    }

    private boolean generatePowerUp() {
        int x = abs(generator.nextInt()) % mapSize;
        int y = abs(generator.nextInt()) % mapSize;
        if (fieldIsEmpty(new Vector2d(x, y))) {
            System.out.print(x);
            System.out.println(y);
            PowerUp powerUp = new PowerUp(abs(generator.nextInt()) % 6 + 1, x, y, numberOfPowerUps, visualizer);
            addPowerUp(powerUp);
            timeFromLastPowerUpAddition = 0;
            return true;
        }
        return false;
    }

    public void addElement(IMapElement element) {
        occupiedFields.put(element.getCoordinates(), element);
    }

    public void addPlayer(Player player) {
        this.player = player;
        addElement(player);
    }

    public void removeElement(IMapElement element) {
        occupiedFields.remove(element.getCoordinates(), element);
    }

    public int getMapSize() {
        return mapSize;
    }

    public boolean fieldIsEmpty(Vector2d vector) {
        for (Obstacle iterator : obstacleList) {
            if (iterator.getCoordinates().getX() == vector.getX() && iterator.getCoordinates().getY() == vector.getY()) {
                return false;
            }
        }
        return !occupiedFields.containsKey(vector) && !player.getCoordinates().equals(vector);
    }

    public void handlePositionChange(Vector2d oldVector, Vector2d newVector, IMapElement element) {
        occupiedFields.remove(oldVector, element);
        occupiedFields.put(newVector, element);
    }

    public void handleTankElimination(Tank tank) {
        removeElement(tank);
    }

    public void handleObstacleElimination(Obstacle obstacle) {
        removeElement(obstacle);
    }

    public void addTank(Tank tank) {
        tankList.add(tank);
        addElement(tank);
    }

    public void addPowerUp(PowerUp powerUp) {
        numberOfPowerUps++;
        powerUpList.add(powerUp);
    }

    public void addObstacle(Obstacle element) {
        obstacleList.add(element);
        addElement(element);
    }

    public void addBullet(IBullet bullet) {
        bulletList.add(bullet);
    }

    public void print() {
        System.out.println(powerUpList.toString());
        System.out.println(player.getCoordinates());
        for (PowerUp p : powerUpList) {
            System.out.println(p.getCoordinates());
        }
        char[][] lineToString = new char[mapSize][2 * mapSize + 2];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < 2 * mapSize + 2; j++) {
                if (j % 2 == 0) {
                    lineToString[i][j] = '|';
                } else {
                    lineToString[i][j] = '_';
                }
            }
        }
        int x;
        int y;
        x = player.getCoordinates().getX();
        y = player.getCoordinates().getY();
        lineToString[mapSize - 1 - y][2 * x + 1] = 'M';
        for (Tank tank : tankList) {
            x = tank.getCoordinates().getX();
            y = tank.getCoordinates().getY();
            lineToString[mapSize - 1 - y][2 * x + 1] = tank.getDirection().toChar();
        }
        for (Obstacle obstacle : obstacleList) {
            x = obstacle.getCoordinates().getX();
            y = obstacle.getCoordinates().getY();
            lineToString[mapSize - 1 - y][2 * x + 1] = 'O';
        }
        for (PowerUp powerUp : powerUpList) {
            x = powerUp.getCoordinates().getX();
            y = powerUp.getCoordinates().getY();
            lineToString[mapSize - 1 - y][2 * x + 1] = 'P';
        }
        for (IBullet bullet : bulletList) {
            x = bullet.getCoordinates().getX();
            y = bullet.getCoordinates().getY();
            lineToString[mapSize - 1 - y][2 * x + 1] = 'B';
        }
        String[] lines = new String[mapSize];
        for (int i = 0; i < mapSize; i++) {
            lines[i] = "";
            for (int j = 0; j < 2 * mapSize + 1; j++) {
                lines[i] = lines[i] + lineToString[i][j];
            }
            lines[i] = lines[i] + "\n";
            System.out.print(lines[i]);
        }
        System.out.println();
    }

    public Vector2d getLowerLeft() {
        return lowerLeft;
    }

    public Vector2d getUpperRight() {
        return upperRight;
    }

    public Player getPlayer() {
        return player;
    }

    public int getNumber() {
        return number;
    }

    public int getPowerUpLifeTime() {
        return powerUpLifeTime;
    }
}