import javafx.stage.Stage;
import javafx.application.Application;

import java.util.Scanner;

public class Main extends Application {
    public static void main(String[] args) {
        System.out.println("Hello World");
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        SquareMap map;
        Scanner scanner = new Scanner(System.in);
        int level = scanner.nextInt();
        switch (level) {
            case 1:
                map = new SquareMap(12, 1, 1, 1, 100, 5, 20, 1, 20);
                break;
            case 2:
                map = new SquareMap(12, 1, 1, 2, 10, 20, 20, 1, 10);
                break;
            case 3:
                map = new SquareMap(12, 2, 2, 2, 20, 20, 20, 1, 20);
                break;
            case 4:
                map = new SquareMap(12, 2, 1, 3, 10, 20, 20, 1, 10);
                break;
            case 5:
                map = new SquareMap(16, 3, 3, 3, 20, 20, 20, 1, 20);
                break;
            case 6:
                map = new SquareMap(16, 3, 2, 4, 10, 20, 20, 1, 10);
                break;
            case 7:
                map = new SquareMap(16, 4, 4, 4, 20, 20, 20, 1, 20);
                break;
            case 8:
                map = new SquareMap(16, 4, 3, 5, 10, 20, 20, 1, 10);
                break;
            case 9:
                map = new SquareMap(20, 5, 5, 5, 20, 20, 20, 1, 10);
                break;
            case 10:
                map = new SquareMap(20, 5, 4, 6, 10, 20, 20, 1, 10);
                break;
            default:
                map = new SquareMap(12, 1, 1, 1, 20, 20, 20, 1, 19);
                break;
        }
        Visualization visualization = new Visualization(stage, map);
        map.setVisualizer(visualization.getMapVisualizer());
        visualization.render();
        map.print();
    }

}
