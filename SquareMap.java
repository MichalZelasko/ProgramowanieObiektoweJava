import java.util.*;

import static java.lang.StrictMath.abs;

public class SquareMap {
    private int mapSize;
    private List<Tank> tankList = new ArrayList<>();
    private List<Obstacle> obstacleList = new ArrayList<>();
    private List<PowerUp> powerUpList = new ArrayList<>();
    private List<IMapElement> elementsList = new ArrayList<>();
    private List<IBullet> bulletList = new ArrayList<>();
    private HashMap<Vector2d, IMapElement> occupiedFields;
    private Player player;
    private int timeToTankAdd;
    private int timeToPowerUpAdd;
    private int timeToObstacleAdd;
    private int timeFromLastTankAddition = 0;
    private int timeFromLastPowerUpAddition = 0;
    private int timeFromLastObstacleAddition = 0;
    private int numberOfPowerUps = 0;
    private Random generator = new Random();
    private Vector2d lowerLeft = new Vector2d(0 , 0);
    private Vector2d upperRight;

    public SquareMap(int mapSize, int numberOfEnemies, int numberOfPowerUps, int numberOfObstacles, int timeToTankAdd, int timeToPowerUpAdd, int timeToObstacleAdd, int number){
        this.mapSize = mapSize;
        upperRight = new Vector2d(mapSize - 1, mapSize - 1);
        this.timeToTankAdd = timeToTankAdd;
        this.timeToPowerUpAdd = timeToPowerUpAdd;
        this.timeToObstacleAdd = timeToObstacleAdd;
        this.player = new Player(this);
        occupiedFields = new HashMap<>();
        addPlayer(player);
        print();
        prepareMap(numberOfEnemies, numberOfPowerUps, numberOfObstacles, number);
    }

    public void prepareMap(int numberOfEnemies, int numberOfPowerUps, int numberOfObstacles, int number){
        int iterator = 0;
        while(iterator < numberOfEnemies) {
            if(generateTank(number)) {
                iterator++;
            }
        }
        iterator = 0;
        while(iterator < numberOfPowerUps) {
            if(generatePowerUp()) {
                iterator++;
            }
        }
        iterator = 0;
        while(iterator < numberOfObstacles) {
            if(generateObstacle()) {
                iterator++;
            }
        }
    }

    public void oneRound(int number) {
        player.takeTurn();
        for(Tank iterator : tankList) {
            iterator.takeTurn();
        }
        for(IBullet iterator : bulletList) {
            iterator.move();
        }
        for(IBullet bulletIterator : bulletList) {
            if(player.getCoordinates().getX() == bulletIterator.getCoordinates().getX() &&player.getCoordinates().getY() == bulletIterator.getCoordinates().getY()) {
                bulletIterator.hit(true);
                player.beHit(bulletIterator.getForce());
            }
            for(Tank tankIterator : tankList) {
                if(tankIterator.getCoordinates().equals(bulletIterator.getCoordinates())) {
                    bulletIterator.hit(true);
                    tankIterator.beHit(bulletIterator.getForce());
                }
            }
            for(Obstacle obstacleIterator : obstacleList) {
                if(obstacleIterator.getCoordinates().equals(bulletIterator.getCoordinates())) {
                    bulletIterator.hit(false);
                    obstacleIterator.beHit(bulletIterator.getForce());
                }
            }
        }
        for(Tank tankIterator : tankList) {
            if(tankIterator.isNotActive()) {
                handleTankElimination(tankIterator);
            }
        }
        for(Obstacle obstacleIterator : obstacleList) {
            if(obstacleIterator.isActive()) {
                handleObstacleElimination(obstacleIterator);
            }
        }
        tankList.removeIf(Tank::isNotActive);
        obstacleList.removeIf(e -> !e.isActive());
        bulletList.removeIf(IBullet::isNotActive);
        for(PowerUp powerUpIterator : powerUpList) {
            if(player.getCoordinates().equals(powerUpIterator.getCoordinates())) {
                player.getPowerUp(powerUpIterator);
            }
            for(Tank tankIterator : tankList) {
                if(tankIterator.getCoordinates().equals(powerUpIterator.getCoordinates())) {
                    tankIterator.getPowerUp(powerUpIterator);
                }
            }
        }
        powerUpList.removeIf(e -> !e.isActive());
        if(abs(generator.nextInt()) % timeToTankAdd < timeFromLastTankAddition || tankList.size() == 0) {
            generateTank(number);
        }
        if(abs(generator.nextInt()) % timeToPowerUpAdd < timeFromLastPowerUpAddition|| tankList.size() == 0) {
            generatePowerUp();
        }
        if(abs(generator.nextInt()) % timeToObstacleAdd < timeFromLastObstacleAddition || tankList.size() == 0) {
            generateObstacle();
        }

    }

