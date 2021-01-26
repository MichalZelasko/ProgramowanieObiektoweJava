public interface IBullet extends IMapElement{
    public void move();

    public void hit(boolean tankHit);

    public int getForce();

    public boolean isNotActive();
}
