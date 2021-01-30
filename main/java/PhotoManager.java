import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class PhotoManager {
    public static Map<String, Image> imagesCache = new HashMap<>();

    public static ImageView getView(String url, int mapSize) {
        Image photo = imagesCache.get(url);
        ImageView view;
        if (photo == null) {
            photo = new Image(url);
        }
        view = new ImageView(photo);
        view.setFitHeight(500.0 / mapSize);
        view.setFitWidth(500.0 / mapSize);
        return view;
    }
}
