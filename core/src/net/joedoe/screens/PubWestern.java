package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;
import net.joedoe.helpers.GameManager;

public class PubWestern extends Pub {

    public PubWestern(GameMain game) {
        super(game);
        if (GameManager.storyStage == 6) {
            message.setText(name + " enters the pub. No sign of \"Fat Tony\".");
        }
    }

    @Override
    void chatWithAGuest() {
        if (beerCount == 0 && mealCount == 0) {
            message.setText(name + " needs to order something, first.");
        } else if (GameManager.storyStage == 6 && ((beerCount >= 1 && beerCount < 3) || mealCount >= 1)) {
            message.setText("");
            hud.showStory(GameManager.storyStage);
        } else if (beerCount < 5) {
            message.setText(name + " has a pleasant chat with a stranger.");
        } else {
            message.setText("Nobody wants to talk to " + name);
        }
    }

    @Override
    void handleStoryInput() {
        if (storyStage == 88) {
            if (Gdx.input.isKeyJustPressed(Keys.Y)) {
                hud.player.changeMoney(25);
                hud.update();
                message.setText(name + " does the dishwashing.");
                storyStage = -1;
                hud.removeStory();
            } else if (Gdx.input.isKeyJustPressed(Keys.N)) {
                message.setText(name + " declines the job offer.");
                hud.removeStory();
            }
        } else if (storyStage == 89) {
            if (Gdx.input.isKeyJustPressed(Keys.Y)) {
                hud.player.changeMoney(50);
                hud.update();
                message.setText(name + " hands out the flyers successfully.");
                storyStage = -1;
                hud.removeStory();
            } else if (Gdx.input.isKeyJustPressed(Keys.N)) {
                message.setText(name + " declines the job offer.");
                hud.removeStory();
            }
        } else if (GameManager.storyStage == 6 && Gdx.input.isKeyJustPressed(Keys.ANY_KEY)) {
            hud.removeStory();
            GameManager.storyStage++;
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameInfo.BLUE[0], GameInfo.BLUE[1], GameInfo.BLUE[2], 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        title.draw(game.getBatch(), "WESTERN PUB", GameInfo.WIDTH / 2f - 300, GameInfo.HEIGHT / 2f + 200);
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