    private boolean generateTank(int number) {
        int x = abs(generator.nextInt()) % mapSize;
        int y = abs(generator.nextInt()) % mapSize;
        if(abs(x - player.getCoordinates().getX()) > 2 && abs(y - player.getCoordinates().getY()) > 2 && fieldIsEmpty(new Vector2d(x, y))) {
            System.out.print(x);
            System.out.println(y);
            Tank tank = new Tank(x, y, player, this, abs(generator.nextInt()) % number);
            addTank(tank);
            timeFromLastTankAddition = 0;
            return true;
        }
        return false;
    }

    private boolean generateObstacle() {
        int x = abs(generator.nextInt()) % mapSize;
        int y = abs(generator.nextInt()) % mapSize;
        if(fieldIsEmpty(new Vector2d(x, y))) {
            System.out.print(x);
            System.out.println(y);
            Obstacle obstacle= new Obstacle(this, x, y);
            addObstacle(obstacle);
            timeFromLastObstacleAddition = 0;
            return true;
        }
        return false;
    }

    private boolean generatePowerUp() {
        int x = abs(generator.nextInt()) % mapSize;
        int y = abs(generator.nextInt()) % mapSize;
        if(fieldIsEmpty(new Vector2d(x, y))) {
            System.out.print(x);
            System.out.println(y);
            PowerUp powerUp = new PowerUp(abs(generator.nextInt()) % 6 + 1, x, y, numberOfPowerUps);
            addPowerUp(powerUp);
            timeFromLastPowerUpAddition = 0;
            return true;
        }
        return false;
    }

    public void addElement(IMapElement element) {
        elementsList.add(element);
        occupiedFields.put(element.getCoordinates(), element);
    }

    public void addPlayer(Player player){
        this.player = player;
        addElement(player);
    }

    public void removeElement(IMapElement element) {
        elementsList.removeIf(e -> e == element);
        occupiedFields.remove(element.getCoordinates(), element);
    }

    public int getMapSize() {
        return mapSize;
    }

    public boolean fieldIsEmpty(Vector2d vector) {
        return !occupiedFields.containsKey(vector);
    }

    public void handlePositionChange(Vector2d oldVector, Vector2d newVector, IMapElement element) {
        occupiedFields.remove(oldVector, element);
        occupiedFields.put(newVector, element);
    }

    public void handleTankElimination(Tank tank) {
        //tankList.removeIf(e -> e.equals(tank));
        removeElement(tank);
    }

    public void handleObstacleElimination(Obstacle obstacle) {
        //obstacleList.removeIf(e -> e == obstacle);
        removeElement(obstacle);
    }

    public void handlePowerUpElimination(PowerUp powerUp) {
        powerUpList.removeIf(e -> e == powerUp);
        removeElement(powerUp);
    }

    public void addTank(Tank tank) {
        tankList.add(tank);
        addElement(tank);
    }

    public void addPowerUp(PowerUp powerUp) {
        numberOfPowerUps++;
        powerUpList.add(powerUp);
        elementsList.add(powerUp);
    }

    public void addObstacle(Obstacle element) {
        obstacleList.add(element);
        addElement(element);
    }

    public void addBullet(IBullet bullet) {
        bulletList.add(bullet);
    }

    public boolean isNotActive() {
        return player.isNotActive();
    }

    public void print(){
        System.out.println(powerUpList.toString());
        System.out.println(player.getCoordinates());
        for(PowerUp p: powerUpList) {
            System.out.println(p.getCoordinates());
        }
        char[][] lineToString = new char[mapSize][2 * mapSize + 2];
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < 2 * mapSize + 2; j++){
                if(j % 2 == 0){
                    lineToString[i][j] = '|';
                }
                else {
                    lineToString[i][j] = '_';
                }
            }
        }
        int x;
        int y;
        x = player.getCoordinates().getX();
        y = player.getCoordinates().getY();
        lineToString[mapSize - 1 - y][2 * x + 1] = 'M';
        for(Tank tank : tankList) {
            x = tank.getCoordinates().getX();
            y = tank.getCoordinates().getY();
            lineToString[mapSize - 1 - y][2 * x + 1] = tank.getDirection().toChar();
        }
        for(Obstacle obstacle : obstacleList) {
            x = obstacle.getCoordinates().getX();
            y = obstacle.getCoordinates().getY();
            lineToString[mapSize - 1 - y][2 * x + 1] = 'O';
        }
        for(PowerUp powerUp : powerUpList) {
            x = powerUp.getCoordinates().getX();
            y = powerUp.getCoordinates().getY();
            lineToString[mapSize - 1 - y][2 * x + 1] = 'P';
        }
        for(IBullet bullet : bulletList) {
            x = bullet.getCoordinates().getX();
            y = bullet.getCoordinates().getY();
            lineToString[mapSize - 1 - y][2 * x + 1] = 'B';
        }
        String[] lines = new String[mapSize];
        for(int i = 0; i < mapSize; i++) {
            lines[i] = "";
            for(int j = 0; j < 2 * mapSize + 1; j++) {
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
}