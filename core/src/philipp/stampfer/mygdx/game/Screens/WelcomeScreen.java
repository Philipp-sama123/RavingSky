package philipp.stampfer.mygdx.game.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import philipp.stampfer.mygdx.game.RavingSky;

public class WelcomeScreen implements Screen {

    private RavingSky ravingSkyGame;
    private SpriteBatch batch;
    private Texture background;
    TextButton playButton;
    TextButton.TextButtonStyle playButtonStyle;

    public WelcomeScreen(RavingSky ravingSkyGame) {
        this.ravingSkyGame = ravingSkyGame;
        batch = new SpriteBatch();
        background = new Texture((TextureData) Color.BLUE);
playButtonStyle = new TextButton.TextButtonStyle();
        playButton = new TextButton("Start the Game",playButtonStyle);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
}
