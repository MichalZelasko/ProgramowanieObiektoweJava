package agh.cs.lab5;

import org.junit.Assert;
import org.junit.Test;

public class RectangularMapTest {

    @Test
    public void TestCase1() { //one animal placed on the map
        RectangularMap map = new RectangularMap(4, 4);
        new Animal(map);
        String result = " y\\x  0 1 2 3" + System.lineSeparator() +
                "  4: ---------"  + System.lineSeparator() +
                "  3: | | | | |" + System.lineSeparator() +
                "  2: | | | | |" + System.lineSeparator() +
                "  1: | | | | |" + System.lineSeparator() +
                "  0: |N| | | |" + System.lineSeparator() +
                " -1: ---------" + System.lineSeparator();
        Assert.assertEquals(result, map.toString());
    }

    @Test
    public void TestCase2() { //one animal placed and moving on the map
        RectangularMap map = new RectangularMap(4, 4);
        new Animal(map);
        String[] args = {"f", "r", "forward", "right", "f", "r", "forward", "right"};
        MoveDirection[] directions = OptionsParser.parse(args);
        map.run(directions);
        String result = " y\\x  0 1 2 3" + System.lineSeparator() +
                "  4: ---------" + System.lineSeparator() +
                "  3: | | | | |" + System.lineSeparator() +
                "  2: | | | | |" + System.lineSeparator() +
                "  1: | | | | |" + System.lineSeparator() +
                "  0: |N| | | |" + System.lineSeparator() +
                " -1: ---------" + System.lineSeparator();
        Assert.assertEquals(result, map.toString());
    }

    @Test
    public void TestCase3() { //one animal placed and moving on the map, it tries to exceed map limits
        RectangularMap map = new RectangularMap(4, 4);
        new Animal(map);
        String[] args = {"f", "r", "forward", "right", "f", "r", "forward", "f", "f", "right", "left"};
        MoveDirection[] directions = OptionsParser.parse(args);
        map.run(directions);
        String result = " y\\x  0 1 2 3" + System.lineSeparator() +
                "  4: ---------" + System.lineSeparator() +
                "  3: | | | | |" + System.lineSeparator() +
                "  2: | | | | |" + System.lineSeparator() +
                "  1: | | | | |" + System.lineSeparator() +
                "  0: |W| | | |" + System.lineSeparator() +
                " -1: ---------" + System.lineSeparator();
        Assert.assertEquals(result, map.toString());
    }

