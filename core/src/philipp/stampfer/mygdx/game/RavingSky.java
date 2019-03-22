package philipp.stampfer.mygdx.game;

import com.badlogic.gdx.Game;

import philipp.stampfer.mygdx.game.Screens.GameScreen;

public class RavingSky extends Game {
    //todo extract to data class/data structure
    //todo more different types
    public static final int PLAYER_JUMP_HEIGHT = 75;

    public static final int COIN_SPAWN_FREQUENCY = 100;
    public static final int BOMB_SPAWN_FREQUENCY = 250;

    public static final int COIN_VELOCITY_IN_PX = 4;
    public static final int BOMB_VELOCITY_IN_PX = 6;


    @Override
    public void create() {

        //todo handle game Screens here (switch/case) -- pass parameters!!
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
