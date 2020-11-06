package agh.cs.lab5;

import org.junit.Assert;
import org.junit.Test;

public class MapDirectionTest {
    @Test
    public void nextTestCase1() {
        MapDirection direction = MapDirection.NORTH;
        Assert.assertSame(direction.next(), MapDirection.EAST);
    }

    @Test
    public void nextTestCase2() {
        MapDirection direction = MapDirection.EAST;
        Assert.assertSame(direction.next(), MapDirection.SOUTH);
    }

    @Test
    public void nextTestCase3() {
        MapDirection direction = MapDirection.WEST;
        Assert.assertSame(direction.next(), MapDirection.NORTH);
    }

    @Test
    public void nextTestCase4() {
        MapDirection direction = MapDirection.SOUTH;
        Assert.assertSame(direction.next(), MapDirection.WEST);
    }

    @Test
    public void previousTestCase1() {
        MapDirection direction = MapDirection.NORTH;
        Assert.assertSame(direction.previous(), MapDirection.WEST);
    }

    @Test
    public void previousTestCase2() {
        MapDirection direction = MapDirection.EAST;
        Assert.assertSame(direction.previous(), MapDirection.NORTH);
    }

    @Test
    public void previousTestCase3() {
        MapDirection direction = MapDirection.WEST;
        Assert.assertSame(direction.previous(), MapDirection.SOUTH);
    }

    @Test
    public void previousTestCase4() {
        MapDirection direction = MapDirection.SOUTH;
        Assert.assertSame(direction.previous(), MapDirection.EAST);
    }
}
