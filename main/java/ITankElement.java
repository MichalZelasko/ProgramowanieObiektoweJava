public interface ITankElement extends IMapElement {
    void fire(int type);

    void move();

    void looseLife(int bulletPower);

    void scorePoint();

    void turnLeft();

    void turnRight();

    boolean checkPowerUpAvailability(int type);

    void beHit(int force);

    void powerUpActualize();
}
