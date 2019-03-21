package philipp.stampfer.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class RavingSky extends ApplicationAdapter {
    public static final int PLAYER_JUMP_HEIGHT = 125;
    public static final int COIN_SPAWN_FREQUENCY = 100;
    public static final int BOMB_SPAWN_FREQUENCY = 250;
    public static final int COIN_VELOCITY_IN_PX = 4;
    public static final int BOMB_VELOCITY_IN_PX = 6;

    SpriteBatch batch;

    Texture background;
    Texture[] mainCharacter;
    Texture dizzyMainCharacter;

    Texture coin;
    Texture bomb;

    BitmapFont textToShow;

    int mainCharacterState;
    int pause = 0;
    int mainCharacterY;

    int bombCount;
    int coinCount;

    int gameState = 0;
    int score = 0;

    float gravity = 7.5f;
    float velocity = 0;

    ArrayList<Integer> coinXs = new ArrayList<Integer>();
    ArrayList<Integer> coinYs = new ArrayList<Integer>();
    ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();

    ArrayList<Integer> bombXs = new ArrayList<Integer>();
    ArrayList<Integer> bombYs = new ArrayList<Integer>();
    ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();

    Random randomValue;

    Rectangle mainCharacterRectangle;
    Rectangle offScreenRectangle;

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");

        createMainCharacter();

        offScreenRectangle = new Rectangle(-Gdx.graphics.getWidth(), 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        coin = new Texture("coin.png");
        bomb = new Texture("bomb.png");

        dizzyMainCharacter = new Texture("dizzy-1.png");
        randomValue = new Random();
        createTextToShow();
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 1) {
            spawnCoin();
            spawnBomb();

            coinRectangles.clear();
            for (int i = 0; i < coinXs.size(); i++) {
                batch.draw(coin, coinXs.get(i), coinYs.get(i));
                coinXs.set(i, coinXs.get(i) - COIN_VELOCITY_IN_PX);

                coinRectangles.add(new Rectangle(
                        coinXs.get(i),
                        coinYs.get(i),
                        coin.getWidth(),
                        coin.getHeight()
                ));
            }

            bombRectangles.clear();
            for (int i = 0; i < bombXs.size(); i++) {
                batch.draw(bomb, bombXs.get(i), bombYs.get(i));
                bombXs.set(i, bombXs.get(i) - BOMB_VELOCITY_IN_PX);

                bombRectangles.add(new Rectangle(
                        bombXs.get(i),
                        bombYs.get(i),
                        bomb.getWidth(),
                        bomb.getHeight()
                ));
            }

            if (Gdx.input.justTouched()) {
                velocity = -PLAYER_JUMP_HEIGHT;
            }

            if (pause < 2) {
                pause++;
            } else {
                pause = 0;
                if (mainCharacterState < 3) {
                    mainCharacterState++;
                } else {
                    mainCharacterState = 0;
                }
            }


            velocity += gravity;
            mainCharacterY -= velocity;

            if (mainCharacterY <= 0) {
                mainCharacterY = 0;
            }

            mainCharacterRectangle = new Rectangle(
                    Gdx.graphics.getWidth() / 2 - mainCharacter[mainCharacterState].getWidth() / 2,
                    mainCharacterY,
                    mainCharacter[mainCharacterState].getWidth(),
                    mainCharacter[mainCharacterState].getHeight()
            );

            for (int i = 0; i < coinRectangles.size(); i++) {
                if (Intersector.overlaps(mainCharacterRectangle, coinRectangles.get(i))) {
                    coinTouched(i);
                    break;
                }
                if (Intersector.overlaps(offScreenRectangle, coinRectangles.get(i))) {
                    coinOutOfScreen(i);
                    break;
                }
            }

            for (int i = 0; i < bombRectangles.size(); i++) {
                if (Intersector.overlaps(mainCharacterRectangle, bombRectangles.get(i))) {
                    Gdx.app.log("Bomb", "collision");
                    gameState = 2;
                }
                if (Intersector.overlaps(offScreenRectangle, bombRectangles.get(i))) {
                    bombOutOfScreen(i);
                    break;
                }
            }
        } else if (gameState == 2) {
            if (Gdx.input.justTouched()) {
                restartGame();
            }
        }

        if (gameState == 2) {
            batch.draw(
                    dizzyMainCharacter,
                    Gdx.graphics.getWidth() / 2 - dizzyMainCharacter.getWidth() / 2,
                    mainCharacterY
            );
        } else {
            batch.draw(
                    mainCharacter[mainCharacterState],
                    Gdx.graphics.getWidth() / 2 - mainCharacter[mainCharacterState].getWidth() / 2,
                    mainCharacterY
            );
        }
        textToShow.draw(batch, String.valueOf(score), 100, 200);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }


    public void makeCoin() {
        float height = randomValue.nextFloat() * Gdx.graphics.getHeight();
        coinYs.add((int) height);
        coinXs.add(Gdx.graphics.getWidth());
    }

    public void makeBomb() {
        float height = randomValue.nextFloat() * Gdx.graphics.getHeight();
        bombYs.add((int) height);
        bombXs.add(Gdx.graphics.getWidth());
    }

    private void createMainCharacter() {
        mainCharacter = new Texture[4];
        mainCharacter[0] = new Texture("frame-1.png");
        mainCharacter[1] = new Texture("frame-2.png");
        mainCharacter[2] = new Texture("frame-3.png");
        mainCharacter[3] = new Texture("frame-4.png");
        mainCharacterY = Gdx.graphics.getHeight() / 2;
    }

    private void createTextToShow() {
        textToShow = new BitmapFont();
        textToShow.setColor(Color.WHITE);
        textToShow.getData().setScale(10);

    }

    private void spawnCoin() {
        if (coinCount < COIN_SPAWN_FREQUENCY) {
            coinCount++;
        } else {
            coinCount = 0;
            makeCoin();
        }
    }

    private void spawnBomb() {
        if (bombCount < BOMB_SPAWN_FREQUENCY) {
            bombCount++;
        } else {
            bombCount = 0;
            makeBomb();
        }
    }

    private void removeCoins() {
        coinXs.clear();
        coinYs.clear();
        coinRectangles.clear();
        coinCount = 0;
    }

    private void removeBombs() {
        bombXs.clear();
        bombYs.clear();
        bombRectangles.clear();
        bombCount = 0;
    }

    private void bombOutOfScreen(int bombOutOfScreenIndex) {
        bombRectangles.remove(bombOutOfScreenIndex);
        bombXs.remove(bombOutOfScreenIndex);
        bombYs.remove(bombOutOfScreenIndex);
    }

    private void coinOutOfScreen(int coinOutOfScreenIndex) {
        coinRectangles.remove(coinOutOfScreenIndex);
        coinXs.remove(coinOutOfScreenIndex);
        coinYs.remove(coinOutOfScreenIndex);
    }

    private void coinTouched(int touchedCoin) {
        Gdx.app.log("Coin", "collision");
        score++;
        coinRectangles.remove(touchedCoin);
        coinXs.remove(touchedCoin);
        coinYs.remove(touchedCoin);
    }

    private void restartGame() {
        score = 0;
        gameState = 1;
        velocity = 0;
        removeCoins();
        removeBombs();
        mainCharacterY = Gdx.graphics.getHeight() / 2;
    }


}
