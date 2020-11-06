package agh.cs.lab5;

import org.junit.Assert;
import org.junit.Test;

public class Vector2dTest {
    @Test
    public void equalsTestCase1() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(1, 2);
        Assert.assertTrue(vector1.equals(vector2));
    }

    @Test
    public void equalsTestCase2() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(2, 4);
        Assert.assertFalse(vector1.equals(vector2));
    }

    @Test
    public void toStringTestCase() {
        Vector2d vector1 = new Vector2d(1, 2);
        Assert.assertEquals(vector1.toString(), "(1,2)");
    }

    @Test
    public void precedesTestCase1() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(2, 4);
        Assert.assertTrue(vector1.precedes(vector2));
    }

    @Test
    public void precedesTestCase2() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(0, 4);
        Assert.assertFalse(vector1.precedes(vector2));
    }

    @Test
    public void precedesTestCase3() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(2, 0);
        Assert.assertFalse(vector1.precedes(vector2));
    }

    @Test
    public void precedesTestCase4() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(0, 0);
        Assert.assertFalse(vector1.precedes(vector2));
    }

    @Test
    public void precedesTestCase5() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(1, 2);
        Assert.assertTrue(vector1.precedes(vector2));
    }

    @Test
    public void followsTestCase1() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(2, 4);
        Assert.assertFalse(vector1.follows(vector2));
    }

    @Test
    public void followsTestCase2() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(0, 4);
        Assert.assertFalse(vector1.follows(vector2));
    }

    @Test
    public void followsTestCase3() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(2, 0);
        Assert.assertFalse(vector1.follows(vector2));
    }

    @Test
    public void followsTestCase4() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(0, 0);
        Assert.assertTrue(vector1.follows(vector2));
    }

    @Test
    public void followsTestCase5() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(1, 2);
        Assert.assertTrue(vector1.follows(vector2));
    }

    @Test
    public void upperRightTestCase1() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(2, 4);
        Assert.assertEquals(vector1.upperRight(vector2), new Vector2d(2, 4));
    }

    @Test
    public void upperRightTestCase2() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(0, 4);
        Assert.assertEquals(vector1.upperRight(vector2), new Vector2d(1, 4));
    }

    @Test
    public void lowerLeftTestCase1() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(2, 4);
        Assert.assertEquals(vector1.lowerLeft(vector2), new Vector2d(1, 2));
    }

    @Test
    public void lowerLeftTestCase2() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(0, 4);
        Assert.assertEquals(vector1.lowerLeft(vector2), new Vector2d(0, 2));
    }

    @Test
    public void addTestCase() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(0, 4);
        Assert.assertEquals(vector1.add(vector2), new Vector2d(1, 6));
    }

    @Test
    public void subtractTestCase() {
        Vector2d vector1 = new Vector2d(1, 2);
        Vector2d vector2 = new Vector2d(0, 4);
        Assert.assertEquals(vector1.subtract(vector2), new Vector2d(1, -2));
    }

    @Test
    public void oppositeTestCase() {
        Vector2d vector1 = new Vector2d(1, 2);
        Assert.assertEquals(vector1.opposite(), new Vector2d(-1, -2));
    }
}