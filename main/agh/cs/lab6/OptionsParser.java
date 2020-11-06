package agh.cs.lab5;

import java.util.List;
import java.util.ArrayList;

public class OptionsParser {
    public static MoveDirection[] parse(String[] args) {
        List<MoveDirection> directions = new ArrayList<>();
        for (String arg : args) {
            switch (arg) {
                case "f":
                case "forward":
                    directions.add(MoveDirection.FORWARD);
                    break;
                case "b":
                case "backward":
                    directions.add(MoveDirection.BACKWARD);
                    break;
                case "l":
                case "left":
                    directions.add(MoveDirection.LEFT);
                    break;
                case "r":
                case "right":
                    directions.add(MoveDirection.RIGHT);
                    break;
                default:
                    throw new IllegalArgumentException(arg + " is illegal move specification.");
            }

        }
        return directions.toArray(new MoveDirection[0]);
    }
}