    @Test
    public void TestCase4() { //two animals are tried to be placed on the map in the same point, only the first one is placed successfully
        try {
            RectangularMap map = new RectangularMap(4, 4);
            new Animal(map);
            new Animal(map);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("There is an animal at this place.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase5() { //two animals are tried to be placed on the map in different places, both are placed successfully. Test of Animal(map, position)
        RectangularMap map = new RectangularMap(4, 4);
        new Animal(map);
        new Animal(map, new Vector2d(2, 3));
        String result = " y\\x  0 1 2 3" + System.lineSeparator() +
                "  4: ---------" + System.lineSeparator() +
                "  3: | | |N| |" + System.lineSeparator() +
                "  2: | | | | |" + System.lineSeparator() +
                "  1: | | | | |" + System.lineSeparator() +
                "  0: |N| | | |" + System.lineSeparator() +
                " -1: ---------" + System.lineSeparator();
        Assert.assertEquals(result, map.toString());
    }

    @Test
    public void TestCase6() { //two animals are tried to be placed on the map, both are running without collisions
        try {
            RectangularMap map = new RectangularMap(4, 4);
            new Animal(map);
            new Animal(map, new Vector2d(2, 4));
            String[] args = {"f", "r", "forward", "right", "f", "r", "forward", "f", "f", "right", "left"};
            MoveDirection[] directions = OptionsParser.parse(args);
            map.run(directions);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("Animal out of the map.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase7() { //two animals are tried to be placed on the map, both are running without collisions
        String[] args = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(args);
        IWorldMap map = new RectangularMap(10, 5);
        new Animal(map, new Vector2d(2, 2));
        new Animal(map, new Vector2d(3, 4));
        map.run(directions);
        String result = " y\\x  0 1 2 3 4 5 6 7 8 9" +  System.lineSeparator() +
                "  5: ---------------------" +  System.lineSeparator() +
                "  4: | | | |N| | | | | | |" +  System.lineSeparator() +
                "  3: | | | | | | | | | | |" +  System.lineSeparator() +
                "  2: | | | | | | | | | | |" +  System.lineSeparator() +
                "  1: | | | | | | | | | | |" +  System.lineSeparator() +
                "  0: | | |S| | | | | | | |" +  System.lineSeparator() +
                " -1: ---------------------" + System.lineSeparator();
        Assert.assertEquals(result, map.toString());
    }

    @Test
    public void TestCase8() { //three animals, two placed in the same point running on the map, collision case
        try {
            String[] args = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
            MoveDirection[] directions = new OptionsParser().parse(args);
            IWorldMap map = new RectangularMap(10, 5);
            new Animal(map, new Vector2d(2, 2));
            new Animal(map, new Vector2d(2, 2));
            new Animal(map, new Vector2d(3, 4));
            map.run(directions);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("There is an animal at this place.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase9() { //more animals running on the map
        String[] args = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(args);
        IWorldMap map = new RectangularMap(10, 5);
        new Animal(map);
        new Animal(map, new Vector2d(2, 1));
        new Animal(map, new Vector2d(2, 2));
        new Animal(map, new Vector2d(3, 4));
        map.run(directions);
        String result = " y\\x  0 1 2 3 4 5 6 7 8 9" + System.lineSeparator() +
                "  5: ---------------------" + System.lineSeparator() +
                "  4: |N| | |N| | | | | | |" + System.lineSeparator() +
                "  3: | | | | | | | | | | |" + System.lineSeparator() +
                "  2: | | |S| | | | | | | |" + System.lineSeparator() +
                "  1: | | |N| | | | | | | |" + System.lineSeparator() +
                "  0: | | | | | | | | | | |" + System.lineSeparator() +
                " -1: ---------------------" + System.lineSeparator();
        Assert.assertEquals(result, map.toString());
    }

    @Test
    public void TestCase10() { //new animals placed while running, errors in args
        String[] args = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(args);
        IWorldMap map = new RectangularMap(10, 5);
        new Animal(map);
        new Animal(map, new Vector2d(2, 1));
        new Animal(map, new Vector2d(2, 2));
        new Animal(map, new Vector2d(3, 4));
        map.run(directions);
        try {
            new Animal(map, new Vector2d(2, 1));
            String[] args1 = {"f", "b", "r", "l", "f", "f", "r", "trash", "f", "f", "error", "f", "1", "f", "l", "left"};
            MoveDirection[] directions1 = new OptionsParser().parse(args);
            map.run(directions1);
            new Animal(map);
            map.run(directions1);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("There is an animal at this place.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase11() { //empty map
        IWorldMap map = new RectangularMap(10, 5);
        String result = " y\\x  0 1 2 3 4 5 6 7 8 9" + System.lineSeparator() +
                "  5: ---------------------" + System.lineSeparator() +
                "  4: | | | | | | | | | | |" + System.lineSeparator() +
                "  3: | | | | | | | | | | |" + System.lineSeparator() +
                "  2: | | | | | | | | | | |" + System.lineSeparator() +
                "  1: | | | | | | | | | | |" + System.lineSeparator() +
                "  0: | | | | | | | | | | |" + System.lineSeparator() +
                " -1: ---------------------" + System.lineSeparator();
        Assert.assertEquals(result, map.toString());
    }

    @Test
    public void TestCase12() { //empty map, running
        String[] args = {"f", "b", "r", "l", "f", "f", "r", "r", "f", "f", "f", "f", "f", "f", "f", "f"};
        MoveDirection[] directions = new OptionsParser().parse(args);
        IWorldMap map = new RectangularMap(10, 5);
        map.run(directions);
        String result = " y\\x  0 1 2 3 4 5 6 7 8 9" + System.lineSeparator() +
                "  5: ---------------------" + System.lineSeparator() +
                "  4: | | | | | | | | | | |" + System.lineSeparator() +
                "  3: | | | | | | | | | | |" + System.lineSeparator() +
                "  2: | | | | | | | | | | |" + System.lineSeparator() +
                "  1: | | | | | | | | | | |" + System.lineSeparator() +
                "  0: | | | | | | | | | | |" + System.lineSeparator() +
                " -1: ---------------------" + System.lineSeparator();
        Assert.assertEquals(result, map.toString());
    }

}
