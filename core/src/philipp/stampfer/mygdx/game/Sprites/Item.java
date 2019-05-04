package philipp.stampfer.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import philipp.stampfer.mygdx.game.RavingSky;

public class Item {
    private ArrayList<Integer> itemXs = new ArrayList<Integer>();
    private ArrayList<Integer> itemYs = new ArrayList<Integer>();
    private ArrayList<Rectangle> itemRectangles = new ArrayList<Rectangle>();

    private Texture itemTexture;

    public int itemCount;

    private Random randomValue;

    public Item(Texture itemTexture) {
        randomValue = new Random();
        this.itemTexture = itemTexture;
    }

    public void makeItem() {//private
        float height = randomValue.nextFloat() * Gdx.graphics.getHeight();
        itemXs.add(Gdx.graphics.getWidth());
        itemYs.add((int) height);
    }

    public void spawnItem() {
        if (itemCount < RavingSky.COIN_SPAWN_FREQUENCY) {
            itemCount++;
        } else {
            itemCount = 0;
            makeItem();
        }
    }

    public void removeItem() {
        itemXs.clear();
        itemYs.clear();
        itemRectangles.clear();
        itemCount = 0;
    }

    public void itemTouched(int touchedItem) {//private
        Gdx.app.log("Item", "collision");
        // score++;
        itemRectangles.remove(touchedItem);
        itemXs.remove(touchedItem);
        itemYs.remove(touchedItem);
    }

    public void itemOutOfScreen(int itemOutOfScreenIndex) {//private
        itemRectangles.remove(itemOutOfScreenIndex);
        itemXs.remove(itemOutOfScreenIndex);
        itemYs.remove(itemOutOfScreenIndex);
    }

    public void drawItemOnScreen(boolean isMainCharacterAwesome, Batch batch) {
        itemRectangles.clear();
        for (int i = 0; i < itemXs.size(); i++) {

            batch.draw(itemTexture, itemXs.get(i), itemYs.get(i));

            if (isMainCharacterAwesome) {
                itemXs.set(i, itemXs.get(i) - RavingSky.BOMB_VELOCITY_IN_PX * RavingSky.AWESOME_VELOCITY_MULTIPLIER_IN_PX);
            } else {
                itemXs.set(i, itemXs.get(i) - RavingSky.BOMB_VELOCITY_IN_PX);
            }

            itemRectangles.add(new Rectangle(
                    itemXs.get(i),
                    itemYs.get(i),
                    itemTexture.getWidth(),
                    itemTexture.getHeight()
            ));
        }
    }

    public void checkForItemColission(Rectangle mainCharacterRectangle, Rectangle offScreenRectangle){
        for (int i = 0; i < itemRectangles.size(); i++) {
            if (Intersector.overlaps(mainCharacterRectangle, itemRectangles.get(i))) {
                itemTouched(i);
                break;
            }
            if (Intersector.overlaps(offScreenRectangle, itemRectangles.get(i))) {
                itemOutOfScreen(i);
                break;
            }
        }
    }
}