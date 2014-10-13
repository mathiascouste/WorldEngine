package view.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageManager {
    private static Map<String, ImageManager> imageManagers = new HashMap<String, ImageManager>();

    public static ImageManager createImageManager(String ident) {
        ImageManager iM = new ImageManager(ident);
        imageManagers.put(ident, iM);
        return iM;
    }

    public static ImageManager get(String ident) {
        return imageManagers.get(ident);
    }

    public static Image getImage(String ident, String img) {
        ImageManager iM = get(ident);
        if (iM == null) {
            return null;
        } else {
            return iM.getImage(img);
        }
    }

    private Map<String, String> imagesPath;
    private Map<String, Image> images;

    private ImageManager(String ident) {
        this.imagesPath = new HashMap<String, String>();
        this.images = new HashMap<String, Image>();
    }

    public void addImage(String ident, String path) {
        this.imagesPath.put(ident, path);
    }

    public Image getImage(String ident) {
        return this.images.get(ident);
    }

    public void loadAll() {
        Iterator<?> it = this.imagesPath.entrySet().iterator();
        while (it.hasNext()) {
            @SuppressWarnings("unchecked")
            Map.Entry<String, String> pairs = (Map.Entry<String, String>) it
                    .next();
            String ident = pairs.getKey();
            String path = pairs.getValue();
            this.images.put(ident, loadImage(path));
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    private Image loadImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            WELogger.log(e.getMessage());
        }
        return img;
    }
}
