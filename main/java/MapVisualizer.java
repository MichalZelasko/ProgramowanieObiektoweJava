import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class MapVisualizer implements ISimulationObserver {
    private static final String backgroundTexturePath = "Background.png";
    private static final String bulletETexturePath = "BulletE.png";
    private static final String bulletNTexturePath = "BulletN.png";
    private static final String bulletNETexturePath = "BulletNE.png";
    private static final String bulletNWTexturePath = "BulletNW.png";
    private static final String bulletSTexturePath = "BulletS.png";
    private static final String bulletSETexturePath = "BulletSE.png";
    private static final String bulletSWTexturePath = "BulletSW.png";
    private static final String bulletWTexturePath = "BulletW.png";
    private static final String playerETexturePath = "PlayerE.png";
    private static final String playerNTexturePath = "PlayerN.png";
    private static final String playerNETexturePath = "PlayerNE.png";
    private static final String playerNWTexturePath = "PlayerNW.png";
    private static final String playerSTexturePath = "PlayerS.png";
    private static final String playerSETexturePath = "PlayerSE.png";
    private static final String playerSWTexturePath = "PlayerSW.png";
    private static final String playerWTexturePath = "PlayerW.png";
    private static final String tankETexturePath = "TankE.png";
    private static final String tankNTexturePath = "TankN.png";
    private static final String tankNETexturePath = "TankNE.png";
    private static final String tankNWTexturePath = "TankNW.png";
    private static final String tankSTexturePath = "TankS.png";
    private static final String tankSETexturePath = "TankSE.png";
    private static final String tankSWTexturePath = "TankSW.png";
    private static final String tankWTexturePath = "TankW.png";
    private static final String obstacleTexturePath = "obstacle.png";
    private static final String powerUp1TexturePath = "PowerUp1.png";
    private static final String powerUp2TexturePath = "PowerUp2.png";
    private static final String powerUp3TexturePath = "PowerUp3.png";
    private static final String powerUp4TexturePath = "PowerUp4.png";
    private static final String powerUp5TexturePath = "PowerUp5.png";
    private static final String powerUp6TexturePath = "PowerUp6.png";
    private final VBox trackedTankPanel;
    private final VBox actualStatistics;
    private Player trackedTank;
    private final GridPane mapVisualization;
    private final Map<Vector2d, FieldType> currentFields;
    private final Map<Vector2d, ImageView> currentTextures;
    private final Map<Vector2d, FieldType> fieldsToUpdate;
    private final Map<Vector2d, Tank> tanks;
    private final Map<Vector2d, IBullet> bullets;
    private final Map<Vector2d, PowerUp> powerUps;
    private final Map<Vector2d, Obstacle> obstacles;
    private final SquareMap map;

    public MapVisualizer(SquareMap map) {
        this.trackedTankPanel = new VBox();
        this.actualStatistics = new VBox();
        this.mapVisualization = new GridPane();
        this.currentTextures = new HashMap<>();
        this.fieldsToUpdate = new HashMap<>();
        this.currentFields = new HashMap<>();
        this.tanks = new HashMap<>();
        this.powerUps = new HashMap<>();
        this.bullets = new HashMap<>();
        this.obstacles = new HashMap<>();
        this.map = map;
        initMap(map.getMapSize(), map.getMapSize(), mapVisualization);
    }

    private void initMap(int width, int height, GridPane mapGrid) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2d pos = new Vector2d(x, y);
                ImageView image;
                image = getTexture(FieldType.BACKGROUND);
                currentTextures.put(pos, image);
                mapGrid.add(image, x, y);
            }
        }
        mapGrid.setAlignment(Pos.CENTER);
    }

    public void handleDayFinished() {
        Map<Vector2d, FieldType> copyFieldsToUpdate = new HashMap<>(fieldsToUpdate);
        Platform.runLater(() -> {
            for (Vector2d field : copyFieldsToUpdate.keySet()) {
                ImageView texture = getTexture(copyFieldsToUpdate.get(field));
                updateTrackedTankPanel();
                updatePowerUps();
                updateMapGrid(field, texture);
            }
        });
        fieldsToUpdate.clear();
    }

    public void handleElementChange(IMapElement eventTarget, MapElementAction context, Object oldValue) {
        switch (context) {
            case TANK_APPEARENCE: {
                Tank added = (Tank) eventTarget;
                Vector2d field = added.getCoordinates().toVisualization(map.getMapSize());
                tanks.put(field, added);
                updateFieldIfNeeded(field);
                break;
            }
            case PLAYER_APPEARENCE: {
                trackedTank = (Player) eventTarget;
                Vector2d field = eventTarget.getCoordinates().toVisualization(map.getMapSize());
                updateFieldIfNeeded(field);
                break;
            }
            case POWER_UP_APPEARENCE: {
                PowerUp added = (PowerUp) eventTarget;
                Vector2d field = added.getCoordinates().toVisualization(map.getMapSize());
                powerUps.put(field, added);
                updateFieldIfNeeded(field);
                break;
            }
            case OBSTACLE_APPEARENCE: {
                Obstacle added = (Obstacle) eventTarget;
                Vector2d field = added.getCoordinates().toVisualization(map.getMapSize());
                obstacles.put(field, added);
                updateFieldIfNeeded(field);
                break;
            }
            case BULLET_APPEARENCE: {
                IBullet added = (IBullet) eventTarget;
                Vector2d field = added.getCoordinates().toVisualization(map.getMapSize());
                bullets.put(field, added);
                updateFieldIfNeeded(field);
                break;
            }
            case TANK_POSITION_CHANGE: {
                Tank tank = (Tank) eventTarget;
                Vector2d oldPosition = ((Vector2d) oldValue).toVisualization(map.getMapSize());
                tanks.remove(oldPosition, tank);
                tanks.putIfAbsent(tank.getCoordinates().toVisualization(map.getMapSize()), tank);
                updateFieldIfNeeded(oldPosition);
                updateFieldIfNeeded(tank.getCoordinates().toVisualization(map.getMapSize()));
                break;
            }
            case PLAYER_POSITION_CHANGE: {
                Vector2d oldPosition = ((Vector2d) oldValue).toVisualization(map.getMapSize());
                updateFieldIfNeeded(oldPosition);
                updateFieldIfNeeded(trackedTank.getCoordinates().toVisualization(map.getMapSize()));
                break;
            }
            case BULLET_POSITION_CHANGE: {
                IBullet bullet = (IBullet) eventTarget;
                Vector2d oldPosition = ((Vector2d) oldValue).toVisualization(map.getMapSize());
                bullets.remove(oldPosition, bullet);
                bullets.putIfAbsent(bullet.getCoordinates().toVisualization(map.getMapSize()), bullet);
                updateFieldIfNeeded(oldPosition);
                updateFieldIfNeeded(bullet.getCoordinates().toVisualization(map.getMapSize()));
                break;
            }
            case TANK_DESTROYED: {
                Tank destroyed = (Tank) eventTarget;
                Vector2d field = destroyed.getCoordinates().toVisualization(map.getMapSize());
                tanks.remove(field, destroyed);
                updateFieldIfNeeded(field);
                break;
            }
            case OBSTACLE_DESTROYED: {
                Obstacle destroyed = (Obstacle) eventTarget;
                Vector2d field = destroyed.getCoordinates().toVisualization(map.getMapSize());
                obstacles.remove(field, destroyed);
                updateFieldIfNeeded(field);
                break;
            }
            case BULLET_DESTROYED: {
                IBullet destroyed = (IBullet) eventTarget;
                Vector2d field = destroyed.getCoordinates().toVisualization(map.getMapSize());
                bullets.remove(field, destroyed);
                updateFieldIfNeeded(field);
                break;
            }
            case PLAYER_DESTROYED: {
                updateFieldIfNeeded(trackedTank.getCoordinates().toVisualization(map.getMapSize()));
                break;
            }
            case POWER_UP_CONSUMPTION: {
                PowerUp eaten = (PowerUp) eventTarget;
                Vector2d field = eaten.getCoordinates().toVisualization(map.getMapSize());
                powerUps.remove(field);
                updateFieldIfNeeded(field);
                break;
            }
            case TANK_ROTATION: {
                Tank tank = (Tank) eventTarget;
                Vector2d field = tank.getCoordinates().toVisualization(map.getMapSize());
                updateFieldIfNeeded(field);
                break;
            }
            case PLAYER_ROTATION: {
                Player tank = (Player) eventTarget;
                Vector2d field = tank.getCoordinates().toVisualization(map.getMapSize());
                updateFieldIfNeeded(field);
                break;
            }
        }
    }

    private ImageView getTexture(FieldType fieldType) {
        ImageView view = null;
        if (fieldType == null) {
            fieldType = FieldType.BACKGROUND;
        }
        int mapSize = map.getMapSize();
        switch (fieldType) {
            case TANK_N:
                view = PhotoManager.getView(tankNTexturePath, mapSize);
                break;
            case TANK_NE:
                view = PhotoManager.getView(tankNETexturePath, mapSize);
                break;
            case TANK_E:
                view = PhotoManager.getView(tankETexturePath, mapSize);
                break;
            case TANK_SE:
                view = PhotoManager.getView(tankSETexturePath, mapSize);
                break;
            case TANK_S:
                view = PhotoManager.getView(tankSTexturePath, mapSize);
                break;
            case TANK_SW:
                view = PhotoManager.getView(tankSWTexturePath, mapSize);
                break;
            case TANK_W:
                view = PhotoManager.getView(tankWTexturePath, mapSize);
                break;
            case TANK_NW:
                view = PhotoManager.getView(tankNWTexturePath, mapSize);
                break;
            case PLAYER_N:
                view = PhotoManager.getView(playerNTexturePath, mapSize);
                break;
            case PLAYER_NE:
                view = PhotoManager.getView(playerNETexturePath, mapSize);
                break;
            case PLAYER_E:
                view = PhotoManager.getView(playerETexturePath, mapSize);
                break;
            case PLAYER_SE:
                view = PhotoManager.getView(playerSETexturePath, mapSize);
                break;
            case PLAYER_S:
                view = PhotoManager.getView(playerSTexturePath, mapSize);
                break;
            case PLAYER_SW:
                view = PhotoManager.getView(playerSWTexturePath, mapSize);
                break;
            case PLAYER_W:
                view = PhotoManager.getView(playerWTexturePath, mapSize);
                break;
            case PLAYER_NW:
                view = PhotoManager.getView(playerNWTexturePath, mapSize);
                break;
            case PLAYER_NA:
                view = PhotoManager.getView(playerNTexturePath, mapSize);
                break;
            case BULLET_N:
                view = PhotoManager.getView(bulletNTexturePath, mapSize);
                break;
            case BULLET_NE:
                view = PhotoManager.getView(bulletNETexturePath, mapSize);
                break;
            case BULLET_E:
                view = PhotoManager.getView(bulletETexturePath, mapSize);
                break;
            case BULLET_SE:
                view = PhotoManager.getView(bulletSETexturePath, mapSize);
                break;
            case BULLET_S:
                view = PhotoManager.getView(bulletSTexturePath, mapSize);
                break;
            case BULLET_SW:
                view = PhotoManager.getView(bulletSWTexturePath, mapSize);
                break;
            case BULLET_W:
                view = PhotoManager.getView(bulletWTexturePath, mapSize);
                break;
            case BULLET_NW:
                view = PhotoManager.getView(bulletNWTexturePath, mapSize);
                break;
            case POWER_UP_1:
                view = PhotoManager.getView(powerUp1TexturePath, mapSize);
                break;
            case POWER_UP_2:
                view = PhotoManager.getView(powerUp2TexturePath, mapSize);
                break;
            case POWER_UP_3:
                view = PhotoManager.getView(powerUp3TexturePath, mapSize);
                break;
            case POWER_UP_4:
                view = PhotoManager.getView(powerUp4TexturePath, mapSize);
                break;
            case POWER_UP_5:
                view = PhotoManager.getView(powerUp5TexturePath, mapSize);
                break;
            case POWER_UP_6:
                view = PhotoManager.getView(powerUp6TexturePath, mapSize);
                break;
            case OBSTACLE:
                view = PhotoManager.getView(obstacleTexturePath, mapSize);
                break;
            case BACKGROUND:
                view = PhotoManager.getView(backgroundTexturePath, mapSize);
                break;
        }
        return view;
    }

    private void updateFieldIfNeeded(Vector2d field) {
        System.out.println(field);
        FieldType current = currentFields.get(field);
        FieldType updated = FieldType.BACKGROUND;
        if (obstacles.containsKey(field)) {
            updated = FieldType.OBSTACLE;
        }
        if (powerUps.containsKey(field)) {
            switch (powerUps.get(field).getType()) {
                case 1:
                    updated = FieldType.POWER_UP_1;
                    break;
                case 2:
                    updated = FieldType.POWER_UP_2;
                    break;
                case 3:
                    updated = FieldType.POWER_UP_3;
                    break;
                case 4:
                    updated = FieldType.POWER_UP_4;
                    break;
                case 5:
                    updated = FieldType.POWER_UP_5;
                    break;
                case 6:
                    updated = FieldType.POWER_UP_6;
                    break;
            }
        }
        if (bullets.containsKey(field)) {
            switch (bullets.get(field).getDirection()) {
                case NORTH:
                    updated = FieldType.BULLET_N;
                    break;
                case NORTHEAST:
                    updated = FieldType.BULLET_NE;
                    break;
                case EAST:
                    updated = FieldType.BULLET_E;
                    break;
                case SOUTHEAST:
                    updated = FieldType.BULLET_SE;
                    break;
                case SOUTH:
                    updated = FieldType.BULLET_S;
                    break;
                case SOUTHWEST:
                    updated = FieldType.BULLET_SW;
                    break;
                case WEST:
                    updated = FieldType.BULLET_W;
                    break;
                case NORTHWEST:
                    updated = FieldType.BULLET_NW;
                    break;
            }
        }
        if (tanks.containsKey(field)) {
            switch (tanks.get(field).getDirection()) {
                case NORTH:
                    updated = FieldType.TANK_N;
                    break;
                case NORTHEAST:
                    updated = FieldType.TANK_NE;
                    break;
                case EAST:
                    updated = FieldType.TANK_E;
                    break;
                case SOUTHEAST:
                    updated = FieldType.TANK_SE;
                    break;
                case SOUTH:
                    updated = FieldType.TANK_S;
                    break;
                case SOUTHWEST:
                    updated = FieldType.TANK_SW;
                    break;
                case WEST:
                    updated = FieldType.TANK_W;
                    break;
                case NORTHWEST:
                    updated = FieldType.TANK_NW;
                    break;
            }
        }
        if (trackedTank.getCoordinates().toVisualization(map.getMapSize()).getX() == field.getX() && trackedTank.getCoordinates().toVisualization(map.getMapSize()).getY() == field.getY()) {
            switch (trackedTank.getDirection()) {
                case NORTH:
                    updated = FieldType.PLAYER_N;
                    break;
                case NORTHEAST:
                    updated = FieldType.PLAYER_NE;
                    break;
                case EAST:
                    updated = FieldType.PLAYER_E;
                    break;
                case SOUTHEAST:
                    updated = FieldType.PLAYER_SE;
                    break;
                case SOUTH:
                    updated = FieldType.PLAYER_S;
                    break;
                case SOUTHWEST:
                    updated = FieldType.PLAYER_SW;
                    break;
                case WEST:
                    updated = FieldType.PLAYER_W;
                    break;
                case NORTHWEST:
                    updated = FieldType.PLAYER_NW;
                    break;
            }
        }
        if (current == null || current != updated) {
            System.out.println(updated.toString());
            fieldsToUpdate.put(field, updated);
            currentFields.put(field, updated);
        }
    }

    private void updateMapGrid(Vector2d field, ImageView newTexture) {
        mapVisualization.getChildren().remove(currentTextures.get(field));
        mapVisualization.add(newTexture, field.getX(), field.getY());
        currentTextures.put(field, newTexture);
    }

    private void updateTrackedTankPanel() {
        trackedTankPanel.getChildren().clear();
        trackedTankPanel.getChildren().add(new Label("Player score: "));
        trackedTankPanel.getChildren().add(new Label("score: " + trackedTank.getScore()));
        trackedTankPanel.getChildren().add(new Label("                                    "));
        if (trackedTank.isNotActive()) {
            trackedTankPanel.getChildren().add(new Label("Game over"));
            return;
        }
        if (trackedTank.getImmortalityTime() > 0) {
            trackedTankPanel.getChildren().add(new Label("I'm immortal for next " + trackedTank.getImmortalityTime() + " rounds."));
            return;
        }
        trackedTankPanel.getChildren().add(new Label("Number of available lives: " + trackedTank.getNumberOfRemainingLives()));
    }

    private void updatePowerUps() {
        actualStatistics.getChildren().clear();
        if (trackedTank.isNotActive()) {
            actualStatistics.getChildren().add(new Label("Game over"));
            return;
        }
        actualStatistics.getChildren().add(new Label("Actual available powerUps: "));
        for (PowerUp iterator : trackedTank.getPowerUpList()) {
            if (iterator.isAvailable()) {
                actualStatistics.getChildren().add(new Label("Available powerUp of type: "
                        + iterator.getType()
                        + ": "
                        + iterator.getPowerUpType()));
            }
        }
    }

    public Parent getMapVisualization() {
        return new HBox(mapVisualization, trackedTankPanel, actualStatistics);
    }

    public SquareMap getMap() {
        return map;
    }
}
