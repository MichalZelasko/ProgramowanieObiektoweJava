import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Choose the level (between 1 and 10):");
        int level;
        Scanner scanner = new Scanner(System.in);
        try {
            level = scanner.nextInt();
        } catch(InputMismatchException ex) {
            System.out.println("Wrong choice of the level. Default level : 1 chosen");
            level = 1;
        }
        if(level > 10) {
            System.out.println("Wrong choice of the level. Default level : 1 chosen");
        }
        System.out.println("Chosen level: " + level);
        System.out.println("Choose the mapSize (between 10 and 60):");
        int mapSize;
        try {
            mapSize = scanner.nextInt();
        } catch(InputMismatchException ex) {
            System.out.println("Wrong choice of the mapSize. Default level : 10 chosen");
            mapSize = 10;
        }
        System.out.println("Chosen mapSize: " + mapSize);
        SquareMap map;
        int number;
        switch(level){
            case 1 :
                number = 1;
                map = new SquareMap(mapSize,1, 1, 0, 20, 20, 20, number);
                break;
            case 2 :
                number = 2;
                map = new SquareMap(mapSize,1, 1, 1, 10, 10, 10, number);
                break;
            case 3 :
                number = 1;
                map = new SquareMap(mapSize,2, 2, 1, 20, 20, 20, number);
                break;
            case 4 :
                number = 2;
                map = new SquareMap(mapSize,2, 2, 2, 10, 10, 10, number);
                break;
            case 5 :
                number = 2;
                map = new SquareMap(mapSize,4, 4, 2, 20, 20, 20, number);
                break;
            case 6 :
                number = 4;
                map = new SquareMap(mapSize,4, 4, 4, 10, 10, 10, number);
                break;
            case 7 :
                number = 2;
                map = new SquareMap(mapSize,8, 8, 4, 20, 20, 20, number);
                break;
            case 8 :
                number = 4;
                map = new SquareMap(mapSize,8, 8, 8, 10, 10, 10, number);
                break;
            case 9 :
                number = 8;
                map = new SquareMap(mapSize,12, 12, 8, 20, 20, 20, number);
                break;
            case 10 :
                number = 8;
                map = new SquareMap(mapSize,12, 12, 12, 10, 10, 10, number);
                break;
            default :
                number = 1;
                map = new SquareMap(mapSize,1, 1, 1, 10, 10, 10, number);
                break;
        }
        map.print();
        while(!map.isNotActive()) {
            System.out.println("I am active.");
            map.oneRound(number);
            map.print();
            System.out.println(map.getPlayer().getScore());
        }
        System.out.println("Game over.");
    }
}
