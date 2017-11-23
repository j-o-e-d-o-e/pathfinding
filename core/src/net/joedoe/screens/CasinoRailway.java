package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.helpers.GameManager;

public class CasinoRailway extends Casino {

    public CasinoRailway(GameMain game) {
        super(game);
        if (GameManager.storyStage == 7) {
            message.setText("\"Fat Tony\" already sits at the poker table.");
        }
    }

    void handleStoryInput() {
        if (GameManager.storyStage == 7 && Gdx.input.isKeyJustPressed(Keys.ANY_KEY)) {
            hud.removeStory();
            GameManager.storyStage++;
        }
    }

    @Override
    void playPoker() {
        if (hud.player.getMoney() >= 15) {
            int rand = ((int) (Math.random() * (10 - 1))) + 1;
            if (rand > 8) {
                hud.player.changeMoney(30);
                message.setText("");
                hud.showStory(GameManager.storyStage);
            } else {
                hud.player.changeMoney(-15);
                message.setText(name + " has lost!");
            }
        } else {
            message.setText(name + " doesn't have enough money!");
        }
        hud.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameInfo.GREEN[0], GameInfo.GREEN[1], GameInfo.GREEN[2], 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        title.draw(game.getBatch(), "CASINO", GameInfo.WIDTH / 2f - 200, GameInfo.HEIGHT / 2f + 200);
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        stage.draw();
        stage.act();
        hud.getStage().draw();
        hud.getStage().act();
        if (GameManager.storyMode) {
            handleStoryInput();
        }
    }
}
