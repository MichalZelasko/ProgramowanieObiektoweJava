package agh.cs.lab5;

import org.junit.Assert;
import org.junit.Test;

public class OptionParserTest {
    @Test
    public void TestCase1() {
        String[] test = {"f"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("N", a.toString());
    }

    @Test
    public void TestCase2() {
        String[] test = {"b"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("N", a.toString());
    }

    @Test
    public void TestCase3() {
        String[] test = {"l"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("W", a.toString());
    }

    @Test
    public void TestCase4() {
        String[] test = {"r"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("E", a.toString());
    }

    @Test
    public void TestCase5() {
        String[] test = {"forward"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("N", a.toString());
    }

    @Test
    public void TestCase6() {
        String[] test = {"backward"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("N", a.toString());
    }

    @Test
    public void TestCase7() {
        String[] test = {"left"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("W", a.toString());
    }

    @Test
    public void TestCase8() {
        String[] test = {"right"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("E", a.toString());
    }

    @Test
    public void TestCase9() {
        String[] test = {" "};
        try {
            MoveDirection[] direction = OptionsParser.parse(test);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("  is illegal move specification.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase10() {
        String[] test = {""};
        try {
            MoveDirection[] direction = OptionsParser.parse(test);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals(" is illegal move specification.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase11() {
        String[] test = {"trash"};
        try {
            MoveDirection[] direction = OptionsParser.parse(test);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("trash is illegal move specification.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase12() {
        String[] test = {"r", "r", "r", "right", "left", "l", "l", "l"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("N", a.toString());
    }

    @Test
    public void TestCase13() {
        String[] test = {"f", "right", "f", "r", "f", "r", "forward", "r"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("N", a.toString());
    }

    @Test
    public void TestCase14() {
        String[] test = {"left", "f", "f", "forward"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("W", a.toString());
    }

    @Test
    public void TestCase15() {
        String[] test = {"b", "backward", "b", "r"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("E", a.toString());
    }

    @Test
    public void TestCase16() {
        String[] test = {"r", "f", "f", "f"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("E", a.toString());
    }

    @Test
    public void TestCase17() {
        String[] test = {"f", "f", "f", "l"};
        MoveDirection[] direction = OptionsParser.parse(test);
        IWorldMap m=new RectangularMap(5,5);
        Animal a = new Animal(m);
        for (MoveDirection moveDirection : direction) {
            a.move(moveDirection);
        }
        Assert.assertEquals("W", a.toString());
    }

    @Test
    public void TestCase18() {
        String[] test = {"f", "f", "f", "l", "forward", "forward", "forward", "trash", "1", "right"};
        try {
            MoveDirection[] direction = OptionsParser.parse(test);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("trash is illegal move specification.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase19() {
        String[] test = {"fjkdhf", "fdfd", "ffsd", "lfds", "forwards", "forwards", "forwards", "trash", "1fdfs", "right"};
        try {
            MoveDirection[] direction = OptionsParser.parse(test);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("fjkdhf is illegal move specification.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase20() {
        String[] test = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        try {
            MoveDirection[] direction = OptionsParser.parse(test);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("1 is illegal move specification.", ex.getLocalizedMessage());
        }
    }

    @Test
    public void TestCase21() {
        String[] test = {"r", "right", "l", "left", "f", "forward", "b", "backward", " ", "trash"};
        try {
            MoveDirection[] direction = OptionsParser.parse(test);
        }catch(IllegalArgumentException ex){
            Assert.assertEquals("  is illegal move specification.", ex.getLocalizedMessage());
        }
    }
}