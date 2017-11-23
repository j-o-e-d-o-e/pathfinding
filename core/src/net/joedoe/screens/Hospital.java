package net.joedoe.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import net.joedoe.GameInfo;
import net.joedoe.GameMain;

public class Hospital extends Place {

    public Hospital(GameMain game) {
        super(game);
        title.setColor(GameInfo.GREEN[0], GameInfo.GREEN[1], GameInfo.GREEN[2], 1);
        createTable();
    }

    public void createTable() {
        message = new Label(name + " enters the hospital.", skin);
        button1 = new TextButton(" 80 $ ", skin);
        button2 = new TextButton(" 45 $ ", skin);
        button3 = new TextButton(" 20 $ ", skin);
        buttonLeave = new TextButton(" Leave ", skin);
        button1.addListener(listener);
        button2.addListener(listener);
        button3.addListener(listener);
        buttonLeave.addListener(listener);
        table = new Table(skin);
        // table.debug();
        table.setPosition(-25, 0);
        table.setFillParent(true);
        table.add(message).width(160).left();
        table.row().padTop(10);
        table.add("Recover full health:").left();
        table.add(button1);
        table.row().padTop(10);
        table.add("Recover 50 health:").left();
        table.add(button2);
        table.row().padTop(10);
        table.add("Recover 20 health:").left();
        table.add(button3);
        table.row().padTop(10);
        table.add(buttonLeave).padLeft(60);
        stage.addActor(table);
    }

    public void handleInput(String choice) {
        if (choice.contains("Leave")) {
            dispose();
            game.setScreen(new City(game));
        } else if (hud.player.getHealth() == 100) {
            message.setText(name + " is already fully recovered!");
        } else {
            if (choice.contains("80")) {
                if (hud.player.getMoney() >= 80) {
                    hud.player.setHealth(100);
                    hud.player.changeMoney(-80);
                    hud.update();
                    message.setText("");
                } else {
                    message.setText(name + " doesn't have enough money!");
                }
            } else if (choice.contains("45")) {
                if (hud.player.getMoney() >= 45) {
                    hud.player.changeHealth(50);
                    hud.player.changeMoney(-45);
                    hud.update();
                    message.setText("");
                } else {
                    message.setText(name + " doesn't have enough money!");
                }
            } else if (choice.contains("20")) {
                if (hud.player.getMoney() >= 20) {
                    hud.player.changeHealth(20);
                    hud.player.changeMoney(-20);
                    hud.update();
                    message.setText("");
                } else {
                    message.setText(name + " doesn't have enough money!");
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(GameInfo.RED[0], GameInfo.RED[1], GameInfo.RED[2], 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().begin();
        title.draw(game.getBatch(), "HOSPITAL", GameInfo.WIDTH / 2f - 265, GameInfo.HEIGHT / 2f + 200);
        game.getBatch().end();
        game.getBatch().setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();
        hud.getStage().act();
        stage.draw();
        stage.act();
    }

    @Override
    public void dispose() {
        stage.dispose();
        title.dispose();
        skin.dispose();
    }
}
