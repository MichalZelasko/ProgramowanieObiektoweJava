import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Visualization {
    private final MapVisualizer mapVisualizer;
    private final Stage window;
    private SquareMap map;
    private int canMove;

    public Visualization(Stage window, SquareMap map) {
        this.mapVisualizer = new MapVisualizer(map);
        this.map = map;
        this.window = window;
        canMove = 0;
    }

    public void render() {
        window.setTitle("Game: play");
        Parent root = new VBox(mapVisualizer.getMapVisualization());
        Scene scene = new Scene(root, 1000, 800);
        setUpKeyEvent(scene);
        window.setScene(scene);
        window.show();
    }

    private void setUpKeyEvent(Scene scene) {
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case LEFT:
                    map.getPlayer().turnLeft();
                    break;
                case RIGHT:
                    map.getPlayer().turnRight();
                    break;
                case UP:
                    canMove--;
                    map.getPlayer().move();
                    map.oneRound(map.getNumber(), canMove);
                    break;
                case DOWN:
                    canMove--;
                    map.getPlayer().moveBackwards();
                    map.oneRound(map.getNumber(), canMove);
                    break;
                case SPACE:
                    canMove--;
                    map.getPlayer().fire(1);
                    map.oneRound(map.getNumber(), canMove);
                    break;
                case DIGIT2:
                    if (map.getPlayer().checkPowerUpAvailability(2)) {
                        map.getPlayer().fire(2);
                    }
                    map.oneRound(map.getNumber(), canMove);
                    break;
                case DIGIT3:
                    if (map.getPlayer().checkPowerUpAvailability(3)) {
                        map.getPlayer().fire(3);
                    }
                    map.oneRound(map.getNumber(), canMove);
                    break;
                case DIGIT4:
                    if (map.getPlayer().checkPowerUpAvailability(4)) {
                        map.getPlayer().fire(4);
                    }
                    map.oneRound(map.getNumber(), canMove);
                    break;
                case DIGIT1:
                    if (map.getPlayer().checkPowerUpAvailability(1)) {
                        canMove = 2;
                        setUpKeyEventForExtraMove(scene);
                    }
                    setUpKeyEvent(scene);
                    map.oneRound(map.getNumber(), canMove);
                    break;
            }
        });
    }

    private void setUpKeyEventForExtraMove(Scene scene) {
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case LEFT:
                    map.getPlayer().turnLeft();
                    setUpKeyEventForExtraMove(scene);
                    break;
                case RIGHT:
                    map.getPlayer().turnRight();
                    setUpKeyEventForExtraMove(scene);
                    break;
                case UP:
                    map.getPlayer().move();
                    break;
                case DOWN:
                    map.getPlayer().moveBackwards();
                    break;
                case SPACE:
                    map.getPlayer().fire(1);
                    break;
                case DIGIT2:
                    if (map.getPlayer().checkPowerUpAvailability(2)) {
                        map.getPlayer().fire(2);
                    }
                    break;
                case DIGIT3:
                    if (map.getPlayer().checkPowerUpAvailability(3)) {
                        map.getPlayer().fire(3);
                    }
                    break;
                case DIGIT4:
                    if (map.getPlayer().checkPowerUpAvailability(4)) {
                        map.getPlayer().fire(4);
                    }
                    break;
                case DIGIT1:
                    if (map.getPlayer().checkPowerUpAvailability(1)) {
                        setUpKeyEventForExtraMove(scene);
                    }
                    break;
            }
        });
    }

    public MapVisualizer getMapVisualizer() {
        return mapVisualizer;
    }
}
