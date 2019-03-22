package philipp.stampfer.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

import philipp.stampfer.mygdx.game.RavingSky;

public class GameScreen implements Screen {
    private RavingSky ravingSkyGame;
    private SpriteBatch batch;

    private Texture background;
    private Texture[] mainCharacter;
    private Texture dizzyMainCharacter;

    private Texture coin;
    private Texture bomb;

    private BitmapFont textToShow;

    private int mainCharacterState;
    private int pause = 0;
    private int mainCharacterY;

    private int bombCount;
    private int coinCount;

    private int gameState = 0; // todo extract

    private int score = 0;

    private float gravity = 5f;
    private float velocity = 0;

    private ArrayList<Integer> coinXs = new ArrayList<Integer>();
    private ArrayList<Integer> coinYs = new ArrayList<Integer>();
    private ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();

    private ArrayList<Integer> bombXs = new ArrayList<Integer>();
    private ArrayList<Integer> bombYs = new ArrayList<Integer>();
    private ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();

    private Random randomValue;

    private Rectangle mainCharacterRectangle;
    private Rectangle offScreenRectangle;

    public GameScreen(RavingSky ravingSkyGame) {
        this.ravingSkyGame = ravingSkyGame;
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
    public void show() {

    }

    @Override
    public void render(float delta) {
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
                coinXs.set(i, coinXs.get(i) - RavingSky.COIN_VELOCITY_IN_PX);

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
                bombXs.set(i, bombXs.get(i) - RavingSky.BOMB_VELOCITY_IN_PX);

                bombRectangles.add(new Rectangle(
                        bombXs.get(i),
                        bombYs.get(i),
                        bomb.getWidth(),
                        bomb.getHeight()
                ));
            }

            if (Gdx.input.justTouched()) {
                velocity = -RavingSky.PLAYER_JUMP_HEIGHT;
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
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

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
        if (coinCount < RavingSky.COIN_SPAWN_FREQUENCY) {
            coinCount++;
        } else {
            coinCount = 0;
            makeCoin();
        }
    }

    private void spawnBomb() {
        if (bombCount < RavingSky.BOMB_SPAWN_FREQUENCY) {
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
