package net.joedoe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.joedoe.entities.Player;
import net.joedoe.helpers.GameManager;
import net.joedoe.screens.Fight;

public class GameMain extends Game {
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        GameManager.getInstance().initializeGameData("joe doe", 0);
        GameManager.storyStage = 8;
        GameManager.player = new Player();
        setScreen(new Fight(this));
    }

    @Override
    public void render() {
        super.render();
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    public void dispose() {
        batch.dispose();
    }
}
