public interface IBullet extends IMapElement {
    void move();

    void hit(boolean tankHit);

    int getForce();

    boolean isNotActive();

    MapDirection getDirection();
}
