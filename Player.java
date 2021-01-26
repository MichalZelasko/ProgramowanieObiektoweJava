import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.min;

public class Player implements ITankElement{
    private Vector2d coordinates;
    private MapDirection direction;
    private int numberOfRemainingLives;
    private int numberOfPoints;
    private boolean isActive;
    private SquareMap map;
    private Scanner scanner;
    private List<PowerUp> powerUpList = new ArrayList<>();

    public Player(SquareMap map){
        coordinates = new Vector2d(map.getMapSize() / 2, map.getMapSize() / 2);
        numberOfRemainingLives = 2;
        numberOfPoints = 0;
        direction = MapDirection.NORTH;
        isActive = true;
        this.map = map;
        scanner = new Scanner(System.in);
    }

    public void turnRight() {
        direction = direction.next();
    }

    public void turnLeft() {
        direction = direction.previous();
    }

    public void looseLife(int bulletPower) {
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
    }

    public void getPowerUp(PowerUp powerUp) {
        switch(powerUp.getType()) {
            case 5 :
                numberOfRemainingLives = 100000000;
                return;
            case 6 :
                numberOfRemainingLives = min(numberOfRemainingLives, 100000000);
                return;
            default :
                powerUpList.add(powerUp);
        }
        powerUp.deactivate();
    }

    @Override
    public void fire(int type) {
        IBullet bullet;
        if(type == 2) {
            bullet = new CommonBullet(this.getCoordinates(), direction, 2, map, this);
        }
        else if(type == 3) {
            bullet = new DoublePowerBullet(this.getCoordinates(), direction, 1, map, this);
        }
        else if(type == 4) {
            bullet = new BouncingBullet(this.getCoordinates(), direction, 1, map, this);
        }
        else {
            bullet = new CommonBullet(this.getCoordinates(), direction, 1, map, this);
        }
        map.addBullet(bullet);
    }

    @Override
    public void move() {
        if(direction.toByte() % 2 == 0) {
            Vector2d vector = coordinates;
            vector = vector.add(direction.toVector());
            vector = vector.lowerLeft(map.getUpperRight()).upperRight(map.getLowerLeft());
            if (map.fieldIsEmpty(vector)) {
                map.handlePositionChange(coordinates, vector, this);
                coordinates = vector;
            }
        }
    }

    public void usePowerUpToMove() {
        String command = scanner.nextLine();
        commandToMoveInterpreter(command);
        command = scanner.nextLine();
        commandToMoveInterpreter(command);
    }

    public void commandToMoveInterpreter(String command) {
        while(command.equals("l") || command.equals("r")) {
            if (command.equals("l")) {
                turnLeft();
            }
            else {
                turnRight();
            }
            command = scanner.nextLine();
        }
        if(command.equals("f")) {
            fire(1);
        }
        if(command.equals("m")) {
            move();
        }
        if(command.equals("u")) {
            presentPowerUpList();
            int number = scanner.nextInt();
            if(number >= 2 && number <= 4 && checkPowerUpAvailability(number)) {
                fire(number);
            }
        }
    }

    public void takeTurn() {
        String command;
        int number;
        command = scanner.nextLine();
        if(command.equals("u")) {
            presentPowerUpList();
            try{
                number = scanner.nextInt();
            } catch(InputMismatchException ex) {
                System.out.println("Wrong command. You have lost your turn.");
                number = 0;
            }
            if(number == 1 && checkPowerUpAvailability(1)) {
                usePowerUpToMove();
            }
            if(number >= 2 && number <= 4 && checkPowerUpAvailability(number)) {
                fire(number);
            }
        }
        else {
            commandToMoveInterpreter(command);
        }
        if(numberOfRemainingLives <= 0) {
            die();
        }
    }

    public boolean checkPowerUpAvailability(int type) {
        for(PowerUp iterator : powerUpList) {
            if(iterator.getType() == type) {
                powerUpList.removeIf(e -> e == iterator);
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

    public void beHit(int force)  {
        numberOfRemainingLives = numberOfRemainingLives - force;
        if(numberOfRemainingLives <= 0) {
            die();
        }
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public void presentPowerUpList() {
        for(PowerUp powerUp : powerUpList) {
            System.out.println(powerUp.getType());
        }
        System.out.println("");
    }
}
