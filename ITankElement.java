public interface ITankElement extends IMapElement {
    public void fire(int type);

    public void move();

    public void looseLife(int bulletPower);

    public void scorePoint();

    public void turnLeft();

    public void turnRight();

    public boolean checkPowerUpAvailability(int type);

    public void commandToMoveInterpreter(String command);

    public void beHit(int force);
}
