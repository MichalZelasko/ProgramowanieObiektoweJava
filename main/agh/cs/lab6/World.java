package agh.cs.lab5;

public class World {
    public static void main(String[] args) {
        try {
            MoveDirection[] directions = OptionsParser.parse(args);
            GrassField map = new GrassField(10);
            new Animal(map);
            new Animal(map, new Vector2d(2, 3));
            new Animal(map);
            System.out.print(map.toString());
            map.run(directions);
            System.out.print(map.toString());
        }catch(IllegalArgumentException ex){
            System.out.println(ex.getLocalizedMessage());
            System.exit(0);
        }
    }
}
