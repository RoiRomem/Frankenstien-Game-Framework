package Engine;

import java.awt.Image;
import java.util.WeakHashMap;

public class AssetManager {
    private WeakHashMap<String, Image> assets;

    public AssetManager() {
        this.assets = new WeakHashMap<String, Image>();
    }

    public void loadAsset(String id, Image image) {
        assets.put(id, image);
    }

    public Image getAsset(String id) {
        return assets.get(id);
    }

    public void unloadAsset(String id) {
        assets.remove(id);
    }
}